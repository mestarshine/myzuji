package com.myzuji.sadk.org.bouncycastle.math.raw;

import com.myzuji.sadk.org.bouncycastle.util.Pack;

import java.math.BigInteger;

public abstract class Nat192 {
    private static final long M = 4294967295L;

    public Nat192() {
    }

    public static int add(int[] x, int[] y, int[] z) {
        long c = 0L;
        c += ((long) x[0] & 4294967295L) + ((long) y[0] & 4294967295L);
        z[0] = (int) c;
        c >>>= 32;
        c += ((long) x[1] & 4294967295L) + ((long) y[1] & 4294967295L);
        z[1] = (int) c;
        c >>>= 32;
        c += ((long) x[2] & 4294967295L) + ((long) y[2] & 4294967295L);
        z[2] = (int) c;
        c >>>= 32;
        c += ((long) x[3] & 4294967295L) + ((long) y[3] & 4294967295L);
        z[3] = (int) c;
        c >>>= 32;
        c += ((long) x[4] & 4294967295L) + ((long) y[4] & 4294967295L);
        z[4] = (int) c;
        c >>>= 32;
        c += ((long) x[5] & 4294967295L) + ((long) y[5] & 4294967295L);
        z[5] = (int) c;
        c >>>= 32;
        return (int) c;
    }

    public static int addBothTo(int[] x, int[] y, int[] z) {
        long c = 0L;
        c += ((long) x[0] & 4294967295L) + ((long) y[0] & 4294967295L) + ((long) z[0] & 4294967295L);
        z[0] = (int) c;
        c >>>= 32;
        c += ((long) x[1] & 4294967295L) + ((long) y[1] & 4294967295L) + ((long) z[1] & 4294967295L);
        z[1] = (int) c;
        c >>>= 32;
        c += ((long) x[2] & 4294967295L) + ((long) y[2] & 4294967295L) + ((long) z[2] & 4294967295L);
        z[2] = (int) c;
        c >>>= 32;
        c += ((long) x[3] & 4294967295L) + ((long) y[3] & 4294967295L) + ((long) z[3] & 4294967295L);
        z[3] = (int) c;
        c >>>= 32;
        c += ((long) x[4] & 4294967295L) + ((long) y[4] & 4294967295L) + ((long) z[4] & 4294967295L);
        z[4] = (int) c;
        c >>>= 32;
        c += ((long) x[5] & 4294967295L) + ((long) y[5] & 4294967295L) + ((long) z[5] & 4294967295L);
        z[5] = (int) c;
        c >>>= 32;
        return (int) c;
    }

    public static int addTo(int[] x, int[] z) {
        long c = 0L;
        c += ((long) x[0] & 4294967295L) + ((long) z[0] & 4294967295L);
        z[0] = (int) c;
        c >>>= 32;
        c += ((long) x[1] & 4294967295L) + ((long) z[1] & 4294967295L);
        z[1] = (int) c;
        c >>>= 32;
        c += ((long) x[2] & 4294967295L) + ((long) z[2] & 4294967295L);
        z[2] = (int) c;
        c >>>= 32;
        c += ((long) x[3] & 4294967295L) + ((long) z[3] & 4294967295L);
        z[3] = (int) c;
        c >>>= 32;
        c += ((long) x[4] & 4294967295L) + ((long) z[4] & 4294967295L);
        z[4] = (int) c;
        c >>>= 32;
        c += ((long) x[5] & 4294967295L) + ((long) z[5] & 4294967295L);
        z[5] = (int) c;
        c >>>= 32;
        return (int) c;
    }

    public static int addTo(int[] x, int xOff, int[] z, int zOff, int cIn) {
        long c = (long) cIn & 4294967295L;
        c += ((long) x[xOff + 0] & 4294967295L) + ((long) z[zOff + 0] & 4294967295L);
        z[zOff + 0] = (int) c;
        c >>>= 32;
        c += ((long) x[xOff + 1] & 4294967295L) + ((long) z[zOff + 1] & 4294967295L);
        z[zOff + 1] = (int) c;
        c >>>= 32;
        c += ((long) x[xOff + 2] & 4294967295L) + ((long) z[zOff + 2] & 4294967295L);
        z[zOff + 2] = (int) c;
        c >>>= 32;
        c += ((long) x[xOff + 3] & 4294967295L) + ((long) z[zOff + 3] & 4294967295L);
        z[zOff + 3] = (int) c;
        c >>>= 32;
        c += ((long) x[xOff + 4] & 4294967295L) + ((long) z[zOff + 4] & 4294967295L);
        z[zOff + 4] = (int) c;
        c >>>= 32;
        c += ((long) x[xOff + 5] & 4294967295L) + ((long) z[zOff + 5] & 4294967295L);
        z[zOff + 5] = (int) c;
        c >>>= 32;
        return (int) c;
    }

