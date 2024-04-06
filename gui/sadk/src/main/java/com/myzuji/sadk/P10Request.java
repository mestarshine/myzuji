package com.myzuji.sadk;

import com.myzuji.sadk.algorithm.common.Mechanism;
import com.myzuji.sadk.algorithm.common.PKCSObjectIdentifiers;
import com.myzuji.sadk.algorithm.common.PKIException;
import com.myzuji.sadk.algorithm.sm2.SM2PublicKey;
import com.myzuji.sadk.asn1.parser.ASN1Parser;
import com.myzuji.sadk.lib.crypto.bcsoft.BCSoftLib;
import com.myzuji.sadk.lib.crypto.hard.HardLib;
import com.myzuji.sadk.lib.crypto.jni.Session;
import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.CertificationRequest;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.CertificationRequestInfo;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.RSAPublicKey;
import com.myzuji.sadk.org.bouncycastle.asn1.x500.X500Name;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import com.myzuji.sadk.org.bouncycastle.crypto.params.RSAKeyParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.util.PublicKeyFactory;
import com.myzuji.sadk.org.bouncycastle.jcajce.provider.asymmetric.rsa.BCRSAPublicKey;
import com.myzuji.sadk.org.bouncycastle.pkcs.PKCS10CertificationRequest;
import com.myzuji.sadk.system.CompatibleConfig;
import com.myzuji.sadk.system.Mechanisms;
import com.myzuji.sadk.system.SM2OutputFormat;
import com.myzuji.sadk.system.global.P10RequestContextConfig;
import com.myzuji.sadk.util.KeyUtil;
import com.myzuji.sadk.util.Signature;

