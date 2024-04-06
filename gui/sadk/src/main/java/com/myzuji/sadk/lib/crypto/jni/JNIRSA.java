package com.myzuji.sadk.lib.crypto.jni;

public class JNIRSA {
    public JNIRSA() {
    }

    private static native boolean JNIdowithPrivateKey(byte[] var0, byte[] var1, byte[] var2) throws Exception;

    private static native boolean JNIdowithPublicKey(byte[] var0, byte[] var1, byte[] var2) throws Exception;

    public static boolean dowithPrivateKey(byte[] input, byte[] privateKey, byte[] output) throws Exception {
        return JNIdowithPrivateKey(input, privateKey, output);
    }

    public static boolean dowithPublicKey(byte[] input, byte[] publicKey, byte[] output) throws Exception {
        return JNIdowithPublicKey(input, publicKey, output);
    }
}
