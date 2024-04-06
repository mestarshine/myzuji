package com.myzuji.sadk.test;


import com.myzuji.sadk.P10Request;
import com.myzuji.sadk.algorithm.common.Mechanism;
import com.myzuji.sadk.algorithm.common.PKIException;
import com.myzuji.sadk.algorithm.sm2.SM2PrivateKey;
import com.myzuji.sadk.algorithm.sm2.SM2PublicKey;
import com.myzuji.sadk.lib.crypto.JCrypto;
import com.myzuji.sadk.lib.crypto.jni.Session;
import com.myzuji.sadk.org.bouncycastle.util.Strings;
import com.myzuji.sadk.org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.Locale;

public class CerTest {

    private static final String deviceName = JCrypto.JSOFT_LIB;
    private static Session session = null;

    @BeforeAll
    static void init() {
        try {
            JCrypto jCrypto = JCrypto.getInstance();
            jCrypto.initialize(deviceName, null);
            session = jCrypto.openSession(deviceName);
        } catch (PKIException e) {
            e.printStackTrace();
        }
    }

    @Test
    void buildSM2CSR() throws PKIException {
        String subject = "";
        String pwd = "123456";
        P10Request p10Request = new P10Request(session);
        Mechanism mechanism = new Mechanism(Mechanism.SM3_SM2);
        p10Request.generateDoublePKCS10(mechanism, 256, subject, pwd);
        // 可以拿此申请文件数据到CA中申请双证
        System.out.println("[SM2申请P10]: " + new String(p10Request.getBase64P10Data(), StandardCharsets.UTF_8));
        // 签名密钥，用户需要安全保存（加密密钥）
        KeyPair keyPair = p10Request.getKeyPair();
        SM2PrivateKey sm2PrivateKey = (SM2PrivateKey) keyPair.getPrivate();
        SM2PublicKey sm2PublicKey = (SM2PublicKey) keyPair.getPublic();

        System.out.println("[SM2签名私钥]: " + Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
        System.out.println("[SM2签名私钥HEX]: " + sm2PrivateKey.getD().toString(16));
        System.out.println("[SM2签名公钥]: " + Base64.getEncoder().encodeToString(sm2PublicKey.getEncoded()));
        // 临时密钥，用户需要安全保存（加密密钥）
        KeyPair temporaryKeyPair = p10Request.getTemporaryKeyPair();
        SM2PrivateKey tempSm2PrivateKey = (SM2PrivateKey) keyPair.getPrivate();
        SM2PublicKey tempSm2PublicKey = (SM2PublicKey) keyPair.getPublic();
        System.out.println("[SM2加密私钥]: " + Base64.getEncoder().encodeToString(temporaryKeyPair.getPrivate().getEncoded()));
        System.out.println("[SM2加密私钥]: " + tempSm2PrivateKey.getD().toString(16));
        System.out.println("[SM2加密公钥]: " + Base64.getEncoder().encodeToString(tempSm2PublicKey.getEncoded()));
    }

}