    public static int addToEachOther(int[] u, int uOff, int[] v, int vOff) {
        long c = 0L;
        c += ((long) u[uOff + 0] & 4294967295L) + ((long) v[vOff + 0] & 4294967295L);
        u[uOff + 0] = (int) c;
        v[vOff + 0] = (int) c;
        c >>>= 32;
        c += ((long) u[uOff + 1] & 4294967295L) + ((long) v[vOff + 1] & 4294967295L);
        u[uOff + 1] = (int) c;
        v[vOff + 1] = (int) c;
        c >>>= 32;
        c += ((long) u[uOff + 2] & 4294967295L) + ((long) v[vOff + 2] & 4294967295L);
        u[uOff + 2] = (int) c;
        v[vOff + 2] = (int) c;
        c >>>= 32;
        c += ((long) u[uOff + 3] & 4294967295L) + ((long) v[vOff + 3] & 4294967295L);
        u[uOff + 3] = (int) c;
        v[vOff + 3] = (int) c;
        c >>>= 32;
        c += ((long) u[uOff + 4] & 4294967295L) + ((long) v[vOff + 4] & 4294967295L);
        u[uOff + 4] = (int) c;
        v[vOff + 4] = (int) c;
        c >>>= 32;
        c += ((long) u[uOff + 5] & 4294967295L) + ((long) v[vOff + 5] & 4294967295L);
        u[uOff + 5] = (int) c;
        v[vOff + 5] = (int) c;
        c >>>= 32;
        return (int) c;
    }

    public static void copy(int[] x, int[] z) {
        z[0] = x[0];
        z[1] = x[1];
        z[2] = x[2];
        z[3] = x[3];
        z[4] = x[4];
        z[5] = x[5];
    }

    public static int[] create() {
        return new int[6];
    }

    public static int[] createExt() {
        return new int[12];
    }

    public static boolean diff(int[] x, int xOff, int[] y, int yOff, int[] z, int zOff) {
        boolean pos = gte(x, xOff, y, yOff);
        if (pos) {
            sub(x, xOff, y, yOff, z, zOff);
        } else {
            sub(y, yOff, x, xOff, z, zOff);
        }

        return pos;
    }

    public static boolean eq(int[] x, int[] y) {
        for (int i = 5; i >= 0; --i) {
            if (x[i] != y[i]) {
                return false;
            }
        }

        return true;
    }

