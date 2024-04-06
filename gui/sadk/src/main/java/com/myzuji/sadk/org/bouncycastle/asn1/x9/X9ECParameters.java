package com.myzuji.sadk.org.bouncycastle.asn1.x9;

import com.myzuji.sadk.algorithm.common.X9ObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.math.ec.ECAlgorithms;
import com.myzuji.sadk.org.bouncycastle.math.ec.ECCurve;
import com.myzuji.sadk.org.bouncycastle.math.ec.ECPoint;
import com.myzuji.sadk.org.bouncycastle.math.field.PolynomialExtensionField;

import java.math.BigInteger;

public class X9ECParameters extends ASN1Object implements X9ObjectIdentifiers {
    private static final BigInteger ONE = BigInteger.valueOf(1L);
    private X9FieldID fieldID;
    private ECCurve curve;
    private ECPoint g;
    private BigInteger n;
    private BigInteger h;
    private byte[] seed;

    private X9ECParameters(ASN1Sequence seq) {
        if (seq.getObjectAt(0) instanceof ASN1Integer && ((ASN1Integer) seq.getObjectAt(0)).getValue().equals(ONE)) {
            X9Curve x9c = new X9Curve(X9FieldID.getInstance(seq.getObjectAt(1)), ASN1Sequence.getInstance(seq.getObjectAt(2)));
            this.curve = x9c.getCurve();
            Object p = seq.getObjectAt(3);
            if (p instanceof X9ECPoint) {
                this.g = ((X9ECPoint) p).getPoint();
            } else {
                this.g = (new X9ECPoint(this.curve, (ASN1OctetString) p)).getPoint();
            }

            this.n = ((ASN1Integer) seq.getObjectAt(4)).getValue();
            this.seed = x9c.getSeed();
            if (seq.size() == 6) {
                this.h = ((ASN1Integer) seq.getObjectAt(5)).getValue();
            }

        } else {
            throw new IllegalArgumentException("bad version in X9ECParameters");
        }
    }

    public static X9ECParameters getInstance(Object obj) {
        if (obj instanceof X9ECParameters) {
            return (X9ECParameters) obj;
        } else {
            return obj != null ? new X9ECParameters(ASN1Sequence.getInstance(obj)) : null;
        }
    }

    public X9ECParameters(ECCurve curve, ECPoint g, BigInteger n) {
        this(curve, g, n, ONE, (byte[]) null);
    }

    public X9ECParameters(ECCurve curve, ECPoint g, BigInteger n, BigInteger h) {
        this(curve, g, n, h, (byte[]) null);
    }

    public X9ECParameters(ECCurve curve, ECPoint g, BigInteger n, BigInteger h, byte[] seed) {
        this.curve = curve;
        this.g = g.normalize();
        this.n = n;
        this.h = h;
        this.seed = seed;
        if (ECAlgorithms.isFpCurve(curve)) {
            this.fieldID = new X9FieldID(curve.getField().getCharacteristic());
        } else {
            if (!ECAlgorithms.isF2mCurve(curve)) {
                throw new IllegalArgumentException("'curve' is of an unsupported type");
            }

            PolynomialExtensionField field = (PolynomialExtensionField) curve.getField();
            int[] exponents = field.getMinimalPolynomial().getExponentsPresent();
            if (exponents.length == 3) {
                this.fieldID = new X9FieldID(exponents[2], exponents[1]);
            } else {
                if (exponents.length != 5) {
                    throw new IllegalArgumentException("Only trinomial and pentomial curves are supported");
                }

                this.fieldID = new X9FieldID(exponents[4], exponents[1], exponents[2], exponents[3]);
            }
        }

    }

    public ECCurve getCurve() {
        return this.curve;
    }

    public ECPoint getG() {
        return this.g;
    }

    public BigInteger getN() {
        return this.n;
    }

    public BigInteger getH() {
        return this.h == null ? ONE : this.h;
    }

    public byte[] getSeed() {
        return this.seed;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(new ASN1Integer(1L));
        v.add(this.fieldID);
        v.add(new X9Curve(this.curve, this.seed));
        v.add(new X9ECPoint(this.g));
        v.add(new ASN1Integer(this.n));
        if (this.h != null) {
            v.add(new ASN1Integer(this.h));
        }

        return new DERSequence(v);
    }
}
