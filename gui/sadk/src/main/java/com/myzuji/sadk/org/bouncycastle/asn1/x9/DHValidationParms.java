package com.myzuji.sadk.org.bouncycastle.asn1.x9;

import com.myzuji.sadk.org.bouncycastle.asn1.*;

public class DHValidationParms extends ASN1Object {
    private DERBitString seed;
    private ASN1Integer pgenCounter;

    public static DHValidationParms getInstance(ASN1TaggedObject obj, boolean explicit) {
        return getInstance(ASN1Sequence.getInstance(obj, explicit));
    }

    public static DHValidationParms getInstance(Object obj) {
        if (obj instanceof DHValidationParms) {
            return (DHValidationParms) obj;
        } else {
            return obj != null ? new DHValidationParms(ASN1Sequence.getInstance(obj)) : null;
        }
    }

    public DHValidationParms(DERBitString seed, ASN1Integer pgenCounter) {
        if (seed == null) {
            throw new IllegalArgumentException("'seed' cannot be null");
        } else if (pgenCounter == null) {
            throw new IllegalArgumentException("'pgenCounter' cannot be null");
        } else {
            this.seed = seed;
            this.pgenCounter = pgenCounter;
        }
    }

    private DHValidationParms(ASN1Sequence seq) {
        if (seq.size() != 2) {
            throw new IllegalArgumentException("Bad sequence size: " + seq.size());
        } else {
            this.seed = DERBitString.getInstance(seq.getObjectAt(0));
            this.pgenCounter = ASN1Integer.getInstance(seq.getObjectAt(1));
        }
    }

    public DERBitString getSeed() {
        return this.seed;
    }

    public ASN1Integer getPgenCounter() {
        return this.pgenCounter;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(this.seed);
        v.add(this.pgenCounter);
        return new DERSequence(v);
    }
}