    public static int[] fromBigInteger(BigInteger x) {
        if (x.signum() >= 0 && x.bitLength() <= 192) {
            int[] z = create();

            for (int i = 0; x.signum() != 0; x = x.shiftRight(32)) {
                z[i++] = x.intValue();
            }

            return z;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static int getBit(int[] x, int bit) {
        if (bit == 0) {
            return x[0] & 1;
        } else {
            int w = bit >> 5;
            if (w >= 0 && w < 6) {
                int b = bit & 31;
                return x[w] >>> b & 1;
            } else {
                return 0;
            }
        }
    }

    public static boolean gte(int[] x, int[] y) {
        for (int i = 5; i >= 0; --i) {
            int x_i = x[i] ^ Integer.MIN_VALUE;
            int y_i = y[i] ^ Integer.MIN_VALUE;
            if (x_i < y_i) {
                return false;
            }

            if (x_i > y_i) {
                return true;
            }
        }

        return true;
    }

    public static boolean gte(int[] x, int xOff, int[] y, int yOff) {
        for (int i = 5; i >= 0; --i) {
            int x_i = x[xOff + i] ^ Integer.MIN_VALUE;
            int y_i = y[yOff + i] ^ Integer.MIN_VALUE;
            if (x_i < y_i) {
                return false;
            }

            if (x_i > y_i) {
                return true;
            }
        }

        return true;
    }

    public static boolean isOne(int[] x) {
        if (x[0] != 1) {
            return false;
        } else {
            for (int i = 1; i < 6; ++i) {
                if (x[i] != 0) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isZero(int[] x) {
        for (int i = 0; i < 6; ++i) {
            if (x[i] != 0) {
                return false;
            }
        }

        return true;
    }

    public static void mul(int[] x, int[] y, int[] zz) {
        long y_0 = (long) y[0] & 4294967295L;
        long y_1 = (long) y[1] & 4294967295L;
        long y_2 = (long) y[2] & 4294967295L;
        long y_3 = (long) y[3] & 4294967295L;
        long y_4 = (long) y[4] & 4294967295L;
        long y_5 = (long) y[5] & 4294967295L;
        long c = 0L;
        long x_0 = (long) x[0] & 4294967295L;
        c += x_0 * y_0;
        zz[0] = (int) c;
        c >>>= 32;
        c += x_0 * y_1;
        zz[1] = (int) c;
        c >>>= 32;
        c += x_0 * y_2;
        zz[2] = (int) c;
        c >>>= 32;
        c += x_0 * y_3;
        zz[3] = (int) c;
        c >>>= 32;
        c += x_0 * y_4;
        zz[4] = (int) c;
        c >>>= 32;
        c += x_0 * y_5;
        zz[5] = (int) c;
        c >>>= 32;
        zz[6] = (int) c;

        for (int i = 1; i < 6; ++i) {
            c = 0L;
            long x_i = (long) x[i] & 4294967295L;
            c += x_i * y_0 + ((long) zz[i + 0] & 4294967295L);
            zz[i + 0] = (int) c;
            c >>>= 32;
            c += x_i * y_1 + ((long) zz[i + 1] & 4294967295L);
            zz[i + 1] = (int) c;
            c >>>= 32;
            c += x_i * y_2 + ((long) zz[i + 2] & 4294967295L);
            zz[i + 2] = (int) c;
            c >>>= 32;
            c += x_i * y_3 + ((long) zz[i + 3] & 4294967295L);
            zz[i + 3] = (int) c;
            c >>>= 32;
            c += x_i * y_4 + ((long) zz[i + 4] & 4294967295L);
            zz[i + 4] = (int) c;
            c >>>= 32;
            c += x_i * y_5 + ((long) zz[i + 5] & 4294967295L);
            zz[i + 5] = (int) c;
            c >>>= 32;
            zz[i + 6] = (int) c;
        }

    }

    public static void mul(int[] x, int xOff, int[] y, int yOff, int[] zz, int zzOff) {
        long y_0 = (long) y[yOff + 0] & 4294967295L;
        long y_1 = (long) y[yOff + 1] & 4294967295L;
        long y_2 = (long) y[yOff + 2] & 4294967295L;
        long y_3 = (long) y[yOff + 3] & 4294967295L;
        long y_4 = (long) y[yOff + 4] & 4294967295L;
        long y_5 = (long) y[yOff + 5] & 4294967295L;
        long c = 0L;
        long x_0 = (long) x[xOff + 0] & 4294967295L;
        c += x_0 * y_0;
        zz[zzOff + 0] = (int) c;
        c >>>= 32;
        c += x_0 * y_1;
        zz[zzOff + 1] = (int) c;
        c >>>= 32;
        c += x_0 * y_2;
        zz[zzOff + 2] = (int) c;
        c >>>= 32;
        c += x_0 * y_3;
        zz[zzOff + 3] = (int) c;
        c >>>= 32;
        c += x_0 * y_4;
        zz[zzOff + 4] = (int) c;
        c >>>= 32;
        c += x_0 * y_5;
        zz[zzOff + 5] = (int) c;
        c >>>= 32;
        zz[zzOff + 6] = (int) c;

        for (int i = 1; i < 6; ++i) {
            ++zzOff;
            c = 0L;
            long x_i = (long) x[xOff + i] & 4294967295L;
            c += x_i * y_0 + ((long) zz[zzOff + 0] & 4294967295L);
            zz[zzOff + 0] = (int) c;
            c >>>= 32;
            c += x_i * y_1 + ((long) zz[zzOff + 1] & 4294967295L);
            zz[zzOff + 1] = (int) c;
            c >>>= 32;
            c += x_i * y_2 + ((long) zz[zzOff + 2] & 4294967295L);
            zz[zzOff + 2] = (int) c;
            c >>>= 32;
            c += x_i * y_3 + ((long) zz[zzOff + 3] & 4294967295L);
            zz[zzOff + 3] = (int) c;
            c >>>= 32;
            c += x_i * y_4 + ((long) zz[zzOff + 4] & 4294967295L);
            zz[zzOff + 4] = (int) c;
            c >>>= 32;
            c += x_i * y_5 + ((long) zz[zzOff + 5] & 4294967295L);
            zz[zzOff + 5] = (int) c;
            c >>>= 32;
            zz[zzOff + 6] = (int) c;
        }

    }

    public static int mulAddTo(int[] x, int[] y, int[] zz) {
        long y_0 = (long) y[0] & 4294967295L;
        long y_1 = (long) y[1] & 4294967295L;
        long y_2 = (long) y[2] & 4294967295L;
        long y_3 = (long) y[3] & 4294967295L;
        long y_4 = (long) y[4] & 4294967295L;
        long y_5 = (long) y[5] & 4294967295L;
        long zc = 0L;

        for (int i = 0; i < 6; ++i) {
            long c = 0L;
            long x_i = (long) x[i] & 4294967295L;
            c += x_i * y_0 + ((long) zz[i + 0] & 4294967295L);
            zz[i + 0] = (int) c;
            c >>>= 32;
            c += x_i * y_1 + ((long) zz[i + 1] & 4294967295L);
            zz[i + 1] = (int) c;
            c >>>= 32;
            c += x_i * y_2 + ((long) zz[i + 2] & 4294967295L);
            zz[i + 2] = (int) c;
            c >>>= 32;
            c += x_i * y_3 + ((long) zz[i + 3] & 4294967295L);
            zz[i + 3] = (int) c;
            c >>>= 32;
            c += x_i * y_4 + ((long) zz[i + 4] & 4294967295L);
            zz[i + 4] = (int) c;
            c >>>= 32;
            c += x_i * y_5 + ((long) zz[i + 5] & 4294967295L);
            zz[i + 5] = (int) c;
            c >>>= 32;
            c += zc + ((long) zz[i + 6] & 4294967295L);
            zz[i + 6] = (int) c;
            zc = c >>> 32;
        }

        return (int) zc;
    }

    public static int mulAddTo(int[] x, int xOff, int[] y, int yOff, int[] zz, int zzOff) {
        long y_0 = (long) y[yOff + 0] & 4294967295L;
        long y_1 = (long) y[yOff + 1] & 4294967295L;
        long y_2 = (long) y[yOff + 2] & 4294967295L;
        long y_3 = (long) y[yOff + 3] & 4294967295L;
        long y_4 = (long) y[yOff + 4] & 4294967295L;
        long y_5 = (long) y[yOff + 5] & 4294967295L;
        long zc = 0L;

        for (int i = 0; i < 6; ++i) {
            long c = 0L;
            long x_i = (long) x[xOff + i] & 4294967295L;
            c += x_i * y_0 + ((long) zz[zzOff + 0] & 4294967295L);
            zz[zzOff + 0] = (int) c;
            c >>>= 32;
            c += x_i * y_1 + ((long) zz[zzOff + 1] & 4294967295L);
            zz[zzOff + 1] = (int) c;
            c >>>= 32;
            c += x_i * y_2 + ((long) zz[zzOff + 2] & 4294967295L);
            zz[zzOff + 2] = (int) c;
            c >>>= 32;
            c += x_i * y_3 + ((long) zz[zzOff + 3] & 4294967295L);
            zz[zzOff + 3] = (int) c;
            c >>>= 32;
            c += x_i * y_4 + ((long) zz[zzOff + 4] & 4294967295L);
            zz[zzOff + 4] = (int) c;
            c >>>= 32;
            c += x_i * y_5 + ((long) zz[zzOff + 5] & 4294967295L);
            zz[zzOff + 5] = (int) c;
            c >>>= 32;
            c += zc + ((long) zz[zzOff + 6] & 4294967295L);
            zz[zzOff + 6] = (int) c;
            zc = c >>> 32;
            ++zzOff;
        }

        return (int) zc;
    }

    public static long mul33Add(int w, int[] x, int xOff, int[] y, int yOff, int[] z, int zOff) {
        long c = 0L;
        long wVal = (long) w & 4294967295L;
        long x0 = (long) x[xOff + 0] & 4294967295L;
        c += wVal * x0 + ((long) y[yOff + 0] & 4294967295L);
        z[zOff + 0] = (int) c;
        c >>>= 32;
        long x1 = (long) x[xOff + 1] & 4294967295L;
        c += wVal * x1 + x0 + ((long) y[yOff + 1] & 4294967295L);
        z[zOff + 1] = (int) c;
        c >>>= 32;
        long x2 = (long) x[xOff + 2] & 4294967295L;
        c += wVal * x2 + x1 + ((long) y[yOff + 2] & 4294967295L);
        z[zOff + 2] = (int) c;
        c >>>= 32;
        long x3 = (long) x[xOff + 3] & 4294967295L;
        c += wVal * x3 + x2 + ((long) y[yOff + 3] & 4294967295L);
        z[zOff + 3] = (int) c;
        c >>>= 32;
        long x4 = (long) x[xOff + 4] & 4294967295L;
        c += wVal * x4 + x3 + ((long) y[yOff + 4] & 4294967295L);
        z[zOff + 4] = (int) c;
        c >>>= 32;
        long x5 = (long) x[xOff + 5] & 4294967295L;
        c += wVal * x5 + x4 + ((long) y[yOff + 5] & 4294967295L);
        z[zOff + 5] = (int) c;
        c >>>= 32;
        c += x5;
        return c;
    }

    public static int mulWordAddExt(int x, int[] yy, int yyOff, int[] zz, int zzOff) {
        long c = 0L;
        long xVal = (long) x & 4294967295L;
        c += xVal * ((long) yy[yyOff + 0] & 4294967295L) + ((long) zz[zzOff + 0] & 4294967295L);
        zz[zzOff + 0] = (int) c;
        c >>>= 32;
        c += xVal * ((long) yy[yyOff + 1] & 4294967295L) + ((long) zz[zzOff + 1] & 4294967295L);
        zz[zzOff + 1] = (int) c;
        c >>>= 32;
        c += xVal * ((long) yy[yyOff + 2] & 4294967295L) + ((long) zz[zzOff + 2] & 4294967295L);
        zz[zzOff + 2] = (int) c;
        c >>>= 32;
        c += xVal * ((long) yy[yyOff + 3] & 4294967295L) + ((long) zz[zzOff + 3] & 4294967295L);
        zz[zzOff + 3] = (int) c;
        c >>>= 32;
        c += xVal * ((long) yy[yyOff + 4] & 4294967295L) + ((long) zz[zzOff + 4] & 4294967295L);
        zz[zzOff + 4] = (int) c;
        c >>>= 32;
        c += xVal * ((long) yy[yyOff + 5] & 4294967295L) + ((long) zz[zzOff + 5] & 4294967295L);
        zz[zzOff + 5] = (int) c;
        c >>>= 32;
        return (int) c;
    }

    public static int mul33DWordAdd(int x, long y, int[] z, int zOff) {
        long c = 0L;
        long xVal = (long) x & 4294967295L;
        long y00 = y & 4294967295L;
        c += xVal * y00 + ((long) z[zOff + 0] & 4294967295L);
        z[zOff + 0] = (int) c;
        c >>>= 32;
        long y01 = y >>> 32;
        c += xVal * y01 + y00 + ((long) z[zOff + 1] & 4294967295L);
        z[zOff + 1] = (int) c;
        c >>>= 32;
        c += y01 + ((long) z[zOff + 2] & 4294967295L);
        z[zOff + 2] = (int) c;
        c >>>= 32;
        c += (long) z[zOff + 3] & 4294967295L;
        z[zOff + 3] = (int) c;
        c >>>= 32;
        return c == 0L ? 0 : Nat.incAt(6, z, zOff, 4);
    }

    public static int mul33WordAdd(int x, int y, int[] z, int zOff) {
        long c = 0L;
        long xVal = (long) x & 4294967295L;
        long yVal = (long) y & 4294967295L;
        c += yVal * xVal + ((long) z[zOff + 0] & 4294967295L);
        z[zOff + 0] = (int) c;
        c >>>= 32;
        c += yVal + ((long) z[zOff + 1] & 4294967295L);
        z[zOff + 1] = (int) c;
        c >>>= 32;
        c += (long) z[zOff + 2] & 4294967295L;
        z[zOff + 2] = (int) c;
        c >>>= 32;
        return c == 0L ? 0 : Nat.incAt(6, z, zOff, 3);
    }

    public static int mulWordDwordAdd(int x, long y, int[] z, int zOff) {
        long c = 0L;
        long xVal = (long) x & 4294967295L;
        c += xVal * (y & 4294967295L) + ((long) z[zOff + 0] & 4294967295L);
        z[zOff + 0] = (int) c;
        c >>>= 32;
        c += xVal * (y >>> 32) + ((long) z[zOff + 1] & 4294967295L);
        z[zOff + 1] = (int) c;
        c >>>= 32;
        c += (long) z[zOff + 2] & 4294967295L;
        z[zOff + 2] = (int) c;
        c >>>= 32;
        return c == 0L ? 0 : Nat.incAt(6, z, zOff, 3);
    }

    public static int mulWord(int x, int[] y, int[] z, int zOff) {
        long c = 0L;
        long xVal = (long) x & 4294967295L;
        int i = 0;

        do {
            c += xVal * ((long) y[i] & 4294967295L);
            z[zOff + i] = (int) c;
            c >>>= 32;
            ++i;
        } while (i < 6);

        return (int) c;
    }

    public static void square(int[] x, int[] zz) {
        long x_0 = (long) x[0] & 4294967295L;
        int c = 0;
        int i = 5;
        int j = 12;

        long zz_2;
        long x_2;
        do {
            zz_2 = (long) x[i--] & 4294967295L;
            x_2 = zz_2 * zz_2;
            --j;
            zz[j] = c << 31 | (int) (x_2 >>> 33);
            --j;
            zz[j] = (int) (x_2 >>> 1);
            c = (int) x_2;
        } while (i > 0);

        zz_2 = x_0 * x_0;
        long zz_1 = (long) (c << 31) & 4294967295L | zz_2 >>> 33;
        zz[0] = (int) zz_2;
        c = (int) (zz_2 >>> 32) & 1;
        long x_1 = (long) x[1] & 4294967295L;
        zz_2 = (long) zz[2] & 4294967295L;
        zz_1 += x_1 * x_0;
        int w = (int) zz_1;
        zz[1] = w << 1 | c;
        c = w >>> 31;
        zz_2 += zz_1 >>> 32;
        x_2 = (long) x[2] & 4294967295L;
        long zz_3 = (long) zz[3] & 4294967295L;
        long zz_4 = (long) zz[4] & 4294967295L;
        zz_2 += x_2 * x_0;
        w = (int) zz_2;
        zz[2] = w << 1 | c;
        c = w >>> 31;
        zz_3 += (zz_2 >>> 32) + x_2 * x_1;
        zz_4 += zz_3 >>> 32;
        zz_3 &= 4294967295L;
        long x_3 = (long) x[3] & 4294967295L;
        long zz_5 = (long) zz[5] & 4294967295L;
        long zz_6 = (long) zz[6] & 4294967295L;
        zz_3 += x_3 * x_0;
        w = (int) zz_3;
        zz[3] = w << 1 | c;
        c = w >>> 31;
        zz_4 += (zz_3 >>> 32) + x_3 * x_1;
        zz_5 += (zz_4 >>> 32) + x_3 * x_2;
        zz_4 &= 4294967295L;
        zz_6 += zz_5 >>> 32;
        zz_5 &= 4294967295L;
        long x_4 = (long) x[4] & 4294967295L;
        long zz_7 = (long) zz[7] & 4294967295L;
        long zz_8 = (long) zz[8] & 4294967295L;
        zz_4 += x_4 * x_0;
        w = (int) zz_4;
        zz[4] = w << 1 | c;
        c = w >>> 31;
        zz_5 += (zz_4 >>> 32) + x_4 * x_1;
        zz_6 += (zz_5 >>> 32) + x_4 * x_2;
        zz_5 &= 4294967295L;
        zz_7 += (zz_6 >>> 32) + x_4 * x_3;
        zz_6 &= 4294967295L;
        zz_8 += zz_7 >>> 32;
        zz_7 &= 4294967295L;
        long x_5 = (long) x[5] & 4294967295L;
        long zz_9 = (long) zz[9] & 4294967295L;
        long zz_10 = (long) zz[10] & 4294967295L;
        zz_5 += x_5 * x_0;
        w = (int) zz_5;
        zz[5] = w << 1 | c;
        c = w >>> 31;
        zz_6 += (zz_5 >>> 32) + x_5 * x_1;
        zz_7 += (zz_6 >>> 32) + x_5 * x_2;
        zz_8 += (zz_7 >>> 32) + x_5 * x_3;
        zz_9 += (zz_8 >>> 32) + x_5 * x_4;
        zz_10 += zz_9 >>> 32;
        w = (int) zz_6;
        zz[6] = w << 1 | c;
        c = w >>> 31;
        w = (int) zz_7;
        zz[7] = w << 1 | c;
        c = w >>> 31;
        w = (int) zz_8;
        zz[8] = w << 1 | c;
        c = w >>> 31;
        w = (int) zz_9;
        zz[9] = w << 1 | c;
        c = w >>> 31;
        w = (int) zz_10;
        zz[10] = w << 1 | c;
        c = w >>> 31;
        w = zz[11] + (int) (zz_10 >> 32);
        zz[11] = w << 1 | c;
    }

    public static void square(int[] x, int xOff, int[] zz, int zzOff) {
        long x_0 = (long) x[xOff + 0] & 4294967295L;
        int c = 0;
        int i = 5;
        int j = 12;

        long zz_2;
        long x_2;
        do {
            zz_2 = (long) x[xOff + i--] & 4294967295L;
            x_2 = zz_2 * zz_2;
            --j;
            zz[zzOff + j] = c << 31 | (int) (x_2 >>> 33);
            --j;
            zz[zzOff + j] = (int) (x_2 >>> 1);
            c = (int) x_2;
        } while (i > 0);

        zz_2 = x_0 * x_0;
        long zz_1 = (long) (c << 31) & 4294967295L | zz_2 >>> 33;
        zz[zzOff + 0] = (int) zz_2;
        c = (int) (zz_2 >>> 32) & 1;
        long x_1 = (long) x[xOff + 1] & 4294967295L;
        zz_2 = (long) zz[zzOff + 2] & 4294967295L;
        zz_1 += x_1 * x_0;
        int w = (int) zz_1;
        zz[zzOff + 1] = w << 1 | c;
        c = w >>> 31;
        zz_2 += zz_1 >>> 32;
        x_2 = (long) x[xOff + 2] & 4294967295L;
        long zz_3 = (long) zz[zzOff + 3] & 4294967295L;
        long zz_4 = (long) zz[zzOff + 4] & 4294967295L;
        zz_2 += x_2 * x_0;
        w = (int) zz_2;
        zz[zzOff + 2] = w << 1 | c;
        c = w >>> 31;
        zz_3 += (zz_2 >>> 32) + x_2 * x_1;
        zz_4 += zz_3 >>> 32;
        zz_3 &= 4294967295L;
        long x_3 = (long) x[xOff + 3] & 4294967295L;
        long zz_5 = (long) zz[zzOff + 5] & 4294967295L;
        long zz_6 = (long) zz[zzOff + 6] & 4294967295L;
        zz_3 += x_3 * x_0;
        w = (int) zz_3;
        zz[zzOff + 3] = w << 1 | c;
        c = w >>> 31;
        zz_4 += (zz_3 >>> 32) + x_3 * x_1;
        zz_5 += (zz_4 >>> 32) + x_3 * x_2;
        zz_4 &= 4294967295L;
        zz_6 += zz_5 >>> 32;
        zz_5 &= 4294967295L;
        long x_4 = (long) x[xOff + 4] & 4294967295L;
        long zz_7 = (long) zz[zzOff + 7] & 4294967295L;
        long zz_8 = (long) zz[zzOff + 8] & 4294967295L;
        zz_4 += x_4 * x_0;
        w = (int) zz_4;
        zz[zzOff + 4] = w << 1 | c;
        c = w >>> 31;
        zz_5 += (zz_4 >>> 32) + x_4 * x_1;
        zz_6 += (zz_5 >>> 32) + x_4 * x_2;
        zz_5 &= 4294967295L;
        zz_7 += (zz_6 >>> 32) + x_4 * x_3;
        zz_6 &= 4294967295L;
        zz_8 += zz_7 >>> 32;
        zz_7 &= 4294967295L;
        long x_5 = (long) x[xOff + 5] & 4294967295L;
        long zz_9 = (long) zz[zzOff + 9] & 4294967295L;
        long zz_10 = (long) zz[zzOff + 10] & 4294967295L;
        zz_5 += x_5 * x_0;
        w = (int) zz_5;
        zz[zzOff + 5] = w << 1 | c;
        c = w >>> 31;
        zz_6 += (zz_5 >>> 32) + x_5 * x_1;
        zz_7 += (zz_6 >>> 32) + x_5 * x_2;
        zz_8 += (zz_7 >>> 32) + x_5 * x_3;
        zz_9 += (zz_8 >>> 32) + x_5 * x_4;
        zz_10 += zz_9 >>> 32;
        w = (int) zz_6;
        zz[zzOff + 6] = w << 1 | c;
        c = w >>> 31;
        w = (int) zz_7;
        zz[zzOff + 7] = w << 1 | c;
        c = w >>> 31;
        w = (int) zz_8;
        zz[zzOff + 8] = w << 1 | c;
        c = w >>> 31;
        w = (int) zz_9;
        zz[zzOff + 9] = w << 1 | c;
        c = w >>> 31;
        w = (int) zz_10;
        zz[zzOff + 10] = w << 1 | c;
        c = w >>> 31;
        w = zz[zzOff + 11] + (int) (zz_10 >> 32);
        zz[zzOff + 11] = w << 1 | c;
    }

    public static int sub(int[] x, int[] y, int[] z) {
        long c = 0L;
        c += ((long) x[0] & 4294967295L) - ((long) y[0] & 4294967295L);
        z[0] = (int) c;
        c >>= 32;
        c += ((long) x[1] & 4294967295L) - ((long) y[1] & 4294967295L);
        z[1] = (int) c;
        c >>= 32;
        c += ((long) x[2] & 4294967295L) - ((long) y[2] & 4294967295L);
        z[2] = (int) c;
        c >>= 32;
        c += ((long) x[3] & 4294967295L) - ((long) y[3] & 4294967295L);
        z[3] = (int) c;
        c >>= 32;
        c += ((long) x[4] & 4294967295L) - ((long) y[4] & 4294967295L);
        z[4] = (int) c;
        c >>= 32;
        c += ((long) x[5] & 4294967295L) - ((long) y[5] & 4294967295L);
        z[5] = (int) c;
        c >>= 32;
        return (int) c;
    }

    public static int sub(int[] x, int xOff, int[] y, int yOff, int[] z, int zOff) {
        long c = 0L;
        c += ((long) x[xOff + 0] & 4294967295L) - ((long) y[yOff + 0] & 4294967295L);
        z[zOff + 0] = (int) c;
        c >>= 32;
        c += ((long) x[xOff + 1] & 4294967295L) - ((long) y[yOff + 1] & 4294967295L);
        z[zOff + 1] = (int) c;
        c >>= 32;
        c += ((long) x[xOff + 2] & 4294967295L) - ((long) y[yOff + 2] & 4294967295L);
        z[zOff + 2] = (int) c;
        c >>= 32;
        c += ((long) x[xOff + 3] & 4294967295L) - ((long) y[yOff + 3] & 4294967295L);
        z[zOff + 3] = (int) c;
        c >>= 32;
        c += ((long) x[xOff + 4] & 4294967295L) - ((long) y[yOff + 4] & 4294967295L);
        z[zOff + 4] = (int) c;
        c >>= 32;
        c += ((long) x[xOff + 5] & 4294967295L) - ((long) y[yOff + 5] & 4294967295L);
        z[zOff + 5] = (int) c;
        c >>= 32;
        return (int) c;
    }

    public static int subBothFrom(int[] x, int[] y, int[] z) {
        long c = 0L;
        c += ((long) z[0] & 4294967295L) - ((long) x[0] & 4294967295L) - ((long) y[0] & 4294967295L);
        z[0] = (int) c;
        c >>= 32;
        c += ((long) z[1] & 4294967295L) - ((long) x[1] & 4294967295L) - ((long) y[1] & 4294967295L);
        z[1] = (int) c;
        c >>= 32;
        c += ((long) z[2] & 4294967295L) - ((long) x[2] & 4294967295L) - ((long) y[2] & 4294967295L);
        z[2] = (int) c;
        c >>= 32;
        c += ((long) z[3] & 4294967295L) - ((long) x[3] & 4294967295L) - ((long) y[3] & 4294967295L);
        z[3] = (int) c;
        c >>= 32;
        c += ((long) z[4] & 4294967295L) - ((long) x[4] & 4294967295L) - ((long) y[4] & 4294967295L);
        z[4] = (int) c;
        c >>= 32;
        c += ((long) z[5] & 4294967295L) - ((long) x[5] & 4294967295L) - ((long) y[5] & 4294967295L);
        z[5] = (int) c;
        c >>= 32;
        return (int) c;
    }

    public static int subFrom(int[] x, int[] z) {
        long c = 0L;
        c += ((long) z[0] & 4294967295L) - ((long) x[0] & 4294967295L);
        z[0] = (int) c;
        c >>= 32;
        c += ((long) z[1] & 4294967295L) - ((long) x[1] & 4294967295L);
        z[1] = (int) c;
        c >>= 32;
        c += ((long) z[2] & 4294967295L) - ((long) x[2] & 4294967295L);
        z[2] = (int) c;
        c >>= 32;
        c += ((long) z[3] & 4294967295L) - ((long) x[3] & 4294967295L);
        z[3] = (int) c;
        c >>= 32;
        c += ((long) z[4] & 4294967295L) - ((long) x[4] & 4294967295L);
        z[4] = (int) c;
        c >>= 32;
        c += ((long) z[5] & 4294967295L) - ((long) x[5] & 4294967295L);
        z[5] = (int) c;
        c >>= 32;
        return (int) c;
    }

    public static int subFrom(int[] x, int xOff, int[] z, int zOff) {
        long c = 0L;
        c += ((long) z[zOff + 0] & 4294967295L) - ((long) x[xOff + 0] & 4294967295L);
        z[zOff + 0] = (int) c;
        c >>= 32;
        c += ((long) z[zOff + 1] & 4294967295L) - ((long) x[xOff + 1] & 4294967295L);
        z[zOff + 1] = (int) c;
        c >>= 32;
        c += ((long) z[zOff + 2] & 4294967295L) - ((long) x[xOff + 2] & 4294967295L);
        z[zOff + 2] = (int) c;
        c >>= 32;
        c += ((long) z[zOff + 3] & 4294967295L) - ((long) x[xOff + 3] & 4294967295L);
        z[zOff + 3] = (int) c;
        c >>= 32;
        c += ((long) z[zOff + 4] & 4294967295L) - ((long) x[xOff + 4] & 4294967295L);
        z[zOff + 4] = (int) c;
        c >>= 32;
        c += ((long) z[zOff + 5] & 4294967295L) - ((long) x[xOff + 5] & 4294967295L);
        z[zOff + 5] = (int) c;
        c >>= 32;
        return (int) c;
    }

    public static BigInteger toBigInteger(int[] x) {
        byte[] bs = new byte[24];

        for (int i = 0; i < 6; ++i) {
            int x_i = x[i];
            if (x_i != 0) {
                Pack.intToBigEndian(x_i, bs, 5 - i << 2);
            }
        }

        return new BigInteger(1, bs);
    }

    public static void zero(int[] z) {
        z[0] = 0;
        z[1] = 0;
        z[2] = 0;
        z[3] = 0;
        z[4] = 0;
        z[5] = 0;
    }
}
