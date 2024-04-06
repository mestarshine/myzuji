package com.myzuji.sadk.org.bouncycastle.util;

public interface Memoable {
    Memoable copy();

    void reset(Memoable var1);
}
