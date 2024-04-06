package com.myzuji.sadk.org.bouncycastle.asn1.pkcs;

import com.myzuji.sadk.org.bouncycastle.asn1.*;

import java.math.BigInteger;
import java.util.Enumeration;

public class RSAPrivateKey extends ASN1Object {
    private BigInteger version;
    private BigInteger modulus;
    private BigInteger publicExponent;
    private BigInteger privateExponent;
    private BigInteger prime1;
    private BigInteger prime2;
    private BigInteger exponent1;
    private BigInteger exponent2;
    private BigInteger coefficient;
    private ASN1Sequence otherPrimeInfos = null;

    public static RSAPrivateKey getInstance(ASN1TaggedObject obj, boolean explicit) {
        return getInstance(ASN1Sequence.getInstance(obj, explicit));
    }

    public static RSAPrivateKey getInstance(Object obj) {
        if (obj instanceof RSAPrivateKey) {
            return (RSAPrivateKey) obj;
        } else {
            return obj != null ? new RSAPrivateKey(ASN1Sequence.getInstance(obj)) : null;
        }
    }

    public RSAPrivateKey(BigInteger modulus, BigInteger publicExponent, BigInteger privateExponent, BigInteger prime1, BigInteger prime2, BigInteger exponent1, BigInteger exponent2, BigInteger coefficient) {
        this.version = BigInteger.valueOf(0L);
        this.modulus = modulus;
        this.publicExponent = publicExponent;
        this.privateExponent = privateExponent;
        this.prime1 = prime1;
        this.prime2 = prime2;
        this.exponent1 = exponent1;
        this.exponent2 = exponent2;
        this.coefficient = coefficient;
    }

    private RSAPrivateKey(ASN1Sequence seq) {
        Enumeration e = seq.getObjects();
        BigInteger v = ((ASN1Integer) e.nextElement()).getValue();
        if (v.intValue() != 0 && v.intValue() != 1) {
            throw new IllegalArgumentException("wrong version for RSA private key");
        } else {
            this.version = v;
            this.modulus = ((ASN1Integer) e.nextElement()).getValue();
            this.publicExponent = ((ASN1Integer) e.nextElement()).getValue();
            this.privateExponent = ((ASN1Integer) e.nextElement()).getValue();
            this.prime1 = ((ASN1Integer) e.nextElement()).getValue();
            this.prime2 = ((ASN1Integer) e.nextElement()).getValue();
            this.exponent1 = ((ASN1Integer) e.nextElement()).getValue();
            this.exponent2 = ((ASN1Integer) e.nextElement()).getValue();
            this.coefficient = ((ASN1Integer) e.nextElement()).getValue();
            if (e.hasMoreElements()) {
                this.otherPrimeInfos = (ASN1Sequence) e.nextElement();
            }

        }
    }

    public BigInteger getVersion() {
        return this.version;
    }

    public BigInteger getModulus() {
        return this.modulus;
    }

    public BigInteger getPublicExponent() {
        return this.publicExponent;
    }

    public BigInteger getPrivateExponent() {
        return this.privateExponent;
    }

    public BigInteger getPrime1() {
        return this.prime1;
    }

    public BigInteger getPrime2() {
        return this.prime2;
    }

    public BigInteger getExponent1() {
        return this.exponent1;
    }

    public BigInteger getExponent2() {
        return this.exponent2;
    }

    public BigInteger getCoefficient() {
        return this.coefficient;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(new ASN1Integer(this.version));
        v.add(new ASN1Integer(this.getModulus()));
        v.add(new ASN1Integer(this.getPublicExponent()));
        v.add(new ASN1Integer(this.getPrivateExponent()));
        v.add(new ASN1Integer(this.getPrime1()));
        v.add(new ASN1Integer(this.getPrime2()));
        v.add(new ASN1Integer(this.getExponent1()));
        v.add(new ASN1Integer(this.getExponent2()));
        v.add(new ASN1Integer(this.getCoefficient()));
        if (this.otherPrimeInfos != null) {
            v.add(this.otherPrimeInfos);
        }

        return new DERSequence(v);
    }
}
