package com.myzuji.sadk.org.bouncycastle.crypto.params;

import com.myzuji.sadk.org.bouncycastle.crypto.CipherParameters;

import java.math.BigInteger;

public class DHParameters implements CipherParameters {
    private static final int DEFAULT_MINIMUM_LENGTH = 160;
    private BigInteger g;
    private BigInteger p;
    private BigInteger q;
    private BigInteger j;
    private int m;
    private int l;
    private DHValidationParameters validation;

    private static int getDefaultMParam(int lParam) {
        if (lParam == 0) {
            return 160;
        } else {
            return lParam < 160 ? lParam : 160;
        }
    }

    public DHParameters(BigInteger p, BigInteger g) {
        this(p, g, (BigInteger) null, 0);
    }

    public DHParameters(BigInteger p, BigInteger g, BigInteger q) {
        this(p, g, q, 0);
    }

    public DHParameters(BigInteger p, BigInteger g, BigInteger q, int l) {
        this(p, g, q, getDefaultMParam(l), l, (BigInteger) null, (DHValidationParameters) null);
    }

    public DHParameters(BigInteger p, BigInteger g, BigInteger q, int m, int l) {
        this(p, g, q, m, l, (BigInteger) null, (DHValidationParameters) null);
    }

    public DHParameters(BigInteger p, BigInteger g, BigInteger q, BigInteger j, DHValidationParameters validation) {
        this(p, g, q, 160, 0, j, validation);
    }

    public DHParameters(BigInteger p, BigInteger g, BigInteger q, int m, int l, BigInteger j, DHValidationParameters validation) {
        if (l != 0) {
            BigInteger bigL = BigInteger.valueOf(2L ^ (long) (l - 1));
            if (bigL.compareTo(p) == 1) {
                throw new IllegalArgumentException("when l value specified, it must satisfy 2^(l-1) <= p");
            }

            if (l < m) {
                throw new IllegalArgumentException("when l value specified, it may not be less than m value");
            }
        }

        this.g = g;
        this.p = p;
        this.q = q;
        this.m = m;
        this.l = l;
        this.j = j;
        this.validation = validation;
    }

    public BigInteger getP() {
        return this.p;
    }

    public BigInteger getG() {
        return this.g;
    }

    public BigInteger getQ() {
        return this.q;
    }

    public BigInteger getJ() {
        return this.j;
    }

    public int getM() {
        return this.m;
    }

    public int getL() {
        return this.l;
    }

    public DHValidationParameters getValidationParameters() {
        return this.validation;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DHParameters)) {
            return false;
        } else {
            DHParameters pm = (DHParameters) obj;
            if (this.getQ() != null) {
                if (!this.getQ().equals(pm.getQ())) {
                    return false;
                }
            } else if (pm.getQ() != null) {
                return false;
            }

            return pm.getP().equals(this.p) && pm.getG().equals(this.g);
        }
    }

    public int hashCode() {
        return this.getP().hashCode() ^ this.getG().hashCode() ^ (this.getQ() != null ? this.getQ().hashCode() : 0);
    }
}
