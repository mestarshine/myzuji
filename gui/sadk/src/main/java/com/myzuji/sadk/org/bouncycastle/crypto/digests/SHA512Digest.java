package com.myzuji.sadk.org.bouncycastle.crypto.digests;

import com.myzuji.sadk.org.bouncycastle.util.Memoable;
import com.myzuji.sadk.org.bouncycastle.util.Pack;

public class SHA512Digest extends LongDigest {
    private static final int DIGEST_LENGTH = 64;

    public SHA512Digest() {
    }

    public SHA512Digest(SHA512Digest t) {
        super(t);
    }

    public SHA512Digest(byte[] encodedState) {
        this.restoreState(encodedState);
    }

    public String getAlgorithmName() {
        return "SHA-512";
    }

    public int getDigestSize() {
        return 64;
    }

    public int doFinal(byte[] out, int outOff) {
        this.finish();
        Pack.longToBigEndian(this.H1, out, outOff);
        Pack.longToBigEndian(this.H2, out, outOff + 8);
        Pack.longToBigEndian(this.H3, out, outOff + 16);
        Pack.longToBigEndian(this.H4, out, outOff + 24);
        Pack.longToBigEndian(this.H5, out, outOff + 32);
        Pack.longToBigEndian(this.H6, out, outOff + 40);
        Pack.longToBigEndian(this.H7, out, outOff + 48);
        Pack.longToBigEndian(this.H8, out, outOff + 56);
        this.reset();
        return 64;
    }

    public void reset() {
        super.reset();
        this.H1 = 7640891576956012808L;
        this.H2 = -4942790177534073029L;
        this.H3 = 4354685564936845355L;
        this.H4 = -6534734903238641935L;
        this.H5 = 5840696475078001361L;
        this.H6 = -7276294671716946913L;
        this.H7 = 2270897969802886507L;
        this.H8 = 6620516959819538809L;
    }

    public Memoable copy() {
        return new SHA512Digest(this);
    }

    public void reset(Memoable other) {
        SHA512Digest d = (SHA512Digest) other;
        this.copyIn(d);
    }

    public byte[] getEncodedState() {
        byte[] encoded = new byte[this.getEncodedStateSize()];
        super.populateState(encoded);
        return encoded;
    }
}
