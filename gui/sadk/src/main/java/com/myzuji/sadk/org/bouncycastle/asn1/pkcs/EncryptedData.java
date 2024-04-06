package com.myzuji.sadk.org.bouncycastle.asn1.pkcs;

import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class EncryptedData extends ASN1Object {
    ASN1Sequence data;
    ASN1ObjectIdentifier bagId;
    ASN1Primitive bagValue;

    public static EncryptedData getInstance(Object obj) {
        if (obj instanceof EncryptedData) {
            return (EncryptedData) obj;
        } else {
            return obj != null ? new EncryptedData(ASN1Sequence.getInstance(obj)) : null;
        }
    }

    private EncryptedData(ASN1Sequence seq) {
        int version = ((ASN1Integer) seq.getObjectAt(0)).getValue().intValue();
        if (version != 0) {
            throw new IllegalArgumentException("sequence not version 0");
        } else {
            this.data = ASN1Sequence.getInstance(seq.getObjectAt(1));
        }
    }

    public EncryptedData(ASN1ObjectIdentifier contentType, AlgorithmIdentifier encryptionAlgorithm, ASN1Encodable content) {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(contentType);
        v.add(encryptionAlgorithm.toASN1Primitive());
        v.add(new BERTaggedObject(false, 0, content));
        this.data = new BERSequence(v);
    }

    public ASN1ObjectIdentifier getContentType() {
        return ASN1ObjectIdentifier.getInstance(this.data.getObjectAt(0));
    }

    public AlgorithmIdentifier getEncryptionAlgorithm() {
        return AlgorithmIdentifier.getInstance(this.data.getObjectAt(1));
    }

    public ASN1OctetString getContent() {
        if (this.data.size() == 3) {
            ASN1TaggedObject o = ASN1TaggedObject.getInstance(this.data.getObjectAt(2));
            return ASN1OctetString.getInstance(o, false);
        } else {
            return null;
        }
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(new ASN1Integer(0L));
        v.add(this.data);
        return new BERSequence(v);
    }
}
