package com.myzuji.sadk.org.bouncycastle.crypto.engines;

import com.myzuji.sadk.org.bouncycastle.crypto.BlockCipher;
import com.myzuji.sadk.org.bouncycastle.crypto.CipherParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.DataLengthException;
import com.myzuji.sadk.org.bouncycastle.crypto.OutputLengthException;
import com.myzuji.sadk.org.bouncycastle.crypto.params.KeyParameter;
import com.myzuji.sadk.org.bouncycastle.crypto.params.RC2Parameters;

public class RC2Engine implements BlockCipher {
    private static byte[] piTable = new byte[]{-39, 120, -7, -60, 25, -35, -75, -19, 40, -23, -3, 121, 74, -96, -40, -99, -58, 126, 55, -125, 43, 118, 83, -114, 98, 76, 100, -120, 68, -117, -5, -94, 23, -102, 89, -11, -121, -77, 79, 19, 97, 69, 109, -115, 9, -127, 125, 50, -67, -113, 64, -21, -122, -73, 123, 11, -16, -107, 33, 34, 92, 107, 78, -126, 84, -42, 101, -109, -50, 96, -78, 28, 115, 86, -64, 20, -89, -116, -15, -36, 18, 117, -54, 31, 59, -66, -28, -47, 66, 61, -44, 48, -93, 60, -74, 38, 111, -65, 14, -38, 70, 105, 7, 87, 39, -14, 29, -101, -68, -108, 67, 3, -8, 17, -57, -10, -112, -17, 62, -25, 6, -61, -43, 47, -56, 102, 30, -41, 8, -24, -22, -34, -128, 82, -18, -9, -124, -86, 114, -84, 53, 77, 106, 42, -106, 26, -46, 113, 90, 21, 73, 116, 75, -97, -48, 94, 4, 24, -92, -20, -62, -32, 65, 110, 15, 81, -53, -52, 36, -111, -81, 80, -95, -12, 112, 57, -103, 124, 58, -123, 35, -72, -76, 122, -4, 2, 54, 91, 37, 85, -105, 49, 45, 93, -6, -104, -29, -118, -110, -82, 5, -33, 41, 16, 103, 108, -70, -55, -45, 0, -26, -49, -31, -98, -88, 44, 99, 22, 1, 63, 88, -30, -119, -87, 13, 56, 52, 27, -85, 51, -1, -80, -69, 72, 12, 95, -71, -79, -51, 46, -59, -13, -37, 71, -27, -91, -100, 119, 10, -90, 32, 104, -2, 127, -63, -83};
    private static final int BLOCK_SIZE = 8;
    private int[] workingKey;
    private boolean encrypting;

    public RC2Engine() {
    }

    private int[] generateWorkingKey(byte[] key, int bits) {
        int[] xKey = new int[128];

        int len;
        for (len = 0; len != key.length; ++len) {
            xKey[len] = key[len] & 255;
        }

        len = key.length;
        int x;
        int i;
        if (len < 128) {
            i = 0;
            x = xKey[len - 1];

            do {
                x = piTable[x + xKey[i++] & 255] & 255;
                xKey[len++] = x;
            } while (len < 128);
        }

        len = bits + 7 >> 3;
        x = piTable[xKey[128 - len] & 255 >> (7 & -bits)] & 255;
        xKey[128 - len] = x;

        for (i = 128 - len - 1; i >= 0; --i) {
            x = piTable[x ^ xKey[i + len]] & 255;
            xKey[i] = x;
        }

        int[] newKey = new int[64];

        for (i = 0; i != newKey.length; ++i) {
            newKey[i] = xKey[2 * i] + (xKey[2 * i + 1] << 8);
        }

        return newKey;
    }

