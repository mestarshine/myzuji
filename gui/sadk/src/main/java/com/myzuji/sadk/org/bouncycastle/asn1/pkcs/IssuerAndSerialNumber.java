package com.myzuji.sadk.org.bouncycastle.asn1.pkcs;

import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.asn1.x500.X500Name;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.X509Name;

import java.math.BigInteger;

public class IssuerAndSerialNumber extends ASN1Object {
    X500Name name;
    ASN1Integer certSerialNumber;

    public static IssuerAndSerialNumber getInstance(Object obj) {
        if (obj instanceof IssuerAndSerialNumber) {
            return (IssuerAndSerialNumber) obj;
        } else {
            return obj != null ? new IssuerAndSerialNumber(ASN1Sequence.getInstance(obj)) : null;
        }
    }

    private IssuerAndSerialNumber(ASN1Sequence seq) {
        this.name = X500Name.getInstance(seq.getObjectAt(0));
        this.certSerialNumber = (ASN1Integer) seq.getObjectAt(1);
    }

    public IssuerAndSerialNumber(X509Name name, BigInteger certSerialNumber) {
        if (name == null) {
            throw new IllegalArgumentException("Not all mandatory fields set in IssuerAndSerialNumber generator.");
        } else {
            this.name = X500Name.getInstance(name.toASN1Primitive());
            this.certSerialNumber = new ASN1Integer(certSerialNumber);
        }
    }

    public IssuerAndSerialNumber(X509Name name, ASN1Integer certSerialNumber) {
        if (name == null) {
            throw new IllegalArgumentException("Not all mandatory fields set in IssuerAndSerialNumber generator.");
        } else {
            this.name = X500Name.getInstance(name.toASN1Primitive());
            this.certSerialNumber = certSerialNumber;
        }
    }

    public IssuerAndSerialNumber(X500Name name, BigInteger certSerialNumber) {
        this.name = name;
        this.certSerialNumber = new ASN1Integer(certSerialNumber);
    }

    public X500Name getName() {
        return this.name;
    }

    public ASN1Integer getCertificateSerialNumber() {
        return this.certSerialNumber;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(this.name);
        v.add(this.certSerialNumber);
        return new DERSequence(v);
    }
}
