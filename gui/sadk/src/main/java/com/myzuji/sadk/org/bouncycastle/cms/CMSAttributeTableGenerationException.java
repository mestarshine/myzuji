package com.myzuji.sadk.org.bouncycastle.cms;

public class CMSAttributeTableGenerationException extends CMSRuntimeException {
    private static final long serialVersionUID = -7569043591589876051L;
    Exception e;

    public CMSAttributeTableGenerationException(String name) {
        super(name);
    }

    public CMSAttributeTableGenerationException(String name, Exception e) {
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
