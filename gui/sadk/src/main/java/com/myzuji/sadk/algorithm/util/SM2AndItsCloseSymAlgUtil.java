package com.myzuji.sadk.algorithm.util;

import com.myzuji.sadk.algorithm.common.CBCParam;
import com.myzuji.sadk.algorithm.common.Mechanism;
import com.myzuji.sadk.algorithm.sm2.SM2Crypto;
import com.myzuji.sadk.algorithm.sm2.SM2PrivateKey;
import com.myzuji.sadk.algorithm.sm2.SM2PublicKey;
import com.myzuji.sadk.algorithm.sm2.SM4Engine;
import com.myzuji.sadk.lib.crypto.jni.JNISM2;
import com.myzuji.sadk.lib.crypto.jni.JNISymAlg;
import com.myzuji.sadk.org.bouncycastle.asn1.sm2.ASN1SM2Cipher;
import com.myzuji.sadk.org.bouncycastle.crypto.modes.CBCBlockCipher;
import com.myzuji.sadk.org.bouncycastle.crypto.paddings.PKCS7Padding;
import com.myzuji.sadk.org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import com.myzuji.sadk.org.bouncycastle.crypto.params.KeyParameter;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ParametersWithIV;

import java.security.Key;
import java.security.SecureRandom;

public class SM2AndItsCloseSymAlgUtil {
    private static int SM4_BLOCK_SIZE = 16;

    public SM2AndItsCloseSymAlgUtil() {
    }

    public static byte[] generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        return key;
    }

    public static byte[] generateIV() {
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[16];
        random.nextBytes(iv);
        return iv;
    }

    public static byte[] crypto(boolean useJNI, boolean forEncryption, byte[] key, byte[] sourceData, Mechanism algName) throws Exception {
        if (algName.getMechanismType().indexOf("SM4") != -1 && algName.getMechanismType().indexOf("CBC") != -1) {
            CBCParam param = (CBCParam) algName.getParam();
            return useSM4CBCEncrypt(useJNI, forEncryption, key, sourceData, param.getIv());
        } else if (algName.getMechanismType().indexOf("SM4") != -1 && algName.getMechanismType().indexOf("ECB") != -1) {
            return useSM4ECBEncrypt(useJNI, forEncryption, key, sourceData);
        } else {
            throw new Exception("can not support this algorithm to crypto:" + algName.getMechanismType());
        }
    }

    private static byte[] useSM4CBCEncrypt(boolean useJNI, boolean forEncryption, byte[] key, byte[] input, byte[] iv) throws Exception {
        int len1;
        int total;
        if (useJNI) {
            JNISymAlg jni = new JNISymAlg();
            byte[] plainText;
            if (forEncryption) {
                jni.encryptInit(JNISymAlg.NID_ChinaSM4_CBC, key, iv);
                plainText = new byte[input.length + (SM4_BLOCK_SIZE - input.length % SM4_BLOCK_SIZE)];
                len1 = jni.encryptProcess(input, 0, input.length, plainText, 0);
                jni.encryptFinal(plainText, len1);
                return plainText;
            } else {
                jni.decryptInit(JNISymAlg.NID_ChinaSM4_CBC, key, iv);
                plainText = new byte[input.length];
                len1 = jni.decryptProcess(input, 0, input.length, plainText, 0);
                int len2 = jni.decryptFinal(plainText, len1);
                total = len1 + len2;
                byte[] out2 = new byte[total];
                System.arraycopy(plainText, 0, out2, 0, total);
                return out2;
            }
        } else {
            PaddedBufferedBlockCipher cipher = null;
            cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new SM4Engine()), new PKCS7Padding());
            ParametersWithIV param = new ParametersWithIV(new KeyParameter(key), iv);
            cipher.init(forEncryption, param);
            len1 = cipher.getOutputSize(input.length);
            byte[] out = new byte[len1];
            total = cipher.processBytes(input, 0, input.length, out, 0);
            int len2 = cipher.doFinal(out, total);
            total = total + len2;
            if (total < len1) {
                byte[] out2 = new byte[total];
                System.arraycopy(out, 0, out2, 0, total);
                return out2;
            } else {
                return out;
            }
        }
    }

    private static byte[] useSM4ECBEncrypt(boolean useJNI, boolean forEncryption, byte[] key, byte[] input) throws Exception {
        PaddedBufferedBlockCipher cipher = null;
        cipher = new PaddedBufferedBlockCipher(new SM4Engine(), new PKCS7Padding());
        KeyParameter params = new KeyParameter(key);
        cipher.init(forEncryption, params);
        int len = cipher.getOutputSize(input.length);
        byte[] out = new byte[len];
        int len1 = cipher.processBytes(input, 0, input.length, out, 0);
        int len2 = cipher.doFinal(out, len1);
        int total = len1 + len2;
        if (total < len) {
            byte[] out2 = new byte[total];
            System.arraycopy(out, 0, out2, 0, total);
            return out2;
        } else {
            return out;
        }
    }

    public static byte[] sm2Encrypt(boolean isEncrypted, Key sm2Key, byte[] input) throws Exception {
        SM2Crypto crypto = new SM2Crypto();
        if (isEncrypted) {
            SM2PublicKey pubKey = (SM2PublicKey) sm2Key;
            crypto.initEncrypt(pubKey.getQ());
            return crypto.encrypt(input);
        } else {
            SM2PrivateKey priKey = (SM2PrivateKey) sm2Key;
            crypto.initDecrypt(priKey.getDByInt());
            return crypto.decrypt(input);
        }
    }

    public static byte[] sm2EncryptByJNI(boolean isEncrypted, Key sm2Key, byte[] input) throws Exception {
        byte[] dBytes;
        byte[] decryptedBytes;
        if (isEncrypted) {
            dBytes = new byte[input.length + 96];
            SM2PublicKey pubKey = (SM2PublicKey) sm2Key;
            decryptedBytes = BigIntegerUtil.asUnsigned32ByteArray(pubKey.getPubXByInt());
            byte[] pubY = BigIntegerUtil.asUnsigned32ByteArray(pubKey.getPubYByInt());
            JNISM2.encrypt(input, decryptedBytes, pubY, dBytes);
            ASN1SM2Cipher asn1 = new ASN1SM2Cipher(dBytes, 16);
            return asn1.getEncoded();
        } else {
            dBytes = BigIntegerUtil.asUnsigned32ByteArray(((SM2PrivateKey) sm2Key).getDByInt());

            byte[] encryptedBytes;
            ASN1SM2Cipher asn1;
            try {
                asn1 = new ASN1SM2Cipher(input, 1);
                encryptedBytes = asn1.getEncryptedBytes(16);
                decryptedBytes = decrypted(encryptedBytes, dBytes);
            } catch (Exception var9) {
                decryptedBytes = null;
            }

            if (decryptedBytes == null) {
                try {
                    asn1 = new ASN1SM2Cipher(input, 4);
                    encryptedBytes = asn1.getEncryptedBytes(16);
                    decryptedBytes = decrypted(encryptedBytes, dBytes);
                } catch (Exception var8) {
                    decryptedBytes = null;
                }
            }

            if (decryptedBytes == null) {
                decryptedBytes = decrypted(input, dBytes);
            }

            return decryptedBytes;
        }
    }

    private static final byte[] decrypted(byte[] encrypedBytes, byte[] dBytes) throws Exception {
        byte[] plainText = new byte[encrypedBytes.length - 96];
        JNISM2.decrypt(encrypedBytes, dBytes, plainText);
        return plainText;
    }
}
