package com.myzuji.sadk.org.bouncycastle.cert;

public class CertException extends Exception {
    private static final long serialVersionUID = -8612825107953335462L;
    private Throwable cause;

    public CertException(String msg, Throwable cause) {
        super(msg);
        this.cause = cause;
    }

    public CertException(String msg) {
        super(msg);
    }

    public Throwable getCause() {
        return this.cause;
    }
}
