package com.myzuji.sadk.org.bouncycastle.asn1.x9;

import com.myzuji.sadk.algorithm.common.X9ObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.math.ec.ECAlgorithms;
import com.myzuji.sadk.org.bouncycastle.math.ec.ECCurve;

import java.math.BigInteger;

public class X9Curve extends ASN1Object implements X9ObjectIdentifiers {
    private ECCurve curve;
    private byte[] seed;
    private ASN1ObjectIdentifier fieldIdentifier = null;

    public X9Curve(ECCurve curve) {
        this.curve = curve;
        this.seed = null;
        this.setFieldIdentifier();
    }

    public X9Curve(ECCurve curve, byte[] seed) {
        this.curve = curve;
        this.seed = seed;
        this.setFieldIdentifier();
    }

    public X9Curve(X9FieldID fieldID, ASN1Sequence seq) {
        this.fieldIdentifier = fieldID.getIdentifier();
        if (this.fieldIdentifier.equals(prime_field)) {
            BigInteger p = ((ASN1Integer) fieldID.getParameters()).getValue();
            X9FieldElement x9A = new X9FieldElement(p, (ASN1OctetString) seq.getObjectAt(0));
            X9FieldElement x9B = new X9FieldElement(p, (ASN1OctetString) seq.getObjectAt(1));
            this.curve = new ECCurve.Fp(p, x9A.getValue().toBigInteger(), x9B.getValue().toBigInteger());
        } else {
            if (!this.fieldIdentifier.equals(characteristic_two_field)) {
                throw new IllegalArgumentException("This type of ECCurve is not implemented");
            }

            ASN1Sequence parameters = ASN1Sequence.getInstance(fieldID.getParameters());
            int m = ((ASN1Integer) parameters.getObjectAt(0)).getValue().intValue();
            ASN1ObjectIdentifier representation = (ASN1ObjectIdentifier) parameters.getObjectAt(1);
            int k1 = 0;
            int k2 = 0;
            int k3 = 0;
            if (representation.equals(tpBasis)) {
                k1 = ASN1Integer.getInstance(parameters.getObjectAt(2)).getValue().intValue();
            } else {
                if (!representation.equals(ppBasis)) {
                    throw new IllegalArgumentException("This type of EC basis is not implemented");
                }

                ASN1Sequence pentanomial = ASN1Sequence.getInstance(parameters.getObjectAt(2));
                k1 = ASN1Integer.getInstance(pentanomial.getObjectAt(0)).getValue().intValue();
                k2 = ASN1Integer.getInstance(pentanomial.getObjectAt(1)).getValue().intValue();
                k3 = ASN1Integer.getInstance(pentanomial.getObjectAt(2)).getValue().intValue();
            }

            X9FieldElement x9A = new X9FieldElement(m, k1, k2, k3, (ASN1OctetString) seq.getObjectAt(0));
            X9FieldElement x9B = new X9FieldElement(m, k1, k2, k3, (ASN1OctetString) seq.getObjectAt(1));
            this.curve = new ECCurve.F2m(m, k1, k2, k3, x9A.getValue().toBigInteger(), x9B.getValue().toBigInteger());
        }

        if (seq.size() == 3) {
            this.seed = ((DERBitString) seq.getObjectAt(2)).getBytes();
        }

    }

    private void setFieldIdentifier() {
        if (ECAlgorithms.isFpCurve(this.curve)) {
            this.fieldIdentifier = prime_field;
        } else {
            if (!ECAlgorithms.isF2mCurve(this.curve)) {
                throw new IllegalArgumentException("This type of ECCurve is not implemented");
            }

            this.fieldIdentifier = characteristic_two_field;
        }

    }

    public ECCurve getCurve() {
        return this.curve;
    }

    public byte[] getSeed() {
        return this.seed;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        if (this.fieldIdentifier.equals(prime_field)) {
            v.add((new X9FieldElement(this.curve.getA())).toASN1Primitive());
            v.add((new X9FieldElement(this.curve.getB())).toASN1Primitive());
        } else if (this.fieldIdentifier.equals(characteristic_two_field)) {
            v.add((new X9FieldElement(this.curve.getA())).toASN1Primitive());
            v.add((new X9FieldElement(this.curve.getB())).toASN1Primitive());
        }

        if (this.seed != null) {
            v.add(new DERBitString(this.seed));
        }

        return new DERSequence(v);
    }
}
