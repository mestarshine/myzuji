package com.myzuji.sadk.org.bouncycastle.crypto.digests;

import com.myzuji.sadk.org.bouncycastle.crypto.BlockCipher;
import com.myzuji.sadk.org.bouncycastle.crypto.CipherParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.engines.GOST28147Engine;
import com.myzuji.sadk.org.bouncycastle.crypto.params.KeyParameter;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ParametersWithSBox;
import com.myzuji.sadk.org.bouncycastle.util.Arrays;
import com.myzuji.sadk.org.bouncycastle.util.Memoable;
import com.myzuji.sadk.org.bouncycastle.util.Pack;

public class GOST3411Digest implements ExtendedDigest, Memoable {
    private static final int DIGEST_LENGTH = 32;
    private byte[] H = new byte[32];
    private byte[] L = new byte[32];
    private byte[] M = new byte[32];
    private byte[] Sum = new byte[32];
    private byte[][] C = new byte[4][32];
    private byte[] xBuf = new byte[32];
    private int xBufOff;
    private long byteCount;
    private BlockCipher cipher = new GOST28147Engine();
    private byte[] sBox;
    private byte[] K = new byte[32];
    byte[] a = new byte[8];
    short[] wS = new short[16];
    short[] w_S = new short[16];
    byte[] S = new byte[32];
    byte[] U = new byte[32];
    byte[] V = new byte[32];
    byte[] W = new byte[32];
    private static final byte[] C2 = new byte[]{0, -1, 0, -1, 0, -1, 0, -1, -1, 0, -1, 0, -1, 0, -1, 0, 0, -1, -1, 0, -1, 0, 0, -1, -1, 0, 0, 0, -1, -1, 0, -1};

    public GOST3411Digest() {
        this.sBox = GOST28147Engine.getSBox("D-A");
        this.cipher.init(true, new ParametersWithSBox((CipherParameters) null, this.sBox));
        this.reset();
    }

    public GOST3411Digest(byte[] sBoxParam) {
        this.sBox = Arrays.clone(sBoxParam);
        this.cipher.init(true, new ParametersWithSBox((CipherParameters) null, this.sBox));
        this.reset();
    }

    public GOST3411Digest(GOST3411Digest t) {
        this.reset(t);
    }

    public String getAlgorithmName() {
        return "GOST3411";
    }

    public int getDigestSize() {
        return 32;
    }

    public void update(byte in) {
        this.xBuf[this.xBufOff++] = in;
        if (this.xBufOff == this.xBuf.length) {
            this.sumByteArray(this.xBuf);
            this.processBlock(this.xBuf, 0);
            this.xBufOff = 0;
        }

        ++this.byteCount;
    }

    public void update(byte[] in, int inOff, int len) {
        while (this.xBufOff != 0 && len > 0) {
            this.update(in[inOff]);
            ++inOff;
            --len;
        }

        while (len > this.xBuf.length) {
            System.arraycopy(in, inOff, this.xBuf, 0, this.xBuf.length);
            this.sumByteArray(this.xBuf);
            this.processBlock(this.xBuf, 0);
            inOff += this.xBuf.length;
            len -= this.xBuf.length;
            this.byteCount += (long) this.xBuf.length;
        }

        while (len > 0) {
            this.update(in[inOff]);
            ++inOff;
            --len;
        }

    }

    private byte[] P(byte[] in) {
        for (int k = 0; k < 8; ++k) {
            this.K[4 * k] = in[k];
            this.K[1 + 4 * k] = in[8 + k];
            this.K[2 + 4 * k] = in[16 + k];
            this.K[3 + 4 * k] = in[24 + k];
        }

        return this.K;
    }

    private byte[] A(byte[] in) {
        for (int j = 0; j < 8; ++j) {
            this.a[j] = (byte) (in[j] ^ in[j + 8]);
        }

        System.arraycopy(in, 8, in, 0, 24);
        System.arraycopy(this.a, 0, in, 24, 8);
        return in;
    }

    private void E(byte[] key, byte[] s, int sOff, byte[] in, int inOff) {
        this.cipher.init(true, new KeyParameter(key));
        this.cipher.processBlock(in, inOff, s, sOff);
    }

    private void fw(byte[] in) {
        this.cpyBytesToShort(in, this.wS);
        this.w_S[15] = (short) (this.wS[0] ^ this.wS[1] ^ this.wS[2] ^ this.wS[3] ^ this.wS[12] ^ this.wS[15]);
        System.arraycopy(this.wS, 1, this.w_S, 0, 15);
        this.cpyShortToBytes(this.w_S, in);
    }

