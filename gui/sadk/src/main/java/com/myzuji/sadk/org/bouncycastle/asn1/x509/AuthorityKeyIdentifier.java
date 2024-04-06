package com.myzuji.sadk.org.bouncycastle.asn1.x509;

import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.crypto.Digest;
import com.myzuji.sadk.org.bouncycastle.crypto.digests.SHA1Digest;

import java.math.BigInteger;
import java.util.Base64;
import java.util.Enumeration;

public class AuthorityKeyIdentifier extends ASN1Object {
    ASN1OctetString keyidentifier;
    GeneralNames certissuer;
    ASN1Integer certserno;

    public static AuthorityKeyIdentifier getInstance(ASN1TaggedObject obj, boolean explicit) {
        return getInstance(ASN1Sequence.getInstance(obj, explicit));
    }

    public static AuthorityKeyIdentifier getInstance(Object obj) {
        if (obj instanceof AuthorityKeyIdentifier) {
            return (AuthorityKeyIdentifier) obj;
        } else {
            return obj != null ? new AuthorityKeyIdentifier(ASN1Sequence.getInstance(obj)) : null;
        }
    }

    public static AuthorityKeyIdentifier fromExtensions(Extensions extensions) {
        return getInstance(extensions.getExtensionParsedValue(Extension.authorityKeyIdentifier));
    }

    protected AuthorityKeyIdentifier(ASN1Sequence seq) {
        this.keyidentifier = null;
        this.certissuer = null;
        this.certserno = null;
        Enumeration e = seq.getObjects();

        while (e.hasMoreElements()) {
            ASN1TaggedObject o = DERTaggedObject.getInstance(e.nextElement());
            switch (o.getTagNo()) {
                case 0:
                    this.keyidentifier = ASN1OctetString.getInstance(o, false);
                    break;
                case 1:
                    this.certissuer = GeneralNames.getInstance(o, false);
                    break;
                case 2:
                    this.certserno = ASN1Integer.getInstance(o, false);
                    break;
                default:
                    throw new IllegalArgumentException("illegal tag");
            }
        }

    }


    public AuthorityKeyIdentifier(SubjectPublicKeyInfo spki) {
        this.keyidentifier = null;
        this.certissuer = null;
        this.certserno = null;
        Digest digest = new SHA1Digest();
        byte[] resBuf = new byte[digest.getDigestSize()];
        byte[] bytes = spki.getPublicKeyData().getBytes();
        digest.update(bytes, 0, bytes.length);
        digest.doFinal(resBuf, 0);
        this.keyidentifier = new DEROctetString(resBuf);
    }


    public AuthorityKeyIdentifier(SubjectPublicKeyInfo spki, GeneralNames name, BigInteger serialNumber) {
        this.keyidentifier = null;
        this.certissuer = null;
        this.certserno = null;
        Digest digest = new SHA1Digest();
        byte[] resBuf = new byte[digest.getDigestSize()];
        byte[] bytes = spki.getPublicKeyData().getBytes();
        digest.update(bytes, 0, bytes.length);
        digest.doFinal(resBuf, 0);
        this.keyidentifier = new DEROctetString(resBuf);
        this.certissuer = GeneralNames.getInstance(name.toASN1Primitive());
        this.certserno = new ASN1Integer(serialNumber);
    }

    public AuthorityKeyIdentifier(GeneralNames name, BigInteger serialNumber) {
        this((byte[]) null, name, serialNumber);
    }

    public AuthorityKeyIdentifier(byte[] keyIdentifier) {
        this((byte[]) keyIdentifier, (GeneralNames) null, (BigInteger) null);
    }

    public AuthorityKeyIdentifier(byte[] keyIdentifier, GeneralNames name, BigInteger serialNumber) {
        this.keyidentifier = null;
        this.certissuer = null;
        this.certserno = null;
        this.keyidentifier = keyIdentifier != null ? new DEROctetString(keyIdentifier) : null;
        this.certissuer = name;
        this.certserno = serialNumber != null ? new ASN1Integer(serialNumber) : null;
    }

    public byte[] getKeyIdentifier() {
        return this.keyidentifier != null ? this.keyidentifier.getOctets() : null;
    }

    public GeneralNames getAuthorityCertIssuer() {
        return this.certissuer;
    }

    public BigInteger getAuthorityCertSerialNumber() {
        return this.certserno != null ? this.certserno.getValue() : null;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        if (this.keyidentifier != null) {
            v.add(new DERTaggedObject(false, 0, this.keyidentifier));
        }

        if (this.certissuer != null) {
            v.add(new DERTaggedObject(false, 1, this.certissuer));
        }

        if (this.certserno != null) {
            v.add(new DERTaggedObject(false, 2, this.certserno));
        }

        return new DERSequence(v);
    }

    public String toString() {
        String idText = "";
        if (this.keyidentifier != null) {
            idText = Base64.getEncoder().encodeToString(this.keyidentifier.getOctets());
        }

        return "AuthorityKeyIdentifier: KeyID(" + idText + ")";
    }
}
