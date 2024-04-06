package com.myzuji.sadk.org.bouncycastle.asn1.x509;

import com.myzuji.sadk.org.bouncycastle.asn1.*;

public class AttCertIssuer extends ASN1Object implements ASN1Choice {
    ASN1Encodable obj;
    ASN1Primitive choiceObj;

    public static AttCertIssuer getInstance(Object obj) {
        if (obj != null && !(obj instanceof AttCertIssuer)) {
            if (obj instanceof V2Form) {
                return new AttCertIssuer(V2Form.getInstance(obj));
            } else if (obj instanceof GeneralNames) {
                return new AttCertIssuer((GeneralNames) obj);
            } else if (obj instanceof ASN1TaggedObject) {
                return new AttCertIssuer(V2Form.getInstance((ASN1TaggedObject) obj, false));
            } else if (obj instanceof ASN1Sequence) {
                return new AttCertIssuer(GeneralNames.getInstance(obj));
            } else {
                throw new IllegalArgumentException("unknown object in factory: " + obj.getClass().getName());
            }
        } else {
            return (AttCertIssuer) obj;
        }
    }

    public static AttCertIssuer getInstance(ASN1TaggedObject obj, boolean explicit) {
        return getInstance(obj.getObject());
    }

    public AttCertIssuer(GeneralNames names) {
        this.obj = names;
        this.choiceObj = this.obj.toASN1Primitive();
    }

    public AttCertIssuer(V2Form v2Form) {
        this.obj = v2Form;
        this.choiceObj = new DERTaggedObject(false, 0, this.obj);
    }

    public ASN1Encodable getIssuer() {
        return this.obj;
    }

    public ASN1Primitive toASN1Primitive() {
        return this.choiceObj;
    }
}
