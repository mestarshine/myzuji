package com.myzuji.sadk.org.bouncycastle.crypto.params;

import com.myzuji.sadk.org.bouncycastle.crypto.CipherParameters;

import java.math.BigInteger;

public class ElGamalParameters implements CipherParameters {
    private BigInteger g;
    private BigInteger p;
    private int l;

    public ElGamalParameters(BigInteger p, BigInteger g) {
        this(p, g, 0);
    }

    public ElGamalParameters(BigInteger p, BigInteger g, int l) {
        this.g = g;
        this.p = p;
        this.l = l;
    }

    public BigInteger getP() {
        return this.p;
    }

    public BigInteger getG() {
        return this.g;
    }

    public int getL() {
        return this.l;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ElGamalParameters)) {
            return false;
        } else {
            ElGamalParameters pm = (ElGamalParameters) obj;
            return pm.getP().equals(this.p) && pm.getG().equals(this.g) && pm.getL() == this.l;
        }
    }

    public int hashCode() {
        return (this.getP().hashCode() ^ this.getG().hashCode()) + this.l;
    }
}
