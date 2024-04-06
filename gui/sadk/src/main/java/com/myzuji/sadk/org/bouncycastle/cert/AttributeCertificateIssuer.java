package com.myzuji.sadk.org.bouncycastle.cert;

import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Encodable;
import com.myzuji.sadk.org.bouncycastle.asn1.x500.X500Name;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.AttCertIssuer;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.GeneralName;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.GeneralNames;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.V2Form;
import com.myzuji.sadk.org.bouncycastle.util.Selector;

import java.util.ArrayList;
import java.util.List;

public class AttributeCertificateIssuer implements Selector {
    final ASN1Encodable form;

    public AttributeCertificateIssuer(AttCertIssuer issuer) {
        this.form = issuer.getIssuer();
    }

    public AttributeCertificateIssuer(X500Name principal) {
        this.form = new V2Form(new GeneralNames(new GeneralName(principal)));
    }

    public X500Name[] getNames() {
        GeneralNames name;
        if (this.form instanceof V2Form) {
            name = ((V2Form) this.form).getIssuerName();
        } else {
            name = (GeneralNames) this.form;
        }

        GeneralName[] names = name.getNames();
        List l = new ArrayList(names.length);

        for (int i = 0; i != names.length; ++i) {
            if (names[i].getTagNo() == 4) {
                l.add(X500Name.getInstance(names[i].getName()));
            }
        }

        return (X500Name[]) ((X500Name[]) l.toArray(new X500Name[l.size()]));
    }

    private boolean matchesDN(X500Name subject, GeneralNames targets) {
        GeneralName[] names = targets.getNames();

        for (int i = 0; i != names.length; ++i) {
            GeneralName gn = names[i];
            if (gn.getTagNo() == 4 && X500Name.getInstance(gn.getName()).equals(subject)) {
                return true;
            }
        }

        return false;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException var2) {
            return new AttributeCertificateIssuer(AttCertIssuer.getInstance(this.form));
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof AttributeCertificateIssuer)) {
            return false;
        } else {
            AttributeCertificateIssuer other = (AttributeCertificateIssuer) obj;
            return this.form.equals(other.form);
        }
    }

    public int hashCode() {
        return this.form.hashCode();
    }

    public boolean match(Object obj) {
        if (!(obj instanceof X509CertificateHolder)) {
            return false;
        } else {
            X509CertificateHolder x509Cert = (X509CertificateHolder) obj;
            if (this.form instanceof V2Form) {
                V2Form issuer = (V2Form) this.form;
                if (issuer.getBaseCertificateID() != null) {
                    return issuer.getBaseCertificateID().getSerial().getValue().equals(x509Cert.getSerialNumber()) && this.matchesDN(x509Cert.getIssuer(), issuer.getBaseCertificateID().getIssuer());
                }

                GeneralNames name = issuer.getIssuerName();
                if (this.matchesDN(x509Cert.getSubject(), name)) {
                    return true;
                }
            } else {
                GeneralNames name = (GeneralNames) this.form;
                if (this.matchesDN(x509Cert.getSubject(), name)) {
                    return true;
                }
            }

            return false;
        }
    }
}
