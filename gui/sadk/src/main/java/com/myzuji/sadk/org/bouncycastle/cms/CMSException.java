package com.myzuji.sadk.org.bouncycastle.cms;

public class CMSException extends Exception {
    private static final long serialVersionUID = -8915944513114560355L;

    Exception e;

    public CMSException(String msg) {
        super(msg);
    }

    public CMSException(String msg, Exception e) {
        super(msg);
        this.e = e;
    }

    public Exception getUnderlyingException() {
        return this.e;
    }

    public Throwable getCause() {
        return this.e;
    }
}
