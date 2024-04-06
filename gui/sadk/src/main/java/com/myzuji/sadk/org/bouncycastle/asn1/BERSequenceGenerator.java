package com.myzuji.sadk.org.bouncycastle.asn1;

import java.io.IOException;
import java.io.OutputStream;

public class BERSequenceGenerator extends BERGenerator {
    public BERSequenceGenerator(OutputStream out) throws IOException {
        super(out);
        this.writeBERHeader(48);
    }

    public BERSequenceGenerator(OutputStream out, int tagNo, boolean isExplicit) throws IOException {
        super(out, tagNo, isExplicit);
        this.writeBERHeader(48);
    }

    public void addObject(ASN1Encodable object) throws IOException {
        object.toASN1Primitive().encode(new BEROutputStream(this._out));
    }

    public void close() throws IOException {
        this.writeBEREnd();
    }
}
