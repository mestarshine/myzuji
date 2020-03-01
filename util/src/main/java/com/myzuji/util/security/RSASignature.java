package com.myzuji.util.security;


import org.apache.commons.codec.binary.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA 签名工具类
 *
 * @author shine
 * @date 2020/03/01
 */
public class RSASignature {

    /**
     * 签名算法
     */
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";
    private static final String ALGORITHM = "RSA";

    /**
     * RSA签名
     *
     * @param content    待签名数据
     * @param privateKey 私钥
     * @param encode     字符集编码
     * @return 签名值
     */
    public static String sign(String content, String privateKey, String encode) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
            KeyFactory factory = KeyFactory.getInstance(ALGORITHM);
            PrivateKey priKey = factory.generatePrivate(keySpec);

            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);

            signature.initSign(priKey);
            signature.update(content.getBytes(encode));

            byte[] signed = signature.sign();
            return Base64.encodeBase64String(signed);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    /**
     * RSA验签
     *
     * @param content   待签名数据
     * @param publicKey 公钥
     * @param encode    字符集编码
     * @return 验签结果boolean
     */
    public static boolean verify(String content, String publicKey, String encode, String sign) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
            KeyFactory factory = KeyFactory.getInstance(ALGORITHM);
            PublicKey pubKey = factory.generatePublic(keySpec);

            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);

            signature.initVerify(pubKey);
            signature.update(content.getBytes(encode));

            return signature.verify(Base64.decodeBase64(sign));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
