package com.myzuji.sadk.org.bouncycastle.crypto.params;

import java.math.BigInteger;

public class ElGamalPrivateKeyParameters extends ElGamalKeyParameters {
    private BigInteger x;

    public ElGamalPrivateKeyParameters(BigInteger x, ElGamalParameters params) {
        super(true, params);
        this.x = x;
    }

    public BigInteger getX() {
        return this.x;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ElGamalPrivateKeyParameters)) {
            return false;
        } else {
            ElGamalPrivateKeyParameters pKey = (ElGamalPrivateKeyParameters) obj;
            return !pKey.getX().equals(this.x) ? false : super.equals(obj);
        }
    }

    public int hashCode() {
        return this.getX().hashCode();
    }
}
