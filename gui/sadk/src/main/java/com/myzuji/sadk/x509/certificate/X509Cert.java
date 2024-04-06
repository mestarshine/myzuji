package com.myzuji.sadk.x509.certificate;

import com.myzuji.sadk.algorithm.common.Mechanism;
import com.myzuji.sadk.algorithm.common.PKIException;
import com.myzuji.sadk.algorithm.util.SM2OIDUtil;
import com.myzuji.sadk.asn1.parser.ASN1Parser;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Integer;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Object;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Sequence;
import com.myzuji.sadk.org.bouncycastle.asn1.x500.X500Name;
import com.myzuji.sadk.org.bouncycastle.asn1.x500.X500NameStyle;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.*;
import com.myzuji.sadk.signature.sm2.SM2SignUtil;
import com.myzuji.sadk.system.FileHelper;
import com.myzuji.sadk.system.Mechanisms;
import com.myzuji.sadk.system.global.HexCharacter;
import com.myzuji.sadk.util.KeyUtil;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Date;

public class X509Cert {
    private static final byte[] headBytes = "-----BEGIN CERTIFICATE-----".getBytes();
    private static final int headLength;
    private static final byte[] endBytes;
    private static final int endLength;
    Certificate cert;

    public X509Cert(byte[] certData) throws PKIException {
        if (certData == null) {
            throw new IllegalArgumentException("null not allowed for parameters@certData");
        } else {
            this.cert = this.certFrom(certData);
        }
    }

    public X509Cert(InputStream certInputStream) throws PKIException {
        if (certInputStream == null) {
            throw new IllegalArgumentException("null not allowed for parameters@inputStream");
        } else {
            byte[] certData;
            try {
                certData = FileHelper.read(certInputStream);
            } catch (IOException var4) {
                IOException e = var4;
                throw new PKIException(PKIException.INIT_CERT, PKIException.INIT_CERT_DES, e);
            }

            this.cert = this.certFrom(certData);
        }
    }

    public X509Cert(String certFilePath) throws PKIException {
        if (certFilePath == null) {
            throw new IllegalArgumentException("null not allowed for parameters@certFilePath");
        } else {
            byte[] certData;
            try {
                certData = FileHelper.read(certFilePath);
            } catch (IOException var4) {
                IOException e = var4;
                throw new PKIException(PKIException.INIT_CERT, PKIException.INIT_CERT_DES, e);
            }

            this.cert = this.certFrom(certData);
        }
    }


    public X509Cert(Certificate certificate) {
        if (certificate == null) {
            throw new IllegalArgumentException("null not allowed for parameters@certificate");
        } else {
            this.cert = certificate;
        }
    }

    private final Certificate certFrom(byte[] certData) throws PKIException {
        Certificate cert = null;

        try {
            byte[] certHead = new byte[headLength];
            byte[] certEnd = new byte[endLength];
            System.arraycopy(certData, 0, certHead, 0, headLength);
            boolean hasHead = Arrays.equals(certHead, headBytes);
            if (hasHead) {
                certData = ASN1Parser.deleteCRLF(certData);
            }

            int certDataLength = certData.length;
            System.arraycopy(certData, certDataLength - endLength, certEnd, 0, endLength);
            boolean hasEnd = Arrays.equals(certEnd, endBytes);
            int datStarter = 0;
            int datLength = 0;
            byte[] certBytes = null;
            if (hasHead && hasEnd) {
                datStarter = headLength;
                datLength = certDataLength - headLength - endLength;
            } else if (!hasHead && hasEnd) {
                datStarter = 0;
                datLength = certDataLength - endLength;
            } else if (hasHead && !hasEnd) {
                datStarter = headLength;
                datLength = certDataLength - headLength;
            } else {
                certBytes = certData;
            }

            if (certBytes == null) {
                certBytes = new byte[datLength];
                System.arraycopy(certData, datStarter, certBytes, 0, certBytes.length);
            }

            ASN1Sequence seq = ASN1Parser.getDERSequenceFrom(certBytes);
            cert = Certificate.getInstance(seq);
        } catch (Exception var12) {
            Exception ex = var12;
            throw new PKIException(PKIException.INIT_CERT, PKIException.INIT_CERT_DES, ex);
        }

        if (cert == null) {
            throw new PKIException(PKIException.INIT_CERT, PKIException.INIT_CERT_DES);
        } else {
            return cert;
        }
    }