import java.io.*;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class P10Request {
    private String subject = null;
    private PublicKey publicKey = null;
    private PublicKey tempPublicKey = null;
    private ASN1Set attributes = null;
    private KeyPair keyPair = null;
    private KeyPair tempKeyPair;
    private Session session = null;
    private boolean needVerify = false;
    private boolean p10RequestVerifyState = false;
    private int certReqType = 0;
    Mechanism mechanism = null;
    byte[] signature = null;
    byte[] source = null;
    private int formatSignedBytes;
    byte[] base64P10Data;


    public P10Request() {
        this.formatSignedBytes = CompatibleConfig.P10RequestFormatSignedBytes;
    }

    public P10Request(Session session) {
        this.formatSignedBytes = CompatibleConfig.P10RequestFormatSignedBytes;
        this.session = session;
    }


    public void setFormatSignedBytes(int formatSignedBytes) {
        this.formatSignedBytes = formatSignedBytes;
    }

    public void load(byte[] data) throws PKIException {
        if (data == null) {
            throw new PKIException("P10File data should not be null");
        } else {
            String line;
            if (!ASN1Parser.isDERSequence(data)) {
                BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)));

                try {
                    line = bufferReader.readLine();
                    if (line != null && line.toUpperCase().indexOf("BEGIN NEW CERTIFICATE REQUEST") != -1) {
                        StringBuffer strBuffer = new StringBuffer();

                        while ((line = bufferReader.readLine()) != null && line.toUpperCase().indexOf("END NEW CERTIFICATE REQUEST") == -1) {
                            strBuffer.append(line);
                        }

                        data = strBuffer.toString().getBytes();
                    }
                } catch (Exception var7) {
                    throw new PKIException(PKIException.PARSE_P10_ERR, "P10 Text ReadLine decoding failure", var7);
                }
            }

            ASN1Sequence sequence;
            try {
                sequence = ASN1Parser.getDERSequenceFrom(data);
            } catch (Exception var6) {
                throw new PKIException(PKIException.PARSE_P10_ERR, "P10 DERSequence decoding failure", var6);
            }

            line = null;

            CertificationRequest certificationRequest;
            try {
                certificationRequest = new CertificationRequest(sequence);
            } catch (Exception var5) {
                throw new PKIException(PKIException.PARSE_P10_ERR, "P10 CertificationRequest decoding failure", var5);
            }

            this.load(certificationRequest);
        }
    }

    public void load(CertificationRequest certficationRequest) throws PKIException {
        String signAlg = Mechanism.getSignatureAlgName(certficationRequest.getSignatureAlgorithm());
        if (!Mechanisms.isValid(signAlg)) {
            throw new PKIException(PKIException.GEN_P10_ERR, PKIException.GEN_P10_ERR_DES + " " + PKIException.NONSUPPORT_SIGALG_DES + " " + signAlg);
        } else {
            this.mechanism = new Mechanism(signAlg);
            boolean smFlag = Mechanisms.isSM2WithSM3(this.mechanism);
            this.signature = certficationRequest.getSignature().getBytes();
            if (smFlag) {
                try {
                    this.signature = SM2OutputFormat.sm2FormatSigned64Bytes(this.signature);
                } catch (Exception var8) {
                    throw new PKIException("Build FormatSigned64Bytes Failure", var8);
                }
            }

            CertificationRequestInfo crqInfo = certficationRequest.getCertificationRequestInfo();
            this.source = ASN1Parser.parseDERObj2Bytes(crqInfo.toASN1Primitive());
            SubjectPublicKeyInfo spkInfo = crqInfo.getSubjectPublicKeyInfo();
            this.needVerify = P10RequestContextConfig.getP10RequestVerifyState();
            this.subject = crqInfo.getSubject().toString();
            this.attributes = crqInfo.getAttributes();
            if (this.attributes != null && this.attributes.size() > 1) {
                this.prepareTemporaryPublicKey();
            }

            try {
                if (smFlag) {
                    this.publicKey = new SM2PublicKey(spkInfo.getPublicKeyData().getBytes());
                } else {
                    RSAKeyParameters param = null;
                    param = (RSAKeyParameters) PublicKeyFactory.createKey(spkInfo);
                    this.publicKey = new BCRSAPublicKey(param);
                }
            } catch (IOException var7) {
                throw new PKIException("Build PublicKey Failure", var7);
            }

            if (this.needVerify) {
                this.p10RequestVerifyState = this.session.verify(this.mechanism, this.publicKey, this.source, this.signature);
                if (!this.p10RequestVerifyState) {
                    throw new PKIException(PKIException.PARSE_P10_ERR, PKIException.PARSE_P10_ERR_DES + " " + PKIException.PARSE_P10_ERR_VERIFY_SIG_DES);
                }
            }

        }
    }

    public int getKeySize() {
        if (Mechanisms.isSM2WithSM3(this.mechanism)) {
            return 256;
        } else {
            BCRSAPublicKey bcPublicKey = (BCRSAPublicKey) this.publicKey;
            BigInteger n = bcPublicKey.getModulus();
            return n.bitLength();
        }
    }

    public ASN1Set getAttributes() {
        return this.attributes;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public String getSubject() {
        return this.subject;
    }

    private void prepareTemporaryPublicKey() throws PKIException {
        ASN1Sequence sequence = (ASN1Sequence) this.attributes.getObjectAt(1);
        ASN1ObjectIdentifier oid = ASN1ObjectIdentifier.getInstance(sequence.getObjectAt(0));
        if (oid.equals(PKCSObjectIdentifiers.pkcs_9_at_tempPublicKey)) {
            this.certReqType = 1;
            ASN1OctetString asn1oct = ASN1OctetString.getInstance(sequence.getObjectAt(1));
            sequence = ASN1Parser.parseOCT2SEQ(asn1oct);
            byte[] data = ((ASN1OctetString) sequence.getObjectAt(1)).getOctets();
            if (!Mechanisms.isSM2WithSM3(this.mechanism)) {
                RSAPublicKey rsaPubKey = RSAPublicKey.getInstance(data);
                RSAKeyParameters rsaKeyParams = new RSAKeyParameters(false, rsaPubKey.getModulus(), rsaPubKey.getPublicExponent());
                this.tempPublicKey = new BCRSAPublicKey(rsaKeyParams);
            } else {
                byte[] tempPublicKeyX = new byte[32];
                byte[] tempPublicKeyY = new byte[32];
                System.arraycopy(data, 8, tempPublicKeyX, 0, 32);
                System.arraycopy(data, 72, tempPublicKeyY, 0, 32);
                this.tempPublicKey = new SM2PublicKey(tempPublicKeyX, tempPublicKeyY);
            }

        }
    }

    public PublicKey getTemporaryPublicKey() {
        return this.tempPublicKey;
    }

    public byte[] getTemporaryPublicKeyDataFromAttributes(ASN1Set attributes) throws PKIException {
        byte[] tmpPublicKey = null;
        if (attributes != null && attributes.size() > 1) {
            ASN1Sequence sequence = (ASN1Sequence) attributes.getObjectAt(1);
            ASN1ObjectIdentifier oid = ASN1ObjectIdentifier.getInstance(sequence.getObjectAt(0));
            if (!oid.equals(PKCSObjectIdentifiers.pkcs_9_at_tempPublicKey)) {
                throw new PKIException(PKIException.PARSE_P10_ERR, PKIException.PARSE_P10_ERR_ATTRI_ID, new Exception("Not support the Attributes[1] Type ID:" + oid.getId()));
            }

            ASN1OctetString asn1oct = ASN1OctetString.getInstance(sequence.getObjectAt(1));
            sequence = ASN1Parser.parseOCT2SEQ(asn1oct);
            byte[] data = ((ASN1OctetString) sequence.getObjectAt(1)).getOctets();
            if (!Mechanisms.isSM2WithSM3(this.mechanism)) {
                RSAPublicKey rsaPubKey = RSAPublicKey.getInstance(data);

                try {
                    tmpPublicKey = Base64.getEncoder().encode(rsaPubKey.getEncoded());
                } catch (IOException var9) {
                    throw new PKIException("TemporaryPublicKeyDataFromAttributes Failure", var9);
                }
            } else {
                tmpPublicKey = Base64.getEncoder().encode(data);
            }
        }

        return tmpPublicKey;
    }


    public String getTemporaryPublicKeyFromAttributes(ASN1Set attributes) throws PKIException {
        String temporaryPublicKeyText = null;
        byte[] temporaryPublicKeyData = this.getTemporaryPublicKeyDataFromAttributes(attributes);
        if (temporaryPublicKeyData != null) {
            try {
                temporaryPublicKeyText = new String(temporaryPublicKeyData, "UTF-8");
            } catch (UnsupportedEncodingException var5) {
                throw new PKIException("codeing Exception", var5);
            }
        }

        return temporaryPublicKeyText;
    }


    public String generateCertificationRequest(Mechanism mechanism, X500Name x500NameSubject, ASN1Set attributes, PublicKey publicKey, PrivateKey privateKey) throws PKIException {
        try {
            return new String(this.generatePKCS10Request(mechanism, x500NameSubject, attributes, publicKey, privateKey), "UTF-8");
        } catch (UnsupportedEncodingException var7) {
            throw new PKIException("codeing Exception", var7);
        }
    }

    public void generateDoublePKCS10(Mechanism mechanism, int keyLength, String dn, String pwd) throws PKIException {
        String signAlgValue = mechanism.getMechanismType();
        Mechanism enMechanism = Mechanisms.encryptMechanismFrom(mechanism);
        if (null == enMechanism) {
            throw new PKIException("unsupported algorithm: " + mechanism.getMechanismType());
        } else {
            enMechanism.setParam(mechanism.getParam());
            if (null == this.session) {
                throw new PKIException("encryption session uninitialized!");
            } else {
                this.keyPair = KeyUtil.generateKeyPair(enMechanism, keyLength, this.session);
                if (this.session instanceof HardLib) {
                    this.tempKeyPair = KeyUtil.generateKeyPair(enMechanism, keyLength, new BCSoftLib());
                } else {
                    this.tempKeyPair = KeyUtil.generateKeyPair(enMechanism, keyLength, this.session);
                }

                SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(this.keyPair.getPublic().getEncoded());
                SubjectPublicKeyInfo temporaryPublicKeyInfo = SubjectPublicKeyInfo.getInstance(this.tempKeyPair.getPublic().getEncoded());
                X500Name subject = new X500Name(dn);
                ASN1EncodableVector challengePasswordVector = new ASN1EncodableVector();
                DERPrintableString challengePasswordValue = new DERPrintableString(pwd == null ? "111111" : pwd);
                challengePasswordVector.add(PKCSObjectIdentifiers.pkcs_9_at_challengePassword);
                challengePasswordVector.add(challengePasswordValue);
                DERSequence challengePasswordSeq = new DERSequence(challengePasswordVector);
                ASN1EncodableVector tempPublicKeyVector = new ASN1EncodableVector();
                tempPublicKeyVector.add(PKCSObjectIdentifiers.pkcs_9_at_tempPublicKey);
                ASN1Integer derInt = new ASN1Integer(1L);
                DEROctetString tempOctetString = null;
                if (Mechanisms.isRSAType(enMechanism)) {
                    try {
                        tempOctetString = new DEROctetString(temporaryPublicKeyInfo.parsePublicKey());
                    } catch (IOException var29) {
                        throw new PKIException("generateDoublePKCS10Request Failure", var29);
                    }
                } else if (Mechanisms.isSM2Type(enMechanism)) {
                    byte[] tempPublicKeyData = new byte[136];
                    byte[] temporaryPublicKeyData = temporaryPublicKeyInfo.getPublicKeyData().getBytes();
                    byte[] temporaryPublicKeyX = new byte[32];
                    byte[] temporaryPublicKeyY = new byte[32];
                    System.arraycopy(temporaryPublicKeyData, 1, temporaryPublicKeyX, 0, 32);
                    System.arraycopy(temporaryPublicKeyData, 33, temporaryPublicKeyY, 0, 32);
                    byte[] CONST1 = new byte[]{0, -76, 0, 0};
                    byte[] CONST2 = new byte[]{0, 1, 0, 0};
                    System.arraycopy(CONST1, 0, tempPublicKeyData, 0, 4);
                    System.arraycopy(CONST2, 0, tempPublicKeyData, 4, 4);
                    System.arraycopy(temporaryPublicKeyX, 0, tempPublicKeyData, 8, 32);
                    System.arraycopy(temporaryPublicKeyY, 0, tempPublicKeyData, 72, 32);
                    tempOctetString = new DEROctetString(tempPublicKeyData);
                }

                ASN1EncodableVector intAndPublicKeyVector = new ASN1EncodableVector();
                intAndPublicKeyVector.add(derInt);
                intAndPublicKeyVector.add(tempOctetString);
                DERSequence tempPublicKeyInfoSeq = new DERSequence(intAndPublicKeyVector);
                DEROctetString intAndPublicKeyOctetString = null;

                try {
                    intAndPublicKeyOctetString = new DEROctetString(tempPublicKeyInfoSeq);
                } catch (IOException var28) {
                    throw new PKIException("generateDoublePKCS10Request Failure", var28);
                }

                tempPublicKeyVector.add(intAndPublicKeyOctetString);
                DERSequence tempPubKeySeq = new DERSequence(tempPublicKeyVector);
                ASN1EncodableVector chaPasswordAndPubKeyVector = new ASN1EncodableVector();
                chaPasswordAndPubKeyVector.add(challengePasswordSeq);
                chaPasswordAndPubKeyVector.add(tempPubKeySeq);
                this.attributes = new DERSet(chaPasswordAndPubKeyVector);
                CertificationRequestInfo certRequestInfo = new CertificationRequestInfo(subject, publicKeyInfo, this.attributes);
                Object obj = Mechanism.getObjectIdentifier(signAlgValue);
                AlgorithmIdentifier signAlg = new AlgorithmIdentifier(ASN1ObjectIdentifier.getInstance(obj), DERNull.INSTANCE);

                try {
                    byte[] sourceData = certRequestInfo.getEncoded("DER");
                    byte[] signature = this.session.sign(mechanism, this.keyPair.getPrivate(), sourceData);
                    if (Mechanisms.isSM2WithSM3(signAlgValue)) {
                        signature = this.sm2FormatSignedBytes(signature);
                    }

                    CertificationRequest certRequest = new CertificationRequest(certRequestInfo, signAlg, new DERBitString(signature));
                    PKCS10CertificationRequest p10 = new PKCS10CertificationRequest(certRequest);
                    base64P10Data = Base64.getEncoder().encode(p10.getEncoded());
                    publicKey = keyPair.getPublic();
                    tempPublicKey = tempKeyPair.getPublic();
                } catch (PKIException var26) {
                    throw var26;
                } catch (Exception var27) {
                    throw new PKIException("Generated P10 Failure", var27);
                }
            }
        }
    }

    private final byte[] sm2FormatSignedBytes(byte[] signedBytes) throws IOException {
        return this.formatSignedBytes == 2 ? SM2OutputFormat.sm2FormatSigned64Bytes(signedBytes) : SM2OutputFormat.sm2FormatSignedBytes(signedBytes);
    }

    public byte[] generatePKCS10Request(Mechanism mechanism, int keyLength, Session session) throws PKIException {
        this.session = session;
        return this.generatePKCS10Request(mechanism, keyLength);
    }

    public byte[] generatePKCS10Request(Mechanism mechanism, int keyLength) throws PKIException {
        String signAlgValue = mechanism.getMechanismType();
        Mechanism enMechanism = Mechanisms.encryptMechanismFrom(mechanism);
        if (null == enMechanism) {
            throw new PKIException("unsupported algorithm: " + mechanism.getMechanismType());
        } else {
            enMechanism.setParam(mechanism.getParam());
            if (null == this.session) {
                throw new PKIException("encryption session uninitialized!");
            } else {
                this.keyPair = KeyUtil.generateKeyPair(enMechanism, keyLength, this.session);
                SubjectPublicKeyInfo pubInfo = SubjectPublicKeyInfo.getInstance(this.keyPair.getPublic().getEncoded());
                X500Name subject = new X500Name("CN=certRequisition,O=CFCA TEST CA,C=CN");
                CertificationRequestInfo certRequestInfo = new CertificationRequestInfo(subject, pubInfo, (ASN1Set) null);
                AlgorithmIdentifier signAlgID = Mechanism.getAlgorithmIdentifier(signAlgValue);

                try {
                    byte[] sourceData = certRequestInfo.getEncoded("DER");
                    byte[] signature = this.session.sign(mechanism, this.keyPair.getPrivate(), sourceData);
                    if (Mechanisms.isSM2WithSM3(signAlgValue)) {
                        signature = this.sm2FormatSignedBytes(signature);
                    }

                    CertificationRequest certRequest = new CertificationRequest(certRequestInfo, signAlgID, new DERBitString(signature));
                    PKCS10CertificationRequest p10 = new PKCS10CertificationRequest(certRequest);
                    return Base64.getEncoder().encode(p10.getEncoded());
                } catch (PKIException var13) {
                    throw var13;
                } catch (Exception var14) {
                    throw new PKIException("Generated P10 Failure", var14);
                }
            }
        }
    }

    public byte[] generatePKCS10Request(Mechanism mechanism, X500Name x500NameSubject, ASN1Set attributes, PublicKey publicKey, PrivateKey privateKey, Session session) throws PKIException {
        this.session = session;
        return this.generatePKCS10Request(mechanism, x500NameSubject, attributes, publicKey, privateKey);
    }

    public byte[] generatePKCS10Request(Mechanism mechanism, X500Name x500NameSubject, ASN1Set attributes, PublicKey publicKey, PrivateKey privateKey) throws PKIException {
        SubjectPublicKeyInfo pubInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());
        CertificationRequestInfo certRequestInfo = new CertificationRequestInfo(x500NameSubject, pubInfo, attributes);
        AlgorithmIdentifier signAlgID = Mechanism.getAlgorithmIdentifier(mechanism.getMechanismType());
        if (null == this.session) {
            throw new PKIException("encryption session uninitialized!");
        } else {
            try {
                byte[] sourceData = certRequestInfo.getEncoded("DER");
                byte[] signature = this.session.sign(mechanism, privateKey, sourceData);
                if (Mechanisms.isSM2WithSM3(mechanism)) {
                    signature = this.sm2FormatSignedBytes(signature);
                }

                CertificationRequest certRequest = new CertificationRequest(certRequestInfo, signAlgID, new DERBitString(signature));
                PKCS10CertificationRequest p10 = new PKCS10CertificationRequest(certRequest);
                return Base64.getEncoder().encode(p10.getEncoded());
            } catch (PKIException var13) {
                throw var13;
            } catch (Exception var14) {
                throw new PKIException("Generated P10 Failure", var14);
            }
        }
    }

    public byte[] generatePKCS10Request(Mechanism mechanism, X500Name x500NameSubject, ASN1Set attributes, PublicKey publicKey, byte[] signature) throws PKIException {
        SubjectPublicKeyInfo pubInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());
        CertificationRequestInfo certRequestInfo = new CertificationRequestInfo(x500NameSubject, pubInfo, attributes);
        AlgorithmIdentifier signAlgID = Mechanism.getAlgorithmIdentifier(mechanism.getMechanismType());

        try {
            CertificationRequest certRequest = new CertificationRequest(certRequestInfo, signAlgID, new DERBitString(signature));
            PKCS10CertificationRequest p10 = new PKCS10CertificationRequest(certRequest);
            return Base64.getEncoder().encode(p10.getEncoded());
        } catch (Exception var11) {
            throw new PKIException("Generated P10 Failure", var11);
        }
    }

    public byte[] generatePKCS10Request(Mechanism mechanism, CertificationRequestInfo certRequestInfo, byte[] signature) throws PKIException {
        AlgorithmIdentifier signAlgID = Mechanism.getAlgorithmIdentifier(mechanism.getMechanismType());

        try {
            CertificationRequest certRequest = new CertificationRequest(certRequestInfo, signAlgID, new DERBitString(signature));
            PKCS10CertificationRequest p10 = new PKCS10CertificationRequest(certRequest);
            return Base64.getEncoder().encode(p10.getEncoded());
        } catch (Exception var7) {
            throw new PKIException("Generated P10 Failure", var7);
        }
    }

    public CertificationRequestInfo generateCertificationRequestInfo(X500Name x500NameSubject, ASN1Set attributes, PublicKey publicKey) {
        SubjectPublicKeyInfo pubInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());
        CertificationRequestInfo certRequestInfo = new CertificationRequestInfo(x500NameSubject, pubInfo, attributes);
        return certRequestInfo;
    }

    public byte[] parseCertificationRequestInfoToBytes(CertificationRequestInfo certRequestInfo) throws PKIException {
        try {
            byte[] sourceData = certRequestInfo.getEncoded("DER");
            return sourceData;
        } catch (IOException var4) {
            throw new PKIException("Parsed P10 Failure", var4);
        }
    }

    public KeyPair getKeyPair() {
        return this.keyPair;
    }

    public KeyPair getTemporaryKeyPair() {
        return this.tempKeyPair;
    }

    public PrivateKey getTemporaryPrivateKey() {
        return this.tempKeyPair.getPrivate();
    }

    public PrivateKey getPrivateKey() {
        return this.keyPair.getPrivate();
    }

    public String getSubjectFromP10Request(byte[] base64P10Request) throws PKIException {
        PKCS10CertificationRequest p10 = this.decodedP10(base64P10Request);

        try {
            return p10.getSubject().toString();
        } catch (Exception var4) {
            throw new PKIException("Parsed P10 Failure", var4);
        }
    }

    public String getSignatureAlgorithmFromP10Request(byte[] base64P10Request) throws PKIException {
        PKCS10CertificationRequest p10 = this.decodedP10(base64P10Request);

        try {
            return this.getValidSignatureAlgName(p10.getSignatureAlgorithm());
        } catch (Exception var4) {
            throw new PKIException("Parsed P10 Failure", var4);
        }
    }

    public String getSignatureAlgorithm() {
        return this.mechanism.getMechanismType();
    }

    public byte[] getSignatureFromP10Request(byte[] base64P10Request) throws PKIException {
        PKCS10CertificationRequest p10 = this.decodedP10(base64P10Request);
        return p10.getSignature();
    }

    public byte[] getSignature() {
        return this.signature;
    }

    public boolean isP10RequestSignatureValid(byte[] base64P10Request) throws PKIException {
        PKCS10CertificationRequest p10 = this.decodedP10(base64P10Request);

        try {
            byte[] sign = p10.getSignature();
            byte[] src = p10.toASN1Structure().getCertificationRequestInfo().getEncoded("DER");
            String sigAlgName = this.getValidSignatureAlgName(p10.getSignatureAlgorithm());
            return (new Signature()).p1VerifyMessage(sigAlgName, src, sign, this.getPubKeyFromSubPubKeyInfo(p10.getSubjectPublicKeyInfo()), this.session);
        } catch (PKIException var6) {
            throw var6;
        } catch (Exception var7) {
            throw new PKIException("Parsed P10 Failure", var7);
        }
    }

    private final PKCS10CertificationRequest decodedP10(byte[] base64P10Request) throws PKIException {
        try {
            return new PKCS10CertificationRequest(Base64.getDecoder().decode(base64P10Request));
        } catch (Exception var3) {
            throw new PKIException("Parsed P10 Failure", var3);
        }
    }


    public boolean getP10RequestVerifyState() {
        return this.p10RequestVerifyState;
    }

    public PublicKey getPubKeyFromSubPubKeyInfo(SubjectPublicKeyInfo spki) throws PKIException {
        try {
            if (spki == null) {
                return null;
            } else if (Mechanisms.isSM2PublicKey(spki.getAlgorithm())) {
                byte[] pubData = spki.getPublicKeyData().getBytes();
                int len = pubData.length;
                if (len == 65) {
                    byte[] pubX = new byte[32];
                    byte[] pubY = new byte[32];
                    System.arraycopy(pubData, 1, pubX, 0, 32);
                    System.arraycopy(pubData, 33, pubY, 0, 32);
                    PublicKey pubKey = KeyUtil.getSM2PublicKey(pubX, pubY);
                    if (this.session != null && this.session instanceof HardLib) {
                        pubKey = ((HardLib) this.session).SM2HardPublicKey(new Mechanism("SM2"), (PublicKey) pubKey);
                    }

                    return (PublicKey) pubKey;
                } else {
                    throw new PKIException(PKIException.SPKI_KEY, PKIException.SPKI_KEY_DES);
                }
            } else if (spki.getAlgorithm().getAlgorithm().toString().equals(PKCSObjectIdentifiers.rsaEncryption.toString())) {
                RSAKeyParameters param = (RSAKeyParameters) PublicKeyFactory.createKey(spki);
                return new BCRSAPublicKey(param);
            } else {
                throw new PKIException("can not support this key type:" + spki.getAlgorithm().getAlgorithm());
            }
        } catch (PKIException var7) {
            throw var7;
        } catch (Exception var8) {
            throw new PKIException("Build SubjectPublicKeyInfo Failure", var8);
        }
    }

    public int getCertReqType() {
        return this.certReqType;
    }

    private final String getValidSignatureAlgName(AlgorithmIdentifier signatureAlgorithm) throws PKIException {
        if (!Mechanism.isValid(signatureAlgorithm)) {
            throw new PKIException("can not support such sign Alg:" + signatureAlgorithm);
        } else {
            return Mechanism.getSignatureAlgName(signatureAlgorithm);
        }
    }

    public byte[] getBase64P10Data() {
        return base64P10Data;
    }
}
