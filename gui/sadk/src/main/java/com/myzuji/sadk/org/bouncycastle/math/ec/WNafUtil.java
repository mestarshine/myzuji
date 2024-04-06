package com.myzuji.sadk.org.bouncycastle.math.ec;

import java.math.BigInteger;

public abstract class WNafUtil {
    public static final String PRECOMP_NAME = "bc_wnaf";
    private static final int[] DEFAULT_WINDOW_SIZE_CUTOFFS = new int[]{13, 41, 121, 337, 897, 2305};
    private static final byte[] EMPTY_BYTES = new byte[0];
    private static final int[] EMPTY_INTS = new int[0];

    public WNafUtil() {
    }

    public static int[] generateCompactNaf(BigInteger k) {
        if (k.bitLength() >>> 16 != 0) {
            throw new IllegalArgumentException("'k' must have bitlength < 2^16");
        } else if (k.signum() == 0) {
            return EMPTY_INTS;
        } else {
            BigInteger _3k = k.shiftLeft(1).add(k);
            int bits = _3k.bitLength();
            int[] naf = new int[bits >> 1];
            BigInteger diff = _3k.xor(k);
            int highBit = bits - 1;
            int length = 0;
            int zeroes = 0;

            for (int i = 1; i < highBit; ++i) {
                if (!diff.testBit(i)) {
                    ++zeroes;
                } else {
                    int digit = k.testBit(i) ? -1 : 1;
                    naf[length++] = digit << 16 | zeroes;
                    zeroes = 1;
                    ++i;
                }
            }

            naf[length++] = 65536 | zeroes;
            if (naf.length > length) {
                naf = trim(naf, length);
            }

            return naf;
        }
    }

    public static int[] generateCompactWindowNaf(int width, BigInteger k) {
        if (width == 2) {
            return generateCompactNaf(k);
        } else if (width >= 2 && width <= 16) {
            if (k.bitLength() >>> 16 != 0) {
                throw new IllegalArgumentException("'k' must have bitlength < 2^16");
            } else if (k.signum() == 0) {
                return EMPTY_INTS;
            } else {
                int[] wnaf = new int[k.bitLength() / width + 1];
                int pow2 = 1 << width;
                int mask = pow2 - 1;
                int sign = pow2 >>> 1;
                boolean carry = false;
                int length = 0;
                int pos = 0;

                while (pos <= k.bitLength()) {
                    if (k.testBit(pos) == carry) {
                        ++pos;
                    } else {
                        k = k.shiftRight(pos);
                        int digit = k.intValue() & mask;
                        if (carry) {
                            ++digit;
                        }

                        carry = (digit & sign) != 0;
                        if (carry) {
                            digit -= pow2;
                        }

                        int zeroes = length > 0 ? pos - 1 : pos;
                        wnaf[length++] = digit << 16 | zeroes;
                        pos = width;
                    }
                }

                if (wnaf.length > length) {
                    wnaf = trim(wnaf, length);
                }

                return wnaf;
            }
        } else {
            throw new IllegalArgumentException("'width' must be in the range [2, 16]");
        }
    }

    public static byte[] generateJSF(BigInteger g, BigInteger h) {
        int digits = Math.max(g.bitLength(), h.bitLength()) + 1;
        byte[] jsf = new byte[digits];
        BigInteger k0 = g;
        BigInteger k1 = h;
        int j = 0;
        int d0 = 0;
        int d1 = 0;

        int u0 = 0;
        int u1 = 0;
        for (int offset = 0; (d0 | d1) != 0 || k0.bitLength() > offset || k1.bitLength() > offset; jsf[j++] = (byte) (u0 << 4 | u1 & 15)) {
            int n0 = (k0.intValue() >>> offset) + d0 & 7;
            int n1 = (k1.intValue() >>> offset) + d1 & 7;
            u0 = n0 & 1;
            if (u0 != 0) {
                u0 -= n0 & 2;
                if (n0 + u0 == 4 && (n1 & 3) == 2) {
                    u0 = -u0;
                }
            }

            u1 = n1 & 1;
            if (u1 != 0) {
                u1 -= n1 & 2;
                if (n1 + u1 == 4 && (n0 & 3) == 2) {
                    u1 = -u1;
                }
            }

            if (d0 << 1 == 1 + u0) {
                d0 ^= 1;
            }

            if (d1 << 1 == 1 + u1) {
                d1 ^= 1;
            }

            ++offset;
            if (offset == 30) {
                offset = 0;
                k0 = k0.shiftRight(30);
                k1 = k1.shiftRight(30);
            }
        }

        if (jsf.length > j) {
            jsf = trim(jsf, j);
        }

        return jsf;
    }

