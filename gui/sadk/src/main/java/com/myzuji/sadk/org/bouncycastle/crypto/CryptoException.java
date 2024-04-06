package com.myzuji.sadk.org.bouncycastle.crypto;

public class CryptoException extends Exception {
    private static final long serialVersionUID = -7505286754721845770L;
    private Throwable cause;

    public CryptoException() {
    }

    public CryptoException(String message) {
        super(message);
    }

    public CryptoException(String message, Throwable cause) {
        super(message);
        this.cause = cause;
    }

    public Throwable getCause() {
        return this.cause;
    }
}
