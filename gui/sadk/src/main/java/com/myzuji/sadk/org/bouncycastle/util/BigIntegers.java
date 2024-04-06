package com.myzuji.sadk.org.bouncycastle.util;

import java.math.BigInteger;
import java.security.SecureRandom;

public final class BigIntegers {
    private static final int MAX_ITERATIONS = 1000;
    private static final BigInteger ZERO = BigInteger.valueOf(0L);

    public BigIntegers() {
    }

    public static byte[] asUnsignedByteArray(BigInteger value) {
        byte[] bytes = value.toByteArray();
        if (bytes[0] == 0) {
            byte[] tmp = new byte[bytes.length - 1];
            System.arraycopy(bytes, 1, tmp, 0, tmp.length);
            return tmp;
        } else {
            return bytes;
        }
    }

    public static byte[] asUnsignedByteArray(int length, BigInteger value) {
        byte[] bytes = value.toByteArray();
        if (bytes.length == length) {
            return bytes;
        } else {
            int start = bytes[0] == 0 ? 1 : 0;
            int count = bytes.length - start;
            if (count > length) {
                throw new IllegalArgumentException("standard length exceeded for value");
            } else {
                byte[] tmp = new byte[length];
                System.arraycopy(bytes, start, tmp, tmp.length - count, count);
                return tmp;
            }
        }
    }

    public static BigInteger createRandomInRange(BigInteger min, BigInteger max, SecureRandom random) {
        int cmp = min.compareTo(max);
        if (cmp >= 0) {
            if (cmp > 0) {
                throw new IllegalArgumentException("'min' may not be greater than 'max'");
            } else {
                return min;
            }
        } else if (min.bitLength() > max.bitLength() / 2) {
            return createRandomInRange(ZERO, max.subtract(min), random).add(min);
        } else {
            for (int i = 0; i < 1000; ++i) {
                BigInteger x = new BigInteger(max.bitLength(), random);
                if (x.compareTo(min) >= 0 && x.compareTo(max) <= 0) {
                    return x;
                }
            }

            return (new BigInteger(max.subtract(min).bitLength() - 1, random)).add(min);
        }
    }

    public static final BigInteger fromUnsignedByteArray(byte[] buf) {
        if (buf == null) {
            throw new IllegalArgumentException("null not allowed for buf");
        } else {
            return new BigInteger(1, buf);
        }
    }

    public static final BigInteger fromUnsignedByteArray(byte[] buf, int off, int length) {
        if (buf != null && buf.length >= off) {
            if (buf.length < off + length) {
                length = buf.length - off;
            }

            byte[] value = new byte[length];
            System.arraycopy(buf, off, value, 0, value.length);
            return new BigInteger(1, value);
        } else {
            throw new IllegalArgumentException("null/length not allowed for buf");
        }
    }
}
