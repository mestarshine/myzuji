package com.myzuji.sadk.lib.cryptokit.jni;

public class JNISymAlg {
    private long lkeyContext = 0L;
    public static int NID_rc4 = 5;
    public static int NID_des_ede3_ecb = 33;
    public static int NID_des_ede3_cbc = 44;
    public static int NID_ChinaSM4_CBC = 923;

    public JNISymAlg() {
    }

    private native long JNIencryptInit(int var1, byte[] var2, byte[] var3) throws Exception;

    private native int JNIencryptProcess(long var1, byte[] var3, int var4, int var5, byte[] var6, int var7) throws Exception;

    private native int JNIencryptFinal(long var1, byte[] var3, int var4) throws Exception;

    private native long JNIdecryptInit(int var1, byte[] var2, byte[] var3) throws Exception;

    private native int JNIdecryptProcess(long var1, byte[] var3, int var4, int var5, byte[] var6, int var7) throws Exception;

    private native int JNIdecryptFinal(long var1, byte[] var3, int var4) throws Exception;

    public void encryptInit(int alg, byte[] key, byte[] iv) throws Exception {
        this.lkeyContext = this.JNIencryptInit(alg, key, iv);
        if (0L == this.lkeyContext) {
            throw new Exception("Init algorithm failed!");
        }
    }

    public int encryptProcess(byte[] plaint, int inoff, int plaintLen, byte[] cipher, int outoff) throws Exception {
        if (0L == this.lkeyContext) {
            throw new Exception("context is uninitialized!");
        } else {
            return this.JNIencryptProcess(this.lkeyContext, plaint, inoff, plaintLen, cipher, outoff);
        }
    }

    public int JNIencryptFinal(byte[] cipher, int outoff) throws Exception {
        int outlen = 1;
        if (0L == this.lkeyContext) {
            throw new Exception("context is uninitialized!");
        } else {
            outlen = this.JNIencryptFinal(this.lkeyContext, cipher, outoff);
            this.lkeyContext = 0L;
            return outlen;
        }
    }

    public void decryptInit(int alg, byte[] key, byte[] iv) throws Exception {
        this.lkeyContext = this.JNIdecryptInit(alg, key, iv);
        if (0L == this.lkeyContext) {
            throw new Exception("Init algorithm failed!");
        }
    }

    public int decryptProcess(byte[] cipher, int inoff, int cipherLen, byte[] plaint, int outoff) throws Exception {
        if (0L == this.lkeyContext) {
            throw new Exception("context is uninitialized!");
        } else {
            return this.JNIdecryptProcess(this.lkeyContext, cipher, inoff, cipherLen, plaint, outoff);
        }
    }

    public int JNIdecryptFinal(byte[] plaint, int outoff) throws Exception {
        int outlen = 1;
        if (0L == this.lkeyContext) {
            throw new Exception("context is uninitialized!");
        } else {
            outlen = this.JNIdecryptFinal(this.lkeyContext, plaint, outoff);
            this.lkeyContext = 0L;
            return outlen;
        }
    }
}

