package com.myzuji.sadk.org.bouncycastle.math.ec;

import com.myzuji.sadk.org.bouncycastle.math.ec.endo.ECEndomorphism;
import com.myzuji.sadk.org.bouncycastle.math.ec.endo.GLVEndomorphism;
import com.myzuji.sadk.org.bouncycastle.math.field.FiniteField;
import com.myzuji.sadk.org.bouncycastle.math.field.PolynomialExtensionField;

import java.math.BigInteger;

public class ECAlgorithms {
    public ECAlgorithms() {
    }

    public static boolean isF2mCurve(ECCurve c) {
        FiniteField field = c.getField();
        return field.getDimension() > 1 && field.getCharacteristic().equals(ECConstants.TWO) && field instanceof PolynomialExtensionField;
    }

    public static boolean isFpCurve(ECCurve c) {
        return c.getField().getDimension() == 1;
    }

    public static ECPoint sumOfMultiplies(ECPoint[] ps, BigInteger[] ks) {
        if (ps != null && ks != null && ps.length == ks.length && ps.length >= 1) {
            int count = ps.length;
            switch (count) {
                case 1:
                    return ps[0].multiply(ks[0]);
                case 2:
                    return sumOfTwoMultiplies(ps[0], ks[0], ps[1], ks[1]);
                default:
                    ECPoint p = ps[0];
                    ECCurve c = p.getCurve();
                    ECPoint[] imported = new ECPoint[count];
                    imported[0] = p;

                    for (int i = 1; i < count; ++i) {
                        imported[i] = importPoint(c, ps[i]);
                    }

                    ECEndomorphism endomorphism = c.getEndomorphism();
                    return endomorphism instanceof GLVEndomorphism ? validatePoint(implSumOfMultipliesGLV(imported, ks, (GLVEndomorphism) endomorphism)) : validatePoint(implSumOfMultiplies(imported, ks));
            }
        } else {
            throw new IllegalArgumentException("point and scalar arrays should be non-null, and of equal, non-zero, length");
        }
    }

    public static ECPoint sumOfTwoMultiplies(ECPoint P, BigInteger a, ECPoint Q, BigInteger b) {
        ECCurve cp = P.getCurve();
        Q = importPoint(cp, Q);
        if (cp instanceof ECCurve.F2m) {
            ECCurve.F2m f2mCurve = (ECCurve.F2m) cp;
            if (f2mCurve.isKoblitz()) {
                return validatePoint(P.multiply(a).add(Q.multiply(b)));
            }
        }

        ECEndomorphism endomorphism = cp.getEndomorphism();
        return endomorphism instanceof GLVEndomorphism ? validatePoint(implSumOfMultipliesGLV(new ECPoint[]{P, Q}, new BigInteger[]{a, b}, (GLVEndomorphism) endomorphism)) : validatePoint(implShamirsTrickWNaf(P, a, Q, b));
    }

    public static ECPoint shamirsTrick(ECPoint P, BigInteger k, ECPoint Q, BigInteger l) {
        ECCurve cp = P.getCurve();
        Q = importPoint(cp, Q);
        return validatePoint(implShamirsTrickJsf(P, k, Q, l));
    }

    public static ECPoint importPoint(ECCurve c, ECPoint p) {
        ECCurve cp = p.getCurve();
        if (!c.equals(cp)) {
            throw new IllegalArgumentException("Point must be on the same curve");
        } else {
            return c.importPoint(p);
        }
    }

    public static void montgomeryTrick(ECFieldElement[] zs, int off, int len) {
        ECFieldElement[] c = new ECFieldElement[len];
        c[0] = zs[off];
        int i = 0;

        while (true) {
            ++i;
            if (i >= len) {
                --i;

                ECFieldElement u;
                ECFieldElement tmp;
                for (u = c[i].invert(); i > 0; u = u.multiply(tmp)) {
                    int j = off + i--;
                    tmp = zs[j];
                    zs[j] = c[i].multiply(u);
                }

                zs[off] = u;
                return;
            }

            c[i] = c[i - 1].multiply(zs[off + i]);
        }
    }

    public static ECPoint referenceMultiply(ECPoint p, BigInteger k) {
        BigInteger x = k.abs();
        ECPoint q = p.getCurve().getInfinity();
        int t = x.bitLength();
        if (t > 0) {
            if (x.testBit(0)) {
                q = p;
            }

            for (int i = 1; i < t; ++i) {
                p = p.twice();
                if (x.testBit(i)) {
                    q = q.add(p);
                }
            }
        }

        return k.signum() < 0 ? q.negate() : q;
    }

    public static ECPoint validatePoint(ECPoint p) {
        if (!p.isValid()) {
            throw new IllegalArgumentException("Invalid point");
        } else {
            return p;
        }
    }

