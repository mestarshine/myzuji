package com.myzuji.sadk.org.bouncycastle.util.encoders;

import com.myzuji.sadk.org.bouncycastle.util.Strings;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Base64Kit {
    private static final Base64Encoder encoder = new Base64Encoder();

    public Base64Kit() {
    }

    public static String toBase64String(byte[] data) {
        return toBase64String(data, 0, data.length);
    }

    public static String toBase64String(byte[] data, int off, int length) {
        byte[] encoded = encode(data, off, length);
        return Strings.fromByteArray(encoded);
    }

    public static byte[] encode(byte[] data) {
        return encode(data, 0, data.length);
    }

    public static byte[] encode(byte[] data, int off, int length) {
        int len = (length + 2) / 3 * 4;
        ByteArrayOutputStream bOut = new ByteArrayOutputStream(len);

        try {
            encoder.encode(data, off, length, bOut);
        } catch (Exception var6) {
            Exception e = var6;
            throw new EncoderException("exception encoding base64 string: " + e.getMessage(), e);
        }

        return bOut.toByteArray();
    }

    public static int encode(byte[] data, OutputStream out) throws IOException {
        return encoder.encode(data, 0, data.length, out);
    }

    public static int encode(byte[] data, int off, int length, OutputStream out) throws IOException {
        return encoder.encode(data, off, length, out);
    }

    public static byte[] decode(byte[] data) {
        int len = data.length / 4 * 3;
        ByteArrayOutputStream bOut = new ByteArrayOutputStream(len);

        try {
            encoder.decode(data, 0, data.length, bOut);
        } catch (Exception var4) {
            Exception e = var4;
            throw new DecoderException("unable to decode base64 data: " + e.getMessage(), e);
        }

        return bOut.toByteArray();
    }

    public static byte[] decode(String data) {
        int len = data.length() / 4 * 3;
        ByteArrayOutputStream bOut = new ByteArrayOutputStream(len);

        try {
            encoder.decode(data, bOut);
        } catch (Exception var4) {
            Exception e = var4;
            throw new DecoderException("unable to decode base64 string: " + e.getMessage(), e);
        }

        return bOut.toByteArray();
    }

    public static int decode(String data, OutputStream out) throws IOException {
        return encoder.decode(data, out);
    }

    public static boolean isBase64Compatability(byte[] data) {
        return encoder.isBase64Compatability(data);
    }
}
