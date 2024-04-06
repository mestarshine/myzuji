package com.myzuji.sadk.algorithm.common;

import com.myzuji.sadk.algorithm.sm2.SM2PublicKey;
import com.myzuji.sadk.algorithm.util.BigIntegerUtil;
import com.myzuji.sadk.asn1.parser.ASN1Node;
import com.myzuji.sadk.asn1.parser.PKCS7SignFileParser;
import com.myzuji.sadk.lib.crypto.jni.JNISoftLib;
import com.myzuji.sadk.lib.crypto.jni.Session;
import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.ContentInfo;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.IssuerAndSerialNumber;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.SignedData;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.SignerInfo;
import com.myzuji.sadk.org.bouncycastle.asn1.x500.X500Name;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.Certificate;
import com.myzuji.sadk.signature.rsa.RSASignUtil;
import com.myzuji.sadk.signature.sm2.SM2HashUtil;
import com.myzuji.sadk.signature.sm2.SM2PackageUtil;
import com.myzuji.sadk.signature.sm2.SM2SignerInfo;
import com.myzuji.sadk.system.Mechanisms;
import com.myzuji.sadk.system.global.SM2ContextConfig;
import com.myzuji.sadk.util.CertUtil;
import com.myzuji.sadk.x509.certificate.X509Cert;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.PublicKey;
import java.util.Enumeration;

public class PKCS7SignedFile {
    private String digestAlgorithm;
    private byte[] signature;
    private byte[] sourceData;
    private X509Cert signerCert;
    private Session session = null;

    public PKCS7SignedFile(Session session) {
        this.session = session;
    }

    public String getDigestAlgorithm() {
        return this.digestAlgorithm;
    }

    public byte[] getSignature() {
        return this.signature;
    }

    public byte[] getSourceData() {
        return this.sourceData;
    }

