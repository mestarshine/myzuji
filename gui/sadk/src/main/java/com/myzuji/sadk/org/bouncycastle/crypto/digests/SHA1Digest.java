package com.myzuji.sadk.org.bouncycastle.crypto.digests;

import com.myzuji.sadk.org.bouncycastle.util.Memoable;
import com.myzuji.sadk.org.bouncycastle.util.Pack;

public class SHA1Digest extends GeneralDigest implements EncodableDigest {
    private static final int DIGEST_LENGTH = 20;
    private int H1;
    private int H2;
    private int H3;
    private int H4;
    private int H5;
    private int[] X = new int[80];
    private int xOff;
    private static final int Y1 = 1518500249;
    private static final int Y2 = 1859775393;
    private static final int Y3 = -1894007588;
    private static final int Y4 = -899497514;

    public SHA1Digest() {
        this.reset();
    }

    public SHA1Digest(SHA1Digest t) {
        super(t);
        this.copyIn(t);
    }

    public SHA1Digest(byte[] encodedState) {
        super(encodedState);
        this.H1 = Pack.bigEndianToInt(encodedState, 16);
        this.H2 = Pack.bigEndianToInt(encodedState, 20);
        this.H3 = Pack.bigEndianToInt(encodedState, 24);
        this.H4 = Pack.bigEndianToInt(encodedState, 28);
        this.H5 = Pack.bigEndianToInt(encodedState, 32);
        this.xOff = Pack.bigEndianToInt(encodedState, 36);

        for (int i = 0; i != this.xOff; ++i) {
            this.X[i] = Pack.bigEndianToInt(encodedState, 40 + i * 4);
        }

    }

    private void copyIn(SHA1Digest t) {
        this.H1 = t.H1;
        this.H2 = t.H2;
        this.H3 = t.H3;
        this.H4 = t.H4;
        this.H5 = t.H5;
        System.arraycopy(t.X, 0, this.X, 0, t.X.length);
        this.xOff = t.xOff;
    }

    public String getAlgorithmName() {
        return "SHA-1";
    }

    public int getDigestSize() {
        return 20;
    }

    protected void processWord(byte[] in, int inOff) {
        int n = in[inOff] << 24;
        ++inOff;
        n |= (in[inOff] & 255) << 16;
        ++inOff;
        n |= (in[inOff] & 255) << 8;
        ++inOff;
        n |= in[inOff] & 255;
        this.X[this.xOff] = n;
        if (++this.xOff == 16) {
            this.processBlock();
        }

    }

    protected void processLength(long bitLength) {
        if (this.xOff > 14) {
            this.processBlock();
        }

        this.X[14] = (int) (bitLength >>> 32);
        this.X[15] = (int) (bitLength & -1L);
    }

    public int doFinal(byte[] out, int outOff) {
        this.finish();
        Pack.intToBigEndian(this.H1, out, outOff);
        Pack.intToBigEndian(this.H2, out, outOff + 4);
        Pack.intToBigEndian(this.H3, out, outOff + 8);
        Pack.intToBigEndian(this.H4, out, outOff + 12);
        Pack.intToBigEndian(this.H5, out, outOff + 16);
        this.reset();
        return 20;
    }

    public void reset() {
        super.reset();
        this.H1 = 1732584193;
        this.H2 = -271733879;
        this.H3 = -1732584194;
        this.H4 = 271733878;
        this.H5 = -1009589776;
        this.xOff = 0;

        for (int i = 0; i != this.X.length; ++i) {
            this.X[i] = 0;
        }

    }

    private int f(int u, int v, int w) {
        return u & v | ~u & w;
    }

    private int h(int u, int v, int w) {
        return u ^ v ^ w;
    }

    private int g(int u, int v, int w) {
        return u & v | u & w | v & w;
    }

