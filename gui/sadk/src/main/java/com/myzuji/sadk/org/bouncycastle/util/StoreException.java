package com.myzuji.sadk.org.bouncycastle.util;

public class StoreException extends RuntimeException {
    private static final long serialVersionUID = -2496026808079481796L;
    private Throwable _e;

    public StoreException(String s, Throwable e) {
        super(s);
        this._e = e;
    }

    public Throwable getCause() {
        return this._e;
    }
}
