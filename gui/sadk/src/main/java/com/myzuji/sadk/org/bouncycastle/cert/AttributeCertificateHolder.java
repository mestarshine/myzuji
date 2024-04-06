package com.myzuji.sadk.org.bouncycastle.cert;

import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Integer;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Sequence;
import com.myzuji.sadk.org.bouncycastle.asn1.x500.X500Name;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.*;
import com.myzuji.sadk.org.bouncycastle.operator.DigestCalculator;
import com.myzuji.sadk.org.bouncycastle.operator.DigestCalculatorProvider;
import com.myzuji.sadk.org.bouncycastle.util.Arrays;
import com.myzuji.sadk.org.bouncycastle.util.Selector;

import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class AttributeCertificateHolder implements Selector {
    private static DigestCalculatorProvider digestCalculatorProvider;
    final Holder holder;

    AttributeCertificateHolder(ASN1Sequence seq) {
        this.holder = Holder.getInstance(seq);
    }

    public AttributeCertificateHolder(X500Name issuerName, BigInteger serialNumber) {
        this.holder = new Holder(new IssuerSerial(new GeneralNames(new GeneralName(issuerName)), new ASN1Integer(serialNumber)));
    }

    public AttributeCertificateHolder(X509CertificateHolder cert) {
        this.holder = new Holder(new IssuerSerial(this.generateGeneralNames(cert.getIssuer()), new ASN1Integer(cert.getSerialNumber())));
    }

    public AttributeCertificateHolder(X500Name principal) {
        this.holder = new Holder(this.generateGeneralNames(principal));
    }

    public AttributeCertificateHolder(int digestedObjectType, ASN1ObjectIdentifier digestAlgorithm, ASN1ObjectIdentifier otherObjectTypeID, byte[] objectDigest) {
        this.holder = new Holder(new ObjectDigestInfo(digestedObjectType, otherObjectTypeID, new AlgorithmIdentifier(digestAlgorithm), Arrays.clone(objectDigest)));
    }

    public int getDigestedObjectType() {
        return this.holder.getObjectDigestInfo() != null ? this.holder.getObjectDigestInfo().getDigestedObjectType().getValue().intValue() : -1;
    }

    public AlgorithmIdentifier getDigestAlgorithm() {
        return this.holder.getObjectDigestInfo() != null ? this.holder.getObjectDigestInfo().getDigestAlgorithm() : null;
    }

    public byte[] getObjectDigest() {
        return this.holder.getObjectDigestInfo() != null ? this.holder.getObjectDigestInfo().getObjectDigest().getBytes() : null;
    }

    public ASN1ObjectIdentifier getOtherObjectTypeID() {
        if (this.holder.getObjectDigestInfo() != null) {
            new ASN1ObjectIdentifier(this.holder.getObjectDigestInfo().getOtherObjectTypeID().getId());
        }

        return null;
    }

    private GeneralNames generateGeneralNames(X500Name principal) {
        return new GeneralNames(new GeneralName(principal));
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

    private X500Name[] getPrincipals(GeneralName[] names) {
        List l = new ArrayList(names.length);

        for (int i = 0; i != names.length; ++i) {
            if (names[i].getTagNo() == 4) {
                l.add(X500Name.getInstance(names[i].getName()));
            }
        }

        return (X500Name[]) ((X500Name[]) l.toArray(new X500Name[l.size()]));
    }

    public X500Name[] getEntityNames() {
        return this.holder.getEntityName() != null ? this.getPrincipals(this.holder.getEntityName().getNames()) : null;
    }

    public X500Name[] getIssuer() {
        return this.holder.getBaseCertificateID() != null ? this.getPrincipals(this.holder.getBaseCertificateID().getIssuer().getNames()) : null;
    }

    public BigInteger getSerialNumber() {
        return this.holder.getBaseCertificateID() != null ? this.holder.getBaseCertificateID().getSerial().getValue() : null;
    }

    public Object clone() {
        return new AttributeCertificateHolder((ASN1Sequence) this.holder.toASN1Primitive());
    }

    public boolean match(Object obj) {
        if (!(obj instanceof X509CertificateHolder)) {
            return false;
        } else {
            X509CertificateHolder x509Cert = (X509CertificateHolder) obj;
            if (this.holder.getBaseCertificateID() == null) {
                if (this.holder.getEntityName() != null && this.matchesDN(x509Cert.getSubject(), this.holder.getEntityName())) {
                    return true;
                } else {
                    if (this.holder.getObjectDigestInfo() != null) {
                        try {
                            DigestCalculator digCalc = digestCalculatorProvider.get(this.holder.getObjectDigestInfo().getDigestAlgorithm());
                            OutputStream digOut = digCalc.getOutputStream();
                            switch (this.getDigestedObjectType()) {
                                case 0:
                                    digOut.write(x509Cert.getSubjectPublicKeyInfo().getEncoded());
                                    break;
                                case 1:
                                    digOut.write(x509Cert.getEncoded());
                            }

                            digOut.close();
                            if (!Arrays.areEqual(digCalc.getDigest(), this.getObjectDigest())) {
                                return false;
                            }
                        } catch (Exception var5) {
                            return false;
                        }
                    }

                    return false;
                }
            } else {
                return this.holder.getBaseCertificateID().getSerial().getValue().equals(x509Cert.getSerialNumber()) && this.matchesDN(x509Cert.getIssuer(), this.holder.getBaseCertificateID().getIssuer());
            }
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof AttributeCertificateHolder)) {
            return false;
        } else {
            AttributeCertificateHolder other = (AttributeCertificateHolder) obj;
            return this.holder.equals(other.holder);
        }
    }

    public int hashCode() {
        return this.holder.hashCode();
    }

    public static void setDigestCalculatorProvider(DigestCalculatorProvider digCalcProvider) {
        digestCalculatorProvider = digCalcProvider;
    }
}
