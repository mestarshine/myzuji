package com.myzuji.sadk.org.bouncycastle.asn1.x509;

import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.asn1.x500.X500Name;

import java.math.BigInteger;
import java.util.Enumeration;

public class CertificateList extends ASN1Object {
    TBSCertList tbsCertList;
    AlgorithmIdentifier sigAlgId;
    DERBitString sig;
    boolean isHashCodeSet = false;
    int hashCodeValue;

    public static CertificateList getInstance(ASN1TaggedObject obj, boolean explicit) {
        return getInstance(ASN1Sequence.getInstance(obj, explicit));
    }

    public static CertificateList getInstance(Object obj) {
        if (obj instanceof CertificateList) {
            return (CertificateList) obj;
        } else {
            return obj != null ? new CertificateList(ASN1Sequence.getInstance(obj)) : null;
        }
    }

    /**
     *
     */
    public CertificateList(ASN1Sequence seq) {
        if (seq.size() == 3) {
            this.tbsCertList = TBSCertList.getInstance(seq.getObjectAt(0));
            this.sigAlgId = AlgorithmIdentifier.getInstance(seq.getObjectAt(1));
            this.sig = DERBitString.getInstance(seq.getObjectAt(2));
        } else {
            throw new IllegalArgumentException("sequence wrong size for CertificateList");
        }
    }

    public TBSCertList getTBSCertList() {
        return this.tbsCertList;
    }

    public TBSCertList.CRLEntry[] getRevokedCertificates() {
        return this.tbsCertList.getRevokedCertificates();
    }

    public final TBSCertList.CRLEntry getCRLEntry(BigInteger certsn) {
        return this.tbsCertList.getCRLEntry(certsn);
    }

    public Enumeration getRevokedCertificateEnumeration() {
        return this.tbsCertList.getRevokedCertificateEnumeration();
    }

    public AlgorithmIdentifier getSignatureAlgorithm() {
        return this.sigAlgId;
    }

    public DERBitString getSignature() {
        return this.sig;
    }

    public int getVersionNumber() {
        return this.tbsCertList.getVersionNumber();
    }

    public X500Name getIssuer() {
        return this.tbsCertList.getIssuer();
    }

    public Time getThisUpdate() {
        return this.tbsCertList.getThisUpdate();
    }

    public Time getNextUpdate() {
        return this.tbsCertList.getNextUpdate();
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(this.tbsCertList);
        v.add(this.sigAlgId);
        v.add(this.sig);
        return new DERSequence(v);
    }

    public int hashCode() {
        if (!this.isHashCodeSet) {
            this.hashCodeValue = super.hashCode();
            this.isHashCodeSet = true;
        }

        return this.hashCodeValue;
    }
}

