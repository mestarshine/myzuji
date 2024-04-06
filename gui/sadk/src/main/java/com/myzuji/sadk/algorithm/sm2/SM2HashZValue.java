package com.myzuji.sadk.algorithm.sm2;

import com.myzuji.sadk.org.bouncycastle.jcajce.provider.asymmetric.sm.SM2Params;

import java.math.BigInteger;

public final class SM2HashZValue {
    public SM2HashZValue() {
    }

    public static byte[] getZa(BigInteger x, BigInteger y, byte[] userId) {
        return SM2Params.calcZ(x, y, userId);
    }

    public static byte[] getSM2Za(byte[] x, byte[] y, byte[] userId) {
        return SM2Params.calcZ(x, y, userId);
    }
}

