package com.myzuji.sadk.org.bouncycastle.cert;

import com.myzuji.sadk.org.bouncycastle.asn1.ASN1InputStream;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.DEROutputStream;
import com.myzuji.sadk.org.bouncycastle.asn1.x500.X500Name;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.*;
import com.myzuji.sadk.org.bouncycastle.operator.ContentVerifier;
import com.myzuji.sadk.org.bouncycastle.operator.ContentVerifierProvider;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.*;

public class X509CRLHolder {
    private CertificateList x509CRL;
    private boolean isIndirect;
    private Extensions extensions;
    private GeneralNames issuerName;

    private static CertificateList parseStream(InputStream stream) throws IOException {
        try {
            return CertificateList.getInstance((new ASN1InputStream(stream, true)).readObject());
        } catch (ClassCastException var2) {
            ClassCastException e = var2;
            throw new CertIOException("malformed data: " + e.getMessage(), e);
        } catch (IllegalArgumentException var3) {
            IllegalArgumentException e = var3;
            throw new CertIOException("malformed data: " + e.getMessage(), e);
        }
    }

    private static boolean isIndirectCRL(Extensions extensions) {
        if (extensions == null) {
            return false;
        } else {
            Extension ext = extensions.getExtension(Extension.issuingDistributionPoint);
            return ext != null && IssuingDistributionPoint.getInstance(ext.getParsedValue()).isIndirectCRL();
        }
    }

    public X509CRLHolder(byte[] crlEncoding) throws IOException {
        this(parseStream(new ByteArrayInputStream(crlEncoding)));
    }

    public X509CRLHolder(InputStream crlStream) throws IOException {
        this(parseStream(crlStream));
    }

    public X509CRLHolder(CertificateList x509CRL) {
        this.x509CRL = x509CRL;
        this.extensions = x509CRL.getTBSCertList().getExtensions();
        this.isIndirect = isIndirectCRL(this.extensions);
        this.issuerName = new GeneralNames(new GeneralName(x509CRL.getIssuer()));
    }

    public byte[] getEncoded() throws IOException {
        return this.x509CRL.getEncoded();
    }

    public X500Name getIssuer() {
        return X500Name.getInstance(this.x509CRL.getIssuer());
    }

    public X509CRLEntryHolder getRevokedCertificate(BigInteger serialNumber) {
        GeneralNames currentCA = this.issuerName;
        Enumeration en = this.x509CRL.getRevokedCertificateEnumeration();

        while (en.hasMoreElements()) {
            TBSCertList.CRLEntry entry = (TBSCertList.CRLEntry) en.nextElement();
            if (entry.getUserCertificate().getValue().equals(serialNumber)) {
                return new X509CRLEntryHolder(entry, this.isIndirect, currentCA);
            }

            if (this.isIndirect && entry.hasExtensions()) {
                Extension currentCaName = entry.getExtensions().getExtension(Extension.certificateIssuer);
                if (currentCaName != null) {
                    currentCA = GeneralNames.getInstance(currentCaName.getParsedValue());
                }
            }
        }

        return null;
    }

    public Collection getRevokedCertificates() {
        TBSCertList.CRLEntry[] entries = this.x509CRL.getRevokedCertificates();
        List l = new ArrayList(entries.length);
        GeneralNames currentCA = this.issuerName;

        X509CRLEntryHolder crlEntry;
        for (Enumeration en = this.x509CRL.getRevokedCertificateEnumeration(); en.hasMoreElements(); currentCA = crlEntry.getCertificateIssuer()) {
            TBSCertList.CRLEntry entry = (TBSCertList.CRLEntry) en.nextElement();
            crlEntry = new X509CRLEntryHolder(entry, this.isIndirect, currentCA);
            l.add(crlEntry);
        }

        return l;
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

    public CertificateList toASN1Structure() {
        return this.x509CRL;
    }

    public boolean isSignatureValid(ContentVerifierProvider verifierProvider) throws CertException {
        TBSCertList tbsCRL = this.x509CRL.getTBSCertList();
        if (!CertUtils.isAlgIdEqual(tbsCRL.getSignature(), this.x509CRL.getSignatureAlgorithm())) {
            throw new CertException("signature invalid - algorithm identifier mismatch");
        } else {
            ContentVerifier verifier;
            try {
                verifier = verifierProvider.get(tbsCRL.getSignature());
                OutputStream sOut = verifier.getOutputStream();
                DEROutputStream dOut = new DEROutputStream(sOut);
                dOut.writeObject(tbsCRL);
                sOut.close();
            } catch (Exception var6) {
                Exception e = var6;
                throw new CertException("unable to process signature: " + e.getMessage(), e);
            }

            return verifier.verify(this.x509CRL.getSignature().getBytes());
        }
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof X509CRLHolder)) {
            return false;
        } else {
            X509CRLHolder other = (X509CRLHolder) o;
            return this.x509CRL.equals(other.x509CRL);
        }
    }

    public int hashCode() {
        return this.x509CRL.hashCode();
    }
}
