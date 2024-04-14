package com.myzuji.gui.service;

import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.pkcs.Attribute;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.util.encoders.Hex;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;

/**
 * 双证书密钥生成
 */
public class BCSM2KeyUtil {
    private String p10Data;
    private String priKeyBase64;
    private String priKeyHex;
    private String pubKeyBase64;
    private String tempPriKeyBase64;
    private String tempPriKeyHex;
    private String tempPubKeyBase64;

    private static DEROctetString getIntAndPublicKeyOctetString(SubjectPublicKeyInfo temporaryPublicKeyInfo) throws IOException {
        ASN1Integer derInt = new ASN1Integer(1L);
        DEROctetString tempOctetString;
        byte[] tempPublicKeyData = new byte[136];
        assert temporaryPublicKeyInfo != null;
        byte[] temporaryPublicKeyData = temporaryPublicKeyInfo.getPublicKeyData().getBytes();
        byte[] temporaryPublicKeyX = new byte[32];
        byte[] temporaryPublicKeyY = new byte[32];
        System.arraycopy(temporaryPublicKeyData, 1, temporaryPublicKeyX, 0, 32);
        System.arraycopy(temporaryPublicKeyData, 33, temporaryPublicKeyY, 0, 32);
        byte[] const1 = new byte[]{0, -76, 0, 0};
        byte[] const2 = new byte[]{0, 1, 0, 0};
        System.arraycopy(const1, 0, tempPublicKeyData, 0, 4);
        System.arraycopy(const2, 0, tempPublicKeyData, 4, 4);
        System.arraycopy(temporaryPublicKeyX, 0, tempPublicKeyData, 8, 32);
        System.arraycopy(temporaryPublicKeyY, 0, tempPublicKeyData, 72, 32);

        tempOctetString = new DEROctetString(tempPublicKeyData);

        ASN1EncodableVector intAndPublicKeyVector = new ASN1EncodableVector();
        intAndPublicKeyVector.add(derInt);
        intAndPublicKeyVector.add(tempOctetString);
        DERSequence tempPublicKeyInfoSeq = new DERSequence(intAndPublicKeyVector);
        DEROctetString intAndPublicKeyOctetString;

        try {
            intAndPublicKeyOctetString = new DEROctetString(tempPublicKeyInfoSeq);
        } catch (IOException e) {
            throw new IOException("generateDoublePKCS10Request Failure", e);
        }
        return intAndPublicKeyOctetString;
    }

