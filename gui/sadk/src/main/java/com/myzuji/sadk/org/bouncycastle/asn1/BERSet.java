package com.myzuji.sadk.org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;

public class BERSet extends ASN1Set {
    public BERSet() {
    }

    public BERSet(ASN1Encodable obj) {
        super(obj);
    }

    public BERSet(ASN1EncodableVector v) {
        super(v, false);
    }

    public BERSet(ASN1Encodable[] a) {
        super(a, false);
    }

    public int encodedLength() throws IOException {
        int length = 0;

        for (Enumeration e = this.getObjects(); e.hasMoreElements(); length += ((ASN1Encodable) e.nextElement()).toASN1Primitive().encodedLength()) {
        }

        return 2 + length + 2;
    }

    public void encode(ASN1OutputStream out) throws IOException {
        out.write(49);
        out.write(128);
        Enumeration e = this.getObjects();

        while (e.hasMoreElements()) {
            out.writeObject((ASN1Encodable) e.nextElement());
        }

        out.write(0);
        out.write(0);
    }
}
