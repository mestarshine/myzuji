package com.myzuji.sadk.org.bouncycastle.cert;

import java.io.IOException;

public class CertIOException extends IOException {
    private static final long serialVersionUID = -6669064443851676259L;
    private Throwable cause;

    public CertIOException(String msg, Throwable cause) {
        super(msg);
        this.cause = cause;
    }

    public CertIOException(String msg) {
        super(msg);
    }

    public Throwable getCause() {
        return this.cause;
    }
}
