package com.myzuji.sadk.org.bouncycastle.util.encoders;

public class DecoderException extends IllegalStateException {

    private static final long serialVersionUID = -5982985029465512027L;

    private Throwable cause;

    DecoderException(String msg, Throwable cause) {
        super(msg);
        this.cause = cause;
    }

    public Throwable getCause() {
        return this.cause;
    }
}