    public void init(boolean encrypting, CipherParameters params) {
        this.encrypting = encrypting;
        if (params instanceof RC2Parameters) {
            RC2Parameters param = (RC2Parameters) params;
            this.workingKey = this.generateWorkingKey(param.getKey(), param.getEffectiveKeyBits());
        } else {
            if (!(params instanceof KeyParameter)) {
                throw new IllegalArgumentException("invalid parameter passed to RC2 init - " + params.getClass().getName());
            }

            byte[] key = ((KeyParameter) params).getKey();
            this.workingKey = this.generateWorkingKey(key, key.length * 8);
        }

    }

    public void reset() {
    }

    public String getAlgorithmName() {
        return "RC2";
    }

    public int getBlockSize() {
        return 8;
    }

    public final int processBlock(byte[] in, int inOff, byte[] out, int outOff) {
        if (this.workingKey == null) {
            throw new IllegalStateException("RC2 engine not initialised");
        } else if (inOff + 8 > in.length) {
            throw new DataLengthException("input buffer too short");
        } else if (outOff + 8 > out.length) {
            throw new OutputLengthException("output buffer too short");
        } else {
            if (this.encrypting) {
                this.encryptBlock(in, inOff, out, outOff);
            } else {
                this.decryptBlock(in, inOff, out, outOff);
            }

            return 8;
        }
    }

    private int rotateWordLeft(int x, int y) {
        x &= 65535;
        return x << y | x >> 16 - y;
    }

    private void encryptBlock(byte[] in, int inOff, byte[] out, int outOff) {
        int x76 = ((in[inOff + 7] & 255) << 8) + (in[inOff + 6] & 255);
        int x54 = ((in[inOff + 5] & 255) << 8) + (in[inOff + 4] & 255);
        int x32 = ((in[inOff + 3] & 255) << 8) + (in[inOff + 2] & 255);
        int x10 = ((in[inOff + 1] & 255) << 8) + (in[inOff + 0] & 255);

        int i;
        for (i = 0; i <= 16; i += 4) {
            x10 = this.rotateWordLeft(x10 + (x32 & ~x76) + (x54 & x76) + this.workingKey[i], 1);
            x32 = this.rotateWordLeft(x32 + (x54 & ~x10) + (x76 & x10) + this.workingKey[i + 1], 2);
            x54 = this.rotateWordLeft(x54 + (x76 & ~x32) + (x10 & x32) + this.workingKey[i + 2], 3);
            x76 = this.rotateWordLeft(x76 + (x10 & ~x54) + (x32 & x54) + this.workingKey[i + 3], 5);
        }

        x10 += this.workingKey[x76 & 63];
        x32 += this.workingKey[x10 & 63];
        x54 += this.workingKey[x32 & 63];
        x76 += this.workingKey[x54 & 63];

        for (i = 20; i <= 40; i += 4) {
            x10 = this.rotateWordLeft(x10 + (x32 & ~x76) + (x54 & x76) + this.workingKey[i], 1);
            x32 = this.rotateWordLeft(x32 + (x54 & ~x10) + (x76 & x10) + this.workingKey[i + 1], 2);
            x54 = this.rotateWordLeft(x54 + (x76 & ~x32) + (x10 & x32) + this.workingKey[i + 2], 3);
            x76 = this.rotateWordLeft(x76 + (x10 & ~x54) + (x32 & x54) + this.workingKey[i + 3], 5);
        }

        x10 += this.workingKey[x76 & 63];
        x32 += this.workingKey[x10 & 63];
        x54 += this.workingKey[x32 & 63];
        x76 += this.workingKey[x54 & 63];

        for (i = 44; i < 64; i += 4) {
            x10 = this.rotateWordLeft(x10 + (x32 & ~x76) + (x54 & x76) + this.workingKey[i], 1);
            x32 = this.rotateWordLeft(x32 + (x54 & ~x10) + (x76 & x10) + this.workingKey[i + 1], 2);
            x54 = this.rotateWordLeft(x54 + (x76 & ~x32) + (x10 & x32) + this.workingKey[i + 2], 3);
            x76 = this.rotateWordLeft(x76 + (x10 & ~x54) + (x32 & x54) + this.workingKey[i + 3], 5);
        }

        out[outOff + 0] = (byte) x10;
        out[outOff + 1] = (byte) (x10 >> 8);
        out[outOff + 2] = (byte) x32;
        out[outOff + 3] = (byte) (x32 >> 8);
        out[outOff + 4] = (byte) x54;
        out[outOff + 5] = (byte) (x54 >> 8);
        out[outOff + 6] = (byte) x76;
        out[outOff + 7] = (byte) (x76 >> 8);
    }

