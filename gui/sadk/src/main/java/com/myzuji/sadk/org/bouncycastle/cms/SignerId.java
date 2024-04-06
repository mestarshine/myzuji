package com.myzuji.sadk.org.bouncycastle.cms;

import com.myzuji.sadk.org.bouncycastle.asn1.x500.X500Name;
import com.myzuji.sadk.org.bouncycastle.cert.selector.X509CertificateHolderSelector;
import com.myzuji.sadk.org.bouncycastle.util.Selector;

import java.math.BigInteger;

public class SignerId implements Selector {
    private X509CertificateHolderSelector baseSelector;

    private SignerId(X509CertificateHolderSelector baseSelector) {
        this.baseSelector = baseSelector;
    }

    public SignerId(byte[] subjectKeyId) {
        this((X500Name) null, (BigInteger) null, subjectKeyId);
    }

    public SignerId(X500Name issuer, BigInteger serialNumber) {
        this(issuer, serialNumber, (byte[]) null);
    }

    public SignerId(X500Name issuer, BigInteger serialNumber, byte[] subjectKeyId) {
        this(new X509CertificateHolderSelector(issuer, serialNumber, subjectKeyId));
    }

    public X500Name getIssuer() {
        return this.baseSelector.getIssuer();
    }

    public BigInteger getSerialNumber() {
        return this.baseSelector.getSerialNumber();
    }

    public byte[] getSubjectKeyIdentifier() {
        return this.baseSelector.getSubjectKeyIdentifier();
    }

    public int hashCode() {
        return this.baseSelector.hashCode();
    }

    public boolean equals(Object o) {
        if (!(o instanceof SignerId)) {
            return false;
        } else {
            SignerId id = (SignerId) o;
            return this.baseSelector.equals(id.baseSelector);
        }
    }

    public boolean match(Object obj) {
        return obj instanceof SignerInformation ? ((SignerInformation) obj).getSID().equals(this) : this.baseSelector.match(obj);
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException var2) {
            return new SignerId(this.baseSelector);
        }
    }
}
