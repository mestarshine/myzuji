package com.myzuji.sadk.org.bouncycastle.asn1.pkcs;

import com.myzuji.sadk.org.bouncycastle.asn1.*;

public class CertBag extends ASN1Object {
    private ASN1ObjectIdentifier certId;
    private ASN1Encodable certValue;

    private CertBag(ASN1Sequence seq) {
        this.certId = (ASN1ObjectIdentifier) seq.getObjectAt(0);
        this.certValue = ((DERTaggedObject) seq.getObjectAt(1)).getObject();
    }

    public static CertBag getInstance(Object o) {
        if (o instanceof CertBag) {
            return (CertBag) o;
        } else {
            return o != null ? new CertBag(ASN1Sequence.getInstance(o)) : null;
        }
    }

    public CertBag(ASN1ObjectIdentifier certId, ASN1Encodable certValue) {
        this.certId = certId;
        this.certValue = certValue;
    }

    public ASN1ObjectIdentifier getCertId() {
        return this.certId;
    }

    public ASN1Encodable getCertValue() {
        return this.certValue;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(this.certId);
        v.add(new DERTaggedObject(0, this.certValue));
        return new DERSequence(v);
    }
}
