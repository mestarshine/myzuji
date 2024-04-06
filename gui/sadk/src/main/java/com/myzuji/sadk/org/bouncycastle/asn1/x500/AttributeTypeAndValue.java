package com.myzuji.sadk.org.bouncycastle.asn1.x500;

import com.myzuji.sadk.org.bouncycastle.asn1.*;

public class AttributeTypeAndValue extends ASN1Object {
    private ASN1ObjectIdentifier type;
    private ASN1Encodable value;

    private AttributeTypeAndValue(ASN1Sequence seq) {
        this.type = (ASN1ObjectIdentifier) seq.getObjectAt(0);
        this.value = seq.getObjectAt(1);
    }

    public static AttributeTypeAndValue getInstance(Object o) {
        if (o instanceof AttributeTypeAndValue) {
            return (AttributeTypeAndValue) o;
        } else if (o != null) {
            return new AttributeTypeAndValue(ASN1Sequence.getInstance(o));
        } else {
            throw new IllegalArgumentException("null value in getInstance()");
        }
    }

    public AttributeTypeAndValue(ASN1ObjectIdentifier type, ASN1Encodable value) {
        this.type = type;
        this.value = value;
    }

    public ASN1ObjectIdentifier getType() {
        return this.type;
    }

    public ASN1Encodable getValue() {
        return this.value;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(this.type);
        v.add(this.value);
        return new DERSequence(v);
    }
}
