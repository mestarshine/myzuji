package com.myzuji.sadk.org.bouncycastle.util.encoders;

import com.myzuji.sadk.org.bouncycastle.util.Strings;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Hex {
    public Hex() {
    }

    public static String toHexString(byte[] data) {
        return toHexString(data, 0, data.length);
    }

    public static String toHexString(byte[] data, int off, int length) {
        byte[] encoded = encode(data, off, length);
        return Strings.fromByteArray(encoded);
    }

    public static byte[] encode(byte[] data) {
        return encode(data, 0, data.length);
    }

    public static byte[] encode(byte[] data, int off, int length) {
        Encoder encoder = new HexEncoder();
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();

        try {
            encoder.encode(data, off, length, bOut);
        } catch (Exception var6) {
            Exception e = var6;
            throw new EncoderException("exception encoding Hex string: " + e.getMessage(), e);
        }

        return bOut.toByteArray();
    }

    public static int encode(byte[] data, OutputStream out) throws IOException {
        Encoder encoder = new HexEncoder();
        return encoder.encode(data, 0, data.length, out);
    }

    public static int encode(byte[] data, int off, int length, OutputStream out) throws IOException {
        Encoder encoder = new HexEncoder();
        return encoder.encode(data, off, length, out);
    }

    public static byte[] decode(byte[] data) {
        Encoder encoder = new HexEncoder();
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();

        try {
            encoder.decode(data, 0, data.length, bOut);
        } catch (Exception var4) {
            Exception e = var4;
            throw new DecoderException("exception decoding Hex data: " + e.getMessage(), e);
        }

        return bOut.toByteArray();
    }

    public static byte[] decode(String data) {
        Encoder encoder = new HexEncoder();
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();

        try {
            encoder.decode(data, bOut);
        } catch (Exception var4) {
            Exception e = var4;
            throw new DecoderException("exception decoding Hex string: " + e.getMessage(), e);
        }

        return bOut.toByteArray();
    }

    public static int decode(String data, OutputStream out) throws IOException {
        Encoder encoder = new HexEncoder();
        return encoder.decode(data, out);
    }
}
