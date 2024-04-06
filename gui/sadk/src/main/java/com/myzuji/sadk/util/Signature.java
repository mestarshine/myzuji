package com.myzuji.sadk.util;

import com.myzuji.sadk.algorithm.common.Mechanism;
import com.myzuji.sadk.algorithm.common.PKCS7SignedData;
import com.myzuji.sadk.algorithm.common.PKCS7SignedFile;
import com.myzuji.sadk.algorithm.common.PKIException;
import com.myzuji.sadk.algorithm.util.BigIntegerUtil;
import com.myzuji.sadk.algorithm.util.FileUtil;
import com.myzuji.sadk.asn1.parser.ASN1Parser;
import com.myzuji.sadk.lib.crypto.jni.Session;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1EncodableVector;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Integer;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Sequence;
import com.myzuji.sadk.org.bouncycastle.asn1.DERSequence;
import com.myzuji.sadk.org.bouncycastle.asn1.cms.AttributeTable;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.Attribute;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.Time;
import com.myzuji.sadk.org.bouncycastle.cms.CMSAttributes;
import com.myzuji.sadk.org.bouncycastle.cms.CMSSignedDataParser;
import com.myzuji.sadk.org.bouncycastle.cms.SignerInformation;
import com.myzuji.sadk.org.bouncycastle.cms.SignerInformationStore;
import com.myzuji.sadk.org.bouncycastle.operator.bc.BcDigestCalculatorProvider;
import com.myzuji.sadk.system.global.FileAndBufferConfig;
import com.myzuji.sadk.x509.certificate.X509Cert;

