package com.myzuji.sadk.org.bouncycastle.cms;

import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.IssuerAndSerialNumber;

public class SignerIdentifier extends ASN1Object implements ASN1Choice {
    private ASN1Encodable id;

    public SignerIdentifier(IssuerAndSerialNumber id) {
        this.id = id;
    }

    public SignerIdentifier(ASN1OctetString id) {
        this.id = new DERTaggedObject(false, 0, id);
    }

    public SignerIdentifier(ASN1Primitive id) {
        this.id = id;
    }

    public static SignerIdentifier getInstance(Object o) {
        if (o != null && !(o instanceof SignerIdentifier)) {
            if (o instanceof IssuerAndSerialNumber) {
                return new SignerIdentifier((IssuerAndSerialNumber) o);
            } else if (o instanceof ASN1OctetString) {
                return new SignerIdentifier((ASN1OctetString) o);
            } else if (o instanceof ASN1Primitive) {
                return new SignerIdentifier((ASN1Primitive) o);
            } else {
                throw new IllegalArgumentException("Illegal object in SignerIdentifier: " + o.getClass().getName());
            }
        } else {
            return (SignerIdentifier) o;
        }
    }

    public boolean isTagged() {
        return this.id instanceof ASN1TaggedObject;
    }

    public ASN1Encodable getId() {
        return (ASN1Encodable) (this.id instanceof ASN1TaggedObject ? ASN1OctetString.getInstance((ASN1TaggedObject) this.id, false) : this.id);
    }

    public ASN1Primitive toASN1Primitive() {
        return this.id.toASN1Primitive();
    }
}
