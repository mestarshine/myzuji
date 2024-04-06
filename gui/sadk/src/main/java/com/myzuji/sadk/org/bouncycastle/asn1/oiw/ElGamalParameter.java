package com.myzuji.sadk.org.bouncycastle.asn1.oiw;

import com.myzuji.sadk.org.bouncycastle.asn1.*;

import java.math.BigInteger;
import java.util.Enumeration;

public class ElGamalParameter extends ASN1Object {
    ASN1Integer p;
    ASN1Integer g;

    public ElGamalParameter(BigInteger p, BigInteger g) {
        this.p = new ASN1Integer(p);
        this.g = new ASN1Integer(g);
    }

    private ElGamalParameter(ASN1Sequence seq) {
        Enumeration e = seq.getObjects();
        this.p = (ASN1Integer) e.nextElement();
        this.g = (ASN1Integer) e.nextElement();
    }

    public static ElGamalParameter getInstance(Object o) {
        if (o instanceof ElGamalParameter) {
            return (ElGamalParameter) o;
        } else {
            return o != null ? new ElGamalParameter(ASN1Sequence.getInstance(o)) : null;
        }
    }

    public BigInteger getP() {
        return this.p.getPositiveValue();
    }

    public BigInteger getG() {
        return this.g.getPositiveValue();
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(this.p);
        v.add(this.g);
        return new DERSequence(v);
    }
}
