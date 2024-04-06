package com.myzuji.sadk.org.bouncycastle.asn1.x509;

import com.myzuji.sadk.org.bouncycastle.asn1.*;

public class AlgorithmIdentifier extends ASN1Object {
    private ASN1ObjectIdentifier objectId;
    private ASN1Encodable parameters;
    private boolean parametersDefined = false;

    public static AlgorithmIdentifier getInstance(ASN1TaggedObject obj, boolean explicit) {
        return getInstance(ASN1Sequence.getInstance(obj, explicit));
    }

    public static AlgorithmIdentifier getInstance(Object obj) {
        if (obj != null && !(obj instanceof AlgorithmIdentifier)) {
            if (obj instanceof ASN1ObjectIdentifier) {
                return new AlgorithmIdentifier((ASN1ObjectIdentifier) obj);
            } else {
                return obj instanceof String ? new AlgorithmIdentifier((String) obj) : new AlgorithmIdentifier(ASN1Sequence.getInstance(obj));
            }
        } else {
            return (AlgorithmIdentifier) obj;
        }
    }

    public AlgorithmIdentifier(ASN1ObjectIdentifier objectId) {
        this.objectId = objectId;
    }


    public AlgorithmIdentifier(String objectId) {
        this.objectId = new ASN1ObjectIdentifier(objectId);
    }

    public AlgorithmIdentifier(ASN1ObjectIdentifier objectId, ASN1Encodable parameters) {
        this.parametersDefined = true;
        this.objectId = objectId;
        this.parameters = parameters;
    }


    public AlgorithmIdentifier(ASN1Sequence seq) {
        if (seq.size() >= 1 && seq.size() <= 2) {
            this.objectId = ASN1ObjectIdentifier.getInstance(seq.getObjectAt(0));
            if (seq.size() == 2) {
                this.parametersDefined = true;
                this.parameters = seq.getObjectAt(1);
            } else {
                this.parameters = null;
            }

        } else {
            throw new IllegalArgumentException("Bad sequence size: " + seq.size());
        }
    }

    public ASN1ObjectIdentifier getAlgorithm() {
        return new ASN1ObjectIdentifier(this.objectId.getId());
    }


    public ASN1ObjectIdentifier getObjectId() {
        return this.objectId;
    }

    public ASN1Encodable getParameters() {
        return this.parameters;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(this.objectId);
        if (this.parametersDefined) {
            if (this.parameters != null) {
                v.add(this.parameters);
            } else {
                v.add(DERNull.INSTANCE);
            }
        }

        return new DERSequence(v);
    }
}

