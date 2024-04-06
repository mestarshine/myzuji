package com.myzuji.sadk.org.bouncycastle.asn1;

import java.io.IOException;

public abstract class ASN1Primitive extends ASN1Object {
    protected ASN1Primitive() {
    }

    public static ASN1Primitive fromByteArray(byte[] data) throws IOException {
        ASN1InputStream aIn = new ASN1InputStream(data);

        ASN1Primitive var2;
        try {
            var2 = aIn.readObject();
        } catch (ClassCastException var6) {
            throw new IOException("cannot recognise object in stream");
        } finally {
            if (aIn != null) {
                aIn.close();
            }

        }

        return var2;
    }

    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            return o instanceof ASN1Encodable && this.asn1Equals(((ASN1Encodable) o).toASN1Primitive());
        }
    }

    public ASN1Primitive toASN1Primitive() {
        return this;
    }

    public ASN1Primitive toDERObject() {
        return this;
    }

    public ASN1Primitive toDLObject() {
        return this;
    }

    public abstract int hashCode();

    public abstract boolean isConstructed();

    public abstract int encodedLength() throws IOException;

    public abstract void encode(ASN1OutputStream var1) throws IOException;

    public abstract boolean asn1Equals(ASN1Primitive var1);
}
