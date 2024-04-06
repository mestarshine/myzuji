package com.myzuji.sadk.org.bouncycastle.math.ec;

import java.math.BigInteger;

public class FixedPointCombMultiplier extends AbstractECMultiplier {
    public FixedPointCombMultiplier() {
    }

    protected ECPoint multiplyPositive(ECPoint p, BigInteger k) {
        ECCurve c = p.getCurve();
        int size = FixedPointUtil.getCombSize(c);
        if (k.bitLength() > size) {
            throw new IllegalStateException("fixed-point comb doesn't support scalars larger than the curve order");
        } else {
            int minWidth = this.getWidthForCombSize(size);
            FixedPointPreCompInfo info = FixedPointUtil.precompute(p, minWidth);
            ECPoint[] lookupTable = info.getPreComp();
            int width = info.getWidth();
            int d = (size + width - 1) / width;
            ECPoint R = c.getInfinity();
            int top = d * width - 1;

            for (int i = 0; i < d; ++i) {
                int index = 0;

                for (int j = top - i; j >= 0; j -= d) {
                    index <<= 1;
                    if (k.testBit(j)) {
                        index |= 1;
                    }
                }

                R = R.twicePlus(lookupTable[index]);
            }

            return R;
        }
    }

    protected int getWidthForCombSize(int combSize) {
        return combSize > 257 ? 6 : 5;
    }
}
