package com.myzuji.sadk.org.bouncycastle.asn1.pkcs;

import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.AlgorithmIdentifier;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Enumeration;

public class PrivateKeyInfo extends ASN1Object {
    private ASN1OctetString privKey;
    private AlgorithmIdentifier algId;
    private ASN1Set attributes;

    public static PrivateKeyInfo getInstance(ASN1TaggedObject obj, boolean explicit) {
        return getInstance(ASN1Sequence.getInstance(obj, explicit));
    }

    public static PrivateKeyInfo getInstance(Object obj) {
        if (obj instanceof PrivateKeyInfo) {
            return (PrivateKeyInfo) obj;
        } else {
            return obj != null ? new PrivateKeyInfo(ASN1Sequence.getInstance(obj)) : null;
        }
    }

    public PrivateKeyInfo(AlgorithmIdentifier algId, ASN1Encodable privateKey) throws IOException {
        this(algId, privateKey, (ASN1Set) null);
    }

    public PrivateKeyInfo(AlgorithmIdentifier algId, ASN1Encodable privateKey, ASN1Set attributes) throws IOException {
        this.privKey = new DEROctetString(privateKey.toASN1Primitive().getEncoded("DER"));
        this.algId = algId;
        this.attributes = attributes;
    }


    public PrivateKeyInfo(ASN1Sequence seq) {
        Enumeration e = seq.getObjects();
        BigInteger version = ((ASN1Integer) e.nextElement()).getValue();
        if (version.intValue() != 0) {
            throw new IllegalArgumentException("wrong version for private key info");
        } else {
            this.algId = AlgorithmIdentifier.getInstance(e.nextElement());
            this.privKey = ASN1OctetString.getInstance(e.nextElement());
            if (e.hasMoreElements()) {
                this.attributes = ASN1Set.getInstance((ASN1TaggedObject) e.nextElement(), false);
            }

        }
    }

    public AlgorithmIdentifier getPrivateKeyAlgorithm() {
        return this.algId;
    }


    public AlgorithmIdentifier getAlgorithmId() {
        return this.algId;
    }

    public ASN1Encodable parsePrivateKey() throws IOException {
        return ASN1Primitive.fromByteArray(this.privKey.getOctets());
    }


    public ASN1Primitive getPrivateKey() {
        try {
            return this.parsePrivateKey().toASN1Primitive();
        } catch (IOException var2) {
            throw new IllegalStateException("unable to parse private key");
        }
    }

    public ASN1Set getAttributes() {
        return this.attributes;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(new ASN1Integer(0L));
        v.add(this.algId);
        v.add(this.privKey);
        if (this.attributes != null) {
            v.add(new DERTaggedObject(false, 0, this.attributes));
        }

        return new DERSequence(v);
    }
}
