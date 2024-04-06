package com.myzuji.sadk.org.bouncycastle.asn1.x509;

import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.asn1.x500.X500Name;

public class TBSCertificate extends ASN1Object {
    ASN1Sequence seq;
    ASN1Integer version;
    ASN1Integer serialNumber;
    AlgorithmIdentifier signature;
    X500Name issuer;
    Time startDate;
    Time endDate;
    X500Name subject;
    SubjectPublicKeyInfo subjectPublicKeyInfo;
    DERBitString issuerUniqueId;
    DERBitString subjectUniqueId;
    Extensions extensions;

    public static TBSCertificate getInstance(ASN1TaggedObject obj, boolean explicit) {
        return getInstance(ASN1Sequence.getInstance(obj, explicit));
    }

    public static TBSCertificate getInstance(Object obj) {
        if (obj instanceof TBSCertificate) {
            return (TBSCertificate) obj;
        } else {
            return obj != null ? new TBSCertificate(ASN1Sequence.getInstance(obj)) : null;
        }
    }

    private TBSCertificate(ASN1Sequence seq) {
        int seqStart = 0;
        this.seq = seq;
        if (seq.getObjectAt(0) instanceof DERTaggedObject) {
            this.version = ASN1Integer.getInstance((ASN1TaggedObject) seq.getObjectAt(0), true);
        } else {
            seqStart = -1;
            this.version = new ASN1Integer(0L);
        }

        this.serialNumber = ASN1Integer.getInstance(seq.getObjectAt(seqStart + 1));
        this.signature = AlgorithmIdentifier.getInstance(seq.getObjectAt(seqStart + 2));
        this.issuer = X500Name.getInstance(seq.getObjectAt(seqStart + 3));
        ASN1Sequence dates = (ASN1Sequence) seq.getObjectAt(seqStart + 4);
        this.startDate = Time.getInstance(dates.getObjectAt(0));
        this.endDate = Time.getInstance(dates.getObjectAt(1));
        this.subject = X500Name.getInstance(seq.getObjectAt(seqStart + 5));
        this.subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(seq.getObjectAt(seqStart + 6));

        for (int extras = seq.size() - (seqStart + 6) - 1; extras > 0; --extras) {
            DERTaggedObject extra = (DERTaggedObject) seq.getObjectAt(seqStart + 6 + extras);
            switch (extra.getTagNo()) {
                case 1:
                    this.issuerUniqueId = DERBitString.getInstance(extra, false);
                    break;
                case 2:
                    this.subjectUniqueId = DERBitString.getInstance(extra, false);
                    break;
                case 3:
                    this.extensions = Extensions.getInstance(ASN1Sequence.getInstance(extra, true));
            }
        }

    }

    public int getVersionNumber() {
        return this.version.getValue().intValue() + 1;
    }

    public ASN1Integer getVersion() {
        return this.version;
    }

    public ASN1Integer getSerialNumber() {
        return this.serialNumber;
    }

    public AlgorithmIdentifier getSignature() {
        return this.signature;
    }

    public X500Name getIssuer() {
        return this.issuer;
    }

    public Time getStartDate() {
        return this.startDate;
    }

    public Time getEndDate() {
        return this.endDate;
    }

    public X500Name getSubject() {
        return this.subject;
    }

    public SubjectPublicKeyInfo getSubjectPublicKeyInfo() {
        return this.subjectPublicKeyInfo;
    }

    public DERBitString getIssuerUniqueId() {
        return this.issuerUniqueId;
    }

    public DERBitString getSubjectUniqueId() {
        return this.subjectUniqueId;
    }

    public Extensions getExtensions() {
        return this.extensions;
    }

    public ASN1Primitive toASN1Primitive() {
        return this.seq;
    }
}
