package com.myzuji.sadk.org.bouncycastle.asn1.cms;

import com.myzuji.sadk.org.bouncycastle.asn1.*;

public class Attributes extends ASN1Object {
    private ASN1Set attributes;

    private Attributes(ASN1Set set) {
        this.attributes = set;
    }

    public Attributes(ASN1EncodableVector v) {
        this.attributes = new DLSet(v);
    }

    public static Attributes getInstance(Object obj) {
        if (obj instanceof Attributes) {
            return (Attributes) obj;
        } else {
            return obj != null ? new Attributes(ASN1Set.getInstance(obj)) : null;
        }
    }

    public com.myzuji.sadk.org.bouncycastle.asn1.cms.Attribute[] getAttributes() {
        com.myzuji.sadk.org.bouncycastle.asn1.cms.Attribute[] rv = new com.myzuji.sadk.org.bouncycastle.asn1.cms.Attribute[this.attributes.size()];

        for (int i = 0; i != rv.length; ++i) {
            rv[i] = Attribute.getInstance(this.attributes.getObjectAt(i));
        }

        return rv;
    }

    public ASN1Primitive toASN1Primitive() {
        return this.attributes;
    }
}
