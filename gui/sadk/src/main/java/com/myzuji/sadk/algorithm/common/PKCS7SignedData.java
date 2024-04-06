package com.myzuji.sadk.algorithm.common;

import com.myzuji.sadk.algorithm.util.BigIntegerUtil;
import com.myzuji.sadk.asn1.parser.ASN1Parser;
import com.myzuji.sadk.lib.crypto.jni.Session;
import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.ContentInfo;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.IssuerAndSerialNumber;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.SignedData;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.SignerInfo;
import com.myzuji.sadk.org.bouncycastle.asn1.x500.X500Name;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.Certificate;
import com.myzuji.sadk.signature.sm2.SM2SignerInfo;
import com.myzuji.sadk.system.Mechanisms;
import com.myzuji.sadk.system.global.SM2ContextConfig;
import com.myzuji.sadk.util.CertUtil;
import com.myzuji.sadk.x509.certificate.X509Cert;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Enumeration;

public class PKCS7SignedData {
    private Session session = null;
    private SignedData signedData = null;
    static final ASN1ObjectIdentifier PKCS7 = new ASN1ObjectIdentifier("1.2.840.113549.1.7");
    public static final String DATA;
    public static final String SIGNED_DATA;
    public static final String ENVELOPED_DATA;
    public static final String SIGNED_ENVELOPED_DATA;
    public static final String DIGESTED_DATA;
    public static final String ENCRYPTED_DATA;

    public PKCS7SignedData(Session session) {
        this.session = session;
    }

