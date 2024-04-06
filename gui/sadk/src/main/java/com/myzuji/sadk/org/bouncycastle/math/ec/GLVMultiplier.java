package com.myzuji.sadk.org.bouncycastle.math.ec;

import com.myzuji.sadk.org.bouncycastle.math.ec.endo.GLVEndomorphism;

import java.math.BigInteger;

public class GLVMultiplier extends AbstractECMultiplier {
    protected final ECCurve curve;
    protected final GLVEndomorphism glvEndomorphism;

    public GLVMultiplier(ECCurve curve, GLVEndomorphism glvEndomorphism) {
        if (curve != null && curve.getOrder() != null) {
            this.curve = curve;
            this.glvEndomorphism = glvEndomorphism;
        } else {
            throw new IllegalArgumentException("Need curve with known group order");
        }
    }

    protected ECPoint multiplyPositive(ECPoint p, BigInteger k) {
        if (!this.curve.equals(p.getCurve())) {
            throw new IllegalStateException();
        } else {
            BigInteger n = p.getCurve().getOrder();
            BigInteger[] ab = this.glvEndomorphism.decomposeScalar(k.mod(n));
            BigInteger a = ab[0];
            BigInteger b = ab[1];
            ECPointMap pointMap = this.glvEndomorphism.getPointMap();
            return this.glvEndomorphism.hasEfficientPointMap() ? ECAlgorithms.implShamirsTrickWNaf(p, a, pointMap, b) : ECAlgorithms.implShamirsTrickWNaf(p, a, pointMap.map(p), b);
        }
    }
}
