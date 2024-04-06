package com.myzuji.sadk.org.bouncycastle.math.raw;

import java.util.Random;

public abstract class Mod {
    public Mod() {
    }

    public static int inverse32(int d) {
        int x = d;
        x *= 2 - d * x;
        x *= 2 - d * x;
        x *= 2 - d * x;
        x *= 2 - d * x;
        return x;
    }

    public static void invert(int[] p, int[] x, int[] z) {
        int len = p.length;
        if (Nat.isZero(len, x)) {
            throw new IllegalArgumentException("'x' cannot be 0");
        } else if (Nat.isOne(len, x)) {
            System.arraycopy(x, 0, z, 0, len);
        } else {
            int[] u = Nat.copy(len, x);
            int[] a = Nat.create(len);
            a[0] = 1;
            int ac = 0;
            if ((u[0] & 1) == 0) {
                ac = inversionStep(p, u, len, a, ac);
            }

            if (Nat.isOne(len, u)) {
                inversionResult(p, ac, a, z);
            } else {
                int[] v = Nat.copy(len, p);
                int[] b = Nat.create(len);
                int bc = 0;
                int uvLen = len;

                while (true) {
                    while (u[uvLen - 1] != 0 || v[uvLen - 1] != 0) {
                        if (Nat.gte(uvLen, u, v)) {
                            Nat.subFrom(uvLen, v, u);
                            ac += Nat.subFrom(len, b, a) - bc;
                            ac = inversionStep(p, u, uvLen, a, ac);
                            if (Nat.isOne(uvLen, u)) {
                                inversionResult(p, ac, a, z);
                                return;
                            }
                        } else {
                            Nat.subFrom(uvLen, u, v);
                            bc += Nat.subFrom(len, a, b) - ac;
                            bc = inversionStep(p, v, uvLen, b, bc);
                            if (Nat.isOne(uvLen, v)) {
                                inversionResult(p, bc, b, z);
                                return;
                            }
                        }
                    }

                    --uvLen;
                }
            }
        }
    }

    public static int[] random(int[] p) {
        int len = p.length;
        Random rand = new Random();
        int[] s = Nat.create(len);
        int m = p[len - 1];
        m |= m >>> 1;
        m |= m >>> 2;
        m |= m >>> 4;
        m |= m >>> 8;
        m |= m >>> 16;

        do {
            for (int i = 0; i != len; ++i) {
                s[i] = rand.nextInt();
            }

            s[len - 1] &= m;
        } while (Nat.gte(len, s, p));

        return s;
    }

    public static void add(int[] p, int[] x, int[] y, int[] z) {
        int len = p.length;
        int c = Nat.add(len, x, y, z);
        if (c != 0) {
            Nat.subFrom(len, p, z);
        }

    }

    public static void subtract(int[] p, int[] x, int[] y, int[] z) {
        int len = p.length;
        int c = Nat.sub(len, x, y, z);
        if (c != 0) {
            Nat.addTo(len, p, z);
        }

    }

    private static void inversionResult(int[] p, int ac, int[] a, int[] z) {
        if (ac < 0) {
            Nat.add(p.length, a, p, z);
        } else {
            System.arraycopy(a, 0, z, 0, p.length);
        }

    }

    private static int inversionStep(int[] p, int[] u, int uLen, int[] x, int xc) {
        int len = p.length;

        int count;
        for (count = 0; u[0] == 0; count += 32) {
            Nat.shiftDownWord(uLen, u, 0);
        }

        int i = getTrailingZeroes(u[0]);
        if (i > 0) {
            Nat.shiftDownBits(uLen, u, i, 0);
            count += i;
        }

        for (i = 0; i < count; ++i) {
            if ((x[0] & 1) != 0) {
                if (xc < 0) {
                    xc += Nat.addTo(len, p, x);
                } else {
                    xc += Nat.subFrom(len, p, x);
                }
            }

            Nat.shiftDownBit(len, x, xc);
        }

        return xc;
    }

    private static int getTrailingZeroes(int x) {
        int count;
        for (count = 0; (x & 1) == 0; ++count) {
            x >>>= 1;
        }

        return count;
    }
}
