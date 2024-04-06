package com.myzuji.sadk.org.bouncycastle.util.encoders;

public class EncoderException extends IllegalStateException {

    private static final long serialVersionUID = 6386901431526579027L;

    private Throwable cause;

    EncoderException(String msg, Throwable cause) {
        super(msg);
        this.cause = cause;
    }

    public Throwable getCause() {
        return this.cause;
    }
}
