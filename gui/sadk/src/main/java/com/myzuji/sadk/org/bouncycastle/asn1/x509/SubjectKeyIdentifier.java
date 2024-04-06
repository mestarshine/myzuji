package com.myzuji.sadk.org.bouncycastle.asn1.x509;

import com.myzuji.sadk.org.bouncycastle.asn1.*;

public class SubjectKeyIdentifier extends ASN1Object {
    private byte[] keyidentifier;

    public static SubjectKeyIdentifier getInstance(ASN1TaggedObject obj, boolean explicit) {
        return getInstance(ASN1OctetString.getInstance(obj, explicit));
    }

    public static SubjectKeyIdentifier getInstance(Object obj) {
        if (obj instanceof SubjectKeyIdentifier) {
            return (SubjectKeyIdentifier) obj;
        } else {
            return obj != null ? new SubjectKeyIdentifier(ASN1OctetString.getInstance(obj)) : null;
        }
    }

    public static SubjectKeyIdentifier fromExtensions(Extensions extensions) {
        return getInstance(extensions.getExtensionParsedValue(Extension.subjectKeyIdentifier));
    }

    public SubjectKeyIdentifier(byte[] keyid) {
        this.keyidentifier = keyid;
    }

    protected SubjectKeyIdentifier(ASN1OctetString keyid) {
        this.keyidentifier = keyid.getOctets();
    }

    public byte[] getKeyIdentifier() {
        return this.keyidentifier;
    }

    public ASN1Primitive toASN1Primitive() {
        return new DEROctetString(this.keyidentifier);
    }
}
