package com.myzuji.sadk.org.bouncycastle.asn1.x509;

import com.myzuji.sadk.org.bouncycastle.asn1.*;

import java.math.BigInteger;

public class BasicConstraints extends ASN1Object {
    ASN1Boolean cA = ASN1Boolean.getInstance(false);
    ASN1Integer pathLenConstraint = null;

    public static BasicConstraints getInstance(ASN1TaggedObject obj, boolean explicit) {
        return getInstance(ASN1Sequence.getInstance(obj, explicit));
    }

    public static BasicConstraints getInstance(Object obj) {
        if (obj instanceof BasicConstraints) {
            return (BasicConstraints) obj;
        } else if (obj instanceof X509Extension) {
            return getInstance(X509Extension.convertValueToObject((X509Extension) obj));
        } else {
            return obj != null ? new BasicConstraints(ASN1Sequence.getInstance(obj)) : null;
        }
    }

    public static BasicConstraints fromExtensions(Extensions extensions) {
        return getInstance(extensions.getExtensionParsedValue(Extension.basicConstraints));
    }

    private BasicConstraints(ASN1Sequence seq) {
        if (seq.size() == 0) {
            this.cA = null;
            this.pathLenConstraint = null;
        } else {
            if (seq.getObjectAt(0) instanceof ASN1Boolean) {
                this.cA = ASN1Boolean.getInstance(seq.getObjectAt(0));
            } else {
                this.cA = null;
                this.pathLenConstraint = ASN1Integer.getInstance(seq.getObjectAt(0));
            }

            if (seq.size() > 1) {
                if (this.cA == null) {
                    throw new IllegalArgumentException("wrong sequence in constructor");
                }

                this.pathLenConstraint = ASN1Integer.getInstance(seq.getObjectAt(1));
            }
        }

    }

    public BasicConstraints(boolean cA) {
        if (cA) {
            this.cA = ASN1Boolean.getInstance(true);
        } else {
            this.cA = null;
        }

        this.pathLenConstraint = null;
    }

    public BasicConstraints(int pathLenConstraint) {
        this.cA = ASN1Boolean.getInstance(true);
        this.pathLenConstraint = new ASN1Integer((long) pathLenConstraint);
    }

    public boolean isCA() {
        return this.cA != null && this.cA.isTrue();
    }

    public BigInteger getPathLenConstraint() {
        return this.pathLenConstraint != null ? this.pathLenConstraint.getValue() : null;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        if (this.cA != null) {
            v.add(this.cA);
        }

        if (this.pathLenConstraint != null) {
            v.add(this.pathLenConstraint);
        }

        return new DERSequence(v);
    }

    public String toString() {
        if (this.pathLenConstraint == null) {
            return this.cA == null ? "BasicConstraints: isCa(false)" : "BasicConstraints: isCa(" + this.isCA() + ")";
        } else {
            return "BasicConstraints: isCa(" + this.isCA() + "), pathLenConstraint = " + this.pathLenConstraint.getValue();
        }
    }
}
