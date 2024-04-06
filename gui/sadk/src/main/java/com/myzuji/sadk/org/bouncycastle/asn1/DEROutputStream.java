package com.myzuji.sadk.org.bouncycastle.asn1;

import java.io.IOException;
import java.io.OutputStream;

public class DEROutputStream extends ASN1OutputStream {
    public DEROutputStream(OutputStream os) {
        super(os);
    }

    public void writeObject(ASN1Encodable obj) throws IOException {
        if (obj != null) {
            obj.toASN1Primitive().toDERObject().encode(this);
        } else {
            throw new IOException("null object detected");
        }
    }

    public ASN1OutputStream getDERSubStream() {
        return this;
    }

    public ASN1OutputStream getDLSubStream() {
        return this;
    }

}