import java.io.*;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class Signature {
    private X509Cert signCert = null;
    private String digestAlgorithm = null;
    private byte[] signature = null;
    private byte[] sourceData = null;

    public Signature() {
    }

    public X509Cert getSignerX509CertFromP7SignData(byte[] p7SignedData) throws PKIException {
        PKCS7SignedData pkcs7SignedData = new PKCS7SignedData((Session) null);
        pkcs7SignedData.loadBase64(p7SignedData);
        return pkcs7SignedData.getSignerX509Cert();
    }

    public byte[] getContentFromP7SignData(byte[] p7SignedData) throws PKIException {
        PKCS7SignedData pkcs7SignedData = new PKCS7SignedData((Session) null);
        pkcs7SignedData.loadBase64(p7SignedData);
        return pkcs7SignedData.getSourceData();
    }

    public String getDigestAlgorithmFromP7SignData(byte[] p7SignedData) throws PKIException {
        PKCS7SignedData pkcs7SignedData = new PKCS7SignedData((Session) null);
        pkcs7SignedData.loadBase64(p7SignedData);
        return pkcs7SignedData.getDigestAlgorithm();
    }

    public byte[] getSourceData() {
        return this.sourceData;
    }

    public byte[] getSignature() {
        return this.signature;
    }

    public String getDigestAlgorithm() {
        return this.digestAlgorithm;
    }

    public X509Cert getSignerCert() {
        return this.signCert;
    }

    private byte[] RStoASN1(byte[] signData) throws PKIException {
        if (signData != null && signData.length == 64) {
            byte[] r = null;
            byte[] s = null;
            if ((signData[0] & 128) != 0) {
                r = new byte[33];
                r[0] = 0;
                System.arraycopy(signData, 0, r, 1, 32);
            } else {
                r = new byte[32];
                System.arraycopy(signData, 0, r, 0, 32);
            }

            if ((signData[32] & 128) != 0) {
                s = new byte[33];
                s[0] = 0;
                System.arraycopy(signData, 32, s, 1, 32);
            } else {
                s = new byte[32];
                System.arraycopy(signData, 32, s, 0, 32);
            }

            ASN1Integer R = new ASN1Integer(r);
            ASN1Integer S = new ASN1Integer(s);
            ASN1EncodableVector rsV = new ASN1EncodableVector();
            rsV.add(R);
            rsV.add(S);
            DERSequence rsSequence = new DERSequence(rsV);

            try {
                return rsSequence.getEncoded();
            } catch (Exception var9) {
                Exception e = var9;
                throw new PKIException("SM2Signature Encoded Failure", e);
            }
        } else {
            return signData;
        }
    }

    private byte[] ASN1toRS(byte[] asn1RS) {
        ASN1Sequence sequence = ASN1Sequence.getInstance(asn1RS);
        ASN1Integer R = (ASN1Integer) sequence.getObjectAt(0);
        ASN1Integer S = (ASN1Integer) sequence.getObjectAt(1);
        byte[] r = BigIntegerUtil.asUnsigned32ByteArray(R.getPositiveValue());
        byte[] s = BigIntegerUtil.asUnsigned32ByteArray(S.getPositiveValue());
        byte[] signature = new byte[64];
        System.arraycopy(r, 0, signature, 0, 32);
        System.arraycopy(s, 0, signature, 32, 32);
        return signature;
    }

    public byte[] p1SignByHash(String signAlg, byte[] hashValue, PrivateKey privateKey, Session session) throws PKIException {
        return Base64.getEncoder().encode(this.RStoASN1(session.signByHash(new Mechanism(signAlg), privateKey, hashValue)));
    }

    public byte[] p7SignByHash(String signAlg, byte[] hashValue, PrivateKey privateKey, X509Cert signCert, Session session) throws PKIException {
        PKCS7SignedData p7 = new PKCS7SignedData(session);
        X509Cert[] certs = new X509Cert[]{signCert};
        Mechanism mechanism = new Mechanism(signAlg);
        byte[] signture = session.signByHash(new Mechanism(signAlg), privateKey, hashValue);
        return Base64.getEncoder().encode(p7.packageSignedData(false, (String) null, (byte[]) null, signture, mechanism, certs));
    }

    public boolean p1VerifyByHash(String signAlg, byte[] hashValue, byte[] base64P1SignedData, PublicKey publicKey, Session session) throws PKIException {
        byte[] signature = this.GetP1SignatureValue(signAlg, base64P1SignedData);
        return session.verifyByHash(new Mechanism(signAlg), publicKey, hashValue, signature);
    }

    public boolean p7VerifyByHash(byte[] hashValue, byte[] base64P7SignedData, Session session) throws PKIException {
        PKCS7SignedData p7 = new PKCS7SignedData(session);
        p7.loadBase64(base64P7SignedData);
        this.signCert = p7.getSignerX509Cert();
        this.digestAlgorithm = p7.getDigestAlgorithm();
        this.signature = p7.getSignature();
        return p7.verifyP7SignedDataByHash(hashValue);
    }

    public byte[] p1SignMessage(String signAlg, byte[] sourceData, PrivateKey privateKey, Session session) throws PKIException {
        return Base64.getEncoder().encode(this.RStoASN1(session.sign(new Mechanism(signAlg), privateKey, sourceData)));
    }

    public byte[] p1SignFile(String signAlg, String sourceFilePath, PrivateKey privateKey, Session session) throws PKIException {
        InputStream fis = null;

        byte[] var20;
        try {
            fis = new FileInputStream(sourceFilePath);
            var20 = Base64.getEncoder().encode(this.RStoASN1(session.sign(new Mechanism(signAlg), privateKey, fis)));
        } catch (PKIException var15) {
            PKIException e = var15;
            throw e;
        } catch (Exception var16) {
            Exception e = var16;
            throw new PKIException("P1File Signed Failure", e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception var17) {
                Exception e = var17;
                throw new PKIException("P1File Signed Failure", e);
            }

        }

        return var20;
    }

    public byte[] p7SignMessageAttach(String signAlg, byte[] sourceData, PrivateKey privateKey, X509Cert signCert, Session session) throws PKIException {
        PKCS7SignedData p7 = new PKCS7SignedData(session);
        X509Cert[] certs = new X509Cert[]{signCert};
        Mechanism mechanism = new Mechanism(signAlg);
        byte[] signture = session.sign(mechanism, privateKey, sourceData);
        return Base64.getEncoder().encode(p7.packageSignedData(true, (String) null, sourceData, signture, mechanism, certs));
    }

    public byte[] p7SignMessageDetach(String signAlg, byte[] sourceData, PrivateKey privateKey, X509Cert signCert, Session session) throws PKIException {
        PKCS7SignedData p7 = new PKCS7SignedData(session);
        X509Cert[] certs = new X509Cert[]{signCert};
        Mechanism mechanism = new Mechanism(signAlg);
        byte[] signture = session.sign(mechanism, privateKey, sourceData);
        return Base64.getEncoder().encode(p7.packageSignedData(false, (String) null, sourceData, signture, mechanism, certs));
    }

    public void p7SignFileAttach(String signAlg, String sourceFilePath, String outFilePath, PrivateKey privateKey, X509Cert signCert, Session session) throws PKIException {
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(sourceFilePath);
            PKCS7SignedFile p7 = new PKCS7SignedFile(session);
            X509Cert[] certs = new X509Cert[]{signCert};
            Mechanism mechanism = new Mechanism(signAlg);
            byte[] signture = session.sign(mechanism, privateKey, fis);
            fis = new FileInputStream(sourceFilePath);
            p7.packageSignedFile((String) null, sourceFilePath, outFilePath, signture, mechanism, certs);
        } catch (PKIException var19) {
            PKIException e = var19;
            throw e;
        } catch (Exception var20) {
            Exception e = var20;
            throw new PKIException("P7SignFileAttach Signed Failure", e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception var21) {
                Exception e = var21;
                throw new PKIException("P7SignFileAttach Signed Failure", e);
            }

        }

    }

    public byte[] p7SignFileDetach(String signAlg, String sourceFilePath, PrivateKey privateKey, X509Cert signCert, Session session) throws PKIException {
        InputStream is = null;

        byte[] var11;
        try {
            PKCS7SignedData p7 = new PKCS7SignedData(session);
            X509Cert[] certs = new X509Cert[]{signCert};
            Mechanism mechanism = new Mechanism(signAlg);
            is = new FileInputStream(sourceFilePath);
            byte[] signture = session.sign(mechanism, privateKey, is);
            var11 = Base64.getEncoder().encode(p7.packageSignedData(false, (String) null, (byte[]) null, signture, mechanism, certs));
        } catch (PKIException var21) {
            PKIException e = var21;
            throw e;
        } catch (Exception var22) {
            Exception e = var22;
            throw new PKIException("P7SignFileDetach Signed Failure", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception var20) {
                    Exception e = var20;
                    throw new PKIException("P7SignFileDetach Signed Failure", e);
                }
            }

        }

        return var11;
    }

    public boolean p1VerifyMessage(String signAlg, byte[] sourceData, byte[] base64P1SignedData, PublicKey publicKey, Session session) throws PKIException {
        byte[] signature = this.GetP1SignatureValue(signAlg, base64P1SignedData);
        return session.verify(new Mechanism(signAlg), publicKey, sourceData, signature);
    }

    public boolean p1VerifyFile(String signAlg, String sourceFilePath, byte[] base64P1SignedData, PublicKey publicKey, Session session) throws PKIException {
        byte[] signature = this.GetP1SignatureValue(signAlg, base64P1SignedData);
        InputStream is = null;

        boolean var22;
        try {
            is = new FileInputStream(sourceFilePath);
            var22 = session.verify(new Mechanism(signAlg), publicKey, is, signature);
        } catch (PKIException var18) {
            PKIException e = var18;
            throw e;
        } catch (Exception var19) {
            Exception e = var19;
            throw new PKIException("P1File Verified Failure", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception var17) {
                    Exception e = var17;
                    throw new PKIException("P1File Verified Failure", e);
                }
            }

        }

        return var22;
    }

    public boolean p7VerifyMessageAttach(byte[] base64P7SignedData, Session session) throws PKIException {
        PKCS7SignedData p7 = new PKCS7SignedData(session);
        p7.loadBase64(base64P7SignedData);
        this.signCert = p7.getSignerX509Cert();
        this.sourceData = p7.getSourceData();
        this.digestAlgorithm = p7.getDigestAlgorithm();
        this.signature = p7.getSignature();
        return p7.verifyP7SignedDataAttach();
    }

    public boolean p7VerifyMessageDetach(byte[] sourceData, byte[] base64P7SignedData, Session session) throws PKIException {
        PKCS7SignedData p7 = new PKCS7SignedData(session);
        p7.loadBase64(base64P7SignedData);
        this.signCert = p7.getSignerX509Cert();
        this.digestAlgorithm = p7.getDigestAlgorithm();
        this.signature = p7.getSignature();
        return p7.verifyP7SignedData(sourceData);
    }

    public boolean p7VerifyFileAttach(String signFilePath, String outSourceFilePath, Session session) throws PKIException {
        try {
            File f = new File(signFilePath);
            boolean ret;
            if (f.length() <= (long) FileAndBufferConfig.SIGN_FILE_SIZE) {
                byte[] temps = FileUtil.getBytesFromFile(signFilePath);
                ret = this.p7VerifyMessageAttach(temps, session);
                if (outSourceFilePath != null && !outSourceFilePath.trim().equals("")) {
                    FileOutputStream fos = new FileOutputStream(outSourceFilePath);
                    FileUtil.writeBytesToFile(this.getSourceData(), fos);
                    fos.close();
                }

                return ret;
            } else {
                PKCS7SignedFile p7 = new PKCS7SignedFile(session);
                ret = p7.verifyP7SignedFile(signFilePath, outSourceFilePath);
                this.signCert = p7.getSignerX509Cert();
                this.digestAlgorithm = p7.getDigestAlgorithm();
                this.signature = p7.getSignature();
                return ret;
            }
        } catch (PKIException var8) {
            PKIException e = var8;
            throw e;
        } catch (Exception var9) {
            Exception e = var9;
            throw new PKIException("P7VerifyFileAttach Verified Failure", e);
        }
    }

    public boolean p7VerifyFileDetach(String sourceFilePath, byte[] base64P7SignedData, Session session) throws PKIException {
        InputStream is = null;

        boolean var6;
        try {
            PKCS7SignedData p7 = new PKCS7SignedData(session);
            p7.loadBase64(base64P7SignedData);
            this.signCert = p7.getSignerX509Cert();
            this.digestAlgorithm = p7.getDigestAlgorithm();
            this.signature = p7.getSignature();
            is = new FileInputStream(sourceFilePath);
            var6 = p7.verifyP7SignedData(is);
        } catch (PKIException var16) {
            PKIException e = var16;
            throw e;
        } catch (Exception var17) {
            Exception e = var17;
            throw new PKIException("P7VerifyFileDetach Verified Failure", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception var15) {
                    Exception e = var15;
                    throw new PKIException("P7VerifyFileDetach Verified Failure", e);
                }
            }

        }

        return var6;
    }

    public String getTimeFromTimeStamp(byte[] base64P7SignedData) throws PKIException {
        if (base64P7SignedData == null) {
            throw new PKIException("base64P7SignedData should not be null");
        } else {
            byte[] encoding = null;
            if (ASN1Parser.isDERSequence(base64P7SignedData)) {
                encoding = base64P7SignedData;
            } else {
                try {
                    encoding = Base64.getDecoder().decode(base64P7SignedData);
                } catch (Exception var15) {
                    Exception e = var15;
                    throw new PKIException("base64P7SignedData required base64", e);
                }
            }

            Exception ex2;
            CMSSignedDataParser sp;
            try {
                sp = new CMSSignedDataParser(new BcDigestCalculatorProvider(), new ByteArrayInputStream(encoding));
            } catch (Exception var14) {
                ex2 = var14;
                throw new PKIException("base64P7SignedData parsed failure", ex2);
            }

            try {
                SignerInformationStore signers = sp.getSignerInfos();
                Collection c = signers.getSigners();
                Iterator it = c.iterator();

                String strSignDate;
                Date signTimeDate;
                SimpleDateFormat format;
                for (strSignDate = null; it.hasNext(); strSignDate = format.format(signTimeDate)) {
                    SignerInformation signer = (SignerInformation) it.next();
                    AttributeTable attributeTable = signer.getSignedAttributes();
                    Attribute signTimeAttri = attributeTable.get(CMSAttributes.signingTime);
                    Time time = Time.getInstance(signTimeAttri.getAttrValues().getObjectAt(0).toASN1Primitive());
                    signTimeDate = time.getDate();
                    format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                }

                sp.close();
                return strSignDate;
            } catch (Exception var16) {
                ex2 = var16;
                throw new PKIException("850914", "解析文件签名失败 " + ex2.getMessage(), ex2);
            }
        }
    }

    private final byte[] GetP1SignatureValue(String signAlg, byte[] signedData) throws PKIException {
        if (signAlg == null) {
            throw new PKIException("P1Verify signAlg  should not be null");
        } else if (signedData != null && signedData.length != 0) {
            String algorithm = signAlg.toUpperCase();
            byte[] encoding;
            if (algorithm.indexOf("RSA") >= 0) {
                if (signedData.length % 64 == 0) {
                    return signedData;
                } else {
                    try {
                        return Base64.getDecoder().decode(signedData);
                    } catch (Exception e) {
                        throw new PKIException("RSAP1Verify signature required base64", e);
                    }
                }
            } else if (algorithm.indexOf("SM2") >= 0) {
                if (signedData.length == 64) {
                    return signedData;
                } else if (ASN1Parser.isDERSequence(signedData)) {
                    return this.ASN1toRS(signedData);
                } else {

                    try {
                        encoding = Base64.getDecoder().decode(signedData);
                    } catch (Exception var7) {
                        Exception e = var7;
                        throw new PKIException("SM2P1Verify signature required base64", e);
                    }

                    if (encoding.length == 64) {
                        return encoding;
                    } else if (ASN1Parser.isDERSequence(encoding)) {
                        return this.ASN1toRS(encoding);
                    } else {
                        throw new PKIException("SM2P1Verify signature format invalid");
                    }
                }
            } else {
                throw new PKIException("P1Verify signAlg invalid: " + signAlg);
            }
        } else {
            throw new PKIException("P1Verify signature  should not be null");
        }
    }
}
