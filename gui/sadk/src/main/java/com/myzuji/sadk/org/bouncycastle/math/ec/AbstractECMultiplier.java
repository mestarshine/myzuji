package com.myzuji.sadk.org.bouncycastle.math.ec;

import java.math.BigInteger;

public abstract class AbstractECMultiplier implements ECMultiplier {
    public AbstractECMultiplier() {
    }

    public ECPoint multiply(ECPoint p, BigInteger k) {
        int sign = k.signum();
        if (sign != 0 && !p.isInfinity()) {
            ECPoint positive = this.multiplyPositive(p, k.abs());
            ECPoint result = sign > 0 ? positive : positive.negate();
            return ECAlgorithms.validatePoint(result);
        } else {
            return p.getCurve().getInfinity();
        }
    }

    protected abstract ECPoint multiplyPositive(ECPoint var1, BigInteger var2);
}
