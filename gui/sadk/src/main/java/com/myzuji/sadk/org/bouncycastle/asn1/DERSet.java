package com.myzuji.sadk.org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;

public class DERSet extends ASN1Set {
    private int bodyLength = -1;

    public DERSet() {
    }

    public DERSet(ASN1Encodable obj) {
        super(obj);
    }

    public DERSet(ASN1EncodableVector v) {
        super(v, true);
    }

    public DERSet(ASN1Encodable[] a) {
        super(a, true);
    }

    DERSet(ASN1EncodableVector v, boolean doSort) {
        super(v, doSort);
    }

    private int getBodyLength() throws IOException {
        if (this.bodyLength < 0) {
            int length = 0;

            Object obj;
            for (Enumeration e = this.getObjects(); e.hasMoreElements(); length += ((ASN1Encodable) obj).toASN1Primitive().toDERObject().encodedLength()) {
                obj = e.nextElement();
            }

            this.bodyLength = length;
        }

        return this.bodyLength;
    }

    public int encodedLength() throws IOException {
        int length = this.getBodyLength();
        return 1 + StreamUtil.calculateBodyLength(length) + length;
    }

    public void encode(ASN1OutputStream out) throws IOException {
        ASN1OutputStream dOut = out.getDERSubStream();
        int length = this.getBodyLength();
        out.write(49);
        out.writeLength(length);
        Enumeration e = this.getObjects();

        while (e.hasMoreElements()) {
            Object obj = e.nextElement();
            dOut.writeObject((ASN1Encodable) obj);
        }

    }
}
