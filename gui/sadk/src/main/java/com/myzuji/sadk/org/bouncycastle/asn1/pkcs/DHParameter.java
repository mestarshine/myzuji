package com.myzuji.sadk.org.bouncycastle.asn1.pkcs;

import com.myzuji.sadk.org.bouncycastle.asn1.*;

import java.math.BigInteger;
import java.util.Enumeration;

public class DHParameter extends ASN1Object {
    ASN1Integer p;
    ASN1Integer g;
    ASN1Integer l;

    public DHParameter(BigInteger p, BigInteger g, int l) {
        this.p = new ASN1Integer(p);
        this.g = new ASN1Integer(g);
        if (l != 0) {
            this.l = new ASN1Integer((long) l);
        } else {
            this.l = null;
        }

    }

    public static DHParameter getInstance(Object obj) {
        if (obj instanceof DHParameter) {
            return (DHParameter) obj;
        } else {
            return obj != null ? new DHParameter(ASN1Sequence.getInstance(obj)) : null;
        }
    }

    private DHParameter(ASN1Sequence seq) {
        Enumeration e = seq.getObjects();
        this.p = ASN1Integer.getInstance(e.nextElement());
        this.g = ASN1Integer.getInstance(e.nextElement());
        if (e.hasMoreElements()) {
            this.l = (ASN1Integer) e.nextElement();
        } else {
            this.l = null;
        }

    }

    public BigInteger getP() {
        return this.p.getPositiveValue();
    }

    public BigInteger getG() {
        return this.g.getPositiveValue();
    }

    public BigInteger getL() {
        return this.l == null ? null : this.l.getPositiveValue();
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(this.p);
        v.add(this.g);
        if (this.getL() != null) {
            v.add(this.l);
        }

        return new DERSequence(v);
    }
}
