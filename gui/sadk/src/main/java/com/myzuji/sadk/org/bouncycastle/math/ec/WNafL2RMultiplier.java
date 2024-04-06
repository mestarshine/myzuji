package com.myzuji.sadk.org.bouncycastle.math.ec;

import java.math.BigInteger;

public class WNafL2RMultiplier extends AbstractECMultiplier {
    public WNafL2RMultiplier() {
    }

    protected ECPoint multiplyPositive(ECPoint p, BigInteger k) {
        int width = Math.max(2, Math.min(16, this.getWindowSize(k.bitLength())));
        WNafPreCompInfo wnafPreCompInfo = WNafUtil.precompute(p, width, true);
        ECPoint[] preComp = wnafPreCompInfo.getPreComp();
        ECPoint[] preCompNeg = wnafPreCompInfo.getPreCompNeg();
        int[] wnaf = WNafUtil.generateCompactWindowNaf(width, k);
        ECPoint R = p.getCurve().getInfinity();
        int i = wnaf.length;
        int wi;
        int digit;
        int zeroes;
        int n;
        ECPoint[] table;
        if (i > 1) {
            --i;
            wi = wnaf[i];
            digit = wi >> 16;
            zeroes = wi & '\uffff';
            n = Math.abs(digit);
            table = digit < 0 ? preCompNeg : preComp;
            if (n << 2 < 1 << width) {
                int highest = LongArray.bitLengths[n];
                int scale = width - highest;
                int lowBits = n ^ 1 << highest - 1;
                int i1 = (1 << width - 1) - 1;
                int i2 = (lowBits << scale) + 1;
                R = table[i1 >>> 1].add(table[i2 >>> 1]);
                zeroes -= scale;
            } else {
                R = table[n >>> 1];
            }

            R = R.timesPow2(zeroes);
        }

        while (i > 0) {
            --i;
            wi = wnaf[i];
            digit = wi >> 16;
            zeroes = wi & '\uffff';
            n = Math.abs(digit);
            table = digit < 0 ? preCompNeg : preComp;
            ECPoint r = table[n >>> 1];
            R = R.twicePlus(r);
            R = R.timesPow2(zeroes);
        }

        return R;
    }

    protected int getWindowSize(int bits) {
        return WNafUtil.getWindowSize(bits);
    }
}
