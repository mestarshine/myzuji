package com.myzuji.sadk.org.bouncycastle.cert.selector;

import com.myzuji.sadk.org.bouncycastle.asn1.ASN1OctetString;
import com.myzuji.sadk.org.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import com.myzuji.sadk.org.bouncycastle.asn1.x500.X500Name;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.Extension;
import com.myzuji.sadk.org.bouncycastle.cert.X509CertificateHolder;
import com.myzuji.sadk.org.bouncycastle.util.Arrays;
import com.myzuji.sadk.org.bouncycastle.util.Selector;

import java.math.BigInteger;

public class X509CertificateHolderSelector implements Selector {
    private byte[] subjectKeyId;
    private X500Name issuer;
    private BigInteger serialNumber;

    public X509CertificateHolderSelector(byte[] subjectKeyId) {
        this((X500Name) null, (BigInteger) null, subjectKeyId);
    }

    public X509CertificateHolderSelector(X500Name issuer, BigInteger serialNumber) {
        this(issuer, serialNumber, (byte[]) null);
    }

    public X509CertificateHolderSelector(X500Name issuer, BigInteger serialNumber, byte[] subjectKeyId) {
        this.issuer = issuer;
        this.serialNumber = serialNumber;
        this.subjectKeyId = subjectKeyId;
    }

    public X500Name getIssuer() {
        return this.issuer;
    }

    public BigInteger getSerialNumber() {
        return this.serialNumber;
    }

    public byte[] getSubjectKeyIdentifier() {
        return Arrays.clone(this.subjectKeyId);
    }

    public int hashCode() {
        int code = Arrays.hashCode(this.subjectKeyId);
        if (this.serialNumber != null) {
            code ^= this.serialNumber.hashCode();
        }

        if (this.issuer != null) {
            code ^= this.issuer.hashCode();
        }

        return code;
    }

    public boolean equals(Object o) {
        if (!(o instanceof X509CertificateHolderSelector)) {
            return false;
        } else {
            X509CertificateHolderSelector id = (X509CertificateHolderSelector) o;
            return Arrays.areEqual(this.subjectKeyId, id.subjectKeyId) && this.equalsObj(this.serialNumber, id.serialNumber) && this.equalsObj(this.issuer, id.issuer);
        }
    }

    private boolean equalsObj(Object a, Object b) {
        return a != null ? a.equals(b) : b == null;
    }

    public boolean match(Object obj) {
        if (obj instanceof X509CertificateHolder) {
            X509CertificateHolder certHldr = (X509CertificateHolder) obj;
            if (this.getSerialNumber() != null) {
                IssuerAndSerialNumber iAndS = new IssuerAndSerialNumber(certHldr.toASN1Structure());
                return iAndS.getName().equals(this.issuer) && iAndS.getSerialNumber().getValue().equals(this.serialNumber);
            }

            if (this.subjectKeyId != null) {
                Extension ext = certHldr.getExtension(Extension.subjectKeyIdentifier);
                if (ext == null) {
                    return Arrays.areEqual(this.subjectKeyId, MSOutlookKeyIdCalculator.calculateKeyId(certHldr.getSubjectPublicKeyInfo()));
                }

                byte[] subKeyID = ASN1OctetString.getInstance(ext.getParsedValue()).getOctets();
                return Arrays.areEqual(this.subjectKeyId, subKeyID);
            }
        } else if (obj instanceof byte[]) {
            return Arrays.areEqual(this.subjectKeyId, (byte[]) ((byte[]) obj));
        }

        return false;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException var2) {
            return new X509CertificateHolderSelector(this.issuer, this.serialNumber, this.subjectKeyId);
        }
    }
}
