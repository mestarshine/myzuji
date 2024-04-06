package com.myzuji.sadk.algorithm.util;

import com.myzuji.sadk.algorithm.common.CBCParam;
import com.myzuji.sadk.algorithm.common.Mechanism;
import com.myzuji.sadk.lib.crypto.jni.JNIRSA;
import com.myzuji.sadk.lib.crypto.jni.JNISymAlg;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import com.myzuji.sadk.org.bouncycastle.crypto.AsymmetricBlockCipher;
import com.myzuji.sadk.org.bouncycastle.crypto.InvalidCipherTextException;
import com.myzuji.sadk.org.bouncycastle.crypto.KeyGenerationParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.encodings.PKCS1Encoding;
import com.myzuji.sadk.org.bouncycastle.crypto.engines.DESedeEngine;
import com.myzuji.sadk.org.bouncycastle.crypto.engines.RC4Engine;
import com.myzuji.sadk.org.bouncycastle.crypto.engines.RSAEngine;
import com.myzuji.sadk.org.bouncycastle.crypto.generators.DESedeKeyGenerator;
import com.myzuji.sadk.org.bouncycastle.crypto.modes.CBCBlockCipher;
import com.myzuji.sadk.org.bouncycastle.crypto.paddings.PKCS7Padding;
import com.myzuji.sadk.org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import com.myzuji.sadk.org.bouncycastle.crypto.params.KeyParameter;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ParametersWithIV;
import com.myzuji.sadk.org.bouncycastle.crypto.params.RSAKeyParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.util.PrivateKeyFactory;
import com.myzuji.sadk.org.bouncycastle.crypto.util.PublicKeyFactory;

