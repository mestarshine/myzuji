package com.myzuji.sadk.org.bouncycastle.asn1.pkcs;

import com.myzuji.sadk.org.bouncycastle.asn1.*;

import java.math.BigInteger;
import java.util.Enumeration;

public class RSAPublicKey extends ASN1Object {
    private BigInteger modulus;
    private BigInteger publicExponent;

    public static RSAPublicKey getInstance(ASN1TaggedObject obj, boolean explicit) {
        return getInstance(ASN1Sequence.getInstance(obj, explicit));
    }

    public static RSAPublicKey getInstance(Object obj) {
        if (obj instanceof RSAPublicKey) {
            return (RSAPublicKey) obj;
        } else {
            return obj != null ? new RSAPublicKey(ASN1Sequence.getInstance(obj)) : null;
        }
    }

    public RSAPublicKey(BigInteger modulus, BigInteger publicExponent) {
        this.modulus = modulus;
        this.publicExponent = publicExponent;
    }

    private RSAPublicKey(ASN1Sequence seq) {
        if (seq.size() != 2) {
            throw new IllegalArgumentException("Bad sequence size: " + seq.size());
        } else {
            Enumeration e = seq.getObjects();
            this.modulus = ASN1Integer.getInstance(e.nextElement()).getPositiveValue();
            this.publicExponent = ASN1Integer.getInstance(e.nextElement()).getPositiveValue();
        }
    }

    public BigInteger getModulus() {
        return this.modulus;
    }

    public BigInteger getPublicExponent() {
        return this.publicExponent;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(new ASN1Integer(this.getModulus()));
        v.add(new ASN1Integer(this.getPublicExponent()));
        return new DERSequence(v);
    }
}
