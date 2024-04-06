package com.myzuji.sadk.org.bouncycastle.asn1.pkcs;

import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.asn1.x500.X500Name;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.X509Name;

public class CertificationRequestInfo extends ASN1Object {
    ASN1Integer version = new ASN1Integer(0L);
    X500Name subject;
    SubjectPublicKeyInfo subjectPKInfo;
    ASN1Set attributes = null;

    public static CertificationRequestInfo getInstance(Object obj) {
        if (obj instanceof CertificationRequestInfo) {
            return (CertificationRequestInfo) obj;
        } else {
            return obj != null ? new CertificationRequestInfo(ASN1Sequence.getInstance(obj)) : null;
        }
    }

    public CertificationRequestInfo(X500Name subject, SubjectPublicKeyInfo pkInfo, ASN1Set attributes) {
        this.subject = subject;
        this.subjectPKInfo = pkInfo;
        this.attributes = attributes;
        if (subject == null || this.version == null || this.subjectPKInfo == null) {
            throw new IllegalArgumentException("Not all mandatory fields set in CertificationRequestInfo generator.");
        }
    }


    public CertificationRequestInfo(X509Name subject, SubjectPublicKeyInfo pkInfo, ASN1Set attributes) {
        if (subject == null) {
            throw new IllegalArgumentException("Not all mandatory fields set in CertificationRequestInfo generator.");
        } else {
            this.subject = X500Name.getInstance(subject.toASN1Primitive());
            this.subjectPKInfo = pkInfo;
            this.attributes = attributes;
            if (subject == null || this.version == null || this.subjectPKInfo == null) {
                throw new IllegalArgumentException("Not all mandatory fields set in CertificationRequestInfo generator.");
            }
        }
    }


    public CertificationRequestInfo(ASN1Sequence seq) {
        this.version = (ASN1Integer) seq.getObjectAt(0);
        this.subject = X500Name.getInstance(seq.getObjectAt(1));
        this.subjectPKInfo = SubjectPublicKeyInfo.getInstance(seq.getObjectAt(2));
        if (seq.size() > 3) {
            DERTaggedObject tagobj = (DERTaggedObject) seq.getObjectAt(3);
            this.attributes = ASN1Set.getInstance(tagobj, false);
        }

        if (this.subject == null || this.version == null || this.subjectPKInfo == null) {
            throw new IllegalArgumentException("Not all mandatory fields set in CertificationRequestInfo generator.");
        }
    }

    public ASN1Integer getVersion() {
        return this.version;
    }

    public X500Name getSubject() {
        return this.subject;
    }

    public SubjectPublicKeyInfo getSubjectPublicKeyInfo() {
        return this.subjectPKInfo;
    }

    public ASN1Set getAttributes() {
        return this.attributes;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(this.version);
        v.add(this.subject);
        v.add(this.subjectPKInfo);
        if (this.attributes != null) {
            v.add(new DERTaggedObject(false, 0, this.attributes));
        }

        return new DERSequence(v);
    }
}
