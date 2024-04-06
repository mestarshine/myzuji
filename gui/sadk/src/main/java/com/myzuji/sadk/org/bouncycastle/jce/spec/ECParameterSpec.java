package com.myzuji.sadk.org.bouncycastle.jce.spec;

import com.myzuji.sadk.org.bouncycastle.math.ec.ECCurve;
import com.myzuji.sadk.org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.security.spec.AlgorithmParameterSpec;

public class ECParameterSpec implements AlgorithmParameterSpec {
    private ECCurve curve;
    private byte[] seed;
    private ECPoint G;
    private BigInteger n;
    private BigInteger h;

    public ECParameterSpec(ECCurve curve, ECPoint G, BigInteger n) {
        this.curve = curve;
        this.G = G.normalize();
        this.n = n;
        this.h = BigInteger.valueOf(1L);
        this.seed = null;
    }

    public ECParameterSpec(ECCurve curve, ECPoint G, BigInteger n, BigInteger h) {
        this.curve = curve;
        this.G = G.normalize();
        this.n = n;
        this.h = h;
        this.seed = null;
    }

    public ECParameterSpec(ECCurve curve, ECPoint G, BigInteger n, BigInteger h, byte[] seed) {
        this.curve = curve;
        this.G = G.normalize();
        this.n = n;
        this.h = h;
        this.seed = seed;
    }

    public ECCurve getCurve() {
        return this.curve;
    }

    public ECPoint getG() {
        return this.G;
    }

    public BigInteger getN() {
        return this.n;
    }

    public BigInteger getH() {
        return this.h;
    }

    public byte[] getSeed() {
        return this.seed;
    }

    public boolean equals(Object o) {
        if (!(o instanceof ECParameterSpec)) {
            return false;
        } else {
            ECParameterSpec other = (ECParameterSpec) o;
            return this.getCurve().equals(other.getCurve()) && this.getG().equals(other.getG());
        }
    }

    public int hashCode() {
        return this.getCurve().hashCode() ^ this.getG().hashCode();
    }
}
