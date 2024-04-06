package com.myzuji.sadk.asn1.pkcs;

import com.myzuji.sadk.algorithm.common.CBCParam;
import com.myzuji.sadk.algorithm.common.Mechanism;
import com.myzuji.sadk.algorithm.common.PKCSObjectIdentifiers;
import com.myzuji.sadk.algorithm.common.PKIException;
import com.myzuji.sadk.algorithm.util.RSAAndItsCloseSymAlgUtil;
import com.myzuji.sadk.asn1.parser.ASN1Parser;
import com.myzuji.sadk.lib.crypto.bcsoft.BCSoftLib;
import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.*;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.Certificate;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.DigestInfo;
import com.myzuji.sadk.org.bouncycastle.crypto.CipherParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.DataLengthException;
import com.myzuji.sadk.org.bouncycastle.crypto.Digest;
import com.myzuji.sadk.org.bouncycastle.crypto.InvalidCipherTextException;
import com.myzuji.sadk.org.bouncycastle.crypto.digests.ExtendedDigest;
import com.myzuji.sadk.org.bouncycastle.crypto.digests.MD2Digest;
import com.myzuji.sadk.org.bouncycastle.crypto.digests.MD5Digest;
import com.myzuji.sadk.org.bouncycastle.crypto.digests.SHA1Digest;
import com.myzuji.sadk.org.bouncycastle.crypto.engines.RC2Engine;
import com.myzuji.sadk.org.bouncycastle.crypto.generators.PKCS12ParametersGenerator;
import com.myzuji.sadk.org.bouncycastle.crypto.macs.HMac;
import com.myzuji.sadk.org.bouncycastle.crypto.modes.CBCBlockCipher;
import com.myzuji.sadk.org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import com.myzuji.sadk.org.bouncycastle.crypto.params.KeyParameter;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ParametersWithIV;
import com.myzuji.sadk.org.bouncycastle.jcajce.provider.asymmetric.rsa.BCRSAPrivateCrtKey;
import com.myzuji.sadk.org.bouncycastle.util.Arrays;
import com.myzuji.sadk.signature.rsa.SafeContents;
import com.myzuji.sadk.system.FileHelper;
import com.myzuji.sadk.x509.certificate.X509Cert;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;

public class PKCS12 {
    private Pfx pfx;
    private PrivateKey privateKey = null;
    private X509Cert[] certs = null;
    private static final int ITERATIONS = 2000;

    public PKCS12() {
        this.pfx = null;
    }

    public PKCS12(byte[] pfxFileData) throws PKIException {
        if (pfxFileData == null) {
            throw new PKIException("PFXFile pfxFileData should not be null");
        } else {
            this.load(pfxFileData);
        }
    }

    public void load(Pfx _pfx) {
        this.pfx = _pfx;
    }

    public void load(String fileName) throws PKIException {
        if (fileName == null) {
            throw new PKIException("PFXFile fileName should not be null");
        } else {
            File file = new File(fileName);
            if (!file.exists()) {
                throw new PKIException("PFXFile fileName not found: " + file.getAbsolutePath());
            } else {
                byte[] encoding;
                try {
                    encoding = FileHelper.read(fileName);
                } catch (IOException var5) {
                    IOException e = var5;
                    throw new PKIException("PFXFile reading failure", e);
                }

                this.load(encoding);
            }
        }
    }

