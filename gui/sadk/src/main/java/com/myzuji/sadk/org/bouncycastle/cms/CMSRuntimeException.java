package com.myzuji.sadk.org.bouncycastle.cms;

public class CMSRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -1170736625194773621L;
    Exception e;

    public CMSRuntimeException(String name) {
        super(name);
    }

    public CMSRuntimeException(String name, Exception e) {
        super(name);
        this.e = e;
    }

    public Exception getUnderlyingException() {
        return this.e;
    }

    public Throwable getCause() {
        return this.e;
    }
}
