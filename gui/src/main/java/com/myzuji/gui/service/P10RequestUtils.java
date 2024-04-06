package com.myzuji.gui.service;


import com.myzuji.sadk.P10Request;
import com.myzuji.sadk.algorithm.common.Mechanism;
import com.myzuji.sadk.algorithm.common.PKIException;
import com.myzuji.sadk.algorithm.sm2.SM2PrivateKey;
import com.myzuji.sadk.algorithm.sm2.SM2PublicKey;
import com.myzuji.sadk.lib.crypto.JCrypto;
import com.myzuji.sadk.lib.crypto.jni.Session;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1InputStream;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Integer;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1OctetString;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Sequence;
import com.myzuji.sadk.org.bouncycastle.util.Strings;
import com.myzuji.sadk.org.bouncycastle.util.encoders.Hex;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

public class P10RequestUtils {

    private static final String deviceName = JCrypto.JSOFT_LIB;
    private static Session session = null;

    static {
        try {
            JCrypto jCrypto = JCrypto.getInstance();
            jCrypto.initialize(deviceName, null);
            session = jCrypto.openSession(deviceName);
        } catch (PKIException e) {
            e.printStackTrace();
        }
    }

    public static P10Request buildSM2CSR(String subject, String password) throws PKIException {
        P10Request p10Request = new P10Request(session);
        Mechanism mechanism = new Mechanism(Mechanism.SM3_SM2);
        p10Request.generateDoublePKCS10(mechanism, 256, subject, password);
        return p10Request;
    }

    /**
     * 构建SM2国密 P10等
     *
     * @throws Exception
     */
    public static void buildSM2CSR() throws Exception {
        //dn规则CN=051固定 @+商户号 @+公司营业执照号 后面不变！
        String dn = "CN=051@ECPSS FARMS TEST@N91310107570775676M@1,OU=Organizational-2,OU=FARMS,O=CFCA TEST OCA31,C=CN";
        //密码
        String pwd = "12345678";
        P10Request p10Request = new P10Request(session);
        Mechanism mechanism = new Mechanism(Mechanism.SM3_SM2);
        p10Request.generateDoublePKCS10(mechanism, 256, dn, pwd);
        // 可以拿此申请文件数据到CA中申请双证
        System.out.println("[SM2申请P10]: " + new String(p10Request.getBase64P10Data(), StandardCharsets.UTF_8));
        // 签名密钥，用户需要安全保存（加密密钥）
        KeyPair keyPair = p10Request.getKeyPair();

        SM2PrivateKey sm2PrivateKey = (SM2PrivateKey) p10Request.getPrivateKey();
        SM2PublicKey sm2PublicKey = (SM2PublicKey) p10Request.getPublicKey();

        System.out.println("[SM2签名私钥]: " + Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
        System.out.println("[SM2签名私钥HEX]: " + sm2PrivateKey.getD().toString(16));
        System.out.println("[SM2签名公钥]: " + Base64.getEncoder().encodeToString(sm2PublicKey.getEncoded()));
        // 临时密钥，用户需要安全保存（加密密钥）
        SM2PrivateKey tempSm2PrivateKey = (SM2PrivateKey) p10Request.getTemporaryPrivateKey();
        SM2PublicKey tempSm2PublicKey = (SM2PublicKey) p10Request.getTemporaryPublicKey();
        System.out.println("[SM2加密私钥]: " + Base64.getEncoder().encodeToString(tempSm2PrivateKey.getEncoded()));
        System.out.println("[SM2加密私钥]: " + tempSm2PrivateKey.getD().toString(16));
        System.out.println("[SM2加密公钥]: " + Base64.getEncoder().encodeToString(tempSm2PublicKey.getEncoded()));
    }

    public static PrivateKey loadPrivateKey(String base64Key) {
        return SM2PrivateKey.getInstance(Base64.getDecoder().decode(base64Key));
    }

    public static String base64ToHexKey(String base64Key) {
        SM2PrivateKey tempKey = (SM2PrivateKey) loadPrivateKey(base64Key);
        return tempKey.getD().toString(16);
    }

    public static String parseDoubleCsrResult(String input) {
        return Optional.ofNullable(input)
            .filter(s -> s.length() >= 80)
            .map(s -> s.substring(80))
            .map(s -> s.replace(",", ""))
            .orElse("");
    }

    /**
     * 解析ASN1格式数据
     *
     * @param encrypt .
     * @return .
     * @throws IOException .
     */
    public static String decodeAsn1(String encrypt) throws IOException {
        ASN1InputStream asn1InputStream = new ASN1InputStream(new ByteArrayInputStream(Base64.getDecoder().decode(encrypt)));
        ASN1Sequence sequence = (ASN1Sequence) asn1InputStream.readObject();
        ASN1Integer version = (ASN1Integer) sequence.getObjectAt(0);
        ASN1OctetString encryptedPrivateKeyData = (ASN1OctetString) sequence.getObjectAt(1);
        byte[] cipherText = Arrays.copyOfRange(encryptedPrivateKeyData.getOctets(), 0, encryptedPrivateKeyData.getOctets().length);
        String hex = Strings.fromByteArray(Hex.encode(cipherText));

        System.out.println("[EncryptedPrivateKey-ASN1] version: " + version);
        System.out.println("[EncryptedPrivateKey-ASN1] 密文数据: " + hex);
        return hex;
    }

    /**
     * 构建RSA国际 P10等
     *
     * @throws Exception
     */
    public static void buildRSACSR() throws Exception {
        //dn规则CN=051固定 @+商户号 @+公司营业执照号 后面不变！
        String dn = "CN=051@ECPSS FARMS TEST@N91310107570775676M@1,OU=Organizational-2,OU=FARMS,O=CFCA TEST OCA31,C=CN";
        //密码
        String pwd = "12345678";
        P10Request p10Request = new P10Request(session);
        Mechanism mechanism = new Mechanism(Mechanism.SHA256_RSA);
        p10Request.generateDoublePKCS10(mechanism, 2048, dn, pwd);
        // 可以拿此申请文件数据到CA中申请双证
        System.out.println("[RSA申请P10]: " + new String(p10Request.getBase64P10Data(), StandardCharsets.UTF_8));
        // 签名密钥，用户需要安全保存（加密密钥）
        KeyPair keyPair = p10Request.getKeyPair();
        System.out.println("[RSA签名私钥]: " + Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
        System.out.println("[RSA签名公钥]: " + Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        // 临时密钥，用户需要安全保存（加密密钥）
        KeyPair temporaryKeyPair = p10Request.getTemporaryKeyPair();
        System.out.println("[RSA加密私钥]: " + Base64.getEncoder().encodeToString(temporaryKeyPair.getPrivate().getEncoded()));
        System.out.println("[RSA加密公钥]: " + Base64.getEncoder().encodeToString(temporaryKeyPair.getPublic().getEncoded()));
    }

}
