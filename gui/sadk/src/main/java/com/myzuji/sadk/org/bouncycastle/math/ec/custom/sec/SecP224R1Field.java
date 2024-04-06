package com.myzuji.sadk.org.bouncycastle.math.ec.custom.sec;

import com.myzuji.sadk.org.bouncycastle.math.raw.Nat;
import com.myzuji.sadk.org.bouncycastle.math.raw.Nat224;

import java.math.BigInteger;

public class SecP224R1Field {
    private static final long M = 4294967295L;
    static final int[] P = new int[]{1, 0, 0, -1, -1, -1, -1};
    static final int[] PExt = new int[]{1, 0, 0, -2, -1, -1, 0, 2, 0, 0, -2, -1, -1, -1};
    private static final int[] PExtInv = new int[]{-1, -1, -1, 1, 0, 0, -1, -3, -1, -1, 1};
    private static final int P6 = -1;
    private static final int PExt13 = -1;

    public SecP224R1Field() {
    }

    public static void add(int[] x, int[] y, int[] z) {
        int c = Nat224.add(x, y, z);
        if (c != 0 || z[6] == -1 && Nat224.gte(z, P)) {
            addPInvTo(z);
        }

    }

    public static void addExt(int[] xx, int[] yy, int[] zz) {
        int c = Nat.add(14, xx, yy, zz);
        if ((c != 0 || zz[13] == -1 && Nat.gte(14, zz, PExt)) && Nat.addTo(PExtInv.length, PExtInv, zz) != 0) {
            Nat.incAt(14, zz, PExtInv.length);
        }

    }

    public static void addOne(int[] x, int[] z) {
        int c = Nat.inc(7, x, z);
        if (c != 0 || z[6] == -1 && Nat224.gte(z, P)) {
            addPInvTo(z);
        }

    }

    public static int[] fromBigInteger(BigInteger x) {
        int[] z = Nat224.fromBigInteger(x);
        if (z[6] == -1 && Nat224.gte(z, P)) {
            Nat224.subFrom(P, z);
        }

        return z;
    }

    public static void half(int[] x, int[] z) {
        if ((x[0] & 1) == 0) {
            Nat.shiftDownBit(7, x, 0, z);
        } else {
            int c = Nat224.add(x, P, z);
            Nat.shiftDownBit(7, z, c);
        }

    }

    public static void multiply(int[] x, int[] y, int[] z) {
        int[] tt = Nat224.createExt();
        Nat224.mul(x, y, tt);
        reduce(tt, z);
    }

    public static void multiplyAddToExt(int[] x, int[] y, int[] zz) {
        int c = Nat224.mulAddTo(x, y, zz);
        if ((c != 0 || zz[13] == -1 && Nat.gte(14, zz, PExt)) && Nat.addTo(PExtInv.length, PExtInv, zz) != 0) {
            Nat.incAt(14, zz, PExtInv.length);
        }

    }

    public static void negate(int[] x, int[] z) {
        if (Nat224.isZero(x)) {
            Nat224.zero(z);
        } else {
            Nat224.sub(P, x, z);
        }

    }

    public static void reduce(int[] xx, int[] z) {
        long xx10 = (long) xx[10] & 4294967295L;
        long xx11 = (long) xx[11] & 4294967295L;
        long xx12 = (long) xx[12] & 4294967295L;
        long xx13 = (long) xx[13] & 4294967295L;
        long n = 1L;
        long t0 = ((long) xx[7] & 4294967295L) + xx11 - 1L;
        long t1 = ((long) xx[8] & 4294967295L) + xx12;
        long t2 = ((long) xx[9] & 4294967295L) + xx13;
        long cc = 0L;
        cc += ((long) xx[0] & 4294967295L) - t0;
        long z0 = cc & 4294967295L;
        cc >>= 32;
        cc += ((long) xx[1] & 4294967295L) - t1;
        z[1] = (int) cc;
        cc >>= 32;
        cc += ((long) xx[2] & 4294967295L) - t2;
        z[2] = (int) cc;
        cc >>= 32;
        cc += ((long) xx[3] & 4294967295L) + t0 - xx10;
        long z3 = cc & 4294967295L;
        cc >>= 32;
        cc += ((long) xx[4] & 4294967295L) + t1 - xx11;
        z[4] = (int) cc;
        cc >>= 32;
        cc += ((long) xx[5] & 4294967295L) + t2 - xx12;
        z[5] = (int) cc;
        cc >>= 32;
        cc += ((long) xx[6] & 4294967295L) + xx10 - xx13;
        z[6] = (int) cc;
        cc >>= 32;
        ++cc;
        z3 += cc;
        z0 -= cc;
        z[0] = (int) z0;
        cc = z0 >> 32;
        if (cc != 0L) {
            cc += (long) z[1] & 4294967295L;
            z[1] = (int) cc;
            cc >>= 32;
            cc += (long) z[2] & 4294967295L;
            z[2] = (int) cc;
            z3 += cc >> 32;
        }

        z[3] = (int) z3;
        cc = z3 >> 32;
        if (cc != 0L && Nat.incAt(7, z, 4) != 0 || z[6] == -1 && Nat224.gte(z, P)) {
            addPInvTo(z);
        }

    }

