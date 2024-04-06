package com.myzuji.sadk.algorithm.util;

import com.myzuji.sadk.algorithm.common.Mechanism;
import com.myzuji.sadk.algorithm.common.PKIException;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.DigestInfo;
import com.myzuji.sadk.system.Mechanisms;

public class HashEncoderUtil {
    public HashEncoderUtil() {
    }

    public static byte[] derEncoder(String signAlg, byte[] hash) throws PKIException {
        try {
            if (!"SM3".equals(signAlg) && !"SM-3".equals(signAlg) && !Mechanisms.isSM2WithSM3(signAlg)) {
                AlgorithmIdentifier digestAlgIdentifier = Mechanisms.getDigestAlgIdentifier(signAlg);
                if (digestAlgIdentifier == null) {
                    throw new PKIException(PKIException.DIGEST, PKIException.DIGEST_DES + " " + PKIException.NOT_SUP_DES + " " + signAlg);
                } else {
                    DigestInfo dInfo = new DigestInfo(digestAlgIdentifier, hash);
                    return dInfo.getEncoded("DER");
                }
            } else {
                return hash;
            }
        } catch (PKIException var4) {
            PKIException e = var4;
            throw e;
        } catch (Exception var5) {
            Exception e = var5;
            throw new PKIException("hash encoded failure", e);
        }
    }

    public static byte[] derEncoder(Mechanism mechanism, byte[] digest) throws PKIException {
        return derEncoder(mechanism.getMechanismType(), digest);
    }
}