    public static byte[] generateNaf(BigInteger k) {
        if (k.signum() == 0) {
            return EMPTY_BYTES;
        } else {
            BigInteger _3k = k.shiftLeft(1).add(k);
            int digits = _3k.bitLength() - 1;
            byte[] naf = new byte[digits];
            BigInteger diff = _3k.xor(k);

            for (int i = 1; i < digits; ++i) {
                if (diff.testBit(i)) {
                    naf[i - 1] = (byte) (k.testBit(i) ? -1 : 1);
                    ++i;
                }
            }

            naf[digits - 1] = 1;
            return naf;
        }
    }

    public static byte[] generateWindowNaf(int width, BigInteger k) {
        if (width == 2) {
            return generateNaf(k);
        } else if (width >= 2 && width <= 8) {
            if (k.signum() == 0) {
                return EMPTY_BYTES;
            } else {
                byte[] wnaf = new byte[k.bitLength() + 1];
                int pow2 = 1 << width;
                int mask = pow2 - 1;
                int sign = pow2 >>> 1;
                boolean carry = false;
                int length = 0;
                int pos = 0;

                while (pos <= k.bitLength()) {
                    if (k.testBit(pos) == carry) {
                        ++pos;
                    } else {
                        k = k.shiftRight(pos);
                        int digit = k.intValue() & mask;
                        if (carry) {
                            ++digit;
                        }

                        carry = (digit & sign) != 0;
                        if (carry) {
                            digit -= pow2;
                        }

                        length += length > 0 ? pos - 1 : pos;
                        wnaf[length++] = (byte) digit;
                        pos = width;
                    }
                }

                if (wnaf.length > length) {
                    wnaf = trim(wnaf, length);
                }

                return wnaf;
            }
        } else {
            throw new IllegalArgumentException("'width' must be in the range [2, 8]");
        }
    }

    public static int getNafWeight(BigInteger k) {
        if (k.signum() == 0) {
            return 0;
        } else {
            BigInteger _3k = k.shiftLeft(1).add(k);
            BigInteger diff = _3k.xor(k);
            return diff.bitCount();
        }
    }

    public static WNafPreCompInfo getWNafPreCompInfo(ECPoint p) {
        return getWNafPreCompInfo(p.getCurve().getPreCompInfo(p, "bc_wnaf"));
    }

    public static WNafPreCompInfo getWNafPreCompInfo(PreCompInfo preCompInfo) {
        return preCompInfo != null && preCompInfo instanceof WNafPreCompInfo ? (WNafPreCompInfo) preCompInfo : new WNafPreCompInfo();
    }

    public static int getWindowSize(int bits) {
        return getWindowSize(bits, DEFAULT_WINDOW_SIZE_CUTOFFS);
    }

    public static int getWindowSize(int bits, int[] windowSizeCutoffs) {
        int w;
        for (w = 0; w < windowSizeCutoffs.length && bits >= windowSizeCutoffs[w]; ++w) {
        }

        return w + 2;
    }

