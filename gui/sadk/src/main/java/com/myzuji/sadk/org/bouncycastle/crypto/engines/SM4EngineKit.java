package com.myzuji.sadk.org.bouncycastle.crypto.engines;

import com.myzuji.sadk.org.bouncycastle.crypto.BlockCipher;
import com.myzuji.sadk.org.bouncycastle.crypto.CipherParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.DataLengthException;
import com.myzuji.sadk.org.bouncycastle.crypto.params.KeyParameter;
import com.myzuji.sadk.org.bouncycastle.util.Pack;
import com.myzuji.sadk.org.bouncycastle.util.encoders.Hex;

import java.util.Arrays;

public class SM4EngineKit implements BlockCipher {
    private static final int BLOCK_SIZE = 16;
    private static final byte[][] SboxTable = new byte[][]{{-42, -112, -23, -2, -52, -31, 61, -73, 22, -74, 20, -62, 40, -5, 44, 5}, {43, 103, -102, 118, 42, -66, 4, -61, -86, 68, 19, 38, 73, -122, 6, -103}, {-100, 66, 80, -12, -111, -17, -104, 122, 51, 84, 11, 67, -19, -49, -84, 98}, {-28, -77, 28, -87, -55, 8, -24, -107, -128, -33, -108, -6, 117, -113, 63, -90}, {71, 7, -89, -4, -13, 115, 23, -70, -125, 89, 60, 25, -26, -123, 79, -88}, {104, 107, -127, -78, 113, 100, -38, -117, -8, -21, 15, 75, 112, 86, -99, 53}, {30, 36, 14, 94, 99, 88, -47, -94, 37, 34, 124, 59, 1, 33, 120, -121}, {-44, 0, 70, 87, -97, -45, 39, 82, 76, 54, 2, -25, -96, -60, -56, -98}, {-22, -65, -118, -46, 64, -57, 56, -75, -93, -9, -14, -50, -7, 97, 21, -95}, {-32, -82, 93, -92, -101, 52, 26, 85, -83, -109, 50, 48, -11, -116, -79, -29}, {29, -10, -30, 46, -126, 102, -54, 96, -64, 41, 35, -85, 13, 83, 78, 111}, {-43, -37, 55, 69, -34, -3, -114, 47, 3, -1, 106, 114, 109, 108, 91, 81}, {-115, 27, -81, -110, -69, -35, -68, 127, 17, -39, 92, 65, 31, 16, 90, -40}, {10, -63, 49, -120, -91, -51, 123, -67, 45, 116, -48, 18, -72, -27, -76, -80}, {-119, 105, -105, 74, 12, -106, 119, 126, 101, -71, -15, 9, -59, 110, -58, -124}, {24, -16, 125, -20, 58, -36, 77, 32, 121, -18, 95, 62, -41, -53, 57, 72}};
    private static final int[] FK = new int[]{-1548633402, 1453994832, 1736282519, -1301273892};
    private static final int[] CK = new int[]{462357, 472066609, 943670861, 1415275113, 1886879365, -1936483679, -1464879427, -993275175, -521670923, -66909679, 404694573, 876298825, 1347903077, 1819507329, -2003855715, -1532251463, -1060647211, -589042959, -117504499, 337322537, 808926789, 1280531041, 1752135293, -2071227751, -1599623499, -1128019247, -656414995, -184876535, 269950501, 741554753, 1213159005, 1684763257};
    private int[] workingKey = null;

    public SM4EngineKit() {
    }

    public final void init(boolean forEncryption, CipherParameters params) {
        if (params instanceof KeyParameter) {
            this.workingKey = this.generateWorkingKey(forEncryption, ((KeyParameter) params).getKey());
        } else {
            throw new IllegalArgumentException("invalid parameter passed to SM4 init - " + params.getClass().getName());
        }
    }

