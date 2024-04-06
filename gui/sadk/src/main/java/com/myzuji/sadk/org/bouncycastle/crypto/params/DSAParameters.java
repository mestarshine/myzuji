package com.myzuji.sadk.org.bouncycastle.crypto.params;

import com.myzuji.sadk.org.bouncycastle.crypto.CipherParameters;

import java.math.BigInteger;

public class DSAParameters implements CipherParameters {
    private BigInteger g;
    private BigInteger q;
    private BigInteger p;
    private DSAValidationParameters validation;

    public DSAParameters(BigInteger p, BigInteger q, BigInteger g) {
        this.g = g;
        this.p = p;
        this.q = q;
    }

    public DSAParameters(BigInteger p, BigInteger q, BigInteger g, DSAValidationParameters params) {
        this.g = g;
        this.p = p;
        this.q = q;
        this.validation = params;
    }

    public BigInteger getP() {
        return this.p;
    }

    public BigInteger getQ() {
        return this.q;
    }

    public BigInteger getG() {
        return this.g;
    }

    public DSAValidationParameters getValidationParameters() {
        return this.validation;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DSAParameters)) {
            return false;
        } else {
            DSAParameters pm = (DSAParameters) obj;
            return pm.getP().equals(this.p) && pm.getQ().equals(this.q) && pm.getG().equals(this.g);
        }
    }

    public int hashCode() {
        return this.getP().hashCode() ^ this.getQ().hashCode() ^ this.getG().hashCode();
    }
}
