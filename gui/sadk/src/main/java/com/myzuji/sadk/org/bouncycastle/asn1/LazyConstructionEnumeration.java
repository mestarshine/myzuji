package com.myzuji.sadk.org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;

public class LazyConstructionEnumeration implements Enumeration {
    private ASN1InputStream aIn;
    private Object nextObj;

    public LazyConstructionEnumeration(byte[] encoded) {
        this.aIn = new ASN1InputStream(encoded, true);
        this.nextObj = this.readObject();
    }

    public boolean hasMoreElements() {
        return this.nextObj != null;
    }

    public Object nextElement() {
        Object o = this.nextObj;
        this.nextObj = this.readObject();
        return o;
    }

    private Object readObject() {
        try {
            return this.aIn.readObject();
        } catch (IOException var2) {
            IOException e = var2;
            throw new ASN1ParsingException("malformed DER construction: " + e, e);
        }
    }
}
