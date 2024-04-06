package com.myzuji.sadk.org.bouncycastle.crypto.digests;

import com.myzuji.sadk.org.bouncycastle.util.Memoable;
import com.myzuji.sadk.org.bouncycastle.util.Pack;

public class SM3DigestKit extends GeneralDigest {
    private static final int DIGEST_LENGTH = 32;
    private static final int BLOCK_SIZE = 16;
    private int[] V = new int[8];
    private int[] inwords = new int[16];
    private int xOff;
    private int[] W = new int[68];
    private int[] W1 = new int[64];
    private static final int[] T = new int[64];

    public SM3DigestKit() {
        this.reset();
    }

    public SM3DigestKit(SM3DigestKit t) {
        super(t);
        this.copyIn(t);
    }

    private void copyIn(SM3DigestKit t) {
        System.arraycopy(t.V, 0, this.V, 0, this.V.length);
        System.arraycopy(t.inwords, 0, this.inwords, 0, this.inwords.length);
        this.xOff = t.xOff;
    }

    public String getAlgorithmName() {
        return "SM3";
    }

    public int getDigestSize() {
        return 32;
    }

    public Memoable copy() {
        return new SM3DigestKit(this);
    }

    public void reset(Memoable other) {
        SM3DigestKit d = (SM3DigestKit) other;
        super.copyIn(d);
        this.copyIn(d);
    }

    public void reset() {
        super.reset();
        this.V[0] = 1937774191;
        this.V[1] = 1226093241;
        this.V[2] = 388252375;
        this.V[3] = -628488704;
        this.V[4] = -1452330820;
        this.V[5] = 372324522;
        this.V[6] = -477237683;
        this.V[7] = -1325724082;
        this.xOff = 0;
    }

    public int doFinal(byte[] out, int outOff) {
        this.finish();
        Pack.intToBigEndian(this.V[0], out, outOff + 0);
        Pack.intToBigEndian(this.V[1], out, outOff + 4);
        Pack.intToBigEndian(this.V[2], out, outOff + 8);
        Pack.intToBigEndian(this.V[3], out, outOff + 12);
        Pack.intToBigEndian(this.V[4], out, outOff + 16);
        Pack.intToBigEndian(this.V[5], out, outOff + 20);
        Pack.intToBigEndian(this.V[6], out, outOff + 24);
        Pack.intToBigEndian(this.V[7], out, outOff + 28);
        this.reset();
        return 32;
    }

    protected void processWord(byte[] in, int inOff) {
        int var10000 = (in[inOff] & 255) << 24;
        ++inOff;
        var10000 |= (in[inOff] & 255) << 16;
        ++inOff;
        var10000 |= (in[inOff] & 255) << 8;
        ++inOff;
        int n = var10000 | in[inOff] & 255;
        this.inwords[this.xOff] = n;
        ++this.xOff;
        if (this.xOff >= 16) {
            this.processBlock();
        }

    }

    protected void processLength(long bitLength) {
        if (this.xOff > 14) {
            this.inwords[this.xOff] = 0;
            ++this.xOff;
            this.processBlock();
        }

        while (this.xOff < 14) {
            this.inwords[this.xOff] = 0;
            ++this.xOff;
        }

        this.inwords[this.xOff++] = (int) (bitLength >>> 32);
        this.inwords[this.xOff++] = (int) bitLength;
    }

    private int P0(int x) {
        int r9 = x << 9 | x >>> 23;
        int r17 = x << 17 | x >>> 15;
        return x ^ r9 ^ r17;
    }

    private int P1(int x) {
        int r15 = x << 15 | x >>> 17;
        int r23 = x << 23 | x >>> 9;
        return x ^ r15 ^ r23;
    }

    private int FF0(int x, int y, int z) {
        return x ^ y ^ z;
    }

    private int FF1(int x, int y, int z) {
        return x & y | x & z | y & z;
    }

    private int GG0(int x, int y, int z) {
        return x ^ y ^ z;
    }

    private int GG1(int x, int y, int z) {
        return x & y | ~x & z;
    }

    protected void processBlock() {
        int A;
        for (A = 0; A < 16; ++A) {
            this.W[A] = this.inwords[A];
        }

        int B;
        int C;
        int D;
        int E;
        for (A = 16; A < 68; ++A) {
            B = this.W[A - 3];
            C = B << 15 | B >>> 17;
            D = this.W[A - 13];
            E = D << 7 | D >>> 25;
            this.W[A] = this.P1(this.W[A - 16] ^ this.W[A - 9] ^ C) ^ E ^ this.W[A - 6];
        }

        for (A = 0; A < 64; ++A) {
            this.W1[A] = this.W[A] ^ this.W[A + 4];
        }

        A = this.V[0];
        B = this.V[1];
        C = this.V[2];
        D = this.V[3];
        E = this.V[4];
        int F = this.V[5];
        int G = this.V[6];
        int H = this.V[7];

        int j;
        int a12;
        int s1_;
        int SS1;
        int SS2;
        int TT1;
        int TT2;
        for (j = 0; j < 16; ++j) {
            a12 = A << 12 | A >>> 20;
            s1_ = a12 + E + T[j];
            SS1 = s1_ << 7 | s1_ >>> 25;
            SS2 = SS1 ^ a12;
            TT1 = this.FF0(A, B, C) + D + SS2 + this.W1[j];
            TT2 = this.GG0(E, F, G) + H + SS1 + this.W[j];
            D = C;
            C = B << 9 | B >>> 23;
            B = A;
            A = TT1;
            H = G;
            G = F << 19 | F >>> 13;
            F = E;
            E = this.P0(TT2);
        }

        for (j = 16; j < 64; ++j) {
            a12 = A << 12 | A >>> 20;
            s1_ = a12 + E + T[j];
            SS1 = s1_ << 7 | s1_ >>> 25;
            SS2 = SS1 ^ a12;
            TT1 = this.FF1(A, B, C) + D + SS2 + this.W1[j];
            TT2 = this.GG1(E, F, G) + H + SS1 + this.W[j];
            D = C;
            C = B << 9 | B >>> 23;
            B = A;
            A = TT1;
            H = G;
            G = F << 19 | F >>> 13;
            F = E;
            E = this.P0(TT2);
        }

        int[] var10000 = this.V;
        var10000[0] ^= A;
        var10000 = this.V;
        var10000[1] ^= B;
        var10000 = this.V;
        var10000[2] ^= C;
        var10000 = this.V;
        var10000[3] ^= D;
        var10000 = this.V;
        var10000[4] ^= E;
        var10000 = this.V;
        var10000[5] ^= F;
        var10000 = this.V;
        var10000[6] ^= G;
        var10000 = this.V;
        var10000[7] ^= H;
        this.xOff = 0;
    }

    static {
        int i;
        int n;
        for (i = 0; i < 16; ++i) {
            n = 2043430169;
            T[i] = n << i | n >>> 32 - i;
        }

        for (i = 16; i < 64; ++i) {
            n = i % 32;
            int t = 2055708042;
            T[i] = t << n | t >>> 32 - n;
        }

    }
}
