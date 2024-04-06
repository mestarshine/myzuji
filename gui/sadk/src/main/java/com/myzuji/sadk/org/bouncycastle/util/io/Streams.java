package com.myzuji.sadk.org.bouncycastle.util.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class Streams {
    private static int BUFFER_SIZE = 512;

    public Streams() {
    }

    public static void drain(InputStream inStr) throws IOException {
        byte[] bs = new byte[BUFFER_SIZE];

        while (inStr.read(bs, 0, bs.length) >= 0) {
        }

    }

    public static byte[] readAll(InputStream inStr) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        pipeAll(inStr, buf);
        return buf.toByteArray();
    }

    public static byte[] readAllLimited(InputStream inStr, int limit) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        pipeAllLimited(inStr, (long) limit, buf);
        return buf.toByteArray();
    }

    public static int readFully(InputStream inStr, byte[] buf) throws IOException {
        return readFully(inStr, buf, 0, buf.length);
    }

    public static int readFully(InputStream inStr, byte[] buf, int off, int len) throws IOException {
        int totalRead;
        int numRead;
        for (totalRead = 0; totalRead < len; totalRead += numRead) {
            numRead = inStr.read(buf, off + totalRead, len - totalRead);
            if (numRead < 0) {
                break;
            }
        }

        return totalRead;
    }

    public static void pipeAll(InputStream inStr, OutputStream outStr) throws IOException {
        byte[] bs = new byte[BUFFER_SIZE];

        int numRead;
        while ((numRead = inStr.read(bs, 0, bs.length)) >= 0) {
            outStr.write(bs, 0, numRead);
        }

    }

    public static long pipeAllLimited(InputStream inStr, long limit, OutputStream outStr) throws IOException {
        long total = 0L;
        byte[] bs = new byte[BUFFER_SIZE];

        int numRead;
        while ((numRead = inStr.read(bs, 0, bs.length)) >= 0) {
            total += (long) numRead;
            if (total > limit) {
                throw new StreamOverflowException("Data Overflow");
            }

            outStr.write(bs, 0, numRead);
        }

        return total;
    }
}
