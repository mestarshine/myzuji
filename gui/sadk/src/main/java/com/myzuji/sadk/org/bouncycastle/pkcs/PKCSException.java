package com.myzuji.sadk.org.bouncycastle.pkcs;

public class PKCSException extends Exception {
    private static final long serialVersionUID = 6151126787911627547L;
    private Throwable cause;

    public PKCSException(String msg, Throwable cause) {
        super(msg);
        this.cause = cause;
    }

    public PKCSException(String msg) {
        super(msg);
    }

    public Throwable getCause() {
        return this.cause;
    }
}
