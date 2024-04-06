package com.myzuji.sadk.org.bouncycastle.math.ec.custom.sec;

import com.myzuji.sadk.org.bouncycastle.math.raw.Nat;
import com.myzuji.sadk.org.bouncycastle.math.raw.Nat384;

import java.math.BigInteger;

public class SecP384R1Field {
    private static final long M = 4294967295L;
    static final int[] P = new int[]{-1, 0, 0, -1, -2, -1, -1, -1, -1, -1, -1, -1};
    static final int[] PExt = new int[]{1, -2, 0, 2, 0, -2, 0, 2, 1, 0, 0, 0, -2, 1, 0, -2, -3, -1, -1, -1, -1, -1, -1, -1};
    private static final int[] PExtInv = new int[]{-1, 1, -1, -3, -1, 1, -1, -3, -2, -1, -1, -1, 1, -2, -1, 1, 2};
    private static final int P11 = -1;
    private static final int PExt23 = -1;

    public SecP384R1Field() {
    }

    public static void add(int[] x, int[] y, int[] z) {
        int c = Nat.add(12, x, y, z);
        if (c != 0 || z[11] == -1 && Nat.gte(12, z, P)) {
            addPInvTo(z);
        }

    }

    public static void addExt(int[] xx, int[] yy, int[] zz) {
        int c = Nat.add(24, xx, yy, zz);
        if ((c != 0 || zz[23] == -1 && Nat.gte(24, zz, PExt)) && Nat.addTo(PExtInv.length, PExtInv, zz) != 0) {
            Nat.incAt(24, zz, PExtInv.length);
        }

    }

    public static void addOne(int[] x, int[] z) {
        int c = Nat.inc(12, x, z);
        if (c != 0 || z[11] == -1 && Nat.gte(12, z, P)) {
            addPInvTo(z);
        }

    }

    public static int[] fromBigInteger(BigInteger x) {
        int[] z = Nat.fromBigInteger(384, x);
        if (z[11] == -1 && Nat.gte(12, z, P)) {
            Nat.subFrom(12, P, z);
        }

        return z;
    }

    public static void half(int[] x, int[] z) {
        if ((x[0] & 1) == 0) {
            Nat.shiftDownBit(12, x, 0, z);
        } else {
            int c = Nat.add(12, x, P, z);
            Nat.shiftDownBit(12, z, c);
        }

    }

    public static void multiply(int[] x, int[] y, int[] z) {
        int[] tt = Nat.create(24);
        Nat384.mul(x, y, tt);
        reduce(tt, z);
    }

    public static void negate(int[] x, int[] z) {
        if (Nat.isZero(12, x)) {
            Nat.zero(12, z);
        } else {
            Nat.sub(12, P, x, z);
        }

    }

    public static void reduce(int[] xx, int[] z) {
        long xx16 = (long) xx[16] & 4294967295L;
        long xx17 = (long) xx[17] & 4294967295L;
        long xx18 = (long) xx[18] & 4294967295L;
        long xx19 = (long) xx[19] & 4294967295L;
        long xx20 = (long) xx[20] & 4294967295L;
        long xx21 = (long) xx[21] & 4294967295L;
        long xx22 = (long) xx[22] & 4294967295L;
        long xx23 = (long) xx[23] & 4294967295L;
        long n = 1L;
        long t0 = ((long) xx[12] & 4294967295L) + xx20 - 1L;
        long t1 = ((long) xx[13] & 4294967295L) + xx22;
        long t2 = ((long) xx[14] & 4294967295L) + xx22 + xx23;
        long t3 = ((long) xx[15] & 4294967295L) + xx23;
        long t4 = xx17 + xx21;
        long t5 = xx21 - xx23;
        long t6 = xx22 - xx23;
        long cc = 0L;
        cc += ((long) xx[0] & 4294967295L) + t0 + t5;
        z[0] = (int) cc;
        cc >>= 32;
        cc += ((long) xx[1] & 4294967295L) + xx23 - t0 + t1;
        z[1] = (int) cc;
        cc >>= 32;
        cc += ((long) xx[2] & 4294967295L) - xx21 - t1 + t2;
        z[2] = (int) cc;
        cc >>= 32;
        cc += ((long) xx[3] & 4294967295L) + t0 - t2 + t3 + t5;
        z[3] = (int) cc;
        cc >>= 32;
        cc += ((long) xx[4] & 4294967295L) + xx16 + xx21 + t0 + t1 - t3 + t5;
        z[4] = (int) cc;
        cc >>= 32;
        cc += ((long) xx[5] & 4294967295L) - xx16 + t1 + t2 + t4;
        z[5] = (int) cc;
        cc >>= 32;
        cc += ((long) xx[6] & 4294967295L) + xx18 - xx17 + t2 + t3;
        z[6] = (int) cc;
        cc >>= 32;
        cc += ((long) xx[7] & 4294967295L) + xx16 + xx19 - xx18 + t3;
        z[7] = (int) cc;
        cc >>= 32;
        cc += ((long) xx[8] & 4294967295L) + xx16 + xx17 + xx20 - xx19;
        z[8] = (int) cc;
        cc >>= 32;
        cc += ((long) xx[9] & 4294967295L) + xx18 - xx20 + t4;
        z[9] = (int) cc;
        cc >>= 32;
        cc += ((long) xx[10] & 4294967295L) + xx18 + xx19 - t5 + t6;
        z[10] = (int) cc;
        cc >>= 32;
        cc += ((long) xx[11] & 4294967295L) + xx19 + xx20 - t6;
        z[11] = (int) cc;
        cc >>= 32;
        ++cc;
        reduce32((int) cc, z);
    }