    public final Certificate getCertStructure() {
        return this.cert;
    }

    public final byte[] getEncoded() throws PKIException {
        try {
            return this.cert.getEncoded("DER");
        } catch (Exception var2) {
            Exception ex = var2;
            throw new PKIException(PKIException.ENCODED_CERT, PKIException.ENCODED_CERT_DES, ex);
        }
    }

    public final ASN1Integer getVersion() {
        return this.cert.getVersion();
    }

    public String getIssuer() {
        return this.getIssuer(CFCAStyle.INSTANCE);
    }

    public String getIssuer(X500NameStyle style) {
        X500Name x500IssuerName = this.cert.getIssuer();
        X500Name issuer = new X500Name(style, x500IssuerName);
        return issuer.toString();
    }

    public X500Name getIssuerX500Name() {
        return this.cert.getIssuer();
    }

    public String getSubject() {
        return this.getSubject(CFCAStyle.INSTANCE);
    }

    public String getSubject(X500NameStyle style) {
        X500Name x500SubjectName = this.cert.getSubject();
        X500Name subject = new X500Name(style, x500SubjectName);
        return subject.toString();
    }

    public X500Name getSubjectX500Name() {
        return this.cert.getSubject();
    }

    public final Date getNotBefore() {
        return this.cert.getStartDate().getDate();
    }

    public final Date getNotAfter() {
        return this.cert.getEndDate().getDate();
    }

    public final BigInteger getSerialNumber() {
        return this.cert.getSerialNumber().getPositiveValue();
    }

    public final String getStringSerialNumber() {
        byte[] snData = this.getSerialNumber().toByteArray();
        if (snData == null) {
            return "";
        } else {
            int length = snData.length;
            StringBuffer buf = new StringBuffer();

            for (int i = 0; i != length; ++i) {
                int v = snData[i] & 255;
                buf.append(HexCharacter.DIGITS.charAt(v >>> 4));
                buf.append(HexCharacter.DIGITS.charAt(v & 15));
            }

            return buf.toString();
        }
    }

    public final String getSignatureAlgName() {
        return Mechanism.getSignatureAlgName(this.cert.getSignatureAlgorithm());
    }

    public PublicKey getPublicKey() throws PKIException {
        PublicKey publicKey = null;
        SubjectPublicKeyInfo spki = this.cert.getSubjectPublicKeyInfo();
        String keyAlgorithmId = spki.getAlgorithm().getAlgorithm().getId();
        if (SM2OIDUtil.isSm3WithSM2Encryption(this.cert.getSignatureAlgorithm().getAlgorithm())) {
            publicKey = this.getSM2PublicKey(spki);
        } else {
            throw new PKIException(PKIException.SPKI_KEY, PKIException.SPKI_KEY_DES);
        }

        return publicKey;
    }

    private PublicKey getSM2PublicKey(SubjectPublicKeyInfo subjectPublicKeyInfo) throws PKIException {
        byte[] pubData = subjectPublicKeyInfo.getPublicKeyData().getBytes();
        int len = pubData.length;
        if (len == 65) {
            byte[] pubX = new byte[32];
            byte[] pubY = new byte[32];
            System.arraycopy(pubData, 1, pubX, 0, 32);
            System.arraycopy(pubData, 33, pubY, 0, 32);
            return KeyUtil.getSM2PublicKey(pubX, pubY);
        } else {
            throw new PKIException(PKIException.SPKI_KEY, PKIException.SPKI_KEY_DES);
        }
    }

    public boolean verify(PublicKey publicKey) throws PKIException {
        ASN1ObjectIdentifier oid = this.cert.getSignatureAlgorithm().getAlgorithm();
        if (Mechanisms.isSM2WithSM3(oid)) {
            return SM2SignUtil.verify(this.getTBSCertificate(), (byte[]) null, this.getSignature(), publicKey);
        } else {
            throw new PKIException(PKIException.NONSUPPORT_SIGALG, PKIException.NONSUPPORT_SIGALG_DES + ":" + oid.getId());
        }
    }

    public final byte[] getPublicKeyData() throws PKIException {
        return this.cert.getSubjectPublicKeyInfo().getPublicKeyData().getBytes();
    }

