package com.myzuji.sadk.org.bouncycastle.crypto.digests;

import com.myzuji.sadk.org.bouncycastle.util.Memoable;

public class RIPEMD128Digest extends GeneralDigest {
    private static final int DIGEST_LENGTH = 16;
    private int H0;
    private int H1;
    private int H2;
    private int H3;
    private int[] X = new int[16];
    private int xOff;

    public RIPEMD128Digest() {
        this.reset();
    }

    public RIPEMD128Digest(RIPEMD128Digest t) {
        super(t);
        this.copyIn(t);
    }

    private void copyIn(RIPEMD128Digest t) {
        super.copyIn(t);
        this.H0 = t.H0;
        this.H1 = t.H1;
        this.H2 = t.H2;
        this.H3 = t.H3;
        System.arraycopy(t.X, 0, this.X, 0, t.X.length);
        this.xOff = t.xOff;
    }

    public String getAlgorithmName() {
        return "RIPEMD128";
    }

    public int getDigestSize() {
        return 16;
    }

    protected void processWord(byte[] in, int inOff) {
        this.X[this.xOff++] = in[inOff] & 255 | (in[inOff + 1] & 255) << 8 | (in[inOff + 2] & 255) << 16 | (in[inOff + 3] & 255) << 24;
        if (this.xOff == 16) {
            this.processBlock();
        }

    }

    protected void processLength(long bitLength) {
        if (this.xOff > 14) {
            this.processBlock();
        }

        this.X[14] = (int) (bitLength & -1L);
        this.X[15] = (int) (bitLength >>> 32);
    }

    private void unpackWord(int word, byte[] out, int outOff) {
        out[outOff] = (byte) word;
        out[outOff + 1] = (byte) (word >>> 8);
        out[outOff + 2] = (byte) (word >>> 16);
        out[outOff + 3] = (byte) (word >>> 24);
    }

    public int doFinal(byte[] out, int outOff) {
        this.finish();
        this.unpackWord(this.H0, out, outOff);
        this.unpackWord(this.H1, out, outOff + 4);
        this.unpackWord(this.H2, out, outOff + 8);
        this.unpackWord(this.H3, out, outOff + 12);
        this.reset();
        return 16;
    }

    public void reset() {
        super.reset();
        this.H0 = 1732584193;
        this.H1 = -271733879;
        this.H2 = -1732584194;
        this.H3 = 271733878;
        this.xOff = 0;

        for (int i = 0; i != this.X.length; ++i) {
            this.X[i] = 0;
        }

    }

    private int RL(int x, int n) {
        return x << n | x >>> 32 - n;
    }

    private int f1(int x, int y, int z) {
        return x ^ y ^ z;
    }

    private int f2(int x, int y, int z) {
        return x & y | ~x & z;
    }

    private int f3(int x, int y, int z) {
        return (x | ~y) ^ z;
    }

    private int f4(int x, int y, int z) {
        return x & z | y & ~z;
    }

    private int F1(int a, int b, int c, int d, int x, int s) {
        return this.RL(a + this.f1(b, c, d) + x, s);
    }

    private int F2(int a, int b, int c, int d, int x, int s) {
        return this.RL(a + this.f2(b, c, d) + x + 1518500249, s);
    }

    private int F3(int a, int b, int c, int d, int x, int s) {
        return this.RL(a + this.f3(b, c, d) + x + 1859775393, s);
    }

    private int F4(int a, int b, int c, int d, int x, int s) {
        return this.RL(a + this.f4(b, c, d) + x + -1894007588, s);
    }

    private int FF1(int a, int b, int c, int d, int x, int s) {
        return this.RL(a + this.f1(b, c, d) + x, s);
    }

    private int FF2(int a, int b, int c, int d, int x, int s) {
        return this.RL(a + this.f2(b, c, d) + x + 1836072691, s);
    }

    private int FF3(int a, int b, int c, int d, int x, int s) {
        return this.RL(a + this.f3(b, c, d) + x + 1548603684, s);
    }

    private int FF4(int a, int b, int c, int d, int x, int s) {
        return this.RL(a + this.f4(b, c, d) + x + 1352829926, s);
    }