    public static void reduce32(int x, int[] z) {
        long cc = 0L;
        if (x != 0) {
            long xx07 = (long) x & 4294967295L;
            cc += ((long) z[0] & 4294967295L) - xx07;
            z[0] = (int) cc;
            cc >>= 32;
            if (cc != 0L) {
                cc += (long) z[1] & 4294967295L;
                z[1] = (int) cc;
                cc >>= 32;
                cc += (long) z[2] & 4294967295L;
                z[2] = (int) cc;
                cc >>= 32;
            }

            cc += ((long) z[3] & 4294967295L) + xx07;
            z[3] = (int) cc;
            cc >>= 32;
        }

        if (cc != 0L && Nat.incAt(7, z, 4) != 0 || z[6] == -1 && Nat224.gte(z, P)) {
            addPInvTo(z);
        }

    }

    public static void square(int[] x, int[] z) {
        int[] tt = Nat224.createExt();
        Nat224.square(x, tt);
        reduce(tt, z);
    }

    public static void squareN(int[] x, int n, int[] z) {
        int[] tt = Nat224.createExt();
        Nat224.square(x, tt);
        reduce(tt, z);

        while (true) {
            --n;
            if (n <= 0) {
                return;
            }

            Nat224.square(z, tt);
            reduce(tt, z);
        }
    }

    public static void subtract(int[] x, int[] y, int[] z) {
        int c = Nat224.sub(x, y, z);
        if (c != 0) {
            subPInvFrom(z);
        }

    }

    public static void subtractExt(int[] xx, int[] yy, int[] zz) {
        int c = Nat.sub(14, xx, yy, zz);
        if (c != 0 && Nat.subFrom(PExtInv.length, PExtInv, zz) != 0) {
            Nat.decAt(14, zz, PExtInv.length);
        }

    }

    public static void twice(int[] x, int[] z) {
        int c = Nat.shiftUpBit(7, x, 0, z);
        if (c != 0 || z[6] == -1 && Nat224.gte(z, P)) {
            addPInvTo(z);
        }

    }

    private static void addPInvTo(int[] z) {
        long c = ((long) z[0] & 4294967295L) - 1L;
        z[0] = (int) c;
        c >>= 32;
        if (c != 0L) {
            c += (long) z[1] & 4294967295L;
            z[1] = (int) c;
            c >>= 32;
            c += (long) z[2] & 4294967295L;
            z[2] = (int) c;
            c >>= 32;
        }

        c += ((long) z[3] & 4294967295L) + 1L;
        z[3] = (int) c;
        c >>= 32;
        if (c != 0L) {
            Nat.incAt(7, z, 4);
        }

    }

    private static void subPInvFrom(int[] z) {
        long c = ((long) z[0] & 4294967295L) + 1L;
        z[0] = (int) c;
        c >>= 32;
        if (c != 0L) {
            c += (long) z[1] & 4294967295L;
            z[1] = (int) c;
            c >>= 32;
            c += (long) z[2] & 4294967295L;
            z[2] = (int) c;
            c >>= 32;
        }

        c += ((long) z[3] & 4294967295L) - 1L;
        z[3] = (int) c;
        c >>= 32;
        if (c != 0L) {
            Nat.decAt(7, z, 4);
        }

    }
}

