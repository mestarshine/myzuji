package com.myzuji.sadk.lib.crypto.jni;

import com.myzuji.sadk.lib.cryptokit.jni.JNIHash;

public class JNIDigest {
    private JNIHash jniHash = null;
    private int hashID = 0;
    public static final int NID_md5 = 4;
    public static final int NID_sha1 = 64;
    public static final int NID_sha256 = 672;
    public static final int NID_sha384 = 673;
    public static final int NID_sha512 = 674;
    public static final int NID_ChinaSM3 = 922;

    public JNIDigest() {
        this.jniHash = new JNIHash();
    }

    public int getDigestSize() {
        switch (this.hashID) {
            case 4:
                return 16;
            case 64:
                return 20;
            case 672:
                return 32;
            case 674:
                return 64;
            case 922:
                return 32;
            default:
                return 0;
        }
    }

    public void init(int hashID) throws Exception {
        this.jniHash.init(hashID);
        this.hashID = hashID;
    }

    public void update(byte[] data) throws Exception {
        this.jniHash.update(data);
    }

    public void doFinal(byte[] digestData) throws Exception {
        this.jniHash.doFinal(digestData);
    }
}
