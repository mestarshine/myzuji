package com.myzuji.sadk.org.bouncycastle.util;

public interface Selector extends Cloneable {
    boolean match(Object var1);

    Object clone();
}
