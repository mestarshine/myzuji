package com.myzuji.sadk.org.bouncycastle.asn1.pkcs;

import com.myzuji.sadk.org.bouncycastle.asn1.*;

public class Attribute extends ASN1Object {
    private ASN1ObjectIdentifier attrType;
    private ASN1Set attrValues;

    public static Attribute getInstance(Object o) {
        if (o != null && !(o instanceof Attribute)) {
            if (o instanceof ASN1Sequence) {
                return new Attribute((ASN1Sequence) o);
            } else {
                throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
            }
        } else {
            return (Attribute) o;
        }
    }

    public Attribute(ASN1Sequence seq) {
        this.attrType = (ASN1ObjectIdentifier) seq.getObjectAt(0);
        this.attrValues = (ASN1Set) seq.getObjectAt(1);
    }

    public Attribute(ASN1ObjectIdentifier attrType, ASN1Set attrValues) {
        this.attrType = attrType;
        this.attrValues = attrValues;
    }

    public ASN1ObjectIdentifier getAttrType() {
        return this.attrType;
    }

    public ASN1Set getAttrValues() {
        return this.attrValues;
    }

    public ASN1Encodable[] getAttributeValues() {
        return this.attrValues.toArray();
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(this.attrType);
        v.add(this.attrValues);
        return new DERSequence(v);
    }
}
