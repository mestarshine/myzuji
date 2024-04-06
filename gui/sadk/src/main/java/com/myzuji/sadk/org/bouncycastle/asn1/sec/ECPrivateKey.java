package com.myzuji.sadk.org.bouncycastle.asn1.sec;

import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.util.BigIntegers;

import java.math.BigInteger;
import java.util.Enumeration;

public class ECPrivateKey extends ASN1Object {
    private ASN1Sequence seq;

    private ECPrivateKey(ASN1Sequence seq) {
        this.seq = seq;
    }

    public static ECPrivateKey getInstance(Object obj) {
        if (obj instanceof ECPrivateKey) {
            return (ECPrivateKey) obj;
        } else {
            return obj != null ? new ECPrivateKey(ASN1Sequence.getInstance(obj)) : null;
        }
    }

    public ECPrivateKey(BigInteger key) {
        byte[] bytes = BigIntegers.asUnsignedByteArray(key);
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(new ASN1Integer(1L));
        v.add(new DEROctetString(bytes));
        this.seq = new DERSequence(v);
    }

    public ECPrivateKey(BigInteger key, ASN1Encodable parameters) {
        this(key, (DERBitString) null, parameters);
    }

    public ECPrivateKey(BigInteger key, DERBitString publicKey, ASN1Encodable parameters) {
        byte[] bytes = BigIntegers.asUnsignedByteArray(key);
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(new ASN1Integer(1L));
        v.add(new DEROctetString(bytes));
        if (parameters != null) {
            v.add(new DERTaggedObject(true, 0, parameters));
        }

        if (publicKey != null) {
            v.add(new DERTaggedObject(true, 1, publicKey));
        }

        this.seq = new DERSequence(v);
    }

    public BigInteger getKey() {
        ASN1OctetString octs = (ASN1OctetString) this.seq.getObjectAt(1);
        return new BigInteger(1, octs.getOctets());
    }

    public DERBitString getPublicKey() {
        return (DERBitString) this.getObjectInTag(1);
    }

    public ASN1Primitive getParameters() {
        return this.getObjectInTag(0);
    }

    private ASN1Primitive getObjectInTag(int tagNo) {
        Enumeration e = this.seq.getObjects();

        while (e.hasMoreElements()) {
            ASN1Encodable obj = (ASN1Encodable) e.nextElement();
            if (obj instanceof ASN1TaggedObject) {
                ASN1TaggedObject tag = (ASN1TaggedObject) obj;
                if (tag.getTagNo() == tagNo) {
                    return tag.getObject().toASN1Primitive();
                }
            }
        }

        return null;
    }

    public ASN1Primitive toASN1Primitive() {
        return this.seq;
    }
}