    /**
     * SM2算法生成密钥对
     *
     * @return 密钥对信息
     */
    private static KeyPair generateSm2KeyPair() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        final ECGenParameterSpec sm2Spec = new ECGenParameterSpec("sm2p256v1");
        // 获取一个椭圆曲线类型的密钥对生成器
        final KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", new BouncyCastleProvider());
        SecureRandom random = new SecureRandom();
        // 使用SM2的算法区域初始化密钥生成器
        kpg.initialize(sm2Spec, random);
        // 获取密钥对
        return kpg.generateKeyPair();
    }

    /**
     * @param asn1ObjectIdentifier
     * @param x500Name
     * @author: Linwei
     * @created: 2022/6/21 10:10
     * @desc: 读取CSR中的主题信息
     */
    public static String getX500Field(ASN1ObjectIdentifier asn1ObjectIdentifier, X500Name x500Name) {
        RDN[] rdnArray = x500Name.getRDNs(asn1ObjectIdentifier);
        String retVal = "";
        for (RDN item : rdnArray) {
            retVal += " " + item.getFirst().getValue().toString();
        }
        return retVal;

    }

    public static String getAttributeValue(Attribute[] attributes) throws IOException {
        String string = "";
        for (Attribute attribute : attributes) {
            string += "            Attribute\n" + "                type: (unknown) (" + attribute.getAttrType().toString() + ")\n" + "                values: " + geta(attribute.getAttributeValues()) + "\n";
        }
        return string;
    }

    public static String geta(ASN1Encodable[] encodable) throws IOException {
        for (ASN1Encodable asn1Encodable : encodable) {
            return Hex.toHexString(asn1Encodable.toASN1Primitive().getEncoded());
        }
        return "";
    }

    /**
     * 生成 国密双证书
     *
     * @param dn  DN
     * @param pwd 密码
     * @throws Exception 异常
     */
    public void generateDoublePKCS10(String dn, String pwd) throws Exception {
        KeyPair keyPair = generateSm2KeyPair();
        KeyPair tempKeyPair = generateSm2KeyPair();

        SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());
        SubjectPublicKeyInfo temporaryPublicKeyInfo = SubjectPublicKeyInfo.getInstance(tempKeyPair.getPublic().getEncoded());
        X500Name subject = new X500Name(dn);
        // 创建 CSR
        PKCS10CertificationRequestBuilder builder = new PKCS10CertificationRequestBuilder(subject, publicKeyInfo);
        builder.setAttribute(PKCSObjectIdentifiers.pkcs_9_at_challengePassword, new DERPrintableString(pwd));
        builder.setAttribute(new ASN1ObjectIdentifier("1.2.840.113549.1.9.63"), getIntAndPublicKeyOctetString(temporaryPublicKeyInfo));

        ContentSigner signer = new JcaContentSignerBuilder("SM3withSM2").setProvider(new BouncyCastleProvider()).build(keyPair.getPrivate());
        PKCS10CertificationRequest pkcs10CertificationRequest = builder.build(signer);
        p10Data = Base64.getEncoder().encodeToString(pkcs10CertificationRequest.getEncoded());
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        priKeyBase64 = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        priKeyHex = ((BCECPrivateKey) privateKey).getD().toString(16);
        pubKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        PrivateKey tempPrivateKey = tempKeyPair.getPrivate();
        PublicKey tempPublicKey = tempKeyPair.getPublic();
        tempPriKeyBase64 = Base64.getEncoder().encodeToString(tempPrivateKey.getEncoded());
        tempPriKeyHex = ((BCECPrivateKey) tempPrivateKey).getD().toString(16);
        tempPubKeyBase64 = Base64.getEncoder().encodeToString(tempPublicKey.getEncoded());
    }

    public PKCS10CertificationRequest convertPemToPKCS10CertificationRequest(String csrStr) throws Exception {
        ByteArrayInputStream pemStream = new ByteArrayInputStream(csrStr.getBytes(StandardCharsets.UTF_8));
        Reader pemReader = new BufferedReader(new InputStreamReader(pemStream));
        PEMParser pemParser = new PEMParser(pemReader);
        return (PKCS10CertificationRequest) pemParser.readObject();
    }

    /**
     * @param csrStr
     * @throws Exception
     */
    public void csrParse(String csrStr) throws Exception {
        PKCS10CertificationRequest csr = null;
        Security.addProvider(new BouncyCastleProvider());
        ByteArrayInputStream pemStream = new ByteArrayInputStream(csrStr.getBytes(StandardCharsets.UTF_8));
        Reader pemReader = new BufferedReader(new InputStreamReader(pemStream));
        PEMParser pemParser = new PEMParser(pemReader);
        Object parsedObj = pemParser.readObject();
        if (parsedObj instanceof PKCS10CertificationRequest) {
            csr = (PKCS10CertificationRequest) parsedObj;
        }
        System.out.println("CertificationRequest\n" + "    certificationRequestInfo\n" + "        version: v1 (" + csr.toASN1Structure().getCertificationRequestInfo().getVersion() + ")\n" + "        subject\n" + "            commonName: " + getX500Field(BCStyle.CN, csr.getSubject()) + "\n" + "            organizationalUnitName: " + getX500Field(BCStyle.OU, csr.getSubject()) + "\n" + "            organizationName: " + getX500Field(BCStyle.O, csr.getSubject()) + "\n" + "            countryName: " + getX500Field(BCStyle.C, csr.getSubject()) + "\n" + "        subjectPublicKeyInfo\n" + "            algorithm\n" + "                algorithm: " + csr.getSubjectPublicKeyInfo().getAlgorithm().getParameters().toString() + "\n" +
//                "                namedCurve: sm2p256v1\n" +
            "            subjectPublicKey\n" + "                ECPoint: " + Hex.toHexString(csr.getSubjectPublicKeyInfo().getPublicKeyData().getOctets()) + "\n" + "        attributes\n" + getAttributeValue(csr.getAttributes()) + "    signatureAlgorithm\n" + "        algorithm: " + csr.getSignatureAlgorithm().getAlgorithm().toString() + "\n" + "    signature: : " + Hex.toHexString(csr.getSignature()) + "\n" + "-----BEGIN CERTIFICATE REQUEST-----\n" + Base64.getEncoder().encodeToString(csr.getEncoded()) + "\n-----END CERTIFICATE REQUEST-----");
    }

    public String getP10Data() {
        return p10Data;
    }

    public String getPriKeyBase64() {
        return priKeyBase64;
    }

    public String getPriKeyHex() {
        return priKeyHex;
    }

    public String getPubKeyBase64() {
        return pubKeyBase64;
    }

    public String getTempPriKeyBase64() {
        return tempPriKeyBase64;
    }

    public String getTempPriKeyHex() {
        return tempPriKeyHex;
    }

    public String getTempPubKeyBase64() {
        return tempPubKeyBase64;
    }
}
