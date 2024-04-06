package com.myzuji.sadk.org.bouncycastle.crypto;

public class RuntimeCryptoException extends RuntimeException {

    private static final long serialVersionUID = -726557008910063577L;

    public RuntimeCryptoException() {
    }

    public RuntimeCryptoException(String message) {
        super(message);
    }
}
