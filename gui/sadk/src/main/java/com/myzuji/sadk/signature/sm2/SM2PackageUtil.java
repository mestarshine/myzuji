package com.myzuji.sadk.signature.sm2;

import com.myzuji.sadk.algorithm.common.PKIException;
import com.myzuji.sadk.algorithm.sm2.SM2PrivateKey;
import com.myzuji.sadk.algorithm.sm2.SM2PublicKey;
import com.myzuji.sadk.algorithm.sm2.SM2Result;
import com.myzuji.sadk.algorithm.util.BigIntegerUtil;
import com.myzuji.sadk.lib.crypto.bcsoft.BCSoftSM2;
import com.myzuji.sadk.lib.crypto.jni.JNISM2;

import java.math.BigInteger;
import java.security.Key;

public class SM2PackageUtil {
    public SM2PackageUtil() {
    }

    public static byte[] encryptByBC(byte[] digestData, Key privateKey) throws Exception {
        byte[] sign = new byte[64];
        if (digestData != null && digestData.length == 32) {
            SM2PrivateKey key = null;
            if (privateKey instanceof SM2PrivateKey) {
                key = (SM2PrivateKey) privateKey;
                BCSoftSM2 bcSoftSM2 = new BCSoftSM2();
                SM2Result sm2Ret = new SM2Result();
                bcSoftSM2.sign(digestData, key.getDByInt(), sm2Ret);
                System.arraycopy(BigIntegerUtil.asUnsigned32ByteArray(sm2Ret.r), 0, sign, 0, 32);
                System.arraycopy(BigIntegerUtil.asUnsigned32ByteArray(sm2Ret.s), 0, sign, 32, 32);
                return sign;
            } else {
                throw new PKIException("The private key type is not sm2 type!");
            }
        } else {
            throw new Exception("the digest data is null or not 32 bytes!");
        }
    }

    public static boolean verifyByBC(byte[] digestData, byte[] signature, Key publicKey) throws Exception {
        byte[] r = new byte[32];
        byte[] s = new byte[32];
        SM2PublicKey sm2PubKey = null;
        if (publicKey instanceof SM2PublicKey) {
            sm2PubKey = (SM2PublicKey) publicKey;
        } else {
            sm2PubKey = new SM2PublicKey(publicKey.getEncoded());
        }

        if (digestData != null && digestData.length == 32) {
            if (signature != null && signature.length == 64) {
                System.arraycopy(signature, 0, r, 0, 32);
                System.arraycopy(signature, 32, s, 0, 32);
                BCSoftSM2 sm2 = new BCSoftSM2();
                SM2Result sm2Ret = new SM2Result();
                sm2Ret.r = new BigInteger(1, r);
                sm2Ret.s = new BigInteger(1, s);
                return sm2.verify(digestData, sm2PubKey.getQ(), sm2Ret);
            } else {
                throw new Exception("the signature data is null or not 64 bytes!");
            }
        } else {
            throw new Exception("the digest data is null or not 32 bytes!");
        }
    }

    public static final byte[] encryptByJNI(byte[] digestData, BigInteger da) throws Exception {
        byte[] sign = new byte[64];
        byte[] r = new byte[32];
        byte[] s = new byte[32];
        if (digestData != null && digestData.length == 32) {
            JNISM2.sign(digestData, BigIntegerUtil.asUnsigned32ByteArray(da), r, s);
            System.arraycopy(r, 0, sign, 0, 32);
            System.arraycopy(s, 0, sign, 32, 32);
            return sign;
        } else {
            throw new Exception("the digest data is null or not 32 bytes!");
        }
    }

    public static boolean verifyByJNI(byte[] digestData, byte[] signature, byte[] pubX, byte[] pubY) throws Exception {
        byte[] r = new byte[32];
        byte[] s = new byte[32];
        if (digestData != null && digestData.length == 32) {
            if (signature != null && signature.length == 64) {
                System.arraycopy(signature, 0, r, 0, 32);
                System.arraycopy(signature, 32, s, 0, 32);
                return JNISM2.verify(r, s, pubX, pubY, digestData);
            } else {
                throw new Exception("the signature data is null or not 64 bytes!");
            }
        } else {
            throw new Exception("the digest data is null or not 32 bytes!");
        }
    }
}
