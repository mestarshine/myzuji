package com.myzuji.sadk.org.bouncycastle.asn1;

import java.io.IOException;
import java.io.OutputStream;

public class BEROutputStream extends DEROutputStream {
    public BEROutputStream(OutputStream os) {
        super(os);
    }

    public void writeObject(Object obj) throws IOException {
        if (obj == null) {
            this.writeNull();
        } else if (obj instanceof ASN1Primitive) {
            ((ASN1Primitive) obj).encode(this);
        } else {
            if (!(obj instanceof ASN1Encodable)) {
                throw new IOException("object not BEREncodable");
            }

            ((ASN1Encodable) obj).toASN1Primitive().encode(this);
        }

    }
}