    protected void processBlock(byte[] in, int inOff) {
        System.arraycopy(in, inOff, this.M, 0, 32);
        System.arraycopy(this.H, 0, this.U, 0, 32);
        System.arraycopy(this.M, 0, this.V, 0, 32);

        int n;
        for (n = 0; n < 32; ++n) {
            this.W[n] = (byte) (this.U[n] ^ this.V[n]);
        }

        this.E(this.P(this.W), this.S, 0, this.H, 0);

        for (n = 1; n < 4; ++n) {
            byte[] tmpA = this.A(this.U);

            int j;
            for (j = 0; j < 32; ++j) {
                this.U[j] = (byte) (tmpA[j] ^ this.C[n][j]);
            }

            this.V = this.A(this.A(this.V));

            for (j = 0; j < 32; ++j) {
                this.W[j] = (byte) (this.U[j] ^ this.V[j]);
            }

            this.E(this.P(this.W), this.S, n * 8, this.H, n * 8);
        }

        for (n = 0; n < 12; ++n) {
            this.fw(this.S);
        }

        for (n = 0; n < 32; ++n) {
            this.S[n] ^= this.M[n];
        }

        this.fw(this.S);

        for (n = 0; n < 32; ++n) {
            this.S[n] ^= this.H[n];
        }

        for (n = 0; n < 61; ++n) {
            this.fw(this.S);
        }

        System.arraycopy(this.S, 0, this.H, 0, this.H.length);
    }

    private void finish() {
        Pack.longToLittleEndian(this.byteCount * 8L, this.L, 0);

        while (this.xBufOff != 0) {
            this.update((byte) 0);
        }

        this.processBlock(this.L, 0);
        this.processBlock(this.Sum, 0);
    }

    public int doFinal(byte[] out, int outOff) {
        this.finish();
        System.arraycopy(this.H, 0, out, outOff, this.H.length);
        this.reset();
        return 32;
    }

    public void reset() {
        this.byteCount = 0L;
        this.xBufOff = 0;

        int i;
        for (i = 0; i < this.H.length; ++i) {
            this.H[i] = 0;
        }

        for (i = 0; i < this.L.length; ++i) {
            this.L[i] = 0;
        }

        for (i = 0; i < this.M.length; ++i) {
            this.M[i] = 0;
        }

        for (i = 0; i < this.C[1].length; ++i) {
            this.C[1][i] = 0;
        }

        for (i = 0; i < this.C[3].length; ++i) {
            this.C[3][i] = 0;
        }

        for (i = 0; i < this.Sum.length; ++i) {
            this.Sum[i] = 0;
        }

        for (i = 0; i < this.xBuf.length; ++i) {
            this.xBuf[i] = 0;
        }

        System.arraycopy(C2, 0, this.C[2], 0, C2.length);
    }

    private void sumByteArray(byte[] in) {
        int carry = 0;

        for (int i = 0; i != this.Sum.length; ++i) {
            int sum = (this.Sum[i] & 255) + (in[i] & 255) + carry;
            this.Sum[i] = (byte) sum;
            carry = sum >>> 8;
        }

    }

    private void cpyBytesToShort(byte[] S, short[] wS) {
        for (int i = 0; i < S.length / 2; ++i) {
            wS[i] = (short) (S[i * 2 + 1] << 8 & '\uff00' | S[i * 2] & 255);
        }

    }

    private void cpyShortToBytes(short[] wS, byte[] S) {
        for (int i = 0; i < S.length / 2; ++i) {
            S[i * 2 + 1] = (byte) (wS[i] >> 8);
            S[i * 2] = (byte) wS[i];
        }

    }

    public int getByteLength() {
        return 32;
    }

    public Memoable copy() {
        return new GOST3411Digest(this);
    }

    public void reset(Memoable other) {
        GOST3411Digest t = (GOST3411Digest) other;
        this.sBox = t.sBox;
        this.cipher.init(true, new ParametersWithSBox((CipherParameters) null, this.sBox));
        this.reset();
        System.arraycopy(t.H, 0, this.H, 0, t.H.length);
        System.arraycopy(t.L, 0, this.L, 0, t.L.length);
        System.arraycopy(t.M, 0, this.M, 0, t.M.length);
        System.arraycopy(t.Sum, 0, this.Sum, 0, t.Sum.length);
        System.arraycopy(t.C[1], 0, this.C[1], 0, t.C[1].length);
        System.arraycopy(t.C[2], 0, this.C[2], 0, t.C[2].length);
        System.arraycopy(t.C[3], 0, this.C[3], 0, t.C[3].length);
        System.arraycopy(t.xBuf, 0, this.xBuf, 0, t.xBuf.length);
        this.xBufOff = t.xBufOff;
        this.byteCount = t.byteCount;
    }
}
