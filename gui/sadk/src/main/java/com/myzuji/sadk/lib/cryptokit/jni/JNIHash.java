package com.myzuji.sadk.lib.cryptokit.jni;

public class JNIHash {
    private long lDigestContext = 0L;
    public static int NID_md5 = 4;
    public static int NID_sha1 = 64;
    public static int NID_sha256 = 672;
    public static int NID_sha384 = 673;
    public static int NID_sha512 = 674;
    public static int NID_ChinaSM3 = 922;

    public JNIHash() {
    }

    private native long JNIinit(int var1) throws Exception;

    private native void JNIupdate(long var1, byte[] var3) throws Exception;

    private native void JNIdoFinal(long var1, byte[] var3) throws Exception;

    public void init(int hashID) throws Exception {
        this.lDigestContext = this.JNIinit(hashID);
        if (0L == this.lDigestContext) {
            throw new Exception("Init hash algorithm failed!");
        }
    }

    public void update(byte[] data) throws Exception {
        if (0L == this.lDigestContext) {
            throw new Exception("Digest context is uninitialized!");
        } else {
            this.JNIupdate(this.lDigestContext, data);
        }
    }

    public void doFinal(byte[] digestData) throws Exception {
        if (0L == this.lDigestContext) {
            throw new Exception("Digest context is uninitialized!");
        } else {
            this.JNIdoFinal(this.lDigestContext, digestData);
            this.lDigestContext = 0L;
        }
    }
}