    static ECPoint implShamirsTrickJsf(ECPoint P, BigInteger k, ECPoint Q, BigInteger l) {
        ECCurve curve = P.getCurve();
        ECPoint infinity = curve.getInfinity();
        ECPoint PaddQ = P.add(Q);
        ECPoint PsubQ = P.subtract(Q);
        ECPoint[] points = new ECPoint[]{Q, PsubQ, P, PaddQ};
        curve.normalizeAll(points);
        ECPoint[] table = new ECPoint[]{points[3].negate(), points[2].negate(), points[1].negate(), points[0].negate(), infinity, points[0], points[1], points[2], points[3]};
        byte[] jsf = WNafUtil.generateJSF(k, l);
        ECPoint R = infinity;
        int i = jsf.length;

        while (true) {
            --i;
            if (i < 0) {
                return R;
            }

            int jsfi = jsf[i];
            int kDigit = jsfi << 24 >> 28;
            int lDigit = jsfi << 28 >> 28;
            int index = 4 + kDigit * 3 + lDigit;
            R = R.twicePlus(table[index]);
        }
    }

    static ECPoint implShamirsTrickWNaf(ECPoint P, BigInteger k, ECPoint Q, BigInteger l) {
        boolean negK = k.signum() < 0;
        boolean negL = l.signum() < 0;
        k = k.abs();
        l = l.abs();
        int widthP = Math.max(2, Math.min(16, WNafUtil.getWindowSize(k.bitLength())));
        int widthQ = Math.max(2, Math.min(16, WNafUtil.getWindowSize(l.bitLength())));
        WNafPreCompInfo infoP = WNafUtil.precompute(P, widthP, true);
        WNafPreCompInfo infoQ = WNafUtil.precompute(Q, widthQ, true);
        ECPoint[] preCompP = negK ? infoP.getPreCompNeg() : infoP.getPreComp();
        ECPoint[] preCompQ = negL ? infoQ.getPreCompNeg() : infoQ.getPreComp();
        ECPoint[] preCompNegP = negK ? infoP.getPreComp() : infoP.getPreCompNeg();
        ECPoint[] preCompNegQ = negL ? infoQ.getPreComp() : infoQ.getPreCompNeg();
        byte[] wnafP = WNafUtil.generateWindowNaf(widthP, k);
        byte[] wnafQ = WNafUtil.generateWindowNaf(widthQ, l);
        return implShamirsTrickWNaf(preCompP, preCompNegP, wnafP, preCompQ, preCompNegQ, wnafQ);
    }

    static ECPoint implShamirsTrickWNaf(ECPoint P, BigInteger k, ECPointMap pointMapQ, BigInteger l) {
        boolean negK = k.signum() < 0;
        boolean negL = l.signum() < 0;
        k = k.abs();
        l = l.abs();
        int width = Math.max(2, Math.min(16, WNafUtil.getWindowSize(Math.max(k.bitLength(), l.bitLength()))));
        ECPoint Q = WNafUtil.mapPointWithPrecomp(P, width, true, pointMapQ);
        WNafPreCompInfo infoP = WNafUtil.getWNafPreCompInfo(P);
        WNafPreCompInfo infoQ = WNafUtil.getWNafPreCompInfo(Q);
        ECPoint[] preCompP = negK ? infoP.getPreCompNeg() : infoP.getPreComp();
        ECPoint[] preCompQ = negL ? infoQ.getPreCompNeg() : infoQ.getPreComp();
        ECPoint[] preCompNegP = negK ? infoP.getPreComp() : infoP.getPreCompNeg();
        ECPoint[] preCompNegQ = negL ? infoQ.getPreComp() : infoQ.getPreCompNeg();
        byte[] wnafP = WNafUtil.generateWindowNaf(width, k);
        byte[] wnafQ = WNafUtil.generateWindowNaf(width, l);
        return implShamirsTrickWNaf(preCompP, preCompNegP, wnafP, preCompQ, preCompNegQ, wnafQ);
    }

    private static ECPoint implShamirsTrickWNaf(ECPoint[] preCompP, ECPoint[] preCompNegP, byte[] wnafP, ECPoint[] preCompQ, ECPoint[] preCompNegQ, byte[] wnafQ) {
        int len = Math.max(wnafP.length, wnafQ.length);
        ECCurve curve = preCompP[0].getCurve();
        ECPoint infinity = curve.getInfinity();
        ECPoint R = infinity;
        int zeroes = 0;

        for (int i = len - 1; i >= 0; --i) {
            int wiP = i < wnafP.length ? wnafP[i] : 0;
            int wiQ = i < wnafQ.length ? wnafQ[i] : 0;
            if ((wiP | wiQ) == 0) {
                ++zeroes;
            } else {
                ECPoint r = infinity;
                int nQ;
                ECPoint[] tableQ;
                if (wiP != 0) {
                    nQ = Math.abs(wiP);
                    tableQ = wiP < 0 ? preCompNegP : preCompP;
                    r = r.add(tableQ[nQ >>> 1]);
                }

                if (wiQ != 0) {
                    nQ = Math.abs(wiQ);
                    tableQ = wiQ < 0 ? preCompNegQ : preCompQ;
                    r = r.add(tableQ[nQ >>> 1]);
                }

                if (zeroes > 0) {
                    R = R.timesPow2(zeroes);
                    zeroes = 0;
                }

                R = R.twicePlus(r);
            }
        }

        if (zeroes > 0) {
            R = R.timesPow2(zeroes);
        }

        return R;
    }

