package com.myzuji.sadk.org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;

public class DLSequence extends ASN1Sequence {
    private int bodyLength = -1;

    public DLSequence() {
    }

    public DLSequence(ASN1Encodable obj) {
        super(obj);
    }

    public DLSequence(ASN1EncodableVector v) {
        super(v);
    }

    public DLSequence(ASN1Encodable[] array) {
        super(array);
    }

    private int getBodyLength() throws IOException {
        if (this.bodyLength < 0) {
            int length = 0;

            Object obj;
            for (Enumeration e = this.getObjects(); e.hasMoreElements(); length += ((ASN1Encodable) obj).toASN1Primitive().toDLObject().encodedLength()) {
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
        ASN1OutputStream dOut = out.getDLSubStream();
        int length = this.getBodyLength();
        out.write(48);
        out.writeLength(length);
        Enumeration e = this.getObjects();

        while (e.hasMoreElements()) {
            Object obj = e.nextElement();
            dOut.writeObject((ASN1Encodable) obj);
        }

    }
}
