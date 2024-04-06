package com.myzuji.sadk.org.bouncycastle.crypto.digests;

import com.myzuji.sadk.org.bouncycastle.util.Memoable;
import com.myzuji.sadk.org.bouncycastle.util.Pack;

public abstract class GeneralDigest implements ExtendedDigest, Memoable {
    private static final int BYTE_LENGTH = 64;
    private final byte[] xBuf = new byte[4];
    private int xBufOff;
    private long byteCount;

    protected GeneralDigest() {
        this.xBufOff = 0;
    }

    protected GeneralDigest(GeneralDigest t) {
        this.copyIn(t);
    }

    protected GeneralDigest(byte[] encodedState) {
        System.arraycopy(encodedState, 0, this.xBuf, 0, this.xBuf.length);
        this.xBufOff = Pack.bigEndianToInt(encodedState, 4);
        this.byteCount = Pack.bigEndianToLong(encodedState, 8);
    }

    protected void copyIn(GeneralDigest t) {
        System.arraycopy(t.xBuf, 0, this.xBuf, 0, t.xBuf.length);
        this.xBufOff = t.xBufOff;
        this.byteCount = t.byteCount;
    }

    public void update(byte in) {
        this.xBuf[this.xBufOff++] = in;
        if (this.xBufOff == this.xBuf.length) {
            this.processWord(this.xBuf, 0);
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
            this.processWord(in, inOff);
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

    public void finish() {
        long bitLength = this.byteCount << 3;
        this.update((byte) -128);

        while (this.xBufOff != 0) {
            this.update((byte) 0);
        }

        this.processLength(bitLength);
        this.processBlock();
    }

    public void reset() {
        this.byteCount = 0L;
        this.xBufOff = 0;

        for (int i = 0; i < this.xBuf.length; ++i) {
            this.xBuf[i] = 0;
        }

    }

    protected void populateState(byte[] state) {
        System.arraycopy(this.xBuf, 0, state, 0, this.xBufOff);
        Pack.intToBigEndian(this.xBufOff, state, 4);
        Pack.longToBigEndian(this.byteCount, state, 8);
    }

    public int getByteLength() {
        return 64;
    }

    protected abstract void processWord(byte[] var1, int var2);

    protected abstract void processLength(long var1);

    protected abstract void processBlock();
}
