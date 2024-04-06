package com.myzuji.sadk.algorithm.common;

import java.security.SecureRandom;

public class CBCParam {
    private byte[] iv = new byte[8];


    public CBCParam() {
        this.iv = new byte[8];
        SecureRandom sRandom = new SecureRandom();
        sRandom.nextBytes(this.iv);
    }

    public CBCParam(byte[] iv) {
        this.iv = iv;
    }

    public byte[] getIv() {
        return this.iv;
    }

    public void setIv(byte[] iv) {
        this.iv = iv;
    }
}
