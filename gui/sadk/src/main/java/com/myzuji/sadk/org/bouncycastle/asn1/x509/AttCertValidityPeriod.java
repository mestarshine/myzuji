package com.myzuji.sadk.org.bouncycastle.asn1.x509;

import com.myzuji.sadk.org.bouncycastle.asn1.*;

public class AttCertValidityPeriod extends ASN1Object {
    ASN1GeneralizedTime notBeforeTime;
    ASN1GeneralizedTime notAfterTime;

    public static AttCertValidityPeriod getInstance(Object obj) {
        if (obj instanceof AttCertValidityPeriod) {
            return (AttCertValidityPeriod) obj;
        } else {
            return obj != null ? new AttCertValidityPeriod(ASN1Sequence.getInstance(obj)) : null;
        }
    }

    private AttCertValidityPeriod(ASN1Sequence seq) {
        if (seq.size() != 2) {
            throw new IllegalArgumentException("Bad sequence size: " + seq.size());
        } else {
            this.notBeforeTime = ASN1GeneralizedTime.getInstance(seq.getObjectAt(0));
            this.notAfterTime = ASN1GeneralizedTime.getInstance(seq.getObjectAt(1));
        }
    }

    public AttCertValidityPeriod(ASN1GeneralizedTime notBeforeTime, ASN1GeneralizedTime notAfterTime) {
        this.notBeforeTime = notBeforeTime;
        this.notAfterTime = notAfterTime;
    }

    public ASN1GeneralizedTime getNotBeforeTime() {
        return this.notBeforeTime;
    }

    public ASN1GeneralizedTime getNotAfterTime() {
        return this.notAfterTime;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(this.notBeforeTime);
        v.add(this.notAfterTime);
        return new DERSequence(v);
    }
}
