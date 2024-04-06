package com.myzuji.sadk.org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class ASN1Object implements ASN1Encodable {
    public ASN1Object() {
    }

    public byte[] getEncoded() throws IOException {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        ASN1OutputStream aOut = new ASN1OutputStream(bOut);
        aOut.writeObject(this);
        return bOut.toByteArray();
    }

    public byte[] getEncoded(String encoding) throws IOException {
        ByteArrayOutputStream bOut;
        if (encoding.equals("DER")) {
            bOut = new ByteArrayOutputStream();
            DEROutputStream dOut = new DEROutputStream(bOut);
            dOut.writeObject(this);
            return bOut.toByteArray();
        } else if (encoding.equals("DL")) {
            bOut = new ByteArrayOutputStream();
            DLOutputStream dOut = new DLOutputStream(bOut);
            dOut.writeObject(this);
            return bOut.toByteArray();
        } else {
            return this.getEncoded();
        }
    }

    public int hashCode() {
        return this.toASN1Primitive().hashCode();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof ASN1Encodable)) {
            return false;
        } else {
            ASN1Encodable other = (ASN1Encodable) o;
            return this.toASN1Primitive().equals(other.toASN1Primitive());
        }
    }


    public ASN1Primitive toASN1Object() {
        return this.toASN1Primitive();
    }

    protected static boolean hasEncodedTagValue(Object obj, int tagValue) {
        return obj instanceof byte[] && ((byte[]) ((byte[]) obj))[0] == tagValue;
    }

    public abstract ASN1Primitive toASN1Primitive();
}
