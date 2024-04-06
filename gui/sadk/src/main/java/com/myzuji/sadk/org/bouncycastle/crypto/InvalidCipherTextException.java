package com.myzuji.sadk.org.bouncycastle.crypto;

public class InvalidCipherTextException extends CryptoException {
    private static final long serialVersionUID = 2385527389491252042L;

    public InvalidCipherTextException() {
    }

    public InvalidCipherTextException(String message) {
        super(message);
    }

    public InvalidCipherTextException(String message, Throwable cause) {
        super(message, cause);
    }
}
