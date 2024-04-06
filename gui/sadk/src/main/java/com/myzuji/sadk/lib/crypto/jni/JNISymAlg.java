package com.myzuji.sadk.lib.crypto.jni;

public class JNISymAlg {
    public static int NID_rc4 = 5;
    public static int NID_des_ecb = 29;
    public static int NID_des_cbc = 31;
    public static int NID_des_ede3_ecb = 33;
    public static int NID_des_ede3_cbc = 44;
    public static int NID_ChinaSM4_CBC = 923;
    private com.myzuji.sadk.lib.cryptokit.jni.JNISymAlg jniSymAlg = null;

    public JNISymAlg() {
        this.jniSymAlg = new com.myzuji.sadk.lib.cryptokit.jni.JNISymAlg();
    }

    public void encryptInit(int alg, byte[] key, byte[] iv) throws Exception {
        this.jniSymAlg.encryptInit(alg, key, iv);
    }

    public int encryptProcess(byte[] plaint, int inoff, int plaintLen, byte[] cipher, int outoff) throws Exception {
        return this.jniSymAlg.encryptProcess(plaint, inoff, plaintLen, cipher, outoff);
    }

    public int encryptFinal(byte[] cipher, int outoff) throws Exception {
        return this.jniSymAlg.JNIencryptFinal(cipher, outoff);
    }

    public void decryptInit(int alg, byte[] key, byte[] iv) throws Exception {
        this.jniSymAlg.decryptInit(alg, key, iv);
    }

    public int decryptProcess(byte[] cipher, int inoff, int cipherLen, byte[] plaint, int outoff) throws Exception {
        return this.jniSymAlg.decryptProcess(cipher, inoff, cipherLen, plaint, outoff);
    }

    public int decryptFinal(byte[] plaint, int outoff) throws Exception {
        return this.jniSymAlg.JNIdecryptFinal(plaint, outoff);
    }
}

