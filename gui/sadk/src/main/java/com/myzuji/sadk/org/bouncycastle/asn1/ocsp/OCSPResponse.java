package com.myzuji.sadk.org.bouncycastle.asn1.ocsp;

import com.myzuji.sadk.org.bouncycastle.asn1.*;

public class OCSPResponse extends ASN1Object {
    OCSPResponseStatus responseStatus;
    ResponseBytes responseBytes;

    public OCSPResponse(OCSPResponseStatus responseStatus, ResponseBytes responseBytes) {
        this.responseStatus = responseStatus;
        this.responseBytes = responseBytes;
    }

    private OCSPResponse(ASN1Sequence seq) {
        this.responseStatus = OCSPResponseStatus.getInstance(seq.getObjectAt(0));
        if (seq.size() == 2) {
            this.responseBytes = ResponseBytes.getInstance((ASN1TaggedObject) seq.getObjectAt(1), true);
        }

    }

    public static OCSPResponse getInstance(ASN1TaggedObject obj, boolean explicit) {
        return getInstance(ASN1Sequence.getInstance(obj, explicit));
    }

    public static OCSPResponse getInstance(Object obj) {
        if (obj instanceof OCSPResponse) {
            return (OCSPResponse) obj;
        } else {
            return obj != null ? new OCSPResponse(ASN1Sequence.getInstance(obj)) : null;
        }
    }

    public OCSPResponseStatus getResponseStatus() {
        return this.responseStatus;
    }

    public ResponseBytes getResponseBytes() {
        return this.responseBytes;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(this.responseStatus);
        if (this.responseBytes != null) {
            v.add(new DERTaggedObject(true, 0, this.responseBytes));
        }

        return new DERSequence(v);
    }
}
