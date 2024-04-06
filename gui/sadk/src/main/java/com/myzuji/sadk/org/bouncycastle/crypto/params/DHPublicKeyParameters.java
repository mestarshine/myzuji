package com.myzuji.sadk.org.bouncycastle.crypto.params;

import java.math.BigInteger;

public class DHPublicKeyParameters extends DHKeyParameters {
    private BigInteger y;

    public DHPublicKeyParameters(BigInteger y, DHParameters params) {
        super(false, params);
        this.y = y;
    }

    public BigInteger getY() {
        return this.y;
    }

    public int hashCode() {
        return this.y.hashCode() ^ super.hashCode();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DHPublicKeyParameters)) {
            return false;
        } else {
            DHPublicKeyParameters other = (DHPublicKeyParameters) obj;
            return other.getY().equals(this.y) && super.equals(obj);
        }
    }
}
