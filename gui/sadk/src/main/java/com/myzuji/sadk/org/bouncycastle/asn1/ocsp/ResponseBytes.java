package com.myzuji.sadk.org.bouncycastle.asn1.ocsp;

import com.myzuji.sadk.org.bouncycastle.asn1.*;

public class ResponseBytes extends ASN1Object {
    ASN1ObjectIdentifier responseType;
    ASN1OctetString response;

    public ResponseBytes(ASN1ObjectIdentifier responseType, ASN1OctetString response) {
        this.responseType = responseType;
        this.response = response;
    }

    public ResponseBytes(ASN1Sequence seq) {
        this.responseType = (ASN1ObjectIdentifier) seq.getObjectAt(0);
        this.response = (ASN1OctetString) seq.getObjectAt(1);
    }

    public static ResponseBytes getInstance(ASN1TaggedObject obj, boolean explicit) {
        return getInstance(ASN1Sequence.getInstance(obj, explicit));
    }

    public static ResponseBytes getInstance(Object obj) {
        if (obj != null && !(obj instanceof ResponseBytes)) {
            if (obj instanceof ASN1Sequence) {
                return new ResponseBytes((ASN1Sequence) obj);
            } else {
                throw new IllegalArgumentException("unknown object in factory: " + obj.getClass().getName());
            }
        } else {
            return (ResponseBytes) obj;
        }
    }

    public ASN1ObjectIdentifier getResponseType() {
        return this.responseType;
    }

    public ASN1OctetString getResponse() {
        return this.response;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(this.responseType);
        v.add(this.response);
        return new DERSequence(v);
    }
}
