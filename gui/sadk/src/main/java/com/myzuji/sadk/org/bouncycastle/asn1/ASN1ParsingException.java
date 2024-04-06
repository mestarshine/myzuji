package com.myzuji.sadk.org.bouncycastle.asn1;

public class ASN1ParsingException extends IllegalStateException {
    private static final long serialVersionUID = 1500481698032393366L;

    private Throwable cause;

    public ASN1ParsingException(String message) {
        super(message);
    }

    public ASN1ParsingException(String message, Throwable cause) {
        super(message);
        this.cause = cause;
    }

    public Throwable getCause() {
        return this.cause;
    }
}
