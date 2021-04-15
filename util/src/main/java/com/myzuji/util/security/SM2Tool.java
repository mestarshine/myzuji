package com.myzuji.util.security;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithID;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.SM2Signer;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * 国密SM2
 *
 * @author shine
 * @date 2020/04/05
 */
public class SM2Tool {

    public static final Logger logger = LoggerFactory.getLogger(SM2Tool.class);

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static Map<String, String> genSMKeyPair() {
        Map<String, String> key = null;
        final KeyPair smKeyPair = generateSm2KeyPair();
        if (smKeyPair != null) {
            key = new HashMap<>();
            final PrivateKey privateKey = smKeyPair.getPrivate();
            final PublicKey publicKey = smKeyPair.getPublic();
            key.put("privateKey", Base64.encodeBase64String(privateKey.getEncoded()));
            key.put("publicKey", Base64.encodeBase64String(publicKey.getEncoded()));
        }
        return key;
    }

    public static KeyPair generateSm2KeyPair() {
        final ECGenParameterSpec sm2Spec = new ECGenParameterSpec("sm2p256v1");
        try {
            final KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", "BC");
            kpg.initialize(sm2Spec);
            kpg.initialize(sm2Spec, new SecureRandom());
            return kpg.generateKeyPair();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | NoSuchProviderException e) {
            logger.error("不支持的算法类型{}", e.getMessage());
            return null;
        }
    }

    public static String sign(String privateKey, String content) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("EC", new BouncyCastleProvider());
            PrivateKey privateKey1 = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey)));
            ECPrivateKeyParameters asymmetricKeyParameter = (ECPrivateKeyParameters) ECUtil.generatePrivateKeyParameter(privateKey1);
            ParametersWithRandom parametersWithRandom = new ParametersWithRandom(asymmetricKeyParameter);
            ParametersWithID parametersWithID = new ParametersWithID(parametersWithRandom, Strings.toByteArray("1234567812345678"));
            SM2Signer sm2Signer = new SM2Signer();
            sm2Signer.init(true, parametersWithID);
            sm2Signer.update(content.getBytes(StandardCharsets.UTF_8), 0, content.length());
            return Base64.encodeBase64String(sm2Signer.generateSignature());
        } catch (Exception e) {
            logger.info("私钥：{}，明文：{}，签名错误：{}", privateKey, content, e.getMessage());
        }
        return null;
    }

    public static boolean verify(String publicKey, String content, String sign) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("EC", new BouncyCastleProvider());
            PublicKey publicKey1 = keyFactory.generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(publicKey)));

            ECPublicKeyParameters ecPublicKeyParameters = (ECPublicKeyParameters) ECUtil.generatePublicKeyParameter(publicKey1);

            ParametersWithID parametersWithID = new ParametersWithID(ecPublicKeyParameters, Strings.toByteArray("1234567812345678"));
            SM2Signer sm2Signer = new SM2Signer();
            sm2Signer.init(false, parametersWithID);
            sm2Signer.update(content.getBytes(StandardCharsets.UTF_8), 0, content.length());
            return sm2Signer.verifySignature(Base64.decodeBase64(sign));
        } catch (Exception e) {
            logger.info("公钥：{}，明文：{}，签名：{}，验签错误：{}", publicKey, content, sign, e.getMessage());
        }
        return false;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, CryptoException, InvalidKeySpecException, InvalidKeyException {

    }
}
