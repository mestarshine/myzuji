package com.myzuji.sadk.org.bouncycastle.asn1.x509;

import com.myzuji.sadk.org.bouncycastle.asn1.*;

public class CRLDistPoint extends ASN1Object {
    ASN1Sequence seq = null;

    public static CRLDistPoint getInstance(ASN1TaggedObject obj, boolean explicit) {
        return getInstance(ASN1Sequence.getInstance(obj, explicit));
    }

    public static CRLDistPoint getInstance(Object obj) {
        if (obj instanceof CRLDistPoint) {
            return (CRLDistPoint) obj;
        } else {
            return obj != null ? new CRLDistPoint(ASN1Sequence.getInstance(obj)) : null;
        }
    }

    private CRLDistPoint(ASN1Sequence seq) {
        this.seq = seq;
    }

    public CRLDistPoint(DistributionPoint[] points) {
        ASN1EncodableVector v = new ASN1EncodableVector();

        for (int i = 0; i != points.length; ++i) {
            v.add(points[i]);
        }

        this.seq = new DERSequence(v);
    }

    public DistributionPoint[] getDistributionPoints() {
        DistributionPoint[] dp = new DistributionPoint[this.seq.size()];

        for (int i = 0; i != this.seq.size(); ++i) {
            dp[i] = DistributionPoint.getInstance(this.seq.getObjectAt(i));
        }

        return dp;
    }

    public ASN1Primitive toASN1Primitive() {
        return this.seq;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        String sep = System.getProperty("line.separator");
        buf.append("CRLDistPoint:");
        buf.append(sep);
        DistributionPoint[] dp = this.getDistributionPoints();

        for (int i = 0; i != dp.length; ++i) {
            buf.append("    ");
            buf.append(dp[i]);
            buf.append(sep);
        }

        return buf.toString();
    }
}
