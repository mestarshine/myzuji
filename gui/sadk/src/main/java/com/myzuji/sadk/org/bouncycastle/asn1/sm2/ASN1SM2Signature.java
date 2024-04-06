package com.myzuji.sadk.org.bouncycastle.asn1.sm2;

import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.util.Arrays;
import com.myzuji.sadk.org.bouncycastle.util.BigIntegers;

import java.math.BigInteger;
import java.util.Enumeration;

public final class ASN1SM2Signature extends ASN1Object {
    private ASN1Integer r;
    private ASN1Integer s;

    public static ASN1SM2Signature getInstance(Object o) throws IllegalArgumentException {
        if (o instanceof ASN1SM2Signature) {
            return (ASN1SM2Signature) o;
        } else {
            return o != null ? new ASN1SM2Signature(ASN1Sequence.getInstance(o)) : null;
        }
    }

    public ASN1SM2Signature(BigInteger r, BigInteger s) {
        this.r = new ASN1Integer(r);
        this.s = new ASN1Integer(s);
    }

    public ASN1SM2Signature(ASN1Integer r, ASN1Integer s) {
        this.r = r;
        this.s = s;
    }

    public ASN1SM2Signature(ASN1Sequence seq) {
        Enumeration e = seq.getObjects();
        this.r = (ASN1Integer) e.nextElement();
        this.s = (ASN1Integer) e.nextElement();
    }

    public ASN1SM2Signature(byte[] signature) throws SecurityException {
        if (signature != null && signature.length >= 64) {
            if (signature.length == 64) {
                this.r = new ASN1Integer(new BigInteger(1, Arrays.copyOfRange(signature, 0, 32)));
                this.s = new ASN1Integer(new BigInteger(1, Arrays.copyOfRange(signature, 32, 64)));
            } else {
                ASN1InputStream asn1Is = null;

                try {
                    Enumeration e;
                    try {
                        asn1Is = new ASN1InputStream(signature);
                        e = null;
                        ASN1Sequence seq = (ASN1Sequence) asn1Is.readObject();
                        e = seq.getObjects();
                        this.r = (ASN1Integer) e.nextElement();
                        this.s = (ASN1Integer) e.nextElement();
                    } catch (Exception var12) {
                        throw new SecurityException("Unknown  signature value: " + var12.getMessage());
                    }
                } finally {
                    if (asn1Is != null) {
                        try {
                            asn1Is.close();
                        } catch (Exception var11) {
                        }
                    }

                }
            }

        } else {
            throw new SecurityException("Unknown signature value");
        }
    }

    public final byte[] getRS() {
        byte[] dest = new byte[64];
        System.arraycopy(BigIntegers.asUnsignedByteArray(32, this.r.getPositiveValue()), 0, dest, 0, 32);
        System.arraycopy(BigIntegers.asUnsignedByteArray(32, this.s.getPositiveValue()), 0, dest, 32, 32);
        return dest;
    }

    public ASN1Integer getR() {
        return this.r;
    }

    public ASN1Integer getS() {
        return this.s;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(this.r);
        v.add(this.s);
        return new DERSequence(v);
    }

    public String toString() {
        StringBuffer builder = new StringBuffer();
        builder.append("SM2Signature [r=");
        builder.append(this.r);
        builder.append(", s=");
        builder.append(this.s);
        builder.append(']');
        return builder.toString();
    }
}