import java.security.Key;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RSAAndItsCloseSymAlgUtil {
    private static int HEADER_LENGTH = 10;
    private static int DES3_BLOCK_SIZE = 8;

    public RSAAndItsCloseSymAlgUtil() {
    }

    public static byte[] crypto(boolean useJNI, boolean forEncryption, byte[] key, byte[] sourceData, Mechanism algName) throws Exception {
        if (algName.getMechanismType().indexOf("RC4") != -1) {
            return useRC4Encrypt(useJNI, forEncryption, key, sourceData);
        } else if (algName.getMechanismType().indexOf("DESede") != -1 && algName.getMechanismType().indexOf("CBC") != -1) {
            CBCParam param = (CBCParam) algName.getParam();
            return useDesedeCBCEncrypt(useJNI, forEncryption, key, sourceData, param.getIv());
        } else if (algName.getMechanismType().indexOf("DESede") != -1 && algName.getMechanismType().indexOf("ECB") != -1) {
            return useDesedeECBEncrypt(useJNI, forEncryption, key, sourceData);
        } else if (algName.getMechanismType().indexOf("RSA") != -1) {
            return useRSAECBEncrypt(forEncryption, key, sourceData);
        } else {
            throw new Exception("can not support this algorithm to crypto:" + algName.getMechanismType());
        }
    }

    public static byte[] rsaEncrypt(boolean isEncrypted, Key rsaKey, byte[] input) throws Exception {
        return useRSAECBEncrypt(isEncrypted, rsaKey.getEncoded(), input);
    }

    public static byte[] rsaEncryptByJNI(boolean isEncrypted, Key rsaKey, byte[] input) throws Exception {
        return isEncrypted ? encryptByJNI(input, rsaKey) : decryptByJNI(input, rsaKey);
    }

    private static byte[] addPKCS1Padding(byte[] srcData, RSAPublicKey pubKey) {
        int bitLen = pubKey.getModulus().bitLength();
        int blockLen = (bitLen + 7) / 8;
        byte[] block = new byte[blockLen];
        SecureRandom random = new SecureRandom();
        random.nextBytes(block);
        block[0] = 0;
        block[1] = 2;
        int inLen = srcData.length;

        for (int i = 2; i != block.length - inLen - 1; ++i) {
            while (block[i] == 0) {
                block[i] = (byte) random.nextInt();
            }
        }

        block[block.length - inLen - 1] = 0;
        System.arraycopy(srcData, 0, block, block.length - inLen, inLen);
        return block;
    }

    private static byte[] delPKCS1Padding(byte[] paddingData) throws Exception {
        byte type = paddingData[1];
        if (type != 1 && type != 2) {
            throw new InvalidCipherTextException("unknown block type");
        } else {
            int start;
            for (start = 2; start != paddingData.length; ++start) {
                byte pad = paddingData[start];
                if (pad == 0) {
                    break;
                }

                if (type == 1 && pad != -1) {
                    throw new InvalidCipherTextException("block padding incorrect");
                }
            }

            ++start;
            if (start <= paddingData.length && start >= HEADER_LENGTH) {
                byte[] result = new byte[paddingData.length - start];
                System.arraycopy(paddingData, start, result, 0, result.length);
                return result;
            } else {
                throw new InvalidCipherTextException("no data in block");
            }
        }
    }

    private static byte[] encryptByJNI(byte[] srcData, Key publicKey) throws Exception {
        if (srcData == null) {
            throw new Exception("the source data is null,can not encrypt!!!");
        } else {
            RSAPublicKey pubKey = (RSAPublicKey) publicKey;
            byte[] pkcs1PubKey = PKCS8ToPKCS1Util.RSAP8ToP1PubKey(pubKey);
            byte[] input = addPKCS1Padding(srcData, pubKey);
            int bitLen = pubKey.getModulus().bitLength();
            int blockLen = (bitLen + 7) / 8;
            byte[] output = new byte[blockLen];
            JNIRSA.dowithPublicKey(input, pkcs1PubKey, output);
            return output;
        }
    }

    private static byte[] decryptByJNI(byte[] encryptedData, Key privateKey) throws Exception {
        if (encryptedData == null) {
            throw new Exception("the encrypt data is null,can not decrypt!!!");
        } else {
            RSAPrivateKey priKey = (RSAPrivateKey) privateKey;
            byte[] pkcs1PriKey = PKCS8ToPKCS1Util.RSAP8ToP1PriKey(priKey);
            int bitLen = priKey.getModulus().bitLength();
            int blockLen = (bitLen + 7) / 8;
            byte[] output = new byte[blockLen];
            JNIRSA.dowithPrivateKey(encryptedData, pkcs1PriKey, output);
            return delPKCS1Padding(output);
        }
    }

    private static byte[] useRC4Encrypt(boolean useJNI, boolean isEncrypted, byte[] key, byte[] input) throws Exception {
        byte[] out = new byte[input.length];
        if (useJNI) {
            JNISymAlg jni = new JNISymAlg();
            int len1;
            byte[] plainText;
            if (isEncrypted) {
                jni.encryptInit(JNISymAlg.NID_rc4, key, (byte[]) null);
                plainText = new byte[input.length];
                len1 = jni.encryptProcess(input, 0, input.length, plainText, 0);
                jni.encryptFinal(plainText, len1);
                return plainText;
            } else {
                jni.decryptInit(JNISymAlg.NID_rc4, key, (byte[]) null);
                plainText = new byte[input.length];
                len1 = jni.decryptProcess(input, 0, input.length, plainText, 0);
                jni.decryptFinal(plainText, len1);
                return plainText;
            }
        } else {
            RC4Engine rc4 = new RC4Engine();
            KeyParameter param = new KeyParameter(key);
            rc4.init(isEncrypted, param);
            rc4.processBytes(input, 0, input.length, out, 0);
            return out;
        }
    }

    private static byte[] useDesedeECBEncrypt(boolean useJNI, boolean isEncrypted, byte[] key, byte[] input) throws Exception {
        int len1;
        int total;
        if (useJNI) {
            JNISymAlg jni = new JNISymAlg();
            byte[] plainText;
            if (isEncrypted) {
                jni.encryptInit(JNISymAlg.NID_des_ede3_ecb, key, (byte[]) null);
                plainText = new byte[input.length + (DES3_BLOCK_SIZE - input.length % DES3_BLOCK_SIZE)];
                len1 = jni.encryptProcess(input, 0, input.length, plainText, 0);
                jni.encryptFinal(plainText, len1);
                return plainText;
            } else {
                jni.decryptInit(JNISymAlg.NID_des_ede3_ecb, key, (byte[]) null);
                plainText = new byte[input.length];
                len1 = jni.decryptProcess(input, 0, input.length, plainText, 0);
                int len2 = jni.decryptFinal(plainText, len1);
                total = len1 + len2;
                byte[] out2 = new byte[total];
                System.arraycopy(plainText, 0, out2, 0, total);
                return out2;
            }
        } else {
            PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new DESedeEngine(), new PKCS7Padding());
            KeyParameter params = new KeyParameter(key);
            cipher.init(isEncrypted, params);
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

    private static byte[] useDesedeCBCEncrypt(boolean useJNI, boolean isEncrypted, byte[] key, byte[] input, byte[] iv) throws Exception {
        int len1;
        int total;
        if (useJNI) {
            JNISymAlg jni = new JNISymAlg();
            byte[] plainText;
            if (isEncrypted) {
                jni.encryptInit(JNISymAlg.NID_des_ede3_cbc, key, iv);
                plainText = new byte[input.length + (DES3_BLOCK_SIZE - input.length % DES3_BLOCK_SIZE)];
                len1 = jni.encryptProcess(input, 0, input.length, plainText, 0);
                jni.encryptFinal(plainText, len1);
                return plainText;
            } else {
                jni.decryptInit(JNISymAlg.NID_des_ede3_cbc, key, iv);
                plainText = new byte[input.length];
                len1 = jni.decryptProcess(input, 0, input.length, plainText, 0);
                int len2 = jni.decryptFinal(plainText, len1);
                total = len1 + len2;
                byte[] out2 = new byte[total];
                System.arraycopy(plainText, 0, out2, 0, total);
                return out2;
            }
        } else {
            PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new DESedeEngine()), new PKCS7Padding());
            ParametersWithIV param = new ParametersWithIV(new KeyParameter(key), iv);
            cipher.init(isEncrypted, param);
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

    private static byte[] useRSAECBEncrypt(boolean isEncrypted, byte[] key, byte[] input) throws Exception {
        RSAKeyParameters param = null;
        if (isEncrypted) {
            SubjectPublicKeyInfo pubKeyInfo = SubjectPublicKeyInfo.getInstance(key);
            param = (RSAKeyParameters) PublicKeyFactory.createKey(pubKeyInfo);
            AsymmetricBlockCipher eng = new RSAEngine();
            eng = new PKCS1Encoding(eng);
            eng.init(true, param);
            return eng.processBlock(input, 0, input.length);
        } else {
            param = (RSAKeyParameters) PrivateKeyFactory.createKey(key);
            AsymmetricBlockCipher eng = new RSAEngine();
            eng = new PKCS1Encoding(eng);
            eng.init(false, param);
            return eng.processBlock(input, 0, input.length);
        }
    }

    public static byte[] generateSecretKey(String symmetricAlgorithm) throws Exception {
        String contentEncryptionAlg = symmetricAlgorithm.toUpperCase();
        SecureRandom random = new SecureRandom();
        if (contentEncryptionAlg.indexOf("RC4") != -1) {
            byte[] keyBytes = new byte[16];
            random.nextBytes(keyBytes);
            return keyBytes;
        } else if (contentEncryptionAlg.indexOf("DESEDE") != -1) {
            DESedeKeyGenerator keyGen = new DESedeKeyGenerator();
            keyGen.init(new KeyGenerationParameters(random, 192));
            return keyGen.generateKey();
        } else {
            throw new Exception("can not generate such key:" + contentEncryptionAlg);
        }
    }

    public static byte[] generateIV() {
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[8];
        random.nextBytes(iv);
        return iv;
    }
}
