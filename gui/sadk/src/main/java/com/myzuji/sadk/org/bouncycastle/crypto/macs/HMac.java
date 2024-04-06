package com.myzuji.sadk.org.bouncycastle.crypto.macs;

import com.myzuji.sadk.org.bouncycastle.crypto.CipherParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.Digest;
import com.myzuji.sadk.org.bouncycastle.crypto.Mac;
import com.myzuji.sadk.org.bouncycastle.crypto.digests.ExtendedDigest;
import com.myzuji.sadk.org.bouncycastle.crypto.params.KeyParameter;
import com.myzuji.sadk.org.bouncycastle.util.Integers;
import com.myzuji.sadk.org.bouncycastle.util.Memoable;

import java.util.Hashtable;

public class HMac implements Mac {
    private static final byte IPAD = 54;
    private static final byte OPAD = 92;
    private Digest digest;
    private int digestSize;
    private int blockLength;
    private Memoable ipadState;
    private Memoable opadState;
    private byte[] inputPad;
    private byte[] outputBuf;
    private static Hashtable blockLengths = new Hashtable();

    private static int getByteLength(Digest digest) {
        if (digest instanceof ExtendedDigest) {
            return ((ExtendedDigest) digest).getByteLength();
        } else {
            Integer b = (Integer) blockLengths.get(digest.getAlgorithmName());
            if (b == null) {
                throw new IllegalArgumentException("unknown digest passed: " + digest.getAlgorithmName());
            } else {
                return b;
            }
        }
    }

    public HMac(Digest digest) {
        this(digest, getByteLength(digest));
    }

    private HMac(Digest digest, int byteLength) {
        this.digest = digest;
        this.digestSize = digest.getDigestSize();
        this.blockLength = byteLength;
        this.inputPad = new byte[this.blockLength];
        this.outputBuf = new byte[this.blockLength + this.digestSize];
    }

    public String getAlgorithmName() {
        return this.digest.getAlgorithmName() + "/HMAC";
    }

    public Digest getUnderlyingDigest() {
        return this.digest;
    }

    public void init(CipherParameters params) {
        this.digest.reset();
        byte[] key = ((KeyParameter) params).getKey();
        int keyLength = key.length;
        if (keyLength > this.blockLength) {
            this.digest.update(key, 0, keyLength);
            this.digest.doFinal(this.inputPad, 0);
            keyLength = this.digestSize;
        } else {
            System.arraycopy(key, 0, this.inputPad, 0, keyLength);
        }

        for (int i = keyLength; i < this.inputPad.length; ++i) {
            this.inputPad[i] = 0;
        }

        System.arraycopy(this.inputPad, 0, this.outputBuf, 0, this.blockLength);
        xorPad(this.inputPad, this.blockLength, (byte) 54);
        xorPad(this.outputBuf, this.blockLength, (byte) 92);
        if (this.digest instanceof Memoable) {
            this.opadState = ((Memoable) this.digest).copy();
            ((Digest) this.opadState).update(this.outputBuf, 0, this.blockLength);
        }

        this.digest.update(this.inputPad, 0, this.inputPad.length);
        if (this.digest instanceof Memoable) {
            this.ipadState = ((Memoable) this.digest).copy();
        }

    }

    public int getMacSize() {
        return this.digestSize;
    }

    public void update(byte in) {
        this.digest.update(in);
    }

    public void update(byte[] in, int inOff, int len) {
        this.digest.update(in, inOff, len);
    }

    public int doFinal(byte[] out, int outOff) {
        this.digest.doFinal(this.outputBuf, this.blockLength);
        if (this.opadState != null) {
            ((Memoable) this.digest).reset(this.opadState);
            this.digest.update(this.outputBuf, this.blockLength, this.digest.getDigestSize());
        } else {
            this.digest.update(this.outputBuf, 0, this.outputBuf.length);
        }

        int len = this.digest.doFinal(out, outOff);

        for (int i = this.blockLength; i < this.outputBuf.length; ++i) {
            this.outputBuf[i] = 0;
        }

        if (this.ipadState != null) {
            ((Memoable) this.digest).reset(this.ipadState);
        } else {
            this.digest.update(this.inputPad, 0, this.inputPad.length);
        }

        return len;
    }

    public void reset() {
        this.digest.reset();
        this.digest.update(this.inputPad, 0, this.inputPad.length);
    }

    private static void xorPad(byte[] pad, int len, byte n) {
        for (int i = 0; i < len; ++i) {
            pad[i] ^= n;
        }

    }

    static {
        blockLengths.put("GOST3411", Integers.valueOf(32));
        blockLengths.put("MD2", Integers.valueOf(16));
        blockLengths.put("MD4", Integers.valueOf(64));
        blockLengths.put("MD5", Integers.valueOf(64));
        blockLengths.put("RIPEMD128", Integers.valueOf(64));
        blockLengths.put("RIPEMD160", Integers.valueOf(64));
        blockLengths.put("SHA-1", Integers.valueOf(64));
        blockLengths.put("SHA-224", Integers.valueOf(64));
        blockLengths.put("SHA-256", Integers.valueOf(64));
        blockLengths.put("SHA-384", Integers.valueOf(128));
        blockLengths.put("SHA-512", Integers.valueOf(128));
        blockLengths.put("Tiger", Integers.valueOf(64));
        blockLengths.put("Whirlpool", Integers.valueOf(64));
    }
}