    public static void reduce32(int x, int[] z) {
        long cc = 0L;
        if (x != 0) {
            long xx12 = (long) x & 4294967295L;
            cc += ((long) z[0] & 4294967295L) + xx12;
            z[0] = (int) cc;
            cc >>= 32;
            cc += ((long) z[1] & 4294967295L) - xx12;
            z[1] = (int) cc;
            cc >>= 32;
            if (cc != 0L) {
                cc += (long) z[2] & 4294967295L;
                z[2] = (int) cc;
                cc >>= 32;
            }

            cc += ((long) z[3] & 4294967295L) + xx12;
            z[3] = (int) cc;
            cc >>= 32;
            cc += ((long) z[4] & 4294967295L) + xx12;
            z[4] = (int) cc;
            cc >>= 32;
        }

        if (cc != 0L && Nat.incAt(12, z, 5) != 0 || z[11] == -1 && Nat.gte(12, z, P)) {
            addPInvTo(z);
        }

    }

    public static void square(int[] x, int[] z) {
        int[] tt = Nat.create(24);
        Nat384.square(x, tt);
        reduce(tt, z);
    }

    public static void squareN(int[] x, int n, int[] z) {
        int[] tt = Nat.create(24);
        Nat384.square(x, tt);
        reduce(tt, z);

        while (true) {
            --n;
            if (n <= 0) {
                return;
            }

            Nat384.square(z, tt);
            reduce(tt, z);
        }
    }

    public static void subtract(int[] x, int[] y, int[] z) {
        int c = Nat.sub(12, x, y, z);
        if (c != 0) {
            subPInvFrom(z);
        }

    }

    public static void subtractExt(int[] xx, int[] yy, int[] zz) {
        int c = Nat.sub(24, xx, yy, zz);
        if (c != 0 && Nat.subFrom(PExtInv.length, PExtInv, zz) != 0) {
            Nat.decAt(24, zz, PExtInv.length);
        }

    }

    public static void twice(int[] x, int[] z) {
        int c = Nat.shiftUpBit(12, x, 0, z);
        if (c != 0 || z[11] == -1 && Nat.gte(12, z, P)) {
            addPInvTo(z);
        }

    }

    private static void addPInvTo(int[] z) {
        long c = ((long) z[0] & 4294967295L) + 1L;
        z[0] = (int) c;
        c >>= 32;
        c += ((long) z[1] & 4294967295L) - 1L;
        z[1] = (int) c;
        c >>= 32;
        if (c != 0L) {
            c += (long) z[2] & 4294967295L;
            z[2] = (int) c;
            c >>= 32;
        }

        c += ((long) z[3] & 4294967295L) + 1L;
        z[3] = (int) c;
        c >>= 32;
        c += ((long) z[4] & 4294967295L) + 1L;
        z[4] = (int) c;
        c >>= 32;
        if (c != 0L) {
            Nat.incAt(12, z, 5);
        }

    }

    private static void subPInvFrom(int[] z) {
        long c = ((long) z[0] & 4294967295L) - 1L;
        z[0] = (int) c;
        c >>= 32;
        c += ((long) z[1] & 4294967295L) + 1L;
        z[1] = (int) c;
        c >>= 32;
        if (c != 0L) {
            c += (long) z[2] & 4294967295L;
            z[2] = (int) c;
            c >>= 32;
        }

        c += ((long) z[3] & 4294967295L) - 1L;
        z[3] = (int) c;
        c >>= 32;
        c += ((long) z[4] & 4294967295L) - 1L;
        z[4] = (int) c;
        c >>= 32;
        if (c != 0L) {
            Nat.decAt(12, z, 5);
        }

    }
}
