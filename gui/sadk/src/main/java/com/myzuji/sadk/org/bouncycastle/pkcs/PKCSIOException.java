package com.myzuji.sadk.org.bouncycastle.pkcs;

import java.io.IOException;

public class PKCSIOException extends IOException {
    private static final long serialVersionUID = 8335743409956457774L;
    private Throwable cause;

    public PKCSIOException(String msg, Throwable cause) {
        super(msg);
        this.cause = cause;
    }

    public PKCSIOException(String msg) {
        super(msg);
    }

    public Throwable getCause() {
        return this.cause;
    }
}
