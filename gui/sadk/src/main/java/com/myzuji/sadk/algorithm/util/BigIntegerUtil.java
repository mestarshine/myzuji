package com.myzuji.sadk.algorithm.util;

import java.math.BigInteger;

public class BigIntegerUtil {
    public BigIntegerUtil() {
    }

    public static BigInteger toPositiveInteger(byte[] in) {
        if (in == null) {
            return null;
        } else {
            byte[] bt = (byte[]) null;
            if (in[0] < 0) {
                bt = new byte[in.length + 1];
                bt[0] = 0;
                System.arraycopy(in, 0, bt, 1, bt.length - 1);
            } else {
                bt = in;
            }

            return new BigInteger(bt);
        }
    }

    public static byte[] asUnsigned32ByteArray(BigInteger n) {
        return asUnsignedNByteArray(n, 32);
    }

    public static byte[] asUnsignedNByteArray(BigInteger x, int length) {
        if (x == null) {
            return null;
        } else {
            byte[] tmp = new byte[length];
            byte[] arrays = x.toByteArray();
            int len = arrays.length;
            if (len > length + 1) {
                return null;
            } else if (len == length + 1) {
                if (arrays[0] != 0) {
                    return null;
                } else {
                    System.arraycopy(arrays, 1, tmp, 0, length);
                    return tmp;
                }
            } else {
                System.arraycopy(arrays, 0, tmp, length - len, len);
                return tmp;
            }
        }
    }
}
