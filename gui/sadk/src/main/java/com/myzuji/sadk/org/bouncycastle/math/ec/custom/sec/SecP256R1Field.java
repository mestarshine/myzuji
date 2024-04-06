package com.myzuji.sadk.org.bouncycastle.math.ec.custom.sec;

import com.myzuji.sadk.org.bouncycastle.math.raw.Nat;
import com.myzuji.sadk.org.bouncycastle.math.raw.Nat256;

import java.math.BigInteger;

public class SecP256R1Field {
    private static final long M = 4294967295L;
    static final int[] P = new int[]{-1, -1, -1, 0, 0, 0, 1, -1};
    static final int[] PExt = new int[]{1, 0, 0, -2, -1, -1, -2, 1, -2, 1, -2, 1, 1, -2, 2, -2};
    private static final int P7 = -1;
    private static final int PExt15 = -1;

    public SecP256R1Field() {
    }

    public static void add(int[] x, int[] y, int[] z) {
        int c = Nat256.add(x, y, z);
        if (c != 0 || z[7] == -1 && Nat256.gte(z, P)) {
            addPInvTo(z);
        }

    }

    public static void addExt(int[] xx, int[] yy, int[] zz) {
        int c = Nat.add(16, xx, yy, zz);
        if (c != 0 || (zz[15] & -1) == -1 && Nat.gte(16, zz, PExt)) {
            Nat.subFrom(16, PExt, zz);
        }

    }

    public static void addOne(int[] x, int[] z) {
        int c = Nat.inc(8, x, z);
        if (c != 0 || z[7] == -1 && Nat256.gte(z, P)) {
            addPInvTo(z);
        }

    }

    public static int[] fromBigInteger(BigInteger x) {
        int[] z = Nat256.fromBigInteger(x);
        if (z[7] == -1 && Nat256.gte(z, P)) {
            Nat256.subFrom(P, z);
        }

        return z;
    }

    public static void half(int[] x, int[] z) {
        if ((x[0] & 1) == 0) {
            Nat.shiftDownBit(8, x, 0, z);
        } else {
            int c = Nat256.add(x, P, z);
            Nat.shiftDownBit(8, z, c);
        }

    }

    public static void multiply(int[] x, int[] y, int[] z) {
        int[] tt = Nat256.createExt();
        Nat256.mul(x, y, tt);
        reduce(tt, z);
    }

    public static void multiplyAddToExt(int[] x, int[] y, int[] zz) {
        int c = Nat256.mulAddTo(x, y, zz);
        if (c != 0 || (zz[15] & -1) == -1 && Nat.gte(16, zz, PExt)) {
            Nat.subFrom(16, PExt, zz);
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
        long xx08 = (long) xx[8] & 4294967295L;
        long xx09 = (long) xx[9] & 4294967295L;
        long xx10 = (long) xx[10] & 4294967295L;
        long xx11 = (long) xx[11] & 4294967295L;
        long xx12 = (long) xx[12] & 4294967295L;
        long xx13 = (long) xx[13] & 4294967295L;
        long xx14 = (long) xx[14] & 4294967295L;
        long xx15 = (long) xx[15] & 4294967295L;
        long n = 6L;
        xx08 -= 6L;
        long t0 = xx08 + xx09;
        long t1 = xx09 + xx10;
        long t2 = xx10 + xx11 - xx15;
        long t3 = xx11 + xx12;
        long t4 = xx12 + xx13;
        long t5 = xx13 + xx14;
        long t6 = xx14 + xx15;
        long cc = 0L;
        cc += ((long) xx[0] & 4294967295L) + t0 - t3 - t5;
        z[0] = (int) cc;
        cc >>= 32;
        cc += ((long) xx[1] & 4294967295L) + t1 - t4 - t6;
        z[1] = (int) cc;
        cc >>= 32;
        cc += ((long) xx[2] & 4294967295L) + t2 - t5;
        z[2] = (int) cc;
        cc >>= 32;
        cc += ((long) xx[3] & 4294967295L) + (t3 << 1) + xx13 - xx15 - t0;
        z[3] = (int) cc;
        cc >>= 32;
        cc += ((long) xx[4] & 4294967295L) + (t4 << 1) + xx14 - t1;
        z[4] = (int) cc;
        cc >>= 32;
        cc += ((long) xx[5] & 4294967295L) + (t5 << 1) - t2;
        z[5] = (int) cc;
        cc >>= 32;
        cc += ((long) xx[6] & 4294967295L) + (t6 << 1) + t5 - t0;
        z[6] = (int) cc;
        cc >>= 32;
        cc += ((long) xx[7] & 4294967295L) + (xx15 << 1) + xx08 - t2 - t4;
        z[7] = (int) cc;
        cc >>= 32;
        cc += 6L;
        reduce32((int) cc, z);
    }

    public static void reduce32(int x, int[] z) {
        long cc = 0L;
        if (x != 0) {
            long xx08 = (long) x & 4294967295L;
            cc += ((long) z[0] & 4294967295L) + xx08;
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

            cc += ((long) z[3] & 4294967295L) - xx08;
            z[3] = (int) cc;
            cc >>= 32;
            if (cc != 0L) {
                cc += (long) z[4] & 4294967295L;
                z[4] = (int) cc;
                cc >>= 32;
                cc += (long) z[5] & 4294967295L;
                z[5] = (int) cc;
                cc >>= 32;
            }

            cc += ((long) z[6] & 4294967295L) - xx08;
            z[6] = (int) cc;
            cc >>= 32;
            cc += ((long) z[7] & 4294967295L) + xx08;
            z[7] = (int) cc;
            cc >>= 32;
        }

        if (cc != 0L || z[7] == -1 && Nat256.gte(z, P)) {
            addPInvTo(z);
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
            subPInvFrom(z);
        }

    }

    public static void subtractExt(int[] xx, int[] yy, int[] zz) {
        int c = Nat.sub(16, xx, yy, zz);
        if (c != 0) {
            Nat.addTo(16, PExt, zz);
        }

    }

    public static void twice(int[] x, int[] z) {
        int c = Nat.shiftUpBit(8, x, 0, z);
        if (c != 0 || z[7] == -1 && Nat256.gte(z, P)) {
            addPInvTo(z);
        }

    }

    private static void addPInvTo(int[] z) {
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
            c += (long) z[4] & 4294967295L;
            z[4] = (int) c;
            c >>= 32;
            c += (long) z[5] & 4294967295L;
            z[5] = (int) c;
            c >>= 32;
        }

        c += ((long) z[6] & 4294967295L) - 1L;
        z[6] = (int) c;
        c >>= 32;
        c += ((long) z[7] & 4294967295L) + 1L;
        z[7] = (int) c;
    }

    private static void subPInvFrom(int[] z) {
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
            c += (long) z[4] & 4294967295L;
            z[4] = (int) c;
            c >>= 32;
            c += (long) z[5] & 4294967295L;
            z[5] = (int) c;
            c >>= 32;
        }

        c += ((long) z[6] & 4294967295L) + 1L;
        z[6] = (int) c;
        c >>= 32;
        c += ((long) z[7] & 4294967295L) - 1L;
        z[7] = (int) c;
    }
}