    public X509Cert getSignerX509Cert() {
        return this.signerCert;
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

    public boolean verifyP7SignedFile(String signFile, String saveSrcFilePath) throws Exception {
        PKCS7SignFileParser parser = new PKCS7SignFileParser(new File(signFile));
        parser.parser();
        ASN1Node certificate_node = parser.getCertificate_node();
        ASN1Set certSet = ASN1Set.getInstance(DERTaggedObject.getInstance(certificate_node.getData()), false);
        return CertUtil.isSM2Cert(new X509Cert(Certificate.getInstance(certSet.getObjectAt(0)))) ? this.verifySM2P7SignedFile(signFile, saveSrcFilePath, true, (byte[]) null, parser) : this.verifyRSAP7SignedFile(signFile, saveSrcFilePath, parser);
    }

    private boolean verifySM2P7SignedFile(String signFile, String saveSrcFilePath, boolean ifZValue, byte[] userId, PKCS7SignFileParser parser) throws Exception {
        try {
            ASN1Node sourceData_node = (ASN1Node) ((ASN1Node) parser.getSourceData_node().childNodes.get(1)).childNodes.get(0);
            if (sourceData_node.childNodes.size() == 1) {
                sourceData_node = (ASN1Node) sourceData_node.childNodes.get(0);
            }

            ASN1Node certificate_node = parser.getCertificate_node();
            ASN1Node singerinfo_node = parser.getSingerinfo_node();
            ASN1Set signerInfos = ASN1Set.getInstance(singerinfo_node.getData());
            ASN1Set certSet = ASN1Set.getInstance(DERTaggedObject.getInstance(certificate_node.getData()), false);
            X509Cert[] certs = new X509Cert[certSet.size()];

            for (int i = 0; i < certSet.size(); ++i) {
                Certificate certStru = Certificate.getInstance(certSet.getObjectAt(i));
                certs[i] = new X509Cert(certStru);
            }

            Enumeration signerEnumer = signerInfos.getObjects();
            if (signerEnumer.hasMoreElements()) {
                SM2SignerInfo signerInfo = SM2SignerInfo.getInstance(signerEnumer.nextElement());
                IssuerAndSerialNumber issuerAndSN = signerInfo.getIssuerAndSerialNumber();
                X509Cert signerCert = this.getSignerCert(certs, issuerAndSN);
                this.signerCert = signerCert;
                if (signerCert == null) {
                    throw new PKIException(PKIException.VERIFY_P7_SIGNEDDATA_CERT_NOTFUND_ERR, PKIException.VERIFY_P7_SIGNEDDATA_CERT_NOTFUND_ERR_DES);
                } else {
                    PublicKey pubKey = signerCert.getPublicKey();
                    byte[] r = BigIntegerUtil.asUnsigned32ByteArray(signerInfo.getEncryptedDigestR().getPositiveValue());
                    byte[] s = BigIntegerUtil.asUnsigned32ByteArray(signerInfo.getEncryptedDigestS().getPositiveValue());
                    byte[] signature = new byte[64];
                    System.arraycopy(r, 0, signature, 0, 32);
                    System.arraycopy(s, 0, signature, 32, 32);
                    this.digestAlgorithm = "SM3";
                    this.signature = signature;
                    this.sourceData = "source file is too big, will not display,please see the sourceFilePath attribute".getBytes("UTF8");
                    byte[] hash = null;
                    long valueStartPos = sourceData_node.valueStartPos;
                    long valueLength = sourceData_node.valueLength;
                    if (ifZValue) {
                        hash = SM2HashUtil.hashFile(true, userId, sourceData_node.f.getAbsolutePath(), valueStartPos, valueLength, pubKey, saveSrcFilePath, this.session);
                    } else {
                        hash = SM2HashUtil.hashFile(false, (byte[]) null, sourceData_node.f.getAbsolutePath(), valueStartPos, valueLength, (PublicKey) null, saveSrcFilePath, this.session);
                    }

                    if (this.session instanceof JNISoftLib) {
                        SM2PublicKey sm2PubKey = (SM2PublicKey) pubKey;
                        byte[] pubX = sm2PubKey.getPubXByBytes();
                        byte[] pubY = sm2PubKey.getPubYByBytes();
                        return SM2PackageUtil.verifyByJNI(hash, signature, pubX, pubY);
                    } else {
                        return SM2PackageUtil.verifyByBC(hash, signature, pubKey);
                    }
                }
            } else {
                return false;
            }
        } catch (Exception var28) {
            Exception ex = var28;
            throw new PKIException(PKIException.PARSE_P7_SIGNEDDATA_ERR, PKIException.VERIFY_P7_SIGNEDDATA_ERR_DES, ex);
        }
    }

    private boolean verifyRSAP7SignedFile(String signFile, String saveSrcFilePath, PKCS7SignFileParser parser) throws Exception {
        try {
            ASN1Node sourceData_node = (ASN1Node) ((ASN1Node) parser.getSourceData_node().childNodes.get(1)).childNodes.get(0);
            if (sourceData_node.childNodes.size() == 1) {
                sourceData_node = (ASN1Node) sourceData_node.childNodes.get(0);
            }

            ASN1Node certificate_node = parser.getCertificate_node();
            ASN1Node singerinfo_node = parser.getSingerinfo_node();
            ASN1Set signerInfos = ASN1Set.getInstance(singerinfo_node.getData());
            ASN1Set certSet = ASN1Set.getInstance(DERTaggedObject.getInstance(certificate_node.getData()), false);
            X509Cert[] certs = new X509Cert[certSet.size()];

            for (int i = 0; i < certSet.size(); ++i) {
                Certificate certStru = Certificate.getInstance(certSet.getObjectAt(i));
                certs[i] = new X509Cert(certStru);
            }

            Enumeration signerEnumer = signerInfos.getObjects();
            if (signerEnumer.hasMoreElements()) {
                SignerInfo signerInfo = SignerInfo.getInstance(signerEnumer.nextElement());
                IssuerAndSerialNumber issuerAndSN = signerInfo.getIssuerAndSerialNumber();
                X509Cert signerCert = this.getSignerCert(certs, issuerAndSN);
                if (signerCert == null) {
                    throw new PKIException(PKIException.VERIFY_P7_SIGNEDDATA_CERT_NOTFUND_ERR, PKIException.VERIFY_P7_SIGNEDDATA_CERT_NOTFUND_ERR_DES);
                } else {
                    this.signerCert = signerCert;
                    ASN1ObjectIdentifier encryptionId = signerInfo.getDigestEncryptionAlgorithm().getAlgorithm();
                    if (!encryptionId.equals(PKCSObjectIdentifiers.rsaEncryption) && !encryptionId.equals(PKCSObjectIdentifiers.md5WithRSAEncryption) && !encryptionId.equals(PKCSObjectIdentifiers.sha1WithRSAEncryption) && !encryptionId.equals(PKCSObjectIdentifiers.sha256WithRSAEncryption) && !encryptionId.equals(PKCSObjectIdentifiers.sha512WithRSAEncryption)) {
                        throw new PKIException(PKIException.UNSUPPORT_ENCRYPT_ALG_SIGNANDENVELOP_ERR, PKIException.UNSUPPORT_SIGNED_ALG_SIGNANDENVELOP_ERR_DES);
                    } else {
                        ASN1ObjectIdentifier digestId = signerInfo.getDigestAlgorithm().getAlgorithm();
                        String signM = Mechanisms.getDigestAlgorithmName(digestId);
                        if (signM == null) {
                            throw new PKIException(PKIException.UNSUPPORT_ENCRYPT_ALG_SIGNANDENVELOP_ERR, PKIException.UNSUPPORT_SIGNED_ALG_SIGNANDENVELOP_ERR_DES);
                        } else {
                            this.digestAlgorithm = signM;
                            Key pubKey = signerCert.getPublicKey();
                            byte[] signature = signerInfo.getEncryptedDigest().getOctets();
                            this.signature = signature;
                            this.sourceData = "source file is too big, will not display".getBytes("UTF8");
                            return RSASignUtil.verifySignFile(signM, pubKey, sourceData_node.f.getAbsolutePath(), sourceData_node.valueStartPos, sourceData_node.valueLength, signature, saveSrcFilePath);
                        }
                    }
                }
            } else {
                return false;
            }
        } catch (Exception var19) {
            Exception ex = var19;
            throw new PKIException(PKIException.PARSE_P7_SIGNEDDATA_ERR, PKIException.VERIFY_P7_SIGNEDDATA_ERR_DES, ex);
        }
    }

    public void packageRSASignedFile(String contentType, String sourceFile, String signFile, byte[] signature, Mechanism mechanism, X509Cert[] certs) throws PKIException {
        try {
            if (certs == null) {
                throw new PKIException(PKIException.NULL_ENCRYPT_CERTS_ERR, PKIException.NULL_ENCRYPT_CERTS_ERR_DES);
            } else {
                DEROctetString encryptedData = new DEROctetString(signature);
                BigInteger sn = certs[0].getSerialNumber();
                X500Name issuer = certs[0].getIssuerX500Name();
                IssuerAndSerialNumber issuerAndSn = new IssuerAndSerialNumber(issuer, sn);
                AlgorithmIdentifier digestEncryptAlgIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, DERNull.INSTANCE);
                AlgorithmIdentifier digestAlgIdentifier = Mechanisms.getDigestAlgIdentifier(mechanism);
                SignerInfo signerInfo = new SignerInfo(new ASN1Integer(1L), issuerAndSn, digestAlgIdentifier, (ASN1Set) null, digestEncryptAlgIdentifier, encryptedData, (ASN1Set) null);
                ContentInfo contentInfo = null;
                PKCS7AttachSourceFile derSourceData = new PKCS7AttachSourceFile(new File(sourceFile));
                if (contentType == null) {
                    contentInfo = new ContentInfo(PKCSObjectIdentifiers.data, derSourceData);
                } else {
                    contentInfo = new ContentInfo(new ASN1ObjectIdentifier(contentType), derSourceData);
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
                File sign_file = new File(signFile);
                if (!sign_file.exists()) {
                    sign_file.createNewFile();
                }

                FileOutputStream fos = new FileOutputStream(signFile);
                DEROutputStream dos = new DEROutputStream(fos);
                dos.writeObject(contentInfoTemp);
                dos.close();
            }
        } catch (PKIException var26) {
            throw var26;
        } catch (Exception var27) {
            Exception e = var27;
            throw new PKIException("build signedFile failure", e);
        }
    }

    public void packageSM2SignedFile(String contentType, String sourceFile, String signFile, byte[] signature, X509Cert[] certs) throws PKIException {
        try {
            if (certs == null) {
                throw new PKIException(PKIException.NULL_ENCRYPT_CERTS_ERR, PKIException.NULL_ENCRYPT_CERTS_ERR_DES);
            } else {
                byte[] s = null;
                byte[] r;
                if ((signature[0] & 128) != 0) {
                    r = new byte[33];
                    r[0] = 0;
                    System.arraycopy(signature, 0, r, 1, 32);
                } else {
                    r = new byte[32];
                    System.arraycopy(signature, 0, r, 0, 32);
                }

                if ((signature[32] & 128) != 0) {
                    s = new byte[33];
                    s[0] = 0;
                    System.arraycopy(signature, 32, s, 1, 32);
                } else {
                    s = new byte[32];
                    System.arraycopy(signature, 32, s, 0, 32);
                }

                DERInteger derR = new DERInteger(r);
                DERInteger derS = new DERInteger(s);
                BigInteger sn = certs[0].getSerialNumber();
                X500Name issuer = certs[0].getIssuerX500Name();
                IssuerAndSerialNumber issuerAndSn = new IssuerAndSerialNumber(issuer, sn);
                AlgorithmIdentifier digestEncryptAlgIdentifier = null;
                AlgorithmIdentifier digestAlgIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.sm3, new DERNull());
                SM2SignerInfo signerInfo;
                if (SM2ContextConfig.getSignFormat() == 3) {
                    digestEncryptAlgIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.SM2_sign, new DERNull());
                    signerInfo = new SM2SignerInfo(new ASN1Integer(1L), issuerAndSn, digestAlgIdentifier, (ASN1Set) null, digestEncryptAlgIdentifier, ASN1Integer.getInstance(derR), ASN1Integer.getInstance(derS), (ASN1Set) null);
                } else if (SM2ContextConfig.getSignFormat() == 2) {
                    digestEncryptAlgIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.sm2Encryption, new DERNull());
                    signerInfo = new SM2SignerInfo(new ASN1Integer(1L), issuerAndSn, digestAlgIdentifier, (ASN1Set) null, digestEncryptAlgIdentifier, ASN1Integer.getInstance(derR), ASN1Integer.getInstance(derS), (ASN1Set) null);
                } else {
                    digestEncryptAlgIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.sm2Encryption, new DERNull());
                    signerInfo = new SM2SignerInfo(new ASN1Integer(1L), issuerAndSn, digestAlgIdentifier, (ASN1Set) null, digestEncryptAlgIdentifier, ASN1Integer.getInstance(derR), ASN1Integer.getInstance(derS), (ASN1Set) null);
                }

