package com.myzuji.sadk.org.bouncycastle.util.encoders;

import java.io.IOException;
import java.io.OutputStream;

public class HexEncoder implements Encoder {
    private static final byte[] encodingTable = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
    private static final byte[] decodingTable = new byte[128];

    public HexEncoder() {
    }

    public int encode(byte[] data, int off, int length, OutputStream out) throws IOException {

        for (int i = off; i < off + length; ++i) {
            int value = data[i] & 255;
            out.write(encodingTable[value >>> 4]);
            out.write(encodingTable[value & 15]);
        }

        return length * 2;
    }

    private static boolean ignore(char c) {
        return c == '\n' || c == '\r' || c == '\t' || c == ' ';
    }

    public int decode(byte[] data, int off, int length, OutputStream out) throws IOException {
        int outLen = 0;

        int end;
        for (end = off + length; end > off && ignore((char) data[end - 1]); --end) {
        }

        for (int i = off; i < end; ++outLen) {
            while (i < end && ignore((char) data[i])) {
                ++i;
            }

            byte b1;
            for (b1 = decodingTable[data[i++]]; i < end && ignore((char) data[i]); ++i) {
            }

            byte b2 = decodingTable[data[i++]];
            if ((b1 | b2) < 0) {
                throw new IOException("invalid characters encountered in Hex data");
            }

            out.write(b1 << 4 | b2);
        }

        return outLen;
    }

    public int decode(String data, OutputStream out) throws IOException {
        int length = 0;

        int end;
        for (end = data.length(); end > 0 && ignore(data.charAt(end - 1)); --end) {
        }

        for (int i = 0; i < end; ++length) {
            while (i < end && ignore(data.charAt(i))) {
                ++i;
            }

            byte b1;
            for (b1 = decodingTable[data.charAt(i++)]; i < end && ignore(data.charAt(i)); ++i) {
            }

            byte b2 = decodingTable[data.charAt(i++)];
            if ((b1 | b2) < 0) {
                throw new IOException("invalid characters encountered in Hex string");
            }

            out.write(b1 << 4 | b2);
        }

        return length;
    }

    static {
        int i;
        for (i = 0; i < decodingTable.length; ++i) {
            decodingTable[i] = -1;
        }

        for (i = 0; i < encodingTable.length; ++i) {
            decodingTable[encodingTable[i]] = (byte) i;
        }

        decodingTable[65] = decodingTable[97];
        decodingTable[66] = decodingTable[98];
        decodingTable[67] = decodingTable[99];
        decodingTable[68] = decodingTable[100];
        decodingTable[69] = decodingTable[101];
        decodingTable[70] = decodingTable[102];
    }
}