    static ECPoint implSumOfMultiplies(ECPoint[] ps, BigInteger[] ks) {
        int count = ps.length;
        boolean[] negs = new boolean[count];
        WNafPreCompInfo[] infos = new WNafPreCompInfo[count];
        byte[][] wnafs = new byte[count][];

        for (int i = 0; i < count; ++i) {
            BigInteger ki = ks[i];
            negs[i] = ki.signum() < 0;
            ki = ki.abs();
            int width = Math.max(2, Math.min(16, WNafUtil.getWindowSize(ki.bitLength())));
            infos[i] = WNafUtil.precompute(ps[i], width, true);
            wnafs[i] = WNafUtil.generateWindowNaf(width, ki);
        }

        return implSumOfMultiplies(negs, infos, wnafs);
    }

    static ECPoint implSumOfMultipliesGLV(ECPoint[] ps, BigInteger[] ks, GLVEndomorphism glvEndomorphism) {
        BigInteger n = ps[0].getCurve().getOrder();
        int len = ps.length;
        BigInteger[] abs = new BigInteger[len << 1];
        int i = 0;

        for (int j = 0; i < len; ++i) {
            BigInteger[] ab = glvEndomorphism.decomposeScalar(ks[i].mod(n));
            abs[j++] = ab[0];
            abs[j++] = ab[1];
        }

        ECPointMap pointMap = glvEndomorphism.getPointMap();
        if (glvEndomorphism.hasEfficientPointMap()) {
            return implSumOfMultiplies(ps, pointMap, abs);
        } else {
            ECPoint[] pqs = new ECPoint[len << 1];
            i = 0;

            for (int j = 0; i < len; ++i) {
                ECPoint p = ps[i];
                ECPoint q = pointMap.map(p);
                pqs[j++] = p;
                pqs[j++] = q;
            }

            return implSumOfMultiplies(pqs, abs);
        }
    }

    static ECPoint implSumOfMultiplies(ECPoint[] ps, ECPointMap pointMap, BigInteger[] ks) {
        int halfCount = ps.length;
        int fullCount = halfCount << 1;
        boolean[] negs = new boolean[fullCount];
        WNafPreCompInfo[] infos = new WNafPreCompInfo[fullCount];
        byte[][] wnafs = new byte[fullCount][];

        for (int i = 0; i < halfCount; ++i) {
            int j0 = i << 1;
            int j1 = j0 + 1;
            BigInteger kj0 = ks[j0];
            negs[j0] = kj0.signum() < 0;
            kj0 = kj0.abs();
            BigInteger kj1 = ks[j1];
            negs[j1] = kj1.signum() < 0;
            kj1 = kj1.abs();
            int width = Math.max(2, Math.min(16, WNafUtil.getWindowSize(Math.max(kj0.bitLength(), kj1.bitLength()))));
            ECPoint P = ps[i];
            ECPoint Q = WNafUtil.mapPointWithPrecomp(P, width, true, pointMap);
            infos[j0] = WNafUtil.getWNafPreCompInfo(P);
            infos[j1] = WNafUtil.getWNafPreCompInfo(Q);
            wnafs[j0] = WNafUtil.generateWindowNaf(width, kj0);
            wnafs[j1] = WNafUtil.generateWindowNaf(width, kj1);
        }

        return implSumOfMultiplies(negs, infos, wnafs);
    }

    private static ECPoint implSumOfMultiplies(boolean[] negs, WNafPreCompInfo[] infos, byte[][] wnafs) {
        int len = 0;
        int count = wnafs.length;

        for (int i = 0; i < count; ++i) {
            len = Math.max(len, wnafs[i].length);
        }

        ECCurve curve = infos[0].getPreComp()[0].getCurve();
        ECPoint infinity = curve.getInfinity();
        ECPoint R = infinity;
        int zeroes = 0;

        for (int i = len - 1; i >= 0; --i) {
            ECPoint r = infinity;

            for (int j = 0; j < count; ++j) {
                byte[] wnaf = wnafs[j];
                int wi = i < wnaf.length ? wnaf[i] : 0;
                if (wi != 0) {
                    int n = Math.abs(wi);
                    WNafPreCompInfo info = infos[j];
                    ECPoint[] table = wi < 0 == negs[j] ? info.getPreComp() : info.getPreCompNeg();
                    r = r.add(table[n >>> 1]);
                }
            }

            if (r == infinity) {
                ++zeroes;
            } else {
                if (zeroes > 0) {
                    R = R.timesPow2(zeroes);
                    zeroes = 0;
                }

                R = R.twicePlus(r);
            }
        }

        if (zeroes > 0) {
            R = R.timesPow2(zeroes);
        }

        return R;
    }
}
