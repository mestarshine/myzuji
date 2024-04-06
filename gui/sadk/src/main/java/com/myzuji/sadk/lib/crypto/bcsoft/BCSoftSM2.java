package com.myzuji.sadk.lib.crypto.bcsoft;

import com.myzuji.sadk.algorithm.common.PKIException;
import com.myzuji.sadk.algorithm.sm2.SM2Result;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Integer;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Sequence;
import com.myzuji.sadk.org.bouncycastle.asn1.sm2.ASN1SM2Signature;
import com.myzuji.sadk.org.bouncycastle.crypto.signers.SM2DSASigner;
import com.myzuji.sadk.org.bouncycastle.math.ec.ECPoint;
import com.myzuji.sadk.org.bouncycastle.util.BigIntegers;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

public final class BCSoftSM2 {
    public BCSoftSM2() {
    }

    public static final byte[] sign(byte[] hash, BigInteger userD) throws PKIException {
        return sign(hash, userD, false);
    }

    public static final byte[] sign(byte[] hash, BigInteger userD, boolean asn1Flag) throws PKIException {
        BigInteger[] signature = generateSignature(hash, userD);
        byte[] signValue = null;
        if (asn1Flag) {
            try {
                ASN1SM2Signature asn1SignResult = new ASN1SM2Signature(signature[0], signature[1]);
                signValue = asn1SignResult.getEncoded("DER");
            } catch (IOException var6) {
                IOException e = var6;
                throw new PKIException("signed failure", e);
            }
        } else {
            signValue = new byte[64];
            System.arraycopy(BigIntegers.asUnsignedByteArray(32, signature[0]), 0, signValue, 0, 32);
            System.arraycopy(BigIntegers.asUnsignedByteArray(32, signature[1]), 0, signValue, 32, 32);
        }

        return signValue;
    }


    public void sign(byte[] hash, BigInteger userD, SM2Result sm2Ret) {
        if (sm2Ret == null) {
            throw new SecurityException("null not allowed for sm2Ret");
        } else {
            try {
                BigInteger[] signature = generateSignature(hash, userD);
                sm2Ret.r = signature[0];
                sm2Ret.s = signature[1];
            } catch (PKIException var5) {
                PKIException e = var5;
                throw new SecurityException(e.getMessage());
            }
        }
    }

    private static BigInteger[] generateSignature(byte[] hash, BigInteger userD) throws PKIException {
        if (hash != null && hash.length == 32) {
            if (userD == null) {
                throw new SecurityException("null not allowed for userD");
            } else {
                try {
                    SM2DSASigner engine = new SM2DSASigner();
                    engine.initSign(userD, new SecureRandom());
                    return engine.generateSignature(hash);
                } catch (Exception var3) {
                    Exception e = var3;
                    throw new PKIException("signed failure", e);
                }
            }
        } else {
            throw new SecurityException("null/length not allowed for hash");
        }
    }

    public static final boolean verify(byte[] hash, byte[] signature, ECPoint userKey) {
        if (userKey == null) {
            throw new SecurityException("null not allowed for userKey");
        } else if (hash != null && hash.length == 32) {
            if (signature == null) {
                return false;
            } else {
                BigInteger r = null;
                BigInteger s = null;
                if (signature.length == 64) {
                    r = BigIntegers.fromUnsignedByteArray(signature, 0, 32);
                    s = BigIntegers.fromUnsignedByteArray(signature, 32, 32);
                } else {
                    if (signature.length <= 64) {
                        return false;
                    }

                    try {
                        ASN1Sequence sequence = ASN1Sequence.getInstance(signature);
                        ASN1Integer R = (ASN1Integer) sequence.getObjectAt(0);
                        ASN1Integer S = (ASN1Integer) sequence.getObjectAt(1);
                        r = R.getPositiveValue();
                        s = S.getPositiveValue();
                    } catch (Exception var8) {
                        return false;
                    }
                }

                return verify(hash, userKey, r, s);
            }
        } else {
            return false;
        }
    }

    public boolean verify(byte[] hash, ECPoint userKey, SM2Result sm2Ret) {
        boolean passed = false;
        if (sm2Ret == null) {
            passed = false;
        } else {
            passed = verify(hash, userKey, sm2Ret.r, sm2Ret.s);
        }

        return passed;
    }

    private static final boolean verify(byte[] hash, ECPoint userKey, BigInteger r, BigInteger s) {
        boolean passed = false;
        if (hash != null && hash.length == 32 && userKey != null && r != null && s != null) {
            SM2DSASigner engine = new SM2DSASigner();
            engine.initVerify(userKey);
            passed = engine.verifySignature(hash, r, s);
        } else {
            passed = false;
        }

        return passed;
    }
}