    public byte[] packageSM2SignedData(boolean ifAttach, String contentType, byte[] sourceData, byte[] encryptedData, X509Cert[] certs) throws Exception {
        if (certs == null) {
            throw new PKIException(PKIException.NULL_ENCRYPT_CERTS_ERR, PKIException.NULL_ENCRYPT_CERTS_ERR_DES);
        } else if (encryptedData != null && encryptedData.length == 64) {
            byte[] r = null;
            byte[] s = null;
            if ((encryptedData[0] & 128) != 0) {
                r = new byte[33];
                r[0] = 0;
                System.arraycopy(encryptedData, 0, r, 1, 32);
            } else {
                r = new byte[32];
                System.arraycopy(encryptedData, 0, r, 0, 32);
            }

            if ((encryptedData[32] & 128) != 0) {
                s = new byte[33];
                s[0] = 0;
                System.arraycopy(encryptedData, 32, s, 1, 32);
            } else {
                s = new byte[32];
                System.arraycopy(encryptedData, 32, s, 0, 32);
            }

            ASN1Integer derR = new ASN1Integer(r);
            ASN1Integer derS = new ASN1Integer(s);
            BigInteger sn = certs[0].getSerialNumber();
            X500Name issuer = certs[0].getIssuerX500Name();
            IssuerAndSerialNumber issuerAndSn = new IssuerAndSerialNumber(issuer, sn);
            AlgorithmIdentifier digestEncryptAlgIdentifier = null;
            AlgorithmIdentifier digestAlgIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.sm3, DERNull.INSTANCE);
            SM2SignerInfo signerInfo;
            if (SM2ContextConfig.getSignFormat() == 3) {
                digestEncryptAlgIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.SM2_sign, DERNull.INSTANCE);
                signerInfo = new SM2SignerInfo(new ASN1Integer(1L), issuerAndSn, digestAlgIdentifier, (ASN1Set) null, digestEncryptAlgIdentifier, ASN1Integer.getInstance(derR), ASN1Integer.getInstance(derS), (ASN1Set) null);
            } else if (SM2ContextConfig.getSignFormat() == 2) {
                digestEncryptAlgIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.sm2Encryption, DERNull.INSTANCE);
                signerInfo = new SM2SignerInfo(new ASN1Integer(1L), issuerAndSn, digestAlgIdentifier, (ASN1Set) null, digestEncryptAlgIdentifier, ASN1Integer.getInstance(derR), ASN1Integer.getInstance(derS), (ASN1Set) null);
            } else {
                digestEncryptAlgIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.sm2Encryption, DERNull.INSTANCE);
                signerInfo = new SM2SignerInfo(new ASN1Integer(1L), issuerAndSn, digestAlgIdentifier, (ASN1Set) null, digestEncryptAlgIdentifier, ASN1Integer.getInstance(derR), ASN1Integer.getInstance(derS), (ASN1Set) null);
            }

            ContentInfo contentInfo = null;
            if (ifAttach) {
                DEROctetString derSourceData = new DEROctetString(sourceData);
                if (contentType == null) {
                    contentInfo = new ContentInfo(PKCSObjectIdentifiers.sm2Data, derSourceData);
                } else {
                    contentInfo = new ContentInfo(new ASN1ObjectIdentifier(contentType), derSourceData);
                }
            } else if (contentType == null) {
                contentInfo = new ContentInfo(PKCSObjectIdentifiers.sm2Data, (ASN1Encodable) null);
            } else {
                contentInfo = new ContentInfo(new ASN1ObjectIdentifier(contentType), (ASN1Encodable) null);
            }

            ASN1EncodableVector derV = new ASN1EncodableVector();
            derV.add(digestAlgIdentifier);
            ASN1Set digestAlgorithmSets = new DERSet(derV);
            derV = new ASN1EncodableVector();
            derV.add(signerInfo);
            ASN1Set signerInfos = new DERSet(derV);
            ASN1EncodableVector v = new ASN1EncodableVector();

            for (int i = 0; i < certs.length; ++i) {
                Certificate certStruc = certs[i].getCertStructure();
                v.add(certStruc);
            }

            ASN1Set setCert = new BERSet(v);
            SignedData signedData = new SignedData(new ASN1Integer(1L), digestAlgorithmSets, contentInfo, setCert, (ASN1Set) null, signerInfos);
            ContentInfo contentInfoTemp = new ContentInfo(PKCSObjectIdentifiers.sm2SignedData, signedData);
            return ASN1Parser.parseDERObj2Bytes(contentInfoTemp);
        } else {
            throw new Exception("the encrypt data is null or not 64 bytes!");
        }
    }

    public byte[] packageSignedData(boolean ifAttach, String contentType, byte[] sourceData, byte[] encryptData, Mechanism digestAlgorithm, X509Cert[] certs) throws PKIException {
        try {
            if (certs == null) {
                throw new PKIException(PKIException.NULL_ENCRYPT_CERTS_ERR, PKIException.NULL_ENCRYPT_CERTS_ERR_DES);
            } else {
                return CertUtil.isSM2Cert(certs[0]) ? this.packageSM2SignedData(ifAttach, contentType, sourceData, encryptData, certs) : this.packageRSASignedData(ifAttach, contentType, sourceData, encryptData, digestAlgorithm, certs);
            }
        } catch (PKIException var8) {
            PKIException e = var8;
            throw e;
        } catch (Exception var9) {
            Exception e = var9;
            throw new PKIException("build signedData failure", e);
        }
    }

    public void loadDERData(byte[] data) throws PKIException {
        if (data != null && data.length != 0) {
            if (!ASN1Parser.isDERSequence(data)) {
                throw new PKIException("PKCS7SignedData encoding required DERSequence");
            } else {
                ASN1Sequence seq;
                try {
                    seq = ASN1Sequence.getInstance(data);
                } catch (Exception var4) {
                    Exception e = var4;
                    throw new PKIException("PKCS7SignedData decoding failure", e);
                }

                this.load(seq);
            }
        } else {
            throw new PKIException("PKCS7SignedData encoding required not  be null");
        }
    }

    public void loadBase64(byte[] data) throws PKIException {
        if (data != null && data.length != 0) {
            byte[] encoding = null;
            if (ASN1Parser.isDERSequence(data)) {
                encoding = data;
            } else {
                try {
                    encoding = Base64.getDecoder().decode(data);
                } catch (Exception var6) {
                    Exception e = var6;
                    throw new PKIException("PKCS7SignedData encoding required base64", e);
                }
            }

            ASN1Sequence seq;
            try {
                seq = ASN1Sequence.getInstance(encoding);
            } catch (Exception var5) {
                Exception e = var5;
                throw new PKIException("PKCS7SignedData decoding failure", e);
            }

            this.load(seq);
        } else {
            throw new PKIException("PKCS7SignedData encoding required not  be null");
        }
    }

    private final void load(ASN1Sequence seq) throws PKIException {
        SignedData sd = null;

        try {
            ContentInfo contentInfo = ContentInfo.getInstance(seq);
            sd = SignedData.getInstance(contentInfo.getContent());
        } catch (Exception var4) {
            Exception e = var4;
            throw new PKIException(PKIException.PARSE_P7_SIGNEDDATA_ERR, "PKCS7SignedData decoding failure", e);
        }

        this.signedData = sd;
    }

    public SignedData getSignedData() {
        return this.signedData;
    }

    public boolean verifyP7SignedDataAttach() throws PKIException {
        byte[] sourceData = this.getSourceData();
        ASN1Set aset = this.signedData.getSignerInfos();
        return this.isSM2Cert() ? this.verifySM2SignerInfo((byte[]) ((byte[]) sourceData.clone()), true, (byte[]) null, aset, (X509Cert[]) null) : this.verifyRSASignerInfo((byte[]) ((byte[]) sourceData.clone()), aset, (X509Cert[]) null);
    }

    public byte[] getSourceData() throws PKIException {
        ContentInfo contentInfo = this.signedData.getContentInfo();
        byte[] sourceData = null;
        if (!contentInfo.getContentType().equals(PKCSObjectIdentifiers.data) && !contentInfo.getContentType().equals(PKCSObjectIdentifiers.id_ct_TSTInfo) && !contentInfo.getContentType().equals(PKCSObjectIdentifiers.sm2Data)) {
            sourceData = ASN1Parser.parseDERObj2Bytes(contentInfo.getContent().toASN1Primitive());
        } else {
            if (contentInfo.getContent() == null) {
                throw new PKIException(PKIException.PARSE_P7_SIGNEDDATA_ERR, PKIException.VERIFY_P7_SIGNEDDATA_ERR_DES, new Exception("no sourceData to be verify."));
            }

            sourceData = ((ASN1OctetString) contentInfo.getContent()).getOctets();
        }

        return (byte[]) ((byte[]) sourceData.clone());
    }

    private boolean verifySignerInfoByFile(InputStream sourceFileStream, ASN1Set signerInfos, X509Cert[] certs) throws PKIException {
        try {
            if (certs == null) {
                certs = this.getSignerCerts();
            }

            Enumeration signerEnumer = signerInfos.getObjects();
            if (signerEnumer.hasMoreElements()) {
                SignerInfo signerInfo = SignerInfo.getInstance(signerEnumer.nextElement());
                IssuerAndSerialNumber issuerAndSN = signerInfo.getIssuerAndSerialNumber();
                X509Cert signerCert = this.getSignerCert(certs, issuerAndSN);
                if (signerCert == null) {
                    throw new PKIException(PKIException.VERIFY_P7_SIGNEDDATA_CERT_NOTFUND_ERR, PKIException.VERIFY_P7_SIGNEDDATA_CERT_NOTFUND_ERR_DES);
                } else {
                    ASN1ObjectIdentifier encryptionId = signerInfo.getDigestEncryptionAlgorithm().getAlgorithm();
                    if (!encryptionId.equals(PKCSObjectIdentifiers.rsaEncryption) && !encryptionId.equals(PKCSObjectIdentifiers.md5WithRSAEncryption) && !encryptionId.equals(PKCSObjectIdentifiers.sha1WithRSAEncryption) && !encryptionId.equals(PKCSObjectIdentifiers.sha256WithRSAEncryption) && !encryptionId.equals(PKCSObjectIdentifiers.sha512WithRSAEncryption)) {
                        throw new PKIException(PKIException.UNSUPPORT_ENCRYPT_ALG_SIGNANDENVELOP_ERR, PKIException.UNSUPPORT_SIGNED_ALG_SIGNANDENVELOP_ERR_DES);
                    } else {
                        ASN1ObjectIdentifier digestId = signerInfo.getDigestAlgorithm().getAlgorithm();
                        Mechanism signM = Mechanisms.signMechanismRSAFrom(digestId);
                        if (signM == null) {
                            throw new PKIException(PKIException.UNSUPPORT_ENCRYPT_ALG_SIGNANDENVELOP_ERR, PKIException.UNSUPPORT_SIGNED_ALG_SIGNANDENVELOP_ERR_DES);
                        } else {
                            PublicKey pubKey = signerCert.getPublicKey();
                            byte[] signature = signerInfo.getEncryptedDigest().getOctets();
                            return this.session.verify(signM, pubKey, sourceFileStream, signature);
                        }
                    }
                }
            } else {
                return false;
            }
        } catch (Exception var13) {
            Exception ex = var13;
            throw new PKIException(PKIException.PARSE_P7_SIGNEDDATA_ERR, PKIException.VERIFY_P7_SIGNEDDATA_ERR_DES, ex);
        }
    }

    private boolean verifyRSASignerInfo(byte[] content, ASN1Set signerInfos, X509Cert[] certs) throws PKIException {
        try {
            if (certs == null) {
                certs = this.getSignerCerts();
            }

            Enumeration signerEnumer = signerInfos.getObjects();
            if (signerEnumer.hasMoreElements()) {
                SignerInfo signerInfo = SignerInfo.getInstance(signerEnumer.nextElement());
                IssuerAndSerialNumber issuerAndSN = signerInfo.getIssuerAndSerialNumber();
                X509Cert signerCert = this.getSignerCert(certs, issuerAndSN);
                if (signerCert == null) {
                    throw new PKIException(PKIException.VERIFY_P7_SIGNEDDATA_CERT_NOTFUND_ERR, PKIException.VERIFY_P7_SIGNEDDATA_CERT_NOTFUND_ERR_DES);
                } else {
                    ASN1ObjectIdentifier encryptionId = signerInfo.getDigestEncryptionAlgorithm().getAlgorithm();
                    if (!encryptionId.equals(PKCSObjectIdentifiers.rsaEncryption) && !encryptionId.equals(PKCSObjectIdentifiers.md5WithRSAEncryption) && !encryptionId.equals(PKCSObjectIdentifiers.sha1WithRSAEncryption) && !encryptionId.equals(PKCSObjectIdentifiers.sha256WithRSAEncryption) && !encryptionId.equals(PKCSObjectIdentifiers.sha512WithRSAEncryption)) {
                        throw new PKIException(PKIException.UNSUPPORT_ENCRYPT_ALG_SIGNANDENVELOP_ERR, PKIException.UNSUPPORT_SIGNED_ALG_SIGNANDENVELOP_ERR_DES);
                    } else {
                        ASN1ObjectIdentifier digestId = signerInfo.getDigestAlgorithm().getAlgorithm();
                        Mechanism signM = Mechanisms.signMechanismRSAFrom(digestId);
                        if (signM == null) {
                            throw new PKIException(PKIException.UNSUPPORT_ENCRYPT_ALG_SIGNANDENVELOP_ERR, PKIException.UNSUPPORT_SIGNED_ALG_SIGNANDENVELOP_ERR_DES);
                        } else {
                            PublicKey pubKey = signerCert.getPublicKey();
                            byte[] signature = signerInfo.getEncryptedDigest().getOctets();
                            ASN1Set authAttributes = signerInfo.getAuthenticatedAttributes();
                            if (authAttributes != null) {
                                byte[] bAuthAttries = ASN1Parser.parseDERObj2Bytes(authAttributes);
                                return this.session.verify(signM, pubKey, bAuthAttries, signature);
                            } else {
                                return this.session.verify(signM, pubKey, content, signature);
                            }
                        }
                    }
                }
            } else {
                return false;
            }
        } catch (Exception var15) {
            Exception ex = var15;
            throw new PKIException(PKIException.PARSE_P7_SIGNEDDATA_ERR, PKIException.VERIFY_P7_SIGNEDDATA_ERR_DES, ex);
        }
    }

    private boolean verifySM2SignerInfoByFile(InputStream sourceFileStream, boolean ifZValue, byte[] userId, ASN1Set signerInfos, X509Cert[] certs) throws PKIException {
        try {
            if (certs == null) {
                certs = this.getSignerCerts();
            }

            Enumeration signerEnumer = signerInfos.getObjects();
            if (signerEnumer.hasMoreElements()) {
                SM2SignerInfo signerInfo = SM2SignerInfo.getInstance(signerEnumer.nextElement());
                IssuerAndSerialNumber issuerAndSN = signerInfo.getIssuerAndSerialNumber();
                X509Cert signerCert = this.getSM2SignerCert(certs, issuerAndSN);
                if (signerCert == null) {
                    throw new PKIException(PKIException.VERIFY_P7_SIGNEDDATA_CERT_NOTFUND_ERR, PKIException.VERIFY_P7_SIGNEDDATA_CERT_NOTFUND_ERR_DES);
                } else {
                    PublicKey pubKey = signerCert.getPublicKey();
                    byte[] r = BigIntegerUtil.asUnsigned32ByteArray(signerInfo.getEncryptedDigestR().getPositiveValue());
                    byte[] s = BigIntegerUtil.asUnsigned32ByteArray(signerInfo.getEncryptedDigestS().getPositiveValue());
                    byte[] signature = new byte[64];
                    System.arraycopy(r, 0, signature, 0, 32);
                    System.arraycopy(s, 0, signature, 32, 32);
                    return this.session.verify(Mechanisms.M_SM3_SM2, pubKey, sourceFileStream, signature);
                }
            } else {
                return false;
            }
        } catch (Exception var14) {
            Exception ex = var14;
            throw new PKIException(PKIException.PARSE_SM2_SIGNEDDATA_ERR, PKIException.VERIFY_SM2_SIGNEDDATA_ERR_DES, ex);
        }
    }

    private boolean verifySM2SignerInfo(byte[] content, boolean ifZValue, byte[] userId, ASN1Set signerInfos, X509Cert[] certs) throws PKIException {
        try {
            if (certs == null) {
                certs = this.getSignerCerts();
            }

            Enumeration signerEnumer = signerInfos.getObjects();
            if (signerEnumer.hasMoreElements()) {
                SM2SignerInfo signerInfo = SM2SignerInfo.getInstance(signerEnumer.nextElement());
                IssuerAndSerialNumber issuerAndSN = signerInfo.getIssuerAndSerialNumber();
                X509Cert signerCert = this.getSM2SignerCert(certs, issuerAndSN);
                if (signerCert == null) {
                    throw new PKIException(PKIException.VERIFY_P7_SIGNEDDATA_CERT_NOTFUND_ERR, PKIException.VERIFY_P7_SIGNEDDATA_CERT_NOTFUND_ERR_DES);
                } else {
                    PublicKey pubKey = signerCert.getPublicKey();
                    byte[] r = BigIntegerUtil.asUnsigned32ByteArray(signerInfo.getEncryptedDigestR().getPositiveValue());
                    byte[] s = BigIntegerUtil.asUnsigned32ByteArray(signerInfo.getEncryptedDigestS().getPositiveValue());
                    byte[] signature = new byte[64];
                    System.arraycopy(r, 0, signature, 0, 32);
                    System.arraycopy(s, 0, signature, 32, 32);
                    ASN1Set authAttributes = signerInfo.getAuthenticatedAttributes();
                    if (authAttributes != null) {
                        byte[] bAuthAttries = ASN1Parser.parseDERObj2Bytes(authAttributes);
                        return this.session.verify(Mechanisms.M_SM3_SM2, pubKey, bAuthAttries, signature);
                    } else {
                        return this.session.verify(Mechanisms.M_SM3_SM2, pubKey, content, signature);
                    }
                }
            } else {
                return false;
            }
        } catch (Exception var16) {
            Exception ex = var16;
            throw new PKIException(PKIException.PARSE_SM2_SIGNEDDATA_ERR, PKIException.VERIFY_SM2_SIGNEDDATA_ERR_DES, ex);
        }
    }

    private boolean isSM2Cert() {
        ASN1Set certSet = this.signedData.getCertificates();
        Certificate certStru = Certificate.getInstance(certSet.getObjectAt(0));
        return CertUtil.isSM2Cert(new X509Cert(certStru));
    }

    private X509Cert[] getSignerCerts() throws PKIException {
        ASN1Set certSet = this.signedData.getCertificates();
        X509Cert[] signerCerts = new X509Cert[certSet.size()];

        for (int i = 0; i < certSet.size(); ++i) {
            Certificate certStru = Certificate.getInstance(certSet.getObjectAt(i));
            signerCerts[i] = new X509Cert(certStru);
        }

        return signerCerts;
    }

    public X509Cert getSignerX509Cert() throws PKIException {
        X509Cert signerCerts = null;
        ASN1Set certSet = this.signedData.getCertificates();
        Certificate certStru = Certificate.getInstance(certSet.getObjectAt(0));
        signerCerts = new X509Cert(certStru);
        return signerCerts;
    }

    private X509Cert getSignerCert(X509Cert[] certs, IssuerAndSerialNumber issuerAndSN) throws PKIException {
        String issuer = issuerAndSN.getName().toString();
        BigInteger sn = issuerAndSN.getCertificateSerialNumber().getValue();

        for (int i = 0; i < certs.length; ++i) {
            X500Name tmpIssuer = certs[i].getIssuerX500Name();
            BigInteger tmpSN = certs[i].getSerialNumber();
            if (tmpIssuer.toString().equals(issuer) && tmpSN.compareTo(sn) == 0) {
                return certs[i];
            }
        }

        return null;
    }

    private X509Cert getSM2SignerCert(X509Cert[] certs, IssuerAndSerialNumber issuerAndSN) throws PKIException {
        String issuer = issuerAndSN.getName().toString();
        BigInteger sn = issuerAndSN.getCertificateSerialNumber().getValue();

        for (int i = 0; i < certs.length; ++i) {
            X500Name tmpIssuer = certs[i].getIssuerX500Name();
            BigInteger tmpSN = certs[i].getSerialNumber();
            if (tmpIssuer.toString().equals(issuer) && tmpSN.compareTo(sn) == 0) {
                return certs[i];
            }
        }

        return null;
    }

    public boolean verifyP7SignedData(InputStream sourceFileStream) throws PKIException {
        ASN1Set aset = this.signedData.getSignerInfos();
        return this.isSM2Cert() ? this.verifySM2SignerInfoByFile(sourceFileStream, true, (byte[]) null, aset, (X509Cert[]) null) : this.verifySignerInfoByFile(sourceFileStream, aset, (X509Cert[]) null);
    }

    public boolean verifyP7SignedData(byte[] sourceData) throws PKIException {
        ASN1Set aset = this.signedData.getSignerInfos();
        return this.isSM2Cert() ? this.verifySM2SignerInfo(sourceData, true, (byte[]) null, aset, (X509Cert[]) null) : this.verifyRSASignerInfo(sourceData, aset, (X509Cert[]) null);
    }

    private byte[] getSM2Signature() throws PKIException {
        ASN1Set signerInfos = this.signedData.getSignerInfos();
        Enumeration signerEnumer = signerInfos.getObjects();
        if (signerEnumer.hasMoreElements()) {
            SM2SignerInfo signerInfo = SM2SignerInfo.getInstance(signerEnumer.nextElement());
            byte[] r = BigIntegerUtil.asUnsigned32ByteArray(signerInfo.getEncryptedDigestR().getPositiveValue());
            byte[] s = BigIntegerUtil.asUnsigned32ByteArray(signerInfo.getEncryptedDigestS().getPositiveValue());
            byte[] signature = new byte[64];
            System.arraycopy(r, 0, signature, 0, 32);
            System.arraycopy(s, 0, signature, 32, 32);
            return signature;
        } else {
            throw new PKIException("can not get SM2SignerInfo object!!!");
        }
    }

    public byte[] getSignature() throws PKIException {
        if (this.isSM2Cert()) {
            return this.getSM2Signature();
        } else {
            ASN1Set signerInfos = this.signedData.getSignerInfos();
            Enumeration signerEnumer = signerInfos.getObjects();
            if (signerEnumer.hasMoreElements()) {
                SignerInfo signerInfo = SignerInfo.getInstance(signerEnumer.nextElement());
                return signerInfo.getEncryptedDigest().getOctets();
            } else {
                throw new PKIException("can not get SignerInfo object!!!");
            }
        }
    }

    public String getDigestAlgorithm() throws PKIException {
        if (this.isSM2Cert()) {
            return "SM3";
        } else {
            ASN1Set signerInfos = this.signedData.getSignerInfos();
            Enumeration signerEnumer = signerInfos.getObjects();
            String signM = null;
            if (signerEnumer.hasMoreElements()) {
                SignerInfo signerInfo = SignerInfo.getInstance(signerEnumer.nextElement());
                ASN1ObjectIdentifier digestId = signerInfo.getDigestAlgorithm().getAlgorithm();
                signM = Mechanisms.getDigestAlgorithmName(digestId);
                if (signM == null) {
                    throw new PKIException(PKIException.UNSUPPORT_ENCRYPT_ALG_SIGNANDENVELOP_ERR, PKIException.UNSUPPORT_SIGNED_ALG_SIGNANDENVELOP_ERR_DES);
                } else {
                    return signM;
                }
            } else {
                throw new PKIException("can not get SignerInfo object!!!");
            }
        }
    }

    public boolean verifySM2SignedData(InputStream sourceStream, boolean ifZValue, byte[] userId) throws PKIException {
        ASN1Set aset = this.signedData.getSignerInfos();
        if (ifZValue) {
            return userId != null ? this.verifySM2SignerInfoByFile(sourceStream, true, (byte[]) ((byte[]) userId.clone()), aset, (X509Cert[]) null) : this.verifySM2SignerInfoByFile(sourceStream, true, (byte[]) null, aset, (X509Cert[]) null);
        } else {
            return this.verifySM2SignerInfoByFile(sourceStream, false, (byte[]) null, aset, (X509Cert[]) null);
        }
    }

    public boolean verifySM2SignedData(byte[] sourceData, boolean ifZValue, byte[] userId) throws PKIException {
        ASN1Set aset = this.signedData.getSignerInfos();
        if (ifZValue) {
            return userId != null ? this.verifySM2SignerInfo((byte[]) ((byte[]) sourceData.clone()), true, (byte[]) ((byte[]) userId.clone()), aset, (X509Cert[]) null) : this.verifySM2SignerInfo((byte[]) ((byte[]) sourceData.clone()), true, (byte[]) null, aset, (X509Cert[]) null);
        } else {
            return this.verifySM2SignerInfo((byte[]) ((byte[]) sourceData.clone()), false, (byte[]) null, aset, (X509Cert[]) null);
        }
    }

    public byte[] packageRSASignedData(boolean ifAttach, String contentType, byte[] sourceData, byte[] encryptData, Mechanism mechanism, X509Cert[] certs) throws PKIException {
        if (certs == null) {
            throw new PKIException(PKIException.NULL_ENCRYPT_CERTS_ERR, PKIException.NULL_ENCRYPT_CERTS_ERR_DES);
        } else {
            DEROctetString encryptedData = new DEROctetString(encryptData);
            BigInteger sn = certs[0].getSerialNumber();
            X500Name issuer = certs[0].getIssuerX500Name();
            IssuerAndSerialNumber issuerAndSn = new IssuerAndSerialNumber(issuer, sn);
            AlgorithmIdentifier digestEncryptAlgIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, DERNull.INSTANCE);
            AlgorithmIdentifier digestAlgIdentifier = Mechanisms.getDigestAlgIdentifier(mechanism);
            if (digestAlgIdentifier == null) {
                throw new PKIException("Invalid DigestAlgIdentifier: " + mechanism);
            } else {
                SignerInfo signerInfo = new SignerInfo(new ASN1Integer(1L), issuerAndSn, digestAlgIdentifier, (ASN1Set) null, digestEncryptAlgIdentifier, encryptedData, (ASN1Set) null);
                ContentInfo contentInfo = null;
                if (ifAttach) {
                    DEROctetString derSourceData = new DEROctetString(sourceData);
                    if (contentType == null) {
                        contentInfo = new ContentInfo(PKCSObjectIdentifiers.data, derSourceData);
                    } else {
                        contentInfo = new ContentInfo(new ASN1ObjectIdentifier(contentType), derSourceData);
                    }
                } else if (contentType == null) {
                    contentInfo = new ContentInfo(PKCSObjectIdentifiers.data, (ASN1Encodable) null);
                } else {
                    contentInfo = new ContentInfo(new ASN1ObjectIdentifier(contentType), (ASN1Encodable) null);
                }

                ASN1EncodableVector derV = new ASN1EncodableVector();
                derV.add(digestAlgIdentifier);
                ASN1Set digestAlgorithmSets = new BERSet(derV);
                derV = new ASN1EncodableVector();
                derV.add(signerInfo);
                ASN1Set signerInfos = new DERSet(derV);
                ASN1EncodableVector v = new ASN1EncodableVector();

                for (int i = 0; i < certs.length; ++i) {
                    Certificate certStruc = certs[i].getCertStructure();
                    v.add(certStruc);
                }

                ASN1Set setCert = new BERSet(v);
                SignedData signedData = new SignedData(new ASN1Integer(1L), digestAlgorithmSets, contentInfo, setCert, (ASN1Set) null, signerInfos);
                ContentInfo contentInfoTemp = new ContentInfo(PKCSObjectIdentifiers.signedData, signedData);
                return ASN1Parser.parseDERObj2Bytes(contentInfoTemp);
            }
        }
    }

    public boolean verifyP7SignedDataByHash(byte[] digest) throws PKIException {
        ASN1Set aset = this.signedData.getSignerInfos();
        return this.isSM2Cert() ? this.verifySM2SignerInfoByHash(digest, aset, (X509Cert[]) null) : this.verifySignerInfoByHash(digest, aset, (X509Cert[]) null);
    }

    private boolean verifySignerInfoByHash(byte[] digest, ASN1Set signerInfos, X509Cert[] certs) throws PKIException {
        try {
            if (certs == null) {
                certs = this.getSignerCerts();
            }

            Enumeration signerEnumer = signerInfos.getObjects();
            if (signerEnumer.hasMoreElements()) {
                SignerInfo signerInfo = SignerInfo.getInstance(signerEnumer.nextElement());
                IssuerAndSerialNumber issuerAndSN = signerInfo.getIssuerAndSerialNumber();
                X509Cert signerCert = this.getSignerCert(certs, issuerAndSN);
                if (signerCert == null) {
                    throw new PKIException(PKIException.VERIFY_P7_SIGNEDDATA_CERT_NOTFUND_ERR, PKIException.VERIFY_P7_SIGNEDDATA_CERT_NOTFUND_ERR_DES);
                } else {
                    ASN1ObjectIdentifier encryptionId = signerInfo.getDigestEncryptionAlgorithm().getAlgorithm();
                    if (!encryptionId.equals(PKCSObjectIdentifiers.rsaEncryption) && !encryptionId.equals(PKCSObjectIdentifiers.md5WithRSAEncryption) && !encryptionId.equals(PKCSObjectIdentifiers.sha1WithRSAEncryption) && !encryptionId.equals(PKCSObjectIdentifiers.sha256WithRSAEncryption) && !encryptionId.equals(PKCSObjectIdentifiers.sha512WithRSAEncryption)) {
                        throw new PKIException(PKIException.UNSUPPORT_ENCRYPT_ALG_SIGNANDENVELOP_ERR, PKIException.UNSUPPORT_SIGNED_ALG_SIGNANDENVELOP_ERR_DES);
                    } else {
                        ASN1ObjectIdentifier digestId = signerInfo.getDigestAlgorithm().getAlgorithm();
                        Mechanism signM = Mechanisms.signMechanismRSAFrom(digestId);
                        if (signM == null) {
                            throw new PKIException(PKIException.UNSUPPORT_ENCRYPT_ALG_SIGNANDENVELOP_ERR, PKIException.UNSUPPORT_SIGNED_ALG_SIGNANDENVELOP_ERR_DES);
                        } else {
                            PublicKey pubKey = signerCert.getPublicKey();
                            byte[] signature = signerInfo.getEncryptedDigest().getOctets();
                            return this.session.verifyByHash(signM, pubKey, digest, signature);
                        }
                    }
                }
            } else {
                return false;
            }
        } catch (Exception var13) {
            Exception ex = var13;
            throw new PKIException(PKIException.PARSE_P7_SIGNEDDATA_ERR, PKIException.VERIFY_P7_SIGNEDDATA_ERR_DES, ex);
        }
    }

    private boolean verifySM2SignerInfoByHash(byte[] digest, ASN1Set signerInfos, X509Cert[] certs) throws PKIException {
        try {
            if (certs == null) {
                certs = this.getSignerCerts();
            }

            Enumeration signerEnumer = signerInfos.getObjects();
            if (signerEnumer.hasMoreElements()) {
                SM2SignerInfo signerInfo = SM2SignerInfo.getInstance(signerEnumer.nextElement());
                IssuerAndSerialNumber issuerAndSN = signerInfo.getIssuerAndSerialNumber();
                X509Cert signerCert = this.getSM2SignerCert(certs, issuerAndSN);
                if (signerCert == null) {
                    throw new PKIException(PKIException.VERIFY_P7_SIGNEDDATA_CERT_NOTFUND_ERR, PKIException.VERIFY_P7_SIGNEDDATA_CERT_NOTFUND_ERR_DES);
                } else {
                    PublicKey pubKey = signerCert.getPublicKey();
                    byte[] r = BigIntegerUtil.asUnsigned32ByteArray(signerInfo.getEncryptedDigestR().getPositiveValue());
                    byte[] s = BigIntegerUtil.asUnsigned32ByteArray(signerInfo.getEncryptedDigestS().getPositiveValue());
                    byte[] signature = new byte[64];
                    System.arraycopy(r, 0, signature, 0, 32);
                    System.arraycopy(s, 0, signature, 32, 32);
                    return this.session.verifyByHash(Mechanisms.M_SM3_SM2, pubKey, digest, signature);
                }
            } else {
                return false;
            }
        } catch (Exception var12) {
            Exception ex = var12;
            throw new PKIException(PKIException.PARSE_SM2_SIGNEDDATA_ERR, PKIException.VERIFY_SM2_SIGNEDDATA_ERR_DES, ex);
        }
    }

    static {
        DATA = PKCS7.branch("1").getId();
        SIGNED_DATA = PKCS7.branch("2").getId();
        ENVELOPED_DATA = PKCS7.branch("3").getId();
        SIGNED_ENVELOPED_DATA = PKCS7.branch("4").getId();
        DIGESTED_DATA = PKCS7.branch("5").getId();
        ENCRYPTED_DATA = PKCS7.branch("6").getId();
    }
}
