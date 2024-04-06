package com.myzuji.sadk.org.bouncycastle.asn1.x509;

import com.myzuji.sadk.org.bouncycastle.asn1.*;

public class DistributionPoint extends ASN1Object {
    DistributionPointName distributionPoint;
    ReasonFlags reasons;
    GeneralNames cRLIssuer;

    public static DistributionPoint getInstance(ASN1TaggedObject obj, boolean explicit) {
        return getInstance(ASN1Sequence.getInstance(obj, explicit));
    }

    public static DistributionPoint getInstance(Object obj) {
        if (obj != null && !(obj instanceof DistributionPoint)) {
            if (obj instanceof ASN1Sequence) {
                return new DistributionPoint((ASN1Sequence) obj);
            } else {
                throw new IllegalArgumentException("Invalid DistributionPoint: " + obj.getClass().getName());
            }
        } else {
            return (DistributionPoint) obj;
        }
    }

    public DistributionPoint(ASN1Sequence seq) {
        for (int i = 0; i != seq.size(); ++i) {
            ASN1TaggedObject t = ASN1TaggedObject.getInstance(seq.getObjectAt(i));
            switch (t.getTagNo()) {
                case 0:
                    this.distributionPoint = DistributionPointName.getInstance(t, true);
                    break;
                case 1:
                    this.reasons = new ReasonFlags(DERBitString.getInstance(t, false));
                    break;
                case 2:
                    this.cRLIssuer = GeneralNames.getInstance(t, false);
            }
        }

    }

    public DistributionPoint(DistributionPointName distributionPoint, ReasonFlags reasons, GeneralNames cRLIssuer) {
        this.distributionPoint = distributionPoint;
        this.reasons = reasons;
        this.cRLIssuer = cRLIssuer;
    }

    public DistributionPointName getDistributionPoint() {
        return this.distributionPoint;
    }

    public ReasonFlags getReasons() {
        return this.reasons;
    }

    public GeneralNames getCRLIssuer() {
        return this.cRLIssuer;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        if (this.distributionPoint != null) {
            v.add(new DERTaggedObject(0, this.distributionPoint));
        }

        if (this.reasons != null) {
            v.add(new DERTaggedObject(false, 1, this.reasons));
        }

        if (this.cRLIssuer != null) {
            v.add(new DERTaggedObject(false, 2, this.cRLIssuer));
        }

        return new DERSequence(v);
    }

    public String toString() {
        String sep = System.getProperty("line.separator");
        StringBuffer buf = new StringBuffer();
        buf.append("DistributionPoint: [");
        buf.append(sep);
        if (this.distributionPoint != null) {
            this.appendObject(buf, sep, "distributionPoint", this.distributionPoint.toString());
        }

        if (this.reasons != null) {
            this.appendObject(buf, sep, "reasons", this.reasons.toString());
        }

        if (this.cRLIssuer != null) {
            this.appendObject(buf, sep, "cRLIssuer", this.cRLIssuer.toString());
        }

        buf.append("]");
        buf.append(sep);
        return buf.toString();
    }

    private void appendObject(StringBuffer buf, String sep, String name, String value) {
        String indent = "    ";
        buf.append(indent);
        buf.append(name);
        buf.append(":");
        buf.append(sep);
        buf.append(indent);
        buf.append(indent);
        buf.append(value);
        buf.append(sep);
    }
}