                ContentInfo contentInfo = null;
                PKCS7AttachSourceFile derSourceData = new PKCS7AttachSourceFile(new File(sourceFile));
                if (contentType == null) {
                    contentInfo = new ContentInfo(PKCSObjectIdentifiers.sm2Data, derSourceData);
                } else {
                    contentInfo = new ContentInfo(new ASN1ObjectIdentifier(contentType), derSourceData);
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
                File sign_file = new File(signFile);
                if (!sign_file.exists()) {
                    sign_file.createNewFile();
                }

                FileOutputStream fos = new FileOutputStream(signFile);
                DEROutputStream dos = new DEROutputStream(fos);
                dos.writeObject(contentInfoTemp);
                dos.close();
            }
        } catch (PKIException var28) {
            throw var28;
        } catch (Exception e) {
            throw new PKIException("build signedFile failure", e);
        }
    }

    public void packageSignedFile(String contentType, String sourceFilePath, String signFile, byte[] signture, Mechanism mechanism, X509Cert[] certs) throws PKIException {
        if (certs == null) {
            throw new PKIException(PKIException.NULL_ENCRYPT_CERTS_ERR, PKIException.NULL_ENCRYPT_CERTS_ERR_DES);
        } else {
            if (CertUtil.isSM2Cert(certs[0])) {
                this.packageSM2SignedFile(contentType, sourceFilePath, signFile, signture, certs);
            } else {
                this.packageRSASignedFile(contentType, sourceFilePath, signFile, signture, mechanism, certs);
            }

        }
    }
}
