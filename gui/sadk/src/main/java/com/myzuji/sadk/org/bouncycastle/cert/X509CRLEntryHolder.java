package com.myzuji.sadk.org.bouncycastle.cert;

import com.myzuji.sadk.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.Extension;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.Extensions;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.GeneralNames;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.TBSCertList;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class X509CRLEntryHolder {
    private TBSCertList.CRLEntry entry;
    private GeneralNames ca;

    X509CRLEntryHolder(TBSCertList.CRLEntry entry, boolean isIndirect, GeneralNames previousCA) {
        this.entry = entry;
        this.ca = previousCA;
        if (isIndirect && entry.hasExtensions()) {
            Extension currentCaName = entry.getExtensions().getExtension(Extension.certificateIssuer);
            if (currentCaName != null) {
                this.ca = GeneralNames.getInstance(currentCaName.getParsedValue());
            }
        }

    }

    public BigInteger getSerialNumber() {
        return this.entry.getUserCertificate().getValue();
    }

    public Date getRevocationDate() {
        return this.entry.getRevocationDate().getDate();
    }

    public boolean hasExtensions() {
        return this.entry.hasExtensions();
    }

    public GeneralNames getCertificateIssuer() {
        return this.ca;
    }

    public Extension getExtension(ASN1ObjectIdentifier oid) {
        Extensions extensions = this.entry.getExtensions();
        return extensions != null ? extensions.getExtension(oid) : null;
    }

    public Extensions getExtensions() {
        return this.entry.getExtensions();
    }

    public List getExtensionOIDs() {
        return CertUtils.getExtensionOIDs(this.entry.getExtensions());
    }

    public Set getCriticalExtensionOIDs() {
        return CertUtils.getCriticalExtensionOIDs(this.entry.getExtensions());
    }

    public Set getNonCriticalExtensionOIDs() {
        return CertUtils.getNonCriticalExtensionOIDs(this.entry.getExtensions());
    }
}
