package com.myzuji.sadk.signature.sm2;

import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.IssuerAndSerialNumber;
import com.myzuji.sadk.org.bouncycastle.asn1.sm2.ASN1SM2Signature;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import com.myzuji.sadk.system.global.SM2ContextConfig;

import java.io.IOException;
import java.util.Enumeration;

public class SM2SignerInfo extends ASN1Object {
    private ASN1Integer version;
    private IssuerAndSerialNumber issuerAndSerialNumber;
    private AlgorithmIdentifier digAlgorithm;
    private ASN1Set authenticatedAttributes;
    private AlgorithmIdentifier digEncryptionAlgorithm;
    private ASN1Integer encryptedDigestR;
    private ASN1Integer encryptedDigestS;
    private ASN1Set unauthenticatedAttributes;

    public static SM2SignerInfo getInstance(Object o) {
        if (o instanceof SM2SignerInfo) {
            return (SM2SignerInfo) o;
        } else if (o instanceof ASN1Sequence) {
            return new SM2SignerInfo((ASN1Sequence) o);
        } else {
            throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
        }
    }

    public SM2SignerInfo(ASN1Integer version, IssuerAndSerialNumber issuerAndSerialNumber, AlgorithmIdentifier digAlgorithm, ASN1Set authenticatedAttributes, AlgorithmIdentifier digEncryptionAlgorithm, ASN1Integer encryptedDigestR, ASN1Integer encryptedDigestS, ASN1Set unauthenticatedAttributes) {
        this.version = version;
        this.issuerAndSerialNumber = issuerAndSerialNumber;
        this.digAlgorithm = digAlgorithm;
        this.authenticatedAttributes = authenticatedAttributes;
        this.digEncryptionAlgorithm = digEncryptionAlgorithm;
        this.encryptedDigestR = encryptedDigestR;
        this.encryptedDigestS = encryptedDigestS;
        this.unauthenticatedAttributes = unauthenticatedAttributes;
    }

    public SM2SignerInfo(ASN1Sequence seq) {
        Enumeration e = seq.getObjects();
        this.version = (ASN1Integer) e.nextElement();
        this.issuerAndSerialNumber = IssuerAndSerialNumber.getInstance(e.nextElement());
        this.digAlgorithm = AlgorithmIdentifier.getInstance(e.nextElement());
        Object obj = e.nextElement();
        if (obj instanceof ASN1TaggedObject) {
            this.authenticatedAttributes = ASN1Set.getInstance((ASN1TaggedObject) obj, false);
            this.digEncryptionAlgorithm = AlgorithmIdentifier.getInstance(e.nextElement());
        } else {
            this.authenticatedAttributes = null;
            this.digEncryptionAlgorithm = AlgorithmIdentifier.getInstance(obj);
        }

        Object o = e.nextElement();
        if (o instanceof ASN1Sequence) {
            ASN1Sequence rsSequnence = (ASN1Sequence) o;
            Enumeration eRS = rsSequnence.getObjects();
            this.encryptedDigestR = ASN1Integer.getInstance(eRS.nextElement());
            this.encryptedDigestS = ASN1Integer.getInstance(eRS.nextElement());
        } else if (o instanceof ASN1OctetString) {
            byte[] value = ((ASN1OctetString) o).getOctets();
            ASN1SM2Signature signValue = new ASN1SM2Signature(value);
            this.encryptedDigestR = signValue.getR();
            this.encryptedDigestS = signValue.getS();
        } else {
            this.encryptedDigestR = (ASN1Integer) o;
            this.encryptedDigestS = (ASN1Integer) e.nextElement();
        }

        if (e.hasMoreElements()) {
            this.unauthenticatedAttributes = ASN1Set.getInstance((ASN1TaggedObject) e.nextElement(), false);
        } else {
            this.unauthenticatedAttributes = null;
        }

    }

    public ASN1Integer getVersion() {
        return this.version;
    }

    public IssuerAndSerialNumber getIssuerAndSerialNumber() {
        return this.issuerAndSerialNumber;
    }

    public ASN1Set getAuthenticatedAttributes() {
        return this.authenticatedAttributes;
    }

    public AlgorithmIdentifier getDigestAlgorithm() {
        return this.digAlgorithm;
    }

    public ASN1Integer getEncryptedDigestR() {
        return this.encryptedDigestR;
    }

    public ASN1Integer getEncryptedDigestS() {
        return this.encryptedDigestS;
    }

    public AlgorithmIdentifier getDigestEncryptionAlgorithm() {
        return this.digEncryptionAlgorithm;
    }

    public ASN1Set getUnauthenticatedAttributes() {
        return this.unauthenticatedAttributes;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(this.version);
        v.add(this.issuerAndSerialNumber);
        v.add(this.digAlgorithm);
        if (this.authenticatedAttributes != null) {
            v.add(new DERTaggedObject(false, 0, this.authenticatedAttributes));
        }

        v.add(this.digEncryptionAlgorithm);
        int signFormat = SM2ContextConfig.getSignFormat();
        ASN1EncodableVector rsV;
        DERSequence rsSequence;
        if (3 == signFormat) {
            rsV = new ASN1EncodableVector();
            rsV.add(this.encryptedDigestR);
            rsV.add(this.encryptedDigestS);
            rsSequence = new DERSequence(rsV);

            try {
                DEROctetString rsStr = new DEROctetString(rsSequence);
                v.add(rsStr);
            } catch (IOException var6) {
                IOException e = var6;
                throw new SecurityException("SM2SignerFile Encoded Failure: " + e.getMessage());
            }
        } else if (2 == signFormat) {
            rsV = new ASN1EncodableVector();
            rsV.add(this.encryptedDigestR);
            rsV.add(this.encryptedDigestS);
            rsSequence = new DERSequence(rsV);
            v.add(rsSequence);
        } else if (1 == signFormat) {
            v.add(this.encryptedDigestR);
            v.add(this.encryptedDigestS);
        }

        if (this.unauthenticatedAttributes != null) {
            v.add(new DERTaggedObject(false, 1, this.unauthenticatedAttributes));
        }

        return new DERSequence(v);
    }
}

