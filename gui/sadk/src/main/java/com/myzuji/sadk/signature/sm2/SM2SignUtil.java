package com.myzuji.sadk.signature.sm2;

import com.myzuji.sadk.algorithm.common.PKIException;
import com.myzuji.sadk.algorithm.sm2.SM2PublicKey;

import java.security.PublicKey;

public class SM2SignUtil {
    public SM2SignUtil() {
    }

    public static boolean verify(byte[] sourceData, byte[] userId, byte[] signature, PublicKey pubKey) throws PKIException {
        SM2Signature signer = new SM2Signature();
        if (sourceData != null && signature != null) {
            signer.initVerify(pubKey);
            SM2PublicKey sm2PubKey = (SM2PublicKey) pubKey;
            byte[] z = sm2PubKey.calcZ(userId);
            signer.update(z, 0, z.length);
            return signer.verify(signature, sourceData);
        } else {
            return false;
        }
    }

    public static boolean verify(byte[] sourceData, byte[] signature, PublicKey pubKey) throws PKIException {
        SM2Signature signer = new SM2Signature();
        if (sourceData != null && signature != null) {
            signer.initVerify(pubKey);
            return signer.verify(signature, sourceData);
        } else {
            return false;
        }
    }
}

