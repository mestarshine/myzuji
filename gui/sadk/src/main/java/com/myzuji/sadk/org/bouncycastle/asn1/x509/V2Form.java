package com.myzuji.sadk.org.bouncycastle.asn1.x509;

import com.myzuji.sadk.org.bouncycastle.asn1.*;

public class V2Form extends ASN1Object {
    GeneralNames issuerName;
    IssuerSerial baseCertificateID;
    ObjectDigestInfo objectDigestInfo;

    public static V2Form getInstance(ASN1TaggedObject obj, boolean explicit) {
        return getInstance(ASN1Sequence.getInstance(obj, explicit));
    }

    public static V2Form getInstance(Object obj) {
        if (obj instanceof V2Form) {
            return (V2Form) obj;
        } else {
            return obj != null ? new V2Form(ASN1Sequence.getInstance(obj)) : null;
        }
    }

    public V2Form(GeneralNames issuerName) {
        this(issuerName, (IssuerSerial) null, (ObjectDigestInfo) null);
    }

    public V2Form(GeneralNames issuerName, IssuerSerial baseCertificateID) {
        this(issuerName, baseCertificateID, (ObjectDigestInfo) null);
    }

    public V2Form(GeneralNames issuerName, ObjectDigestInfo objectDigestInfo) {
        this(issuerName, (IssuerSerial) null, objectDigestInfo);
    }

    public V2Form(GeneralNames issuerName, IssuerSerial baseCertificateID, ObjectDigestInfo objectDigestInfo) {
        this.issuerName = issuerName;
        this.baseCertificateID = baseCertificateID;
        this.objectDigestInfo = objectDigestInfo;
    }


    public V2Form(ASN1Sequence seq) {
        if (seq.size() > 3) {
            throw new IllegalArgumentException("Bad sequence size: " + seq.size());
        } else {
            int index = 0;
            if (!(seq.getObjectAt(0) instanceof ASN1TaggedObject)) {
                ++index;
                this.issuerName = GeneralNames.getInstance(seq.getObjectAt(0));
            }

            for (int i = index; i != seq.size(); ++i) {
                ASN1TaggedObject o = ASN1TaggedObject.getInstance(seq.getObjectAt(i));
                if (o.getTagNo() == 0) {
                    this.baseCertificateID = IssuerSerial.getInstance(o, false);
                } else {
                    if (o.getTagNo() != 1) {
                        throw new IllegalArgumentException("Bad tag number: " + o.getTagNo());
                    }

                    this.objectDigestInfo = ObjectDigestInfo.getInstance(o, false);
                }
            }

        }
    }

    public GeneralNames getIssuerName() {
        return this.issuerName;
    }

    public IssuerSerial getBaseCertificateID() {
        return this.baseCertificateID;
    }

    public ObjectDigestInfo getObjectDigestInfo() {
        return this.objectDigestInfo;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        if (this.issuerName != null) {
            v.add(this.issuerName);
        }

        if (this.baseCertificateID != null) {
            v.add(new DERTaggedObject(false, 0, this.baseCertificateID));
        }

        if (this.objectDigestInfo != null) {
            v.add(new DERTaggedObject(false, 1, this.objectDigestInfo));
        }

        return new DERSequence(v);
    }
}