    protected void processBlock() {
        int aa;
        int a = aa = this.H0;
        int bb;
        int b = bb = this.H1;
        int cc;
        int c = cc = this.H2;
        int dd;
        int d = dd = this.H3;
        a = this.F1(a, b, c, d, this.X[0], 11);
        d = this.F1(d, a, b, c, this.X[1], 14);
        c = this.F1(c, d, a, b, this.X[2], 15);
        b = this.F1(b, c, d, a, this.X[3], 12);
        a = this.F1(a, b, c, d, this.X[4], 5);
        d = this.F1(d, a, b, c, this.X[5], 8);
        c = this.F1(c, d, a, b, this.X[6], 7);
        b = this.F1(b, c, d, a, this.X[7], 9);
        a = this.F1(a, b, c, d, this.X[8], 11);
        d = this.F1(d, a, b, c, this.X[9], 13);
        c = this.F1(c, d, a, b, this.X[10], 14);
        b = this.F1(b, c, d, a, this.X[11], 15);
        a = this.F1(a, b, c, d, this.X[12], 6);
        d = this.F1(d, a, b, c, this.X[13], 7);
        c = this.F1(c, d, a, b, this.X[14], 9);
        b = this.F1(b, c, d, a, this.X[15], 8);
        a = this.F2(a, b, c, d, this.X[7], 7);
        d = this.F2(d, a, b, c, this.X[4], 6);
        c = this.F2(c, d, a, b, this.X[13], 8);
        b = this.F2(b, c, d, a, this.X[1], 13);
        a = this.F2(a, b, c, d, this.X[10], 11);
        d = this.F2(d, a, b, c, this.X[6], 9);
        c = this.F2(c, d, a, b, this.X[15], 7);
        b = this.F2(b, c, d, a, this.X[3], 15);
        a = this.F2(a, b, c, d, this.X[12], 7);
        d = this.F2(d, a, b, c, this.X[0], 12);
        c = this.F2(c, d, a, b, this.X[9], 15);
        b = this.F2(b, c, d, a, this.X[5], 9);
        a = this.F2(a, b, c, d, this.X[2], 11);
        d = this.F2(d, a, b, c, this.X[14], 7);
        c = this.F2(c, d, a, b, this.X[11], 13);
        b = this.F2(b, c, d, a, this.X[8], 12);
        a = this.F3(a, b, c, d, this.X[3], 11);
        d = this.F3(d, a, b, c, this.X[10], 13);
        c = this.F3(c, d, a, b, this.X[14], 6);
        b = this.F3(b, c, d, a, this.X[4], 7);
        a = this.F3(a, b, c, d, this.X[9], 14);
        d = this.F3(d, a, b, c, this.X[15], 9);
        c = this.F3(c, d, a, b, this.X[8], 13);
        b = this.F3(b, c, d, a, this.X[1], 15);
        a = this.F3(a, b, c, d, this.X[2], 14);
        d = this.F3(d, a, b, c, this.X[7], 8);
        c = this.F3(c, d, a, b, this.X[0], 13);
        b = this.F3(b, c, d, a, this.X[6], 6);
        a = this.F3(a, b, c, d, this.X[13], 5);
        d = this.F3(d, a, b, c, this.X[11], 12);
        c = this.F3(c, d, a, b, this.X[5], 7);
        b = this.F3(b, c, d, a, this.X[12], 5);
        a = this.F4(a, b, c, d, this.X[1], 11);
        d = this.F4(d, a, b, c, this.X[9], 12);
        c = this.F4(c, d, a, b, this.X[11], 14);
        b = this.F4(b, c, d, a, this.X[10], 15);
        a = this.F4(a, b, c, d, this.X[0], 14);
        d = this.F4(d, a, b, c, this.X[8], 15);
        c = this.F4(c, d, a, b, this.X[12], 9);
        b = this.F4(b, c, d, a, this.X[4], 8);
        a = this.F4(a, b, c, d, this.X[13], 9);
        d = this.F4(d, a, b, c, this.X[3], 14);
        c = this.F4(c, d, a, b, this.X[7], 5);
        b = this.F4(b, c, d, a, this.X[15], 6);
        a = this.F4(a, b, c, d, this.X[14], 8);
        d = this.F4(d, a, b, c, this.X[5], 6);
        c = this.F4(c, d, a, b, this.X[6], 5);
        b = this.F4(b, c, d, a, this.X[2], 12);
        aa = this.FF4(aa, bb, cc, dd, this.X[5], 8);
        dd = this.FF4(dd, aa, bb, cc, this.X[14], 9);
        cc = this.FF4(cc, dd, aa, bb, this.X[7], 9);
        bb = this.FF4(bb, cc, dd, aa, this.X[0], 11);
        aa = this.FF4(aa, bb, cc, dd, this.X[9], 13);
        dd = this.FF4(dd, aa, bb, cc, this.X[2], 15);
        cc = this.FF4(cc, dd, aa, bb, this.X[11], 15);
        bb = this.FF4(bb, cc, dd, aa, this.X[4], 5);
        aa = this.FF4(aa, bb, cc, dd, this.X[13], 7);
        dd = this.FF4(dd, aa, bb, cc, this.X[6], 7);
        cc = this.FF4(cc, dd, aa, bb, this.X[15], 8);
        bb = this.FF4(bb, cc, dd, aa, this.X[8], 11);
        aa = this.FF4(aa, bb, cc, dd, this.X[1], 14);
        dd = this.FF4(dd, aa, bb, cc, this.X[10], 14);
        cc = this.FF4(cc, dd, aa, bb, this.X[3], 12);
        bb = this.FF4(bb, cc, dd, aa, this.X[12], 6);
        aa = this.FF3(aa, bb, cc, dd, this.X[6], 9);
        dd = this.FF3(dd, aa, bb, cc, this.X[11], 13);
        cc = this.FF3(cc, dd, aa, bb, this.X[3], 15);
        bb = this.FF3(bb, cc, dd, aa, this.X[7], 7);
        aa = this.FF3(aa, bb, cc, dd, this.X[0], 12);
        dd = this.FF3(dd, aa, bb, cc, this.X[13], 8);
        cc = this.FF3(cc, dd, aa, bb, this.X[5], 9);
        bb = this.FF3(bb, cc, dd, aa, this.X[10], 11);
        aa = this.FF3(aa, bb, cc, dd, this.X[14], 7);
        dd = this.FF3(dd, aa, bb, cc, this.X[15], 7);
        cc = this.FF3(cc, dd, aa, bb, this.X[8], 12);
        bb = this.FF3(bb, cc, dd, aa, this.X[12], 7);
        aa = this.FF3(aa, bb, cc, dd, this.X[4], 6);
        dd = this.FF3(dd, aa, bb, cc, this.X[9], 15);
        cc = this.FF3(cc, dd, aa, bb, this.X[1], 13);
        bb = this.FF3(bb, cc, dd, aa, this.X[2], 11);
        aa = this.FF2(aa, bb, cc, dd, this.X[15], 9);
        dd = this.FF2(dd, aa, bb, cc, this.X[5], 7);
        cc = this.FF2(cc, dd, aa, bb, this.X[1], 15);
        bb = this.FF2(bb, cc, dd, aa, this.X[3], 11);
        aa = this.FF2(aa, bb, cc, dd, this.X[7], 8);
        dd = this.FF2(dd, aa, bb, cc, this.X[14], 6);
        cc = this.FF2(cc, dd, aa, bb, this.X[6], 6);
        bb = this.FF2(bb, cc, dd, aa, this.X[9], 14);
        aa = this.FF2(aa, bb, cc, dd, this.X[11], 12);
        dd = this.FF2(dd, aa, bb, cc, this.X[8], 13);
        cc = this.FF2(cc, dd, aa, bb, this.X[12], 5);
        bb = this.FF2(bb, cc, dd, aa, this.X[2], 14);
        aa = this.FF2(aa, bb, cc, dd, this.X[10], 13);
        dd = this.FF2(dd, aa, bb, cc, this.X[0], 13);
        cc = this.FF2(cc, dd, aa, bb, this.X[4], 7);
        bb = this.FF2(bb, cc, dd, aa, this.X[13], 5);
        aa = this.FF1(aa, bb, cc, dd, this.X[8], 15);
        dd = this.FF1(dd, aa, bb, cc, this.X[6], 5);
        cc = this.FF1(cc, dd, aa, bb, this.X[4], 8);
        bb = this.FF1(bb, cc, dd, aa, this.X[1], 11);
        aa = this.FF1(aa, bb, cc, dd, this.X[3], 14);
        dd = this.FF1(dd, aa, bb, cc, this.X[11], 14);
        cc = this.FF1(cc, dd, aa, bb, this.X[15], 6);
        bb = this.FF1(bb, cc, dd, aa, this.X[0], 14);
        aa = this.FF1(aa, bb, cc, dd, this.X[5], 6);
        dd = this.FF1(dd, aa, bb, cc, this.X[12], 9);
        cc = this.FF1(cc, dd, aa, bb, this.X[2], 12);
        bb = this.FF1(bb, cc, dd, aa, this.X[13], 9);
        aa = this.FF1(aa, bb, cc, dd, this.X[9], 12);
        dd = this.FF1(dd, aa, bb, cc, this.X[7], 5);
        cc = this.FF1(cc, dd, aa, bb, this.X[10], 15);
        bb = this.FF1(bb, cc, dd, aa, this.X[14], 8);
        dd += c + this.H1;
        this.H1 = this.H2 + d + aa;
        this.H2 = this.H3 + a + bb;
        this.H3 = this.H0 + b + cc;
        this.H0 = dd;
        this.xOff = 0;

        for (int i = 0; i != this.X.length; ++i) {
            this.X[i] = 0;
        }

    }

    public Memoable copy() {
        return new RIPEMD128Digest(this);
    }

    public void reset(Memoable other) {
        RIPEMD128Digest d = (RIPEMD128Digest) other;
        this.copyIn(d);
    }
}
