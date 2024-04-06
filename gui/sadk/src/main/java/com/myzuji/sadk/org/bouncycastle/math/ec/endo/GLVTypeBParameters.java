package com.myzuji.sadk.org.bouncycastle.math.ec.endo;

import java.math.BigInteger;

public class GLVTypeBParameters {
    protected final BigInteger beta;
    protected final BigInteger lambda;
    protected final BigInteger[] v1;
    protected final BigInteger[] v2;
    protected final BigInteger g1;
    protected final BigInteger g2;
    protected final int bits;

    public GLVTypeBParameters(BigInteger beta, BigInteger lambda, BigInteger[] v1, BigInteger[] v2, BigInteger g1, BigInteger g2, int bits) {
        this.beta = beta;
        this.lambda = lambda;
        this.v1 = v1;
        this.v2 = v2;
        this.g1 = g1;
        this.g2 = g2;
        this.bits = bits;
    }

    public BigInteger getBeta() {
        return this.beta;
    }

    public BigInteger getLambda() {
        return this.lambda;
    }

    public BigInteger[] getV1() {
        return this.v1;
    }

    public BigInteger[] getV2() {
        return this.v2;
    }

    public BigInteger getG1() {
        return this.g1;
    }

    public BigInteger getG2() {
        return this.g2;
    }

    public int getBits() {
        return this.bits;
    }
}
