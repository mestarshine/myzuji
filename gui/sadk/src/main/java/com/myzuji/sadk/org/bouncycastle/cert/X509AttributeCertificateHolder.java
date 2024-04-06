package com.myzuji.sadk.org.bouncycastle.cert;

import com.myzuji.sadk.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Primitive;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Sequence;
import com.myzuji.sadk.org.bouncycastle.asn1.DEROutputStream;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.Attribute;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.*;
import com.myzuji.sadk.org.bouncycastle.operator.ContentVerifier;
import com.myzuji.sadk.org.bouncycastle.operator.ContentVerifierProvider;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class X509AttributeCertificateHolder {
    private static Attribute[] EMPTY_ARRAY = new Attribute[0];
    private AttributeCertificate attrCert;
    private Extensions extensions;

    private static AttributeCertificate parseBytes(byte[] certEncoding) throws IOException {
        try {
            return AttributeCertificate.getInstance(ASN1Primitive.fromByteArray(certEncoding));
        } catch (ClassCastException var2) {
            ClassCastException e = var2;
            throw new CertIOException("malformed data: " + e.getMessage(), e);
        } catch (IllegalArgumentException var3) {
            IllegalArgumentException e = var3;
            throw new CertIOException("malformed data: " + e.getMessage(), e);
        }
    }

    public X509AttributeCertificateHolder(byte[] certEncoding) throws IOException {
        this(parseBytes(certEncoding));
    }

    public X509AttributeCertificateHolder(AttributeCertificate attrCert) {
        this.attrCert = attrCert;
        this.extensions = attrCert.getAcinfo().getExtensions();
    }

    public byte[] getEncoded() throws IOException {
        return this.attrCert.getEncoded();
    }

    public int getVersion() {
        return this.attrCert.getAcinfo().getVersion().getValue().intValue() + 1;
    }

    public BigInteger getSerialNumber() {
        return this.attrCert.getAcinfo().getSerialNumber().getValue();
    }

    public AttributeCertificateHolder getHolder() {
        return new AttributeCertificateHolder((ASN1Sequence) this.attrCert.getAcinfo().getHolder().toASN1Primitive());
    }

    public AttributeCertificateIssuer getIssuer() {
        return new AttributeCertificateIssuer(this.attrCert.getAcinfo().getIssuer());
    }

    public Date getNotBefore() {
        return CertUtils.recoverDate(this.attrCert.getAcinfo().getAttrCertValidityPeriod().getNotBeforeTime());
    }

    public Date getNotAfter() {
        return CertUtils.recoverDate(this.attrCert.getAcinfo().getAttrCertValidityPeriod().getNotAfterTime());
    }

    public Attribute[] getAttributes() {
        ASN1Sequence seq = this.attrCert.getAcinfo().getAttributes();
        Attribute[] attrs = new Attribute[seq.size()];

        for (int i = 0; i != seq.size(); ++i) {
            attrs[i] = Attribute.getInstance(seq.getObjectAt(i));
        }

        return attrs;
    }

    public Attribute[] getAttributes(ASN1ObjectIdentifier type) {
        ASN1Sequence seq = this.attrCert.getAcinfo().getAttributes();
        List list = new ArrayList();

        for (int i = 0; i != seq.size(); ++i) {
            Attribute attr = Attribute.getInstance(seq.getObjectAt(i));
            if (attr.getAttrType().equals(type)) {
                list.add(attr);
            }
        }

        if (list.size() == 0) {
            return EMPTY_ARRAY;
        } else {
            return (Attribute[]) ((Attribute[]) list.toArray(new Attribute[list.size()]));
        }
    }

    public boolean hasExtensions() {
        return this.extensions != null;
    }

    public Extension getExtension(ASN1ObjectIdentifier oid) {
        return this.extensions != null ? this.extensions.getExtension(oid) : null;
    }

    public Extensions getExtensions() {
        return this.extensions;
    }

    public List getExtensionOIDs() {
        return CertUtils.getExtensionOIDs(this.extensions);
    }

    public Set getCriticalExtensionOIDs() {
        return CertUtils.getCriticalExtensionOIDs(this.extensions);
    }

    public Set getNonCriticalExtensionOIDs() {
        return CertUtils.getNonCriticalExtensionOIDs(this.extensions);
    }

    public boolean[] getIssuerUniqueID() {
        return CertUtils.bitStringToBoolean(this.attrCert.getAcinfo().getIssuerUniqueID());
    }

    public AlgorithmIdentifier getSignatureAlgorithm() {
        return this.attrCert.getSignatureAlgorithm();
    }

    public byte[] getSignature() {
        return this.attrCert.getSignatureValue().getBytes();
    }

    public AttributeCertificate toASN1Structure() {
        return this.attrCert;
    }

    public boolean isValidOn(Date date) {
        AttCertValidityPeriod certValidityPeriod = this.attrCert.getAcinfo().getAttrCertValidityPeriod();
        return !date.before(CertUtils.recoverDate(certValidityPeriod.getNotBeforeTime())) && !date.after(CertUtils.recoverDate(certValidityPeriod.getNotAfterTime()));
    }

    public boolean isSignatureValid(ContentVerifierProvider verifierProvider) throws CertException {
        AttributeCertificateInfo acinfo = this.attrCert.getAcinfo();
        if (!CertUtils.isAlgIdEqual(acinfo.getSignature(), this.attrCert.getSignatureAlgorithm())) {
            throw new CertException("signature invalid - algorithm identifier mismatch");
        } else {
            ContentVerifier verifier;
            try {
                verifier = verifierProvider.get(acinfo.getSignature());
                OutputStream sOut = verifier.getOutputStream();
                DEROutputStream dOut = new DEROutputStream(sOut);
                dOut.writeObject(acinfo);
                sOut.close();
            } catch (Exception var6) {
                Exception e = var6;
                throw new CertException("unable to process signature: " + e.getMessage(), e);
            }

            return verifier.verify(this.attrCert.getSignatureValue().getBytes());
        }
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof X509AttributeCertificateHolder)) {
            return false;
        } else {
            X509AttributeCertificateHolder other = (X509AttributeCertificateHolder) o;
            return this.attrCert.equals(other.attrCert);
        }
    }

    public int hashCode() {
        return this.attrCert.hashCode();
    }
}
