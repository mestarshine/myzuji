package com.myzuji.sadk.lib.crypto.jni;

public class JNISM2 {
    public JNISM2() {
    }

    private static native boolean JNIgenerateKeypair(byte[] var0, byte[] var1, byte[] var2) throws Exception;

    private static native boolean JNIsign(byte[] var0, byte[] var1, byte[] var2, byte[] var3) throws Exception;

    private static native boolean JNIverify(byte[] var0, byte[] var1, byte[] var2, byte[] var3, byte[] var4) throws Exception;

    private static native boolean JNIcalculateZValue(byte[] var0, byte[] var1, byte[] var2, byte[] var3) throws Exception;

    private static native boolean JNIencrypt(byte[] var0, byte[] var1, byte[] var2, byte[] var3) throws Exception;

    private static native boolean JNIdecrypt(byte[] var0, byte[] var1, byte[] var2) throws Exception;

    public static boolean generateKeypair(byte[] privateKey, byte[] pubx, byte[] puby) throws Exception {
        return JNIgenerateKeypair(privateKey, pubx, puby);
    }

    public static boolean sign(byte[] digest, byte[] privateKey, byte[] r, byte[] s) throws Exception {
        return JNIsign(digest, privateKey, r, s);
    }

    public static boolean verify(byte[] r, byte[] s, byte[] pubx, byte[] puby, byte[] digest) throws Exception {
        return JNIverify(r, s, pubx, puby, digest);
    }

    public static boolean calculateZValue(byte[] pubx, byte[] puby, byte[] userID, byte[] Z) throws Exception {
        return JNIcalculateZValue(pubx, puby, userID, Z);
    }

    public static boolean encrypt(byte[] plaint, byte[] pubx, byte[] puby, byte[] encryptedData) throws Exception {
        return JNIencrypt(plaint, pubx, puby, encryptedData);
    }

    public static boolean decrypt(byte[] encryptedData, byte[] privateKey, byte[] plaint) throws Exception {
        return JNIdecrypt(encryptedData, privateKey, plaint);
    }
}