    public final byte[] getTBSCertificate() throws PKIException {
        try {
            return this.cert.getTBSCertificate().getEncoded("DER");
        } catch (Exception var2) {
            Exception ex = var2;
            throw new PKIException(PKIException.TBSCERT_BYTES, PKIException.TBSCERT_BYTES_DES, ex);
        }
    }

    public final byte[] getSignature() {
        return this.cert.getSignature().getBytes();
    }

    public final SubjectKeyIdentifier getSubjectKeyIdentifier() throws PKIException {
        SubjectKeyIdentifier subjectKeyIdentifier = null;

        try {
            ASN1Object extension = this.getExtensionData(Extension.subjectKeyIdentifier);
            if (extension != null) {
                subjectKeyIdentifier = SubjectKeyIdentifier.getInstance(extension);
            }

            return subjectKeyIdentifier;
        } catch (Exception var3) {
            Exception e = var3;
            throw new PKIException(PKIException.CONSTRUCT_SUBJECT_KEY_IDENTIFIER_ERR, PKIException.CONSTRUCT_SUBJECT_KEY_IDENTIFIER_ERR_DES, e);
        }
    }

    public final AuthorityKeyIdentifier getAuthorityKeyIdentifier() throws PKIException {
        AuthorityKeyIdentifier authorityKeyIdentifier = null;

        try {
            ASN1Object extension = this.getExtensionData(Extension.authorityKeyIdentifier);
            if (extension != null) {
                authorityKeyIdentifier = AuthorityKeyIdentifier.getInstance(extension);
            }

            return authorityKeyIdentifier;
        } catch (Exception var3) {
            Exception e = var3;
            throw new PKIException(PKIException.CONSTRUCT_AUTHORITY_KEY_IDENTIFIER_ERR, PKIException.CONSTRUCT_AUTHORITY_KEY_IDENTIFIER_ERR_DES, e);
        }
    }

    public final ASN1Object getExtensionData(ASN1ObjectIdentifier oid) throws Exception {
        byte[] extensionValue = this.getExtensionByteData(oid);
        return extensionValue == null ? null : ASN1Parser.parseBytes2DERObj(extensionValue);
    }

    public final Extensions getExtensionsData() {
        return this.cert.getTBSCertificate().getExtensions();
    }

    public final byte[] getExtensionByteData(ASN1ObjectIdentifier oid) throws Exception {
        Extensions extensions = this.cert.getTBSCertificate().getExtensions();
        byte[] extensionValue = null;
        if (extensions != null) {
            Extension extension = extensions.getExtension(oid);
            if (extension != null) {
                extensionValue = extension.getExtnValue().getOctets();
            }
        }

        return extensionValue;
    }

    public final CRLDistPoint getCRLDistributionPoints() throws PKIException {
        CRLDistPoint crlDistributPoint = null;

        try {
            ASN1Object extension = this.getExtensionData(Extension.cRLDistributionPoints);
            if (extension != null) {
                crlDistributPoint = CRLDistPoint.getInstance(extension);
            }

            return crlDistributPoint;
        } catch (Exception var3) {
            Exception e = var3;
            throw new PKIException(PKIException.CONSTRUCT_CRL_DIST_POINT_ERR, PKIException.CONSTRUCT_CRL_DIST_POINT_ERR_DES, e);
        }
    }

    public final BasicConstraints getBasicConstraints() throws PKIException {
        BasicConstraints basicConstraints = null;

        try {
            ASN1Object extension = this.getExtensionData(Extension.basicConstraints);
            if (extension != null) {
                basicConstraints = BasicConstraints.getInstance(extension);
            }

            return basicConstraints;
        } catch (Exception var3) {
            Exception e = var3;
            throw new PKIException(PKIException.CONSTRUCT_BASIC_CONSTRAINTS_ERR, PKIException.CONSTRUCT_BASIC_CONSTRAINTS_ERR_DES, e);
        }
    }

    public int hashCode() {
        int prime = 0;
        int result = 1;
        result = 31 * result + (this.cert == null ? 0 : this.cert.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            X509Cert other = (X509Cert) obj;
            if (this.cert == null) {
                if (other.cert != null) {
                    return false;
                }
            } else if (!this.cert.equals(other.cert)) {
                return false;
            }

            return true;
        }
    }

    static {
        headLength = headBytes.length;
        endBytes = "-----END CERTIFICATE-----".getBytes();
        endLength = endBytes.length;
    }
}
