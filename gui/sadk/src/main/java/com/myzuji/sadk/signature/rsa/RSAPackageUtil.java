package com.myzuji.sadk.signature.rsa;

import com.myzuji.sadk.algorithm.common.PKIException;
import com.myzuji.sadk.algorithm.util.PKCS8ToPKCS1Util;
import com.myzuji.sadk.lib.crypto.jni.JNIRSA;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.DigestInfo;
import com.myzuji.sadk.org.bouncycastle.crypto.InvalidCipherTextException;
import com.myzuji.sadk.org.bouncycastle.crypto.params.RSAKeyParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import com.myzuji.sadk.org.bouncycastle.util.Arrays;

import java.security.Key;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RSAPackageUtil {
    private static int HEADER_LENGTH = 10;

    public RSAPackageUtil() {
    }

    private static byte[] addPKCS1Padding(byte[] derDigest, RSAPrivateKey priKey) {
        int bitLen = priKey.getModulus().bitLength();
        int blockLen = (bitLen + 7) / 8;
        byte[] block = new byte[blockLen];
        block[0] = 0;
        block[1] = 1;
        int inLen = derDigest.length;

        for (int i = 2; i != block.length - inLen - 1; ++i) {
            block[i] = -1;
        }

        block[block.length - inLen - 1] = 0;
        System.arraycopy(derDigest, 0, block, block.length - inLen, inLen);
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

    public static byte[] encrypt(byte[] digestData, Key privateKey) throws Exception {
        if (digestData == null) {
            throw new Exception("the digest data is null,can not encrypt!!!");
        } else {
            RSACrypt signer = new RSACrypt();
            RSAPrivateKey priKey = (RSAPrivateKey) privateKey;
            RSAKeyParameters priParameter = generatePrivateKeyParameter(priKey);
            signer.init(true, priParameter);

            try {
                return signer.encrypt(digestData);
            } catch (Exception var6) {
                Exception e = var6;
                throw new PKIException(PKIException.SIGN, PKIException.SIGN_DES, e);
            }
        }
    }

    public static byte[] encryptByJNI(byte[] derDigest, Key privateKey) throws Exception {
        if (derDigest == null) {
            throw new Exception("the digest data is null,can not encrypt!!!");
        } else {
            RSAPrivateKey priKey = (RSAPrivateKey) privateKey;
            byte[] input = addPKCS1Padding(derDigest, priKey);
            byte[] output = new byte[input.length];
            byte[] pkcs1PriKey = PKCS8ToPKCS1Util.RSAP8ToP1PriKey(priKey);
            JNIRSA.dowithPrivateKey(input, pkcs1PriKey, output);
            return output;
        }
    }

    public static byte[] decrypt(byte[] encryptedData, Key publicKey) throws Exception {
        if (encryptedData == null) {
            throw new Exception("the encrypt data is null,can not decrypt!!!");
        } else {
            RSACrypt signer = new RSACrypt();
            RSAPublicKey pubKey = (RSAPublicKey) publicKey;
            RSAKeyParameters pubParameter = generatePublicKeyParameter(pubKey);
            signer.init(false, pubParameter);
            return signer.decrypt(encryptedData);
        }
    }

    public static byte[] decryptByJNI(byte[] encryptedData, Key publicKey) throws Exception {
        if (encryptedData == null) {
            throw new Exception("the encrypt data is null,can not decrypt!!!");
        } else {
            RSAPublicKey pubKey = (RSAPublicKey) publicKey;
            byte[] pkcs1PubKey = PKCS8ToPKCS1Util.RSAP8ToP1PubKey(pubKey);
            int bitLen = pubKey.getModulus().bitLength();
            int blockLen = (bitLen + 7) / 8;
            byte[] output = new byte[blockLen];
            JNIRSA.dowithPublicKey(encryptedData, pkcs1PubKey, output);
            return delPKCS1Padding(output);
        }
    }

    public static boolean isRSAHashEqual(byte[] extractHash, byte[] calcuHash) {
        if (calcuHash != null && extractHash != null) {
            if (extractHash.length == calcuHash.length) {
                return Arrays.constantTimeAreEqual(extractHash, calcuHash);
            } else if (extractHash.length != calcuHash.length - 2) {
                return false;
            } else {
                DigestInfo dInfo = DigestInfo.getInstance(calcuHash);
                int len = dInfo.getDigest().length;
                int sigOffset = extractHash.length - len - 2;
                int expectedOffset = calcuHash.length - len - 2;
                calcuHash[1] = (byte) (calcuHash[1] - 2);
                calcuHash[3] = (byte) (calcuHash[3] - 2);
                int nonEqual = 0;

                int i;
                for (i = 0; i < len; ++i) {
                    nonEqual |= extractHash[sigOffset + i] ^ calcuHash[expectedOffset + i];
                }

                for (i = 0; i < sigOffset; ++i) {
                    nonEqual |= extractHash[i] ^ calcuHash[i];
                }

                return nonEqual == 0;
            }
        } else {
            return false;
        }
    }

    private static RSAKeyParameters generatePublicKeyParameter(RSAPublicKey key) {
        return new RSAKeyParameters(false, key.getModulus(), key.getPublicExponent());
    }

    private static RSAKeyParameters generatePrivateKeyParameter(RSAPrivateKey key) {
        if (key instanceof RSAPrivateCrtKey) {
            RSAPrivateCrtKey k = (RSAPrivateCrtKey) key;
            return new RSAPrivateCrtKeyParameters(k.getModulus(), k.getPublicExponent(), k.getPrivateExponent(), k.getPrimeP(), k.getPrimeQ(), k.getPrimeExponentP(), k.getPrimeExponentQ(), k.getCrtCoefficient());
        } else {
            RSAPrivateKey k = key;
            return new RSAKeyParameters(true, k.getModulus(), k.getPrivateExponent());
        }
    }
}
