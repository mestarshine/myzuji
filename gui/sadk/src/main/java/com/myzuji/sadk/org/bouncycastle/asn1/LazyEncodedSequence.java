package com.myzuji.sadk.org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;

public class LazyEncodedSequence extends ASN1Sequence {
    private byte[] encoded;

    LazyEncodedSequence(byte[] encoded) throws IOException {
        this.encoded = encoded;
    }

    private void parse() {
        Enumeration en = new LazyConstructionEnumeration(this.encoded);

        while (en.hasMoreElements()) {
            this.seq.addElement(en.nextElement());
        }

        this.encoded = null;
    }

    public synchronized ASN1Encodable getObjectAt(int index) {
        if (this.encoded != null) {
            this.parse();
        }

        return super.getObjectAt(index);
    }

    public synchronized Enumeration getObjects() {
        return (Enumeration) (this.encoded == null ? super.getObjects() : new LazyConstructionEnumeration(this.encoded));
    }

    public synchronized int size() {
        if (this.encoded != null) {
            this.parse();
        }

        return super.size();
    }

    public ASN1Primitive toDERObject() {
        if (this.encoded != null) {
            this.parse();
        }

        return super.toDERObject();
    }

    public ASN1Primitive toDLObject() {
        if (this.encoded != null) {
            this.parse();
        }

        return super.toDLObject();
    }

    public int encodedLength() throws IOException {
        return this.encoded != null ? 1 + StreamUtil.calculateBodyLength(this.encoded.length) + this.encoded.length : super.toDLObject().encodedLength();
    }

    public void encode(ASN1OutputStream out) throws IOException {
        if (this.encoded != null) {
            out.writeEncoded(48, this.encoded);
        } else {
            super.toDLObject().encode(out);
        }

    }
}