    public final int processBlock(byte[] in, int inOff, byte[] out, int outOff) throws DataLengthException, IllegalStateException {
        if (this.workingKey == null) {
            throw new IllegalStateException("SM4 engine not initialised");
        } else if (inOff + 16 > in.length) {
            throw new DataLengthException("input buffer too short");
        } else if (outOff + 16 > out.length) {
            throw new DataLengthException("output buffer too short");
        } else {
            int[] X = new int[36];

            int i;
            for (i = 0; i < 4; ++i) {
                X[i] = Pack.bigEndianToInt(in, i * 4 + inOff);
            }

            for (i = 0; i < 32; ++i) {
                X[i + 4] = X[i] ^ T(Pack.intToBigEndian(X[i + 1] ^ X[i + 2] ^ X[i + 3] ^ this.workingKey[i]));
            }

            for (i = 0; i < 4; ++i) {
                Pack.intToBigEndian(X[35 - i], out, i * 4 + outOff);
            }

            return 16;
        }
    }

    public final String getAlgorithmName() {
        return "SM4";
    }

    public final int getBlockSize() {
        return 16;
    }

    public final void reset() {
    }

    private static int bitCycleLeft(int n, int bitLen) {
        bitLen %= 32;
        return n << bitLen | n >>> 32 - bitLen;
    }

    private static int L(int B) {
        return B ^ bitCycleLeft(B, 2) ^ bitCycleLeft(B, 10) ^ bitCycleLeft(B, 18) ^ bitCycleLeft(B, 24);
    }

    private static int L2(int B) {
        return B ^ bitCycleLeft(B, 13) ^ bitCycleLeft(B, 23);
    }

    private static int t(byte[] a) {
        byte[] b = new byte[4];

        for (int i = 0; i < 4; ++i) {
            b[i] = SboxTable[(a[i] & 240) >>> 4][a[i] & 15];
        }

        return Pack.bigEndianToInt(b, 0);
    }

    private static int T(byte[] a) {
        return L(t(a));
    }

    private static int T2(byte[] a) {
        return L2(t(a));
    }

    private final int[] generateWorkingKey(boolean encrypting, byte[] key) throws IllegalArgumentException {
        if (key != null && key.length == 16) {
            int[] newKey = new int[32];
            int[] K = new int[36];
            int[] MK = new int[4];

            for (int i = 0; i < 4; ++i) {
                MK[i] = Pack.bigEndianToInt(key, i * 4);
                K[i] = MK[i] ^ FK[i];
            }

            for (int i = 0; i < 32; ++i) {
                K[i + 4] = K[i] ^ T2(Pack.intToBigEndian(K[i + 1] ^ K[i + 2] ^ K[i + 3] ^ CK[i]));
                newKey[i] = K[i + 4];
            }

            if (encrypting) {
                return newKey;
            } else {
                int[] reverse = new int[32];

                for (int i = 0; i < 32; ++i) {
                    reverse[i] = newKey[31 - i];
                }

                return reverse;
            }
        } else {
            throw new IllegalArgumentException("key is null or length is not 16");
        }
    }

    public static void forTest01() {
        byte[] dat = Hex.decode("0123456789abcdeffedcba9876543210");
        byte[] key = Hex.decode("0123456789abcdeffedcba9876543210");
        byte[] enc = Hex.decode("681edf34d206965e86b3e94f536e4246");
        CipherParameters params = new KeyParameter(key);
        SM4EngineKit engine = new SM4EngineKit();
        engine.init(true, params);
        byte[] out = new byte[16];
        engine.processBlock(dat, 0, out, 0);
        boolean condition = Arrays.equals(enc, out);
        System.err.println(condition);
    }

    public static void forTest02() {
        byte[] dat = Hex.decode("0123456789abcdeffedcba9876543210");
        byte[] key = Hex.decode("0123456789abcdeffedcba9876543210");
        byte[] enc = Hex.decode("595298c7c6fd271f0402f804c33d3f66");
        CipherParameters params = new KeyParameter(key);
        SM4EngineKit engine = new SM4EngineKit();
        engine.init(true, params);
        long endTime = System.currentTimeMillis();
        long strTime = System.currentTimeMillis();

        for (int i = 0; i < 1000000; ++i) {
            engine.processBlock(dat, 0, dat, 0);
            System.arraycopy(dat, 0, dat, 0, 16);
        }

        endTime = System.currentTimeMillis();
        boolean condition = Arrays.equals(enc, dat);
        System.err.println(condition);
        System.err.println("Time: " + (endTime - strTime));
    }

    public static void main(String[] args) {
        forTest01();
        forTest02();
    }
}
