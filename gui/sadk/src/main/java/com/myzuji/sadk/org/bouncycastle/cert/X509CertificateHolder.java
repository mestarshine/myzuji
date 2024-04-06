package com.myzuji.sadk.org.bouncycastle.cert;

import com.myzuji.sadk.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Primitive;
import com.myzuji.sadk.org.bouncycastle.asn1.DEROutputStream;
import com.myzuji.sadk.org.bouncycastle.asn1.x500.X500Name;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.*;
import com.myzuji.sadk.org.bouncycastle.operator.ContentVerifier;
import com.myzuji.sadk.org.bouncycastle.operator.ContentVerifierProvider;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class X509CertificateHolder {
    private Certificate x509Certificate;
    private Extensions extensions;

    private static Certificate parseBytes(byte[] certEncoding) throws IOException {
        try {
            return Certificate.getInstance(ASN1Primitive.fromByteArray(certEncoding));
        } catch (ClassCastException var2) {
            ClassCastException e = var2;
            throw new CertIOException("malformed data: " + e.getMessage(), e);
        } catch (IllegalArgumentException var3) {
            IllegalArgumentException e = var3;
            throw new CertIOException("malformed data: " + e.getMessage(), e);
        }
    }

    public X509CertificateHolder(byte[] certEncoding) throws IOException {
        this(parseBytes(certEncoding));
    }

    public X509CertificateHolder(Certificate x509Certificate) {
        this.x509Certificate = x509Certificate;
        this.extensions = x509Certificate.getTBSCertificate().getExtensions();
    }

    public int getVersionNumber() {
        return this.x509Certificate.getVersionNumber();
    }


    public int getVersion() {
        return this.x509Certificate.getVersionNumber();
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

    public BigInteger getSerialNumber() {
        return this.x509Certificate.getSerialNumber().getValue();
    }

    public X500Name getIssuer() {
        return X500Name.getInstance(this.x509Certificate.getIssuer());
    }

    public X500Name getSubject() {
        return X500Name.getInstance(this.x509Certificate.getSubject());
    }

    public Date getNotBefore() {
        return this.x509Certificate.getStartDate().getDate();
    }

    public Date getNotAfter() {
        return this.x509Certificate.getEndDate().getDate();
    }

    public SubjectPublicKeyInfo getSubjectPublicKeyInfo() {
        return this.x509Certificate.getSubjectPublicKeyInfo();
    }

    public Certificate toASN1Structure() {
        return this.x509Certificate;
    }

    public AlgorithmIdentifier getSignatureAlgorithm() {
        return this.x509Certificate.getSignatureAlgorithm();
    }

    public byte[] getSignature() {
        return this.x509Certificate.getSignature().getBytes();
    }

    public boolean isValidOn(Date date) {
        return !date.before(this.x509Certificate.getStartDate().getDate()) && !date.after(this.x509Certificate.getEndDate().getDate());
    }

    public boolean isSignatureValid(ContentVerifierProvider verifierProvider) throws CertException {
        TBSCertificate tbsCert = this.x509Certificate.getTBSCertificate();
        if (!CertUtils.isAlgIdEqual(tbsCert.getSignature(), this.x509Certificate.getSignatureAlgorithm())) {
            throw new CertException("signature invalid - algorithm identifier mismatch");
        } else {
            ContentVerifier verifier;
            try {
                verifier = verifierProvider.get(tbsCert.getSignature());
                OutputStream sOut = verifier.getOutputStream();
                DEROutputStream dOut = new DEROutputStream(sOut);
                dOut.writeObject(tbsCert);
                sOut.close();
            } catch (Exception var6) {
                Exception e = var6;
                throw new CertException("unable to process signature: " + e.getMessage(), e);
            }

            return verifier.verify(this.x509Certificate.getSignature().getBytes());
        }
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof X509CertificateHolder)) {
            return false;
        } else {
            X509CertificateHolder other = (X509CertificateHolder) o;
            return this.x509Certificate.equals(other.x509Certificate);
        }
    }

    public int hashCode() {
        return this.x509Certificate.hashCode();
    }

    public byte[] getEncoded() throws IOException {
        return this.x509Certificate.getEncoded();
    }
}
