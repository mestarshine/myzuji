package com.myzuji.sadk.org.bouncycastle.asn1.x509;

import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.asn1.x500.X500Name;

import java.math.BigInteger;
import java.util.Enumeration;

public class TBSCertList extends ASN1Object {
    ASN1Integer version;
    AlgorithmIdentifier signature;
    X500Name issuer;
    Time thisUpdate;
    Time nextUpdate;
    ASN1Sequence revokedCertificates;
    Extensions crlExtensions;

    public static TBSCertList getInstance(ASN1TaggedObject obj, boolean explicit) {
        return getInstance(ASN1Sequence.getInstance(obj, explicit));
    }

    public static TBSCertList getInstance(Object obj) {
        if (obj instanceof TBSCertList) {
            return (TBSCertList) obj;
        } else {
            return obj != null ? new TBSCertList(ASN1Sequence.getInstance(obj)) : null;
        }
    }

    public TBSCertList(ASN1Sequence seq) {
        if (seq.size() >= 3 && seq.size() <= 7) {
            int seqPos = 0;
            if (seq.getObjectAt(seqPos) instanceof ASN1Integer) {
                this.version = ASN1Integer.getInstance(seq.getObjectAt(seqPos++));
            } else {
                this.version = null;
            }

            this.signature = AlgorithmIdentifier.getInstance(seq.getObjectAt(seqPos++));
            this.issuer = X500Name.getInstance(seq.getObjectAt(seqPos++));
            this.thisUpdate = Time.getInstance(seq.getObjectAt(seqPos++));
            if (seqPos < seq.size() && (seq.getObjectAt(seqPos) instanceof ASN1UTCTime || seq.getObjectAt(seqPos) instanceof ASN1GeneralizedTime || seq.getObjectAt(seqPos) instanceof Time)) {
                this.nextUpdate = Time.getInstance(seq.getObjectAt(seqPos++));
            }

            if (seqPos < seq.size() && !(seq.getObjectAt(seqPos) instanceof DERTaggedObject)) {
                this.revokedCertificates = ASN1Sequence.getInstance(seq.getObjectAt(seqPos++));
            }

            if (seqPos < seq.size() && seq.getObjectAt(seqPos) instanceof DERTaggedObject) {
                this.crlExtensions = Extensions.getInstance(ASN1Sequence.getInstance((ASN1TaggedObject) seq.getObjectAt(seqPos), true));
            }

        } else {
            throw new IllegalArgumentException("Bad sequence size: " + seq.size());
        }
    }

    public int getVersionNumber() {
        return this.version == null ? 1 : this.version.getValue().intValue() + 1;
    }

    public ASN1Integer getVersion() {
        return this.version;
    }

    public AlgorithmIdentifier getSignature() {
        return this.signature;
    }

    public X500Name getIssuer() {
        return this.issuer;
    }

    public Time getThisUpdate() {
        return this.thisUpdate;
    }

    public Time getNextUpdate() {
        return this.nextUpdate;
    }

    public CRLEntry[] getRevokedCertificates() {
        if (this.revokedCertificates == null) {
            return new CRLEntry[0];
        } else {
            CRLEntry[] entries = new CRLEntry[this.revokedCertificates.size()];

            for (int i = 0; i < entries.length; ++i) {
                entries[i] = CRLEntry.getInstance(this.revokedCertificates.getObjectAt(i));
            }

            return entries;
        }
    }

    final CRLEntry getCRLEntry(BigInteger certsn) {
        CRLEntry value = null;
        if (certsn != null && this.revokedCertificates != null) {
            CRLEntry crlEntry = null;
            int i = 0;

            for (int size = this.revokedCertificates.size(); i < size; ++i) {
                crlEntry = CRLEntry.getInstance(this.revokedCertificates.getObjectAt(i));
                if (certsn.equals(crlEntry.getUserCertificate().getValue())) {
                    value = crlEntry;
                    break;
                }
            }
        }

        return value;
    }

    public Enumeration getRevokedCertificateEnumeration() {
        return (Enumeration) (this.revokedCertificates == null ? new EmptyEnumeration() : new RevokedCertificatesEnumeration(this.revokedCertificates.getObjects()));
    }

    public Extensions getExtensions() {
        return this.crlExtensions;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        if (this.version != null) {
            v.add(this.version);
        }

        v.add(this.signature);
        v.add(this.issuer);
        v.add(this.thisUpdate);
        if (this.nextUpdate != null) {
            v.add(this.nextUpdate);
        }

        if (this.revokedCertificates != null) {
            v.add(this.revokedCertificates);
        }

        if (this.crlExtensions != null) {
            v.add(new DERTaggedObject(0, this.crlExtensions));
        }

        return new DERSequence(v);
    }

    private class EmptyEnumeration implements Enumeration {
        private EmptyEnumeration() {
        }

        public boolean hasMoreElements() {
            return false;
        }

        public Object nextElement() {
            return null;
        }
    }

    private class RevokedCertificatesEnumeration implements Enumeration {
        private final Enumeration en;

        RevokedCertificatesEnumeration(Enumeration en) {
            this.en = en;
        }

        public boolean hasMoreElements() {
            return this.en.hasMoreElements();
        }

        public Object nextElement() {
            return CRLEntry.getInstance(this.en.nextElement());
        }
    }

    public static class CRLEntry extends ASN1Object {
        ASN1Sequence seq;
        Extensions crlEntryExtensions;

        private CRLEntry(ASN1Sequence seq) {
            if (seq.size() >= 2 && seq.size() <= 3) {
                this.seq = seq;
            } else {
                throw new IllegalArgumentException("Bad sequence size: " + seq.size());
            }
        }

        public static CRLEntry getInstance(Object o) {
            if (o instanceof CRLEntry) {
                return (CRLEntry) o;
            } else {
                return o != null ? new CRLEntry(ASN1Sequence.getInstance(o)) : null;
            }
        }

        public ASN1Integer getUserCertificate() {
            return ASN1Integer.getInstance(this.seq.getObjectAt(0));
        }

        public Time getRevocationDate() {
            return Time.getInstance(this.seq.getObjectAt(1));
        }

        public Extensions getExtensions() {
            if (this.crlEntryExtensions == null && this.seq.size() == 3) {
                this.crlEntryExtensions = Extensions.getInstance(this.seq.getObjectAt(2));
            }

            return this.crlEntryExtensions;
        }

        public ASN1Primitive toASN1Primitive() {
            return this.seq;
        }

        public boolean hasExtensions() {
            return this.seq.size() == 3;
        }
    }
}