    public static ECPoint mapPointWithPrecomp(ECPoint p, int width, boolean includeNegated, ECPointMap pointMap) {
        ECCurve c = p.getCurve();
        WNafPreCompInfo wnafPreCompP = precompute(p, width, includeNegated);
        ECPoint q = pointMap.map(p);
        WNafPreCompInfo wnafPreCompQ = getWNafPreCompInfo(c.getPreCompInfo(q, "bc_wnaf"));
        ECPoint twiceP = wnafPreCompP.getTwice();
        if (twiceP != null) {
            ECPoint twiceQ = pointMap.map(twiceP);
            wnafPreCompQ.setTwice(twiceQ);
        }

        ECPoint[] preCompP = wnafPreCompP.getPreComp();
        ECPoint[] preCompQ = new ECPoint[preCompP.length];

        for (int i = 0; i < preCompP.length; ++i) {
            preCompQ[i] = pointMap.map(preCompP[i]);
        }

        wnafPreCompQ.setPreComp(preCompQ);
        if (includeNegated) {
            ECPoint[] preCompNegQ = new ECPoint[preCompQ.length];

            for (int i = 0; i < preCompNegQ.length; ++i) {
                preCompNegQ[i] = preCompQ[i].negate();
            }

            wnafPreCompQ.setPreCompNeg(preCompNegQ);
        }

        c.setPreCompInfo(q, "bc_wnaf", wnafPreCompQ);
        return q;
    }

    public static WNafPreCompInfo precompute(ECPoint p, int width, boolean includeNegated) {
        ECCurve c = p.getCurve();
        WNafPreCompInfo wnafPreCompInfo = getWNafPreCompInfo(c.getPreCompInfo(p, "bc_wnaf"));
        ECPoint[] preComp = wnafPreCompInfo.getPreComp();
        if (preComp == null) {
            preComp = new ECPoint[]{p};
        }

        int preCompLen = preComp.length;
        int reqPreCompLen = 1 << Math.max(0, width - 2);
        int pos;
        if (preCompLen < reqPreCompLen) {
            preComp = resizeTable(preComp, reqPreCompLen);
            if (reqPreCompLen == 2) {
                preComp[1] = preComp[0].threeTimes();
            } else {
                ECPoint twiceP = wnafPreCompInfo.getTwice();
                if (twiceP == null) {
                    twiceP = preComp[0].twice();
                    wnafPreCompInfo.setTwice(twiceP);
                }

                for (pos = preCompLen; pos < reqPreCompLen; ++pos) {
                    preComp[pos] = twiceP.add(preComp[pos - 1]);
                }
            }

            c.normalizeAll(preComp);
        }

        wnafPreCompInfo.setPreComp(preComp);
        if (includeNegated) {
            ECPoint[] preCompNeg = wnafPreCompInfo.getPreCompNeg();
            if (preCompNeg == null) {
                pos = 0;
                preCompNeg = new ECPoint[reqPreCompLen];
            } else {
                pos = preCompNeg.length;
                if (pos < reqPreCompLen) {
                    preCompNeg = resizeTable(preCompNeg, reqPreCompLen);
                }
            }

            while (pos < reqPreCompLen) {
                preCompNeg[pos] = preComp[pos].negate();
                ++pos;
            }

            wnafPreCompInfo.setPreCompNeg(preCompNeg);
        }

        c.setPreCompInfo(p, "bc_wnaf", wnafPreCompInfo);
        return wnafPreCompInfo;
    }

    private static byte[] trim(byte[] a, int length) {
        byte[] result = new byte[length];
        System.arraycopy(a, 0, result, 0, result.length);
        return result;
    }

    private static int[] trim(int[] a, int length) {
        int[] result = new int[length];
        System.arraycopy(a, 0, result, 0, result.length);
        return result;
    }

    private static ECPoint[] resizeTable(ECPoint[] a, int length) {
        ECPoint[] result = new ECPoint[length];
        System.arraycopy(a, 0, result, 0, a.length);
        return result;
    }
}
