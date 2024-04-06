package com.myzuji.sadk.org.bouncycastle.math.ec.custom.djb;

import com.myzuji.sadk.org.bouncycastle.math.raw.Nat;
import com.myzuji.sadk.org.bouncycastle.math.raw.Nat256;

import java.math.BigInteger;

public class Curve25519Field {
    private static final long M = 4294967295L;
    static final int[] P = new int[]{-19, -1, -1, -1, -1, -1, -1, Integer.MAX_VALUE};
    private static final int P7 = Integer.MAX_VALUE;
    private static final int[] PExt = new int[]{361, 0, 0, 0, 0, 0, 0, 0, -19, -1, -1, -1, -1, -1, -1, 1073741823};
    private static final int PInv = 19;

    public Curve25519Field() {
    }

    public static void add(int[] x, int[] y, int[] z) {
        Nat256.add(x, y, z);
        if (Nat256.gte(z, P)) {
            subPFrom(z);
        }

    }

    public static void addExt(int[] xx, int[] yy, int[] zz) {
        Nat.add(16, xx, yy, zz);
        if (Nat.gte(16, zz, PExt)) {
            subPExtFrom(zz);
        }

    }

    public static void addOne(int[] x, int[] z) {
        Nat.inc(8, x, z);
        if (Nat256.gte(z, P)) {
            subPFrom(z);
        }

    }

    public static int[] fromBigInteger(BigInteger x) {
        int[] z = Nat256.fromBigInteger(x);

        while (Nat256.gte(z, P)) {
            Nat256.subFrom(P, z);
        }

        return z;
    }

    public static void half(int[] x, int[] z) {
        if ((x[0] & 1) == 0) {
            Nat.shiftDownBit(8, x, 0, z);
        } else {
            Nat256.add(x, P, z);
            Nat.shiftDownBit(8, z, 0);
        }

    }

    public static void multiply(int[] x, int[] y, int[] z) {
        int[] tt = Nat256.createExt();
        Nat256.mul(x, y, tt);
        reduce(tt, z);
    }

    public static void multiplyAddToExt(int[] x, int[] y, int[] zz) {
        Nat256.mulAddTo(x, y, zz);
        if (Nat.gte(16, zz, PExt)) {
            subPExtFrom(zz);
        }

    }

    public static void negate(int[] x, int[] z) {
        if (Nat256.isZero(x)) {
            Nat256.zero(z);
        } else {
            Nat256.sub(P, x, z);
        }

    }

    public static void reduce(int[] xx, int[] z) {
        int xx07 = xx[7];
        Nat.shiftUpBit(8, xx, 8, xx07, z, 0);
        int c = Nat256.mulByWordAddTo(19, xx, z) << 1;
        int z7 = z[7];
        c += (z7 >>> 31) - (xx07 >>> 31);
        z7 &= Integer.MAX_VALUE;
        z7 += Nat.addWordTo(7, c * 19, z);
        z[7] = z7;
        if (Nat256.gte(z, P)) {
            subPFrom(z);
        }

    }

    public static void reduce27(int x, int[] z) {
        int z7 = z[7];
        int c = x << 1 | z7 >>> 31;
        z7 &= Integer.MAX_VALUE;
        z7 += Nat.addWordTo(7, c * 19, z);
        z[7] = z7;
        if (Nat256.gte(z, P)) {
            subPFrom(z);
        }

    }

    public static void square(int[] x, int[] z) {
        int[] tt = Nat256.createExt();
        Nat256.square(x, tt);
        reduce(tt, z);
    }

    public static void squareN(int[] x, int n, int[] z) {
        int[] tt = Nat256.createExt();
        Nat256.square(x, tt);
        reduce(tt, z);

        while (true) {
            --n;
            if (n <= 0) {
                return;
            }

            Nat256.square(z, tt);
            reduce(tt, z);
        }
    }

    public static void subtract(int[] x, int[] y, int[] z) {
        int c = Nat256.sub(x, y, z);
        if (c != 0) {
            addPTo(z);
        }

    }

    public static void subtractExt(int[] xx, int[] yy, int[] zz) {
        int c = Nat.sub(16, xx, yy, zz);
        if (c != 0) {
            addPExtTo(zz);
        }

    }

    public static void twice(int[] x, int[] z) {
        Nat.shiftUpBit(8, x, 0, z);
        if (Nat256.gte(z, P)) {
            subPFrom(z);
        }

    }

    private static int addPTo(int[] z) {
        long c = ((long) z[0] & 4294967295L) - 19L;
        z[0] = (int) c;
        c >>= 32;
        if (c != 0L) {
            c = (long) Nat.decAt(7, z, 1);
        }

        c += ((long) z[7] & 4294967295L) + 2147483648L;
        z[7] = (int) c;
        c >>= 32;
        return (int) c;
    }

    private static int addPExtTo(int[] zz) {
        long c = ((long) zz[0] & 4294967295L) + ((long) PExt[0] & 4294967295L);
        zz[0] = (int) c;
        c >>= 32;
        if (c != 0L) {
            c = (long) Nat.incAt(8, zz, 1);
        }

        c += ((long) zz[8] & 4294967295L) - 19L;
        zz[8] = (int) c;
        c >>= 32;
        if (c != 0L) {
            c = (long) Nat.decAt(15, zz, 9);
        }

        c += ((long) zz[15] & 4294967295L) + ((long) (PExt[15] + 1) & 4294967295L);
        zz[15] = (int) c;
        c >>= 32;
        return (int) c;
    }

    private static int subPFrom(int[] z) {
        long c = ((long) z[0] & 4294967295L) + 19L;
        z[0] = (int) c;
        c >>= 32;
        if (c != 0L) {
            c = (long) Nat.incAt(7, z, 1);
        }

        c += ((long) z[7] & 4294967295L) - 2147483648L;
        z[7] = (int) c;
        c >>= 32;
        return (int) c;
    }

    private static int subPExtFrom(int[] zz) {
        long c = ((long) zz[0] & 4294967295L) - ((long) PExt[0] & 4294967295L);
        zz[0] = (int) c;
        c >>= 32;
        if (c != 0L) {
            c = (long) Nat.decAt(8, zz, 1);
        }

        c += ((long) zz[8] & 4294967295L) + 19L;
        zz[8] = (int) c;
        c >>= 32;
        if (c != 0L) {
            c = (long) Nat.incAt(15, zz, 9);
        }

        c += ((long) zz[15] & 4294967295L) - ((long) (PExt[15] + 1) & 4294967295L);
        zz[15] = (int) c;
        c >>= 32;
        return (int) c;
    }
}