    protected void processBlock() {
        int A;
        int B;
        for (A = 16; A < 80; ++A) {
            B = this.X[A - 3] ^ this.X[A - 8] ^ this.X[A - 14] ^ this.X[A - 16];
            this.X[A] = B << 1 | B >>> 31;
        }

        A = this.H1;
        B = this.H2;
        int C = this.H3;
        int D = this.H4;
        int E = this.H5;
        int idx = 0;

        int i;
        for (i = 0; i < 4; ++i) {
            E += (A << 5 | A >>> 27) + this.f(B, C, D) + this.X[idx++] + 1518500249;
            B = B << 30 | B >>> 2;
            D += (E << 5 | E >>> 27) + this.f(A, B, C) + this.X[idx++] + 1518500249;
            A = A << 30 | A >>> 2;
            C += (D << 5 | D >>> 27) + this.f(E, A, B) + this.X[idx++] + 1518500249;
            E = E << 30 | E >>> 2;
            B += (C << 5 | C >>> 27) + this.f(D, E, A) + this.X[idx++] + 1518500249;
            D = D << 30 | D >>> 2;
            A += (B << 5 | B >>> 27) + this.f(C, D, E) + this.X[idx++] + 1518500249;
            C = C << 30 | C >>> 2;
        }

        for (i = 0; i < 4; ++i) {
            E += (A << 5 | A >>> 27) + this.h(B, C, D) + this.X[idx++] + 1859775393;
            B = B << 30 | B >>> 2;
            D += (E << 5 | E >>> 27) + this.h(A, B, C) + this.X[idx++] + 1859775393;
            A = A << 30 | A >>> 2;
            C += (D << 5 | D >>> 27) + this.h(E, A, B) + this.X[idx++] + 1859775393;
            E = E << 30 | E >>> 2;
            B += (C << 5 | C >>> 27) + this.h(D, E, A) + this.X[idx++] + 1859775393;
            D = D << 30 | D >>> 2;
            A += (B << 5 | B >>> 27) + this.h(C, D, E) + this.X[idx++] + 1859775393;
            C = C << 30 | C >>> 2;
        }

        for (i = 0; i < 4; ++i) {
            E += (A << 5 | A >>> 27) + this.g(B, C, D) + this.X[idx++] + -1894007588;
            B = B << 30 | B >>> 2;
            D += (E << 5 | E >>> 27) + this.g(A, B, C) + this.X[idx++] + -1894007588;
            A = A << 30 | A >>> 2;
            C += (D << 5 | D >>> 27) + this.g(E, A, B) + this.X[idx++] + -1894007588;
            E = E << 30 | E >>> 2;
            B += (C << 5 | C >>> 27) + this.g(D, E, A) + this.X[idx++] + -1894007588;
            D = D << 30 | D >>> 2;
            A += (B << 5 | B >>> 27) + this.g(C, D, E) + this.X[idx++] + -1894007588;
            C = C << 30 | C >>> 2;
        }

        for (i = 0; i <= 3; ++i) {
            E += (A << 5 | A >>> 27) + this.h(B, C, D) + this.X[idx++] + -899497514;
            B = B << 30 | B >>> 2;
            D += (E << 5 | E >>> 27) + this.h(A, B, C) + this.X[idx++] + -899497514;
            A = A << 30 | A >>> 2;
            C += (D << 5 | D >>> 27) + this.h(E, A, B) + this.X[idx++] + -899497514;
            E = E << 30 | E >>> 2;
            B += (C << 5 | C >>> 27) + this.h(D, E, A) + this.X[idx++] + -899497514;
            D = D << 30 | D >>> 2;
            A += (B << 5 | B >>> 27) + this.h(C, D, E) + this.X[idx++] + -899497514;
            C = C << 30 | C >>> 2;
        }

        this.H1 += A;
        this.H2 += B;
        this.H3 += C;
        this.H4 += D;
        this.H5 += E;
        this.xOff = 0;

        for (i = 0; i < 16; ++i) {
            this.X[i] = 0;
        }

    }

    public Memoable copy() {
        return new SHA1Digest(this);
    }

    public void reset(Memoable other) {
        SHA1Digest d = (SHA1Digest) other;
        super.copyIn(d);
        this.copyIn(d);
    }

    public byte[] getEncodedState() {
        byte[] state = new byte[40 + this.xOff * 4];
        super.populateState(state);
        Pack.intToBigEndian(this.H1, state, 16);
        Pack.intToBigEndian(this.H2, state, 20);
        Pack.intToBigEndian(this.H3, state, 24);
        Pack.intToBigEndian(this.H4, state, 28);
        Pack.intToBigEndian(this.H5, state, 32);
        Pack.intToBigEndian(this.xOff, state, 36);

        for (int i = 0; i != this.xOff; ++i) {
            Pack.intToBigEndian(this.X[i], state, 40 + i * 4);
        }

        return state;
    }
}
