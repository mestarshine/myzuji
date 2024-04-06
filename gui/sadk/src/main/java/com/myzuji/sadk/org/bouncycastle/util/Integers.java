package com.myzuji.sadk.org.bouncycastle.util;

public class Integers {
    public Integers() {
    }

    public static int rotateLeft(int i, int distance) {
        return i << distance ^ i >>> -distance;
    }

    public static int rotateRight(int i, int distance) {
        return i >>> distance ^ i << -distance;
    }

    public static Integer valueOf(int value) {
        return value;
    }
}