    public void load(InputStream in) throws PKIException {
        if (in == null) {
            throw new PKIException("Argument not allowed null for InputStream");
        } else {
            byte[] encoding = null;

            try {
                encoding = new byte[in.available()];
                in.read(encoding);
            } catch (IOException var11) {
                IOException e = var11;
                throw new PKIException("PFXFile reading failure", e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException var10) {
                        IOException e = var10;
                        e.printStackTrace();
                    }
                }

            }

            this.load(encoding);
        }
    }

    public void load(byte[] encoding) throws PKIException {
        if (encoding == null) {
            throw new PKIException("PFXFile encoding should not be null");
        } else {
            ASN1Sequence seq = null;

            Exception e;
            try {
                seq = ASN1Parser.getDERSequenceFrom(encoding);
            } catch (Exception var5) {
                e = var5;
                throw new PKIException("PFXFile encoding decoding failure", e);
            }

            try {
                this.pfx = Pfx.getInstance(seq);
            } catch (Exception var4) {
                e = var4;
                throw new PKIException("PFXFile object decoding failure", e);
            }
        }
    }

    public PrivateKey decrypt(char[] _password) throws PKIException {
        if (this.pfx == null) {
            throw new IllegalStateException("PFXFile must loading first!");
        } else if (_password == null) {
            throw new PKIException("PFXFile password should not be null");
        } else {
            byte[] passwordBytes = null;

            try {
                passwordBytes = PKCS12ParametersGenerator.PKCS12PasswordToBytes(_password);
            } catch (Exception var11) {
                throw new PKIException("PFXFile password invalid");
            }

            if (!this.DecryptAndVerifyMac(passwordBytes)) {
                throw new PKIException("PFXFile password invalid");
            } else {
                ContentInfo keyContent = null;
                ContentInfo certContent = null;

                try {
                    ContentInfo authSafe = this.pfx.getAuthSafe();
                    ASN1OctetString octetString = ASN1OctetString.getInstance(authSafe.getContent());
                    ASN1Sequence sequence = ASN1Parser.parseOCT2SEQ(octetString);
                    AuthenticatedSafe authenticatedSafe = AuthenticatedSafe.getInstance(sequence);
                    ContentInfo[] contentInfo = authenticatedSafe.getContentInfo();

                    for (int i = 0; i < contentInfo.length; ++i) {
                        if (contentInfo[i].getContentType().equals(PKCSObjectIdentifiers.data)) {
                            keyContent = contentInfo[i];
                        } else if (contentInfo[i].getContentType().equals(PKCSObjectIdentifiers.encryptedData)) {
                            certContent = contentInfo[i];
                        }
                    }
                } catch (Exception var12) {
                    Exception e = var12;
                    throw new PKIException("PFXFile Parsed failure", e);
                }

                this.privateKey = this.DecryptKeyContent(keyContent, passwordBytes);
                this.certs = this.DecryptCertContent(certContent, passwordBytes);

                for (int i = 0; i < passwordBytes.length; ++i) {
                    passwordBytes[0] = 0;
                }

                passwordBytes = null;
                return this.privateKey;
            }
        }
    }

    public X509Cert[] getCerts() throws PKIException {
        if (this.certs == null) {
            throw new PKIException("PFXFile hasn't been decrypted yet.");
        } else {
            return this.certs;
        }
    }

    public PrivateKey getPrivateKey() throws PKIException {
        if (this.privateKey == null) {
            throw new PKIException("PFXFile hasn't been decrypted yet.");
        } else {
            return this.privateKey;
        }
    }

    private byte[] PBEDecryptContent(String algName, PKCS12ParametersGenerator p12gen, byte[] en_data) throws PKIException {
        try {
            CipherParameters param;
            if (algName.equals(PKCSObjectIdentifiers.pbeWithSHAAnd128RC2CBC.getId())) {
                param = p12gen.generateDerivedParameters(128, 64);
                return RC2Encrypt(false, param, en_data);
            } else if (algName.equals(PKCSObjectIdentifiers.pbeWithSHAAnd40RC2CBC.getId())) {
                param = p12gen.generateDerivedParameters(40, 64);
                return RC2Encrypt(false, param, en_data);
            } else {
                throw new PKIException("not support pkcs12pbe algorithm: " + algName);
            }
        } catch (Exception var7) {
            Exception e = var7;
            throw new PKIException("PFXFile Decrypted failure", e);
        }
    }

    private final PrivateKey DecryptKeyContent(ContentInfo keyContent, byte[] password) throws PKIException {
        byte[] encoding = null;

        try {
            ASN1OctetString octetString = ASN1OctetString.getInstance(keyContent.getContent());
            ASN1Sequence sequence = ASN1Parser.parseOCT2SEQ(octetString);
            SafeContents safeContents = SafeContents.getInstance(sequence);
            SafeBag[] safeBag = safeContents.getSafeBag();
            SafeBag keyBag = safeBag[0];
            if (keyBag.getBagId().equals(PKCSObjectIdentifiers.keyBag)) {
                encoding = keyBag.getBagValue().toASN1Primitive().getEncoded();
            } else {
                if (!keyBag.getBagId().equals(PKCSObjectIdentifiers.pkcs8ShroudedKeyBag)) {
                    throw new PKIException("PFXFile handle keyBag error. bagId = " + keyBag.getBagId().getId());
                }

                EncryptedPrivateKeyInfo epki = EncryptedPrivateKeyInfo.getInstance(ASN1Parser.parseDERObj2Bytes(keyBag.getBagValue()));
                PBEParameter pbeParamSpec = PBEParameter.getInstance(epki.getEncryptionAlgorithm().getParameters());
                byte[] salt = pbeParamSpec.getSalt();
                int iterations = pbeParamSpec.getIterationCount().intValue();
                PKCS12ParametersGenerator p12gen = new PKCS12ParametersGenerator(new SHA1Digest());
                p12gen.init(password, salt, iterations);
                encoding = this.PBEDecryptContent(epki.getEncryptionAlgorithm().getAlgorithm().getId(), p12gen, epki.getEncryptedData());
            }
        } catch (Exception e) {
            throw new PKIException("PFXFile KeyContent decoding failure", e);
        }

        try {
            PKCS8EncodedKeySpec p8KeySpec = new PKCS8EncodedKeySpec(encoding);
            PrivateKeyInfo info = PrivateKeyInfo.getInstance(p8KeySpec.getEncoded());
            return new BCRSAPrivateCrtKey(info);
        } catch (Exception e) {
            throw new PKIException("PFXFile PrivateKey build failure", e);
        }
    }

    private final X509Cert[] DecryptCertContent(ContentInfo certContent, byte[] password) throws PKIException {
        try {
            EncryptedData encryptedData = EncryptedData.getInstance(certContent.getContent());
            AlgorithmIdentifier aid = encryptedData.getEncryptionAlgorithm();
            PKCS12PBEParams pm = PKCS12PBEParams.getInstance(aid.getParameters());
            byte[] salt = pm.getIV();
            int iterations = pm.getIterations().intValue();
            PKCS12ParametersGenerator p12gen = new PKCS12ParametersGenerator(new SHA1Digest());
            p12gen.init(password, salt, iterations);
            ASN1OctetString octetString = encryptedData.getContent();
            byte[] en_data = octetString.getOctets();
            byte[] de_data = this.PBEDecryptContent(aid.getAlgorithm().getId(), p12gen, en_data);
            ASN1Object obj = ASN1Parser.parseBytes2DERObj(de_data);
            SafeContents safeContents = SafeContents.getInstance((ASN1Sequence) obj);
            SafeBag[] safeBag = safeContents.getSafeBag();
            X509Cert[] certs = new X509Cert[safeBag.length];
            CertBag certBag = null;
            int num = 0;

            for (int i = 0; i < safeBag.length; ++i) {
                if (safeBag[i].getBagId().equals(PKCSObjectIdentifiers.certBag)) {
                    certBag = CertBag.getInstance(safeBag[i].getBagValue());
                    ASN1ObjectIdentifier certId = certBag.getCertId();
                    if (certId.equals(PKCSObjectIdentifiers.x509certType)) {
                        certs[num++] = new X509Cert(ASN1OctetString.getInstance(certBag.getCertValue()).getOctets());
                    } else if (!certId.equals(PKCSObjectIdentifiers.sdsiCertType)) {
                        throw new Exception("PFXFile CertContent not support certBag type, id=" + certId.getId());
                    }
                }
            }

            X509Cert[] out = new X509Cert[num];
            System.arraycopy(certs, 0, out, 0, out.length);
            return out;
        } catch (Exception var20) {
            Exception e = var20;
            throw new PKIException("PFXFile CertContent decoding failure", e);
        }
    }

    private final boolean DecryptAndVerifyMac(byte[] password) throws PKIException {
        try {
            MacData macData = this.pfx.getMacData();
            DigestInfo digestInfo = macData.getMac();
            ASN1ObjectIdentifier oid = digestInfo.getAlgorithmId().getAlgorithm();
            PKCS12ParametersGenerator p12gen = null;
            int keyLen = 0;
            ExtendedDigest digest = null;
            if (oid.equals(PKCSObjectIdentifiers.sha1)) {
                p12gen = new PKCS12ParametersGenerator(new SHA1Digest());
                keyLen = 160;
                digest = new SHA1Digest();
            } else if (oid.equals(PKCSObjectIdentifiers.md2)) {
                p12gen = new PKCS12ParametersGenerator(new MD2Digest());
                keyLen = 128;
                digest = new MD2Digest();
            } else {
                if (!oid.equals(PKCSObjectIdentifiers.md5)) {
                    throw new PKIException("not support digest algorithmIdentifier:" + oid);
                }

                p12gen = new PKCS12ParametersGenerator(new MD5Digest());
                keyLen = 128;
                digest = new MD5Digest();
            }

            byte[] salt = macData.getSalt();
            int iterations = macData.getIterationCount().intValue();
            p12gen.init(password, salt, iterations);
            CipherParameters param = p12gen.generateDerivedMacParameters(keyLen);
            KeyParameter keyParam = (KeyParameter) param;
            ASN1OctetString oct = ASN1OctetString.getInstance(this.pfx.getAuthSafe().getContent());
            byte[] content = oct.getOctets();
            HMac hmac = new HMac((Digest) digest);
            hmac.init(keyParam);
            hmac.update(content, 0, content.length);
            byte[] my_digest = new byte[hmac.getMacSize()];
            hmac.doFinal(my_digest, 0);
            byte[] digest_ori = digestInfo.getDigest();
            return Arrays.areEqual(my_digest, digest_ori);
        } catch (Exception var17) {
            Exception e = var17;
            throw new PKIException("PFXFile MacData checked failure", e);
        }
    }

    private static byte[] RC2Encrypt(boolean isEncrypt, CipherParameters param, byte[] data) throws PKIException {
        try {
            PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new RC2Engine()));
            cipher.init(isEncrypt, param);
            byte[] out = new byte[cipher.getOutputSize(data.length)];
            int returnLength = cipher.processBytes(data, 0, data.length, out, 0);
            int validLength = -1;
            if (returnLength < out.length) {
                validLength = cipher.doFinal(out, returnLength);
            }

            if (isEncrypt) {
                return out;
            } else {
                byte[] d = new byte[out.length - cipher.getBlockSize() + validLength];
                System.arraycopy(out, 0, d, 0, d.length);
                return d;
            }
        } catch (DataLengthException var8) {
            DataLengthException e = var8;
            throw new PKIException("PFXFile Operations failure with DataLengthException", e);
        } catch (IllegalArgumentException var9) {
            IllegalArgumentException e = var9;
            throw new PKIException("PFXFile Operations failure with IllegalArgumentException", e);
        } catch (IllegalStateException var10) {
            IllegalStateException e = var10;
            throw new PKIException("PFXFile Operations failure with IllegalStateException", e);
        } catch (InvalidCipherTextException var11) {
            InvalidCipherTextException e = var11;
            throw new PKIException("PFXFile Operations failure with InvalidCipherTextException", e);
        } catch (Exception var12) {
            Exception e = var12;
            throw new PKIException("PFXFile Operations failure with Exception", e);
        }
    }

    private static EncryptedPrivateKeyInfo GenerateEncryptedPrivateKeyInfo(PrivateKey privateKey, byte[] password) throws PKIException {
        try {
            byte[] keyData = privateKey.getEncoded();
            SecureRandom sRandom = new SecureRandom();
            byte[] salt = new byte[8];
            sRandom.nextBytes(salt);
            PKCS12ParametersGenerator p12gen = new PKCS12ParametersGenerator(new SHA1Digest());
            p12gen.init(password, salt, 2000);
            ParametersWithIV param = (ParametersWithIV) p12gen.generateDerivedParameters(192, 64);
            byte[] keyBytes = ((KeyParameter) param.getParameters()).getKey();
            Mechanism mechanism = new Mechanism("DESede/CBC/PKCS7Padding", new CBCParam(param.getIV()));
            byte[] encryptedKeyBytes = RSAAndItsCloseSymAlgUtil.crypto(false, true, keyBytes, keyData, mechanism);
            DEROctetString deS = new DEROctetString(encryptedKeyBytes);
            ASN1EncodableVector vector = new ASN1EncodableVector();
            vector.add(new DEROctetString(salt));
            vector.add(new ASN1Integer(2000L));
            DERSequence deSeq = new DERSequence(vector);
            AlgorithmIdentifier algId = new AlgorithmIdentifier(PKCSObjectIdentifiers.pbeWithSHAAnd3DESCBC, deSeq);
            vector = new ASN1EncodableVector();
            vector.add(algId);
            vector.add(deS);
            deSeq = new DERSequence(vector);
            return EncryptedPrivateKeyInfo.getInstance(deSeq);
        } catch (Exception var14) {
            Exception e = var14;
            throw new PKIException("PFXFile Encrypted PrivateKeyInfo failure", e);
        }
    }

    private static EncryptedData GenerateSaftContents(ASN1Encodable safeContents, byte[] password) throws PKIException {
        try {
            SecureRandom sRandom = new SecureRandom();
            byte[] salt = new byte[8];
            sRandom.nextBytes(salt);
            PKCS12ParametersGenerator p12gen = new PKCS12ParametersGenerator(new SHA1Digest());
            p12gen.init(password, salt, 2000);
            CipherParameters param = p12gen.generateDerivedParameters(40, 64);
            byte[] en_data = RC2Encrypt(true, param, ASN1Parser.parseDERObj2Bytes(safeContents));
            DEROctetString octString = new DEROctetString(en_data);
            ASN1EncodableVector vector = new ASN1EncodableVector();
            vector.add(new DEROctetString(salt));
            vector.add(new ASN1Integer(2000L));
            DERSequence deSeq = new DERSequence(vector);
            AlgorithmIdentifier algId = new AlgorithmIdentifier(PKCSObjectIdentifiers.pbeWithSHAAnd40RC2CBC, deSeq);
            return new EncryptedData(PKCSObjectIdentifiers.data, algId, octString);
        } catch (Exception var11) {
            Exception e = var11;
            throw new PKIException("PFXFile EncryptedSaftContents failure", e);
        }
    }

    private static MacData GenerateMacData(ContentInfo authSafe, byte[] password) throws PKIException {
        try {
            SecureRandom sRandom = new SecureRandom();
            byte[] salt = new byte[8];
            sRandom.nextBytes(salt);
            PKCS12ParametersGenerator p12gen = new PKCS12ParametersGenerator(new SHA1Digest());
            p12gen.init(password, salt, 2000);
            CipherParameters param = p12gen.generateDerivedMacParameters(160);
            ASN1OctetString oct = ASN1OctetString.getInstance(authSafe.getContent());
            byte[] da = oct.getOctets();
            HMac mac = new HMac(new SHA1Digest());
            mac.init(param);
            mac.update(da, 0, da.length);
            byte[] hmac = new byte[mac.getMacSize()];
            mac.doFinal(hmac, 0);
            DigestInfo digestInfo = new DigestInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.sha1), hmac);
            return new MacData(digestInfo, salt, 2000);
        } catch (Exception var11) {
            Exception e = var11;
            throw new PKIException("PFXFile MacData Generated failure", e);
        }
    }

    public static String generatePfxFile(X509Cert x509Cert, PrivateKey privateKey, String password, String fileName) throws PKIException {
        if (fileName == null) {
            throw new PKIException("PFXFile fileName should not be null");
        } else {
            byte[] data = generatePfxData(x509Cert, privateKey, password);

            try {
                FileHelper.write(fileName, data);
                return fileName;
            } catch (Exception var6) {
                Exception e = var6;
                throw new PKIException("PFXFile write failure", e);
            }
        }
    }

    public static byte[] generatePfxData(X509Cert x509Cert, PrivateKey privateKey, String password) throws PKIException {
        Pfx pfx = generatePfx(x509Cert, privateKey, password);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            DEROutputStream out = new DEROutputStream(baos);
            out.writeObject(pfx);
        } catch (IOException var6) {
            IOException e = var6;
            throw new PKIException("PFXData generating failure", e);
        }

        return baos.toByteArray();
    }

    public static Pfx generatePfx(X509Cert x509Cert, PrivateKey privateKey, String password) throws PKIException {
        if (password != null && password.length() != 0) {
            if (privateKey == null) {
                throw new PKIException("PFXFile privateKey should not be null");
            } else if (x509Cert == null) {
                throw new PKIException("PFXFile x509Cert should not be null");
            } else if (!"RSA".equalsIgnoreCase(privateKey.getAlgorithm())) {
                throw new PKIException("PFXFile Required RSAPrivateKey");
            } else {
                BCSoftLib softLib = new BCSoftLib();
                Mechanism mechanism = new Mechanism("sha256WithRSAEncryption");
                byte[] sourceData = "TESTING".getBytes();

                boolean verifyResult;
                byte[] passwordBytes;
                try {
                    passwordBytes = softLib.sign(mechanism, privateKey, sourceData);
                    verifyResult = softLib.verify(mechanism, x509Cert.getPublicKey(), sourceData, passwordBytes);
                } catch (Exception var32) {
                    Exception e = var32;
                    throw new PKIException("PFXFile x509Cert/privateKey try signing failure", e);
                }

                if (!verifyResult) {
                    throw new PKIException("PFXFile x509Cert/privateKey not match");
                } else {
                    try {
                        passwordBytes = PKCS12ParametersGenerator.PKCS12PasswordToBytes(password.toCharArray());
                    } catch (Exception var31) {
                        Exception e = var31;
                        throw new PKIException("PFXFile password encoding invalid", e);
                    }

                    EncryptedPrivateKeyInfo epki = GenerateEncryptedPrivateKeyInfo(privateKey, passwordBytes);

                    try {
                        Certificate cert = x509Cert.getCertStructure();
                        ASN1Integer dint = cert.getSerialNumber();
                        byte[] sn = ASN1Parser.parseDERObj2Bytes(dint);
                        DEROctetString osn = new DEROctetString(sn);
                        ASN1EncodableVector derV = new ASN1EncodableVector();
                        derV.add(osn);
                        DERSet derSet = new DERSet(derV);
                        Attribute attribute = new Attribute(PKCSObjectIdentifiers.pkcs_9_at_localKeyId, derSet);
                        derV = new ASN1EncodableVector();
                        derV.add(attribute);
                        derSet = new DERSet(derV);
                        SafeBag keyBag = new SafeBag(PKCSObjectIdentifiers.pkcs8ShroudedKeyBag, ASN1Parser.parseBytes2DERObj(epki.getEncoded()), derSet);
                        SafeBag[] keyBags = new SafeBag[]{keyBag};
                        SafeContents safeContents = new SafeContents(keyBags);
                        DEROctetString octString = new DEROctetString(ASN1Parser.parseDERObj2Bytes(safeContents));
                        ContentInfo keyContent = new ContentInfo(PKCSObjectIdentifiers.data, octString);
                        ContentInfo[] contentInfos = new ContentInfo[]{keyContent, null};
                        octString = new DEROctetString(ASN1Parser.parseDERObj2Bytes(cert));
                        CertBag certBag = new CertBag(PKCSObjectIdentifiers.x509certType, octString);
                        SafeBag sbag = new SafeBag(PKCSObjectIdentifiers.certBag, certBag, derSet);
                        SafeBag[] certBags = new SafeBag[]{sbag};
                        safeContents = new SafeContents(certBags);
                        EncryptedData encryptedData = GenerateSaftContents(safeContents, passwordBytes);
                        ContentInfo certContent = new ContentInfo(PKCSObjectIdentifiers.encryptedData, encryptedData);
                        contentInfos[1] = certContent;
                        AuthenticatedSafe authenticatedSafe = new AuthenticatedSafe(contentInfos);
                        octString = new DEROctetString(ASN1Parser.parseDERObj2Bytes(authenticatedSafe));
                        ContentInfo authSafe = new ContentInfo(PKCSObjectIdentifiers.data, octString);
                        MacData macData = GenerateMacData(authSafe, passwordBytes);
                        return new Pfx(authSafe, macData);
                    } catch (IOException var30) {
                        IOException e = var30;
                        throw new PKIException("PFXFile generating failure", e);
                    }
                }
            }
        } else {
            throw new PKIException("PFXFile password should not be null");
        }
    }
}
