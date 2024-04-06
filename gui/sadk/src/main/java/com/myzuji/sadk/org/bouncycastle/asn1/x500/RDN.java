package com.myzuji.sadk.org.bouncycastle.asn1.x500;

import com.myzuji.sadk.org.bouncycastle.asn1.*;

public class RDN extends ASN1Object {
    private ASN1Set values;

    private RDN(ASN1Set values) {
        this.values = values;
    }

    public static RDN getInstance(Object obj) {
        if (obj instanceof RDN) {
            return (RDN) obj;
        } else {
            return obj != null ? new RDN(ASN1Set.getInstance(obj)) : null;
        }
    }

    public RDN(ASN1ObjectIdentifier oid, ASN1Encodable value) {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(oid);
        v.add(value);
        this.values = new DERSet(new DERSequence(v));
    }

    public RDN(AttributeTypeAndValue attrTAndV) {
        this.values = new DERSet(attrTAndV);
    }

    public RDN(AttributeTypeAndValue[] aAndVs) {
        this.values = new DERSet(aAndVs);
    }

    public boolean isMultiValued() {
        return this.values.size() > 1;
    }

    public int size() {
        return this.values.size();
    }

    public AttributeTypeAndValue getFirst() {
        return this.values.size() == 0 ? null : AttributeTypeAndValue.getInstance(this.values.getObjectAt(0));
    }

    public AttributeTypeAndValue[] getTypesAndValues() {
        AttributeTypeAndValue[] tmp = new AttributeTypeAndValue[this.values.size()];

        for (int i = 0; i != tmp.length; ++i) {
            tmp[i] = AttributeTypeAndValue.getInstance(this.values.getObjectAt(i));
        }

        return tmp;
    }

    public ASN1Primitive toASN1Primitive() {
        return this.values;
    }
}
