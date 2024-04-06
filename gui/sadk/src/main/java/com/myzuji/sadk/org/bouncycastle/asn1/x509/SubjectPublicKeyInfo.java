package com.myzuji.sadk.org.bouncycastle.asn1.x509;

import com.myzuji.sadk.org.bouncycastle.asn1.*;

import java.io.IOException;
import java.util.Enumeration;

public class SubjectPublicKeyInfo extends ASN1Object {
    private AlgorithmIdentifier algId;
    private DERBitString keyData;

    public static SubjectPublicKeyInfo getInstance(ASN1TaggedObject obj, boolean explicit) {
        return getInstance(ASN1Sequence.getInstance(obj, explicit));
    }

    public static SubjectPublicKeyInfo getInstance(Object obj) {
        if (obj instanceof SubjectPublicKeyInfo) {
            return (SubjectPublicKeyInfo) obj;
        } else {
            return obj != null ? new SubjectPublicKeyInfo(ASN1Sequence.getInstance(obj)) : null;
        }
    }

    public SubjectPublicKeyInfo(AlgorithmIdentifier algId, ASN1Encodable publicKey) throws IOException {
        this.keyData = new DERBitString(publicKey);
        this.algId = algId;
    }

    public SubjectPublicKeyInfo(AlgorithmIdentifier algId, byte[] publicKey) {
        this.keyData = new DERBitString(publicKey);
        this.algId = algId;
    }

    public SubjectPublicKeyInfo(ASN1Sequence seq) {
        if (seq.size() != 2) {
            throw new IllegalArgumentException("Bad sequence size: " + seq.size());
        } else {
            Enumeration e = seq.getObjects();
            this.algId = AlgorithmIdentifier.getInstance(e.nextElement());
            this.keyData = DERBitString.getInstance(e.nextElement());
        }
    }

    public AlgorithmIdentifier getAlgorithm() {
        return this.algId;
    }


    public AlgorithmIdentifier getAlgorithmId() {
        return this.algId;
    }

    public ASN1Primitive parsePublicKey() throws IOException {
        ASN1InputStream aIn = new ASN1InputStream(this.keyData.getBytes());
        return aIn.readObject();
    }


    public ASN1Primitive getPublicKey() throws IOException {
        ASN1InputStream aIn = new ASN1InputStream(this.keyData.getBytes());
        return aIn.readObject();
    }

    public DERBitString getPublicKeyData() {
        return this.keyData;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(this.algId);
        v.add(this.keyData);
        return new DERSequence(v);
    }
}