    private void decryptBlock(byte[] in, int inOff, byte[] out, int outOff) {
        int x76 = ((in[inOff + 7] & 255) << 8) + (in[inOff + 6] & 255);
        int x54 = ((in[inOff + 5] & 255) << 8) + (in[inOff + 4] & 255);
        int x32 = ((in[inOff + 3] & 255) << 8) + (in[inOff + 2] & 255);
        int x10 = ((in[inOff + 1] & 255) << 8) + (in[inOff + 0] & 255);

        int i;
        for (i = 60; i >= 44; i -= 4) {
            x76 = this.rotateWordLeft(x76, 11) - ((x10 & ~x54) + (x32 & x54) + this.workingKey[i + 3]);
            x54 = this.rotateWordLeft(x54, 13) - ((x76 & ~x32) + (x10 & x32) + this.workingKey[i + 2]);
            x32 = this.rotateWordLeft(x32, 14) - ((x54 & ~x10) + (x76 & x10) + this.workingKey[i + 1]);
            x10 = this.rotateWordLeft(x10, 15) - ((x32 & ~x76) + (x54 & x76) + this.workingKey[i]);
        }

        x76 -= this.workingKey[x54 & 63];
        x54 -= this.workingKey[x32 & 63];
        x32 -= this.workingKey[x10 & 63];
        x10 -= this.workingKey[x76 & 63];

        for (i = 40; i >= 20; i -= 4) {
            x76 = this.rotateWordLeft(x76, 11) - ((x10 & ~x54) + (x32 & x54) + this.workingKey[i + 3]);
            x54 = this.rotateWordLeft(x54, 13) - ((x76 & ~x32) + (x10 & x32) + this.workingKey[i + 2]);
            x32 = this.rotateWordLeft(x32, 14) - ((x54 & ~x10) + (x76 & x10) + this.workingKey[i + 1]);
            x10 = this.rotateWordLeft(x10, 15) - ((x32 & ~x76) + (x54 & x76) + this.workingKey[i]);
        }

        x76 -= this.workingKey[x54 & 63];
        x54 -= this.workingKey[x32 & 63];
        x32 -= this.workingKey[x10 & 63];
        x10 -= this.workingKey[x76 & 63];

        for (i = 16; i >= 0; i -= 4) {
            x76 = this.rotateWordLeft(x76, 11) - ((x10 & ~x54) + (x32 & x54) + this.workingKey[i + 3]);
            x54 = this.rotateWordLeft(x54, 13) - ((x76 & ~x32) + (x10 & x32) + this.workingKey[i + 2]);
            x32 = this.rotateWordLeft(x32, 14) - ((x54 & ~x10) + (x76 & x10) + this.workingKey[i + 1]);
            x10 = this.rotateWordLeft(x10, 15) - ((x32 & ~x76) + (x54 & x76) + this.workingKey[i]);
        }

        out[outOff + 0] = (byte) x10;
        out[outOff + 1] = (byte) (x10 >> 8);
        out[outOff + 2] = (byte) x32;
        out[outOff + 3] = (byte) (x32 >> 8);
        out[outOff + 4] = (byte) x54;
        out[outOff + 5] = (byte) (x54 >> 8);
        out[outOff + 6] = (byte) x76;
        out[outOff + 7] = (byte) (x76 >> 8);
    }
}
