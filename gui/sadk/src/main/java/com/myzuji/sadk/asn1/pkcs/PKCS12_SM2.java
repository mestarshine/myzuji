package com.myzuji.sadk.asn1.pkcs;

import com.myzuji.sadk.algorithm.common.Mechanism;
import com.myzuji.sadk.algorithm.common.PKCSObjectIdentifiers;
import com.myzuji.sadk.algorithm.common.PKIException;
import com.myzuji.sadk.algorithm.sm2.SM2PrivateKey;
import com.myzuji.sadk.algorithm.sm2.SM2PublicKey;
import com.myzuji.sadk.algorithm.sm2.SM4Engine;
import com.myzuji.sadk.asn1.parser.ASN1Parser;
import com.myzuji.sadk.lib.crypto.bcsoft.BCSoftLib;
import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.Certificate;
import com.myzuji.sadk.org.bouncycastle.crypto.DataLengthException;
import com.myzuji.sadk.org.bouncycastle.crypto.InvalidCipherTextException;
import com.myzuji.sadk.org.bouncycastle.crypto.digests.SM3Digest;
import com.myzuji.sadk.org.bouncycastle.crypto.modes.CBCBlockCipher;
import com.myzuji.sadk.org.bouncycastle.crypto.paddings.PKCS7Padding;
import com.myzuji.sadk.org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import com.myzuji.sadk.org.bouncycastle.crypto.params.KeyParameter;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ParametersWithIV;
import com.myzuji.sadk.system.FileHelper;
import com.myzuji.sadk.system.global.SM2ContextConfig;
import com.myzuji.sadk.x509.certificate.X509Cert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.util.Base64;

public class PKCS12_SM2 implements ASN1Encodable, PKCSObjectIdentifiers {
    private ASN1Sequence privateInfo = null;
    private ASN1Sequence publicInfo = null;
    private SM2PrivateKey SM2PrivateKey;
    private X509Cert[] certs;

    public static PKCS12_SM2 getInstance(Object obj) throws PKIException {
        if (obj instanceof PKCS12_SM2) {
            return (PKCS12_SM2) obj;
        } else if (obj instanceof byte[]) {
            return new PKCS12_SM2((byte[]) ((byte[]) obj));
        } else {
            return obj != null ? new PKCS12_SM2(ASN1Sequence.getInstance(obj)) : null;
        }
    }

    public PKCS12_SM2() {
    }

    public PKCS12_SM2(byte[] encoding) throws PKIException {
        if (encoding == null) {
            throw new PKIException("SM2File encoding should not be null");
        } else {
            this.load(encoding);
        }
    }

    public PKCS12_SM2(ASN1Sequence seq) throws PKIException {
        this.parseSM2(seq);
    }

    public PKCS12_SM2(ASN1Sequence publicInfo, ASN1Sequence privateInfo) throws PKIException {
        if (publicInfo == null) {
            throw new PKIException("SM2File publicInfo should not be null");
        } else if (privateInfo == null) {
            throw new PKIException("SM2File privateInfo should not be null");
        } else {
            this.parseSM2Certs(privateInfo, publicInfo);
        }
    }

    public void load(byte[] data) throws PKIException {
        ASN1Sequence seq;
        try {
            seq = ASN1Parser.getDERSequenceFrom(data);
        } catch (Exception var4) {
            Exception e = var4;
            throw new PKIException("SM2File Decoding failure", e);
        }

        this.parseSM2(seq);
    }

    public void parseSM2(ASN1Sequence seq) throws PKIException {
        if (seq != null && seq.size() == 3) {
            this.parseSM2Certs((ASN1Sequence) seq.getObjectAt(1), (ASN1Sequence) seq.getObjectAt(2));
        } else {
            throw new PKIException("invalid SM2File encoding");
        }
    }

    private void parseSM2Certs(ASN1Sequence privateInfo, ASN1Sequence publicInfo) throws PKIException {
        if (privateInfo.size() != 3) {
            throw new PKIException("the sm2 file is not right format,can not get the private part");
        } else if (publicInfo.size() != 2) {
            throw new PKIException("the sm2 file is not right format.can not get the public part");
        } else {
            this.privateInfo = privateInfo;
            this.publicInfo = publicInfo;
            ASN1OctetString pubOctString = (ASN1OctetString) publicInfo.getObjectAt(1);
            this.certs = new X509Cert[]{new X509Cert(pubOctString.getOctets())};
        }
    }

    private static byte[] KDF(byte[] z) {
        byte[] ct = new byte[]{0, 0, 0, 1};
        SM3Digest sm3 = new SM3Digest();
        sm3.update(z, 0, z.length);
        sm3.update(ct, 0, ct.length);
        byte[] hash = new byte[32];
        sm3.doFinal(hash, 0);
        return hash;
    }

    public PrivateKey getPrivateKey() throws PKIException {
        if (this.SM2PrivateKey == null) {
            throw new PKIException(PKIException.DECRYPT_P12_ERR, PKIException.DECRYPT_P12_ERR_DES);
        } else {
            return this.SM2PrivateKey;
        }
    }

    public SM2PrivateKey getPrivateKey(String password) throws PKIException {
        return this.decrypt(password);
    }

    public SM2PrivateKey decrypt(String password) throws PKIException {
        if (this.SM2PrivateKey == null) {
            if (password == null) {
                throw new PKIException("SM2File password should not be null");
            }

            if (this.privateInfo == null) {
                throw new PKIException("SM2File invalid : privateInfo=null");
            }

            ASN1OctetString priOctString = (ASN1OctetString) this.privateInfo.getObjectAt(2);

            byte[] encryptedData;
            try {
                encryptedData = priOctString.getOctets();
            } catch (Exception var9) {
                Exception e = var9;
                throw new PKIException("SM2File decoding failure", e);
            }

            byte[] dBytes = this.SM4DecryptDBytes(password, encryptedData);
            X509Cert cert = this.getPublicCert()[0];
            SM2PublicKey pubKey = (SM2PublicKey) cert.getPublicKey();
            byte[] pubX = pubKey.getPubXByBytes();
            byte[] pubY = pubKey.getPubYByBytes();
            this.SM2PrivateKey = new SM2PrivateKey(dBytes, pubX, pubY);
        }

        return this.SM2PrivateKey;
    }

    private final byte[] SM4DecryptDBytes(String password, byte[] encryptedData) throws PKIException {
        if (password != null && password.length() != 0) {
            byte[] passwordBytes;
            try {
                passwordBytes = password.getBytes("UTF8");
            } catch (UnsupportedEncodingException var22) {
                throw new PKIException("SM2File password decoding failure", var22);
            }

            if (encryptedData != null && encryptedData.length != 0) {
                if (encryptedData.length >= 32 && encryptedData.length <= 64) {
                    byte[] encoding;
                    if (encryptedData.length != 32 && encryptedData.length != 48) {
                        try {
                            encoding = Base64.getDecoder().decode(encryptedData);
                        } catch (Exception var21) {
                            throw new PKIException("SM2File EncryptedData required base64 ");
                        }
                    } else {
                        encoding = encryptedData;
                    }

                    byte[] iv;
                    byte[] sm4;
                    Exception e;
                    try {
                        byte[] hash = KDF(passwordBytes);
                        iv = new byte[16];
                        System.arraycopy(hash, 0, iv, 0, 16);
                        sm4 = new byte[16];
                        System.arraycopy(hash, 16, sm4, 0, 16);
                    } catch (Exception var20) {
                        e = var20;
                        throw new PKIException("SM2File KDF failure", e);
                    }

                    try {
                        PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new SM4Engine()), new PKCS7Padding());
                        ParametersWithIV params = new ParametersWithIV(new KeyParameter(sm4), iv);
                        cipher.init(false, params);
                        int outLength = cipher.getOutputSize(encoding.length);
                        byte[] out = new byte[outLength];
                        int dataLength = cipher.processBytes(encoding, 0, encoding.length, out, 0);
                        int lastLength = cipher.doFinal(out, dataLength);
                        int realLength = dataLength + lastLength;
                        byte[] dBytes = null;
                        if (realLength < outLength) {
                            dBytes = new byte[realLength];
                            System.arraycopy(out, 0, dBytes, 0, realLength);
                        } else {
                            dBytes = out;
                        }

                        return dBytes;
                    } catch (DataLengthException var15) {
                        throw new PKIException("SM2File SM2PrivateKey decrypt failure with IllegalDataLength", var15);
                    } catch (IllegalArgumentException var16) {
                        throw new PKIException("SM2File SM2PrivateKey decrypt failure with IllegalArgument", var16);
                    } catch (IllegalStateException var17) {
                        throw new PKIException("SM2File SM2PrivateKey decrypt failure with IllegalState", var17);
                    } catch (InvalidCipherTextException var18) {
                        throw new PKIException("SM2File SM2PrivateKey decrypt failure with InvalidCipherText", var18);
                    } catch (Exception var19) {
                        e = var19;
                        throw new PKIException("SM2File SM2PrivateKey decrypt failure", e);
                    }
                } else {
                    throw new PKIException("SM2File EncryptedData required length in [32-64] ");
                }
            } else {
                throw new PKIException("SM2File encryptedData should not be null");
            }
        } else {
            throw new PKIException("SM2File password should not be null");
        }
    }

    private static final byte[] SM4EncryptDBytes(String password, byte[] privateKeyData) throws PKIException {
        if (password != null && password.length() != 0) {
            byte[] passwordBytes;
            try {
                passwordBytes = password.getBytes("UTF8");
            } catch (UnsupportedEncodingException var19) {
                UnsupportedEncodingException e = var19;
                throw new PKIException("SM2File password decoding failure", e);
            }

            if (privateKeyData != null && privateKeyData.length != 0) {
                byte[] sm4;
                Exception e;
                byte[] iv;
                try {
                    byte[] hash = KDF(passwordBytes);
                    iv = new byte[16];
                    System.arraycopy(hash, 0, iv, 0, 16);
                    sm4 = new byte[16];
                    System.arraycopy(hash, 16, sm4, 0, 16);
                } catch (Exception var18) {
                    e = var18;
                    throw new PKIException("SM2File KDF failure", e);
                }

                try {
                    PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new SM4Engine()), new PKCS7Padding());
                    ParametersWithIV params = new ParametersWithIV(new KeyParameter(sm4), iv);
                    cipher.init(true, params);
                    int outLength = cipher.getOutputSize(privateKeyData.length);
                    byte[] out = new byte[outLength];
                    int dataLength = cipher.processBytes(privateKeyData, 0, privateKeyData.length, out, 0);
                    int lastLength = cipher.doFinal(out, dataLength);
                    int realLength = dataLength + lastLength;
                    byte[] encryptedData = null;
                    if (realLength < outLength) {
                        encryptedData = new byte[realLength];
                        System.arraycopy(out, 0, encryptedData, 0, realLength);
                    } else {
                        encryptedData = out;
                    }

                    return encryptedData;
                } catch (DataLengthException var13) {
                    throw new PKIException("SM2File SM2PrivateKey encrypt failure with IllegalDataLength", var13);
                } catch (IllegalArgumentException var14) {
                    throw new PKIException("SM2File SM2PrivateKey encrypt failure with IllegalArgument", var14);
                } catch (IllegalStateException var15) {
                    throw new PKIException("SM2File SM2PrivateKey encrypt failure with IllegalState", var15);
                } catch (InvalidCipherTextException var16) {
                    throw new PKIException("SM2File SM2PrivateKey encrypt failure with InvalidCipherText", var16);
                } catch (Exception var17) {
                    e = var17;
                    throw new PKIException("SM2File SM2PrivateKey encrypt failure", e);
                }
            } else {
                throw new PKIException("SM2File EncryptedData should not be null");
            }
        } else {
            throw new PKIException("SM2File password should not be null");
        }
    }

    public X509Cert[] getPublicCert() throws PKIException {
        if (this.certs == null) {
            throw new PKIException("SM2File invalid : certs=null");
        } else {
            return this.certs;
        }
    }

    public ASN1Primitive toASN1Primitive() {
        if (this.privateInfo == null) {
            throw new IllegalArgumentException("SM2File privateInfo should not be null");
        } else if (this.publicInfo == null) {
            throw new IllegalArgumentException("SM2File publicInfo should not be null");
        } else {
            ASN1EncodableVector v = new ASN1EncodableVector();
            v.add(new ASN1Integer(1L));
            v.add(this.privateInfo);
            v.add(this.publicInfo);
            return new BERSequence(v);
        }
    }

    private static PKCS12_SM2 generateSM2(X509Cert x509Cert, PrivateKey privateKey, String password) throws PKIException {
        if (password != null && password.length() != 0) {
            if (privateKey == null) {
                throw new PKIException("SM2File privateKey should not be null");
            } else if (x509Cert == null) {
                throw new PKIException("SM2File x509Cert should not be null");
            } else {
                SM2PrivateKey sm2PrvKey = null;
                if (privateKey instanceof SM2PrivateKey) {
                    sm2PrvKey = (SM2PrivateKey) privateKey;
                    BCSoftLib softLib = new BCSoftLib();
                    Mechanism mechanism = new Mechanism("sm3WithSM2Encryption");
                    byte[] sourceData = "TESTING".getBytes();

                    boolean verifyResult;
                    byte[] dBytes;
                    try {
                        dBytes = softLib.sign(mechanism, sm2PrvKey, sourceData);
                        verifyResult = softLib.verify(mechanism, x509Cert.getPublicKey(), sourceData, dBytes);
                    } catch (Exception var19) {
                        Exception e = var19;
                        throw new PKIException("SM2File x509Cert/privateKey try signing failure", e);
                    }

                    if (!verifyResult) {
                        throw new PKIException("SM2File x509Cert/privateKey not match");
                    } else {
                        dBytes = sm2PrvKey.getDByBytes();
                        if (dBytes != null && dBytes.length == 32) {
                            byte[] encryptedData = SM4EncryptDBytes(password, sm2PrvKey.getDByBytes());

                            try {
                                ASN1EncodableVector publicInfoVector = new ASN1EncodableVector();
                                publicInfoVector.add(PKCSObjectIdentifiers.sm2Data);
                                Certificate cert = x509Cert.getCertStructure();
                                DEROctetString pubDEROctetString = new DEROctetString(cert.getEncoded());
                                publicInfoVector.add(pubDEROctetString);
                                DERSequence publicInfo = new DERSequence(publicInfoVector);
                                ASN1EncodableVector privateInfoVector = new ASN1EncodableVector();
                                privateInfoVector.add(PKCSObjectIdentifiers.sm2Data);
                                privateInfoVector.add(PKCSObjectIdentifiers.SM4_CBC);
                                DEROctetString prvDEROctetString = new DEROctetString(encryptedData);
                                privateInfoVector.add(prvDEROctetString);
                                DERSequence privateInfo = new DERSequence(privateInfoVector);
                                return new PKCS12_SM2(publicInfo, privateInfo);
                            } catch (IOException var17) {
                                IOException e = var17;
                                throw new PKIException("SM2File Generated failure", e);
                            } catch (Exception var18) {
                                Exception e = var18;
                                throw new PKIException("SM2File Generated failure", e);
                            }
                        } else {
                            throw new PKIException("SM2File SM2PrivateKey format invalid");
                        }
                    }
                } else {
                    throw new PKIException("SM2File privateKey must SM2PrivateKey");
                }
            }
        } else {
            throw new PKIException("SM2File password should not be null");
        }
    }

    public static String generateSM2File(X509Cert x509Cert, PrivateKey privateKey, String password, String fileName) throws PKIException {
        if (fileName == null) {
            throw new PKIException("SM2File fileName should not be null");
        } else {
            byte[] encoding = generateSM2Data(x509Cert, privateKey, password);

            try {
                FileHelper.write(fileName, encoding);
                return fileName;
            } catch (IOException var6) {
                IOException e = var6;
                throw new PKIException("Writing SM2File failure with IOException", e);
            }
        }
    }

    public static byte[] generateSM2Data(X509Cert x509Cert, PrivateKey privateKey, String password) throws PKIException {
        byte[] encoding = CombineSM2Data(x509Cert, privateKey, password);
        if (SM2ContextConfig.getBase64State()) {
            encoding = Base64.getEncoder().encode(encoding);
        }

        return encoding;
    }

    public static byte[] CombineSM2Data(X509Cert x509Cert, PrivateKey privateKey, String password) throws PKIException {
        PKCS12_SM2 p12_SM2 = generateSM2(x509Cert, privateKey, password);

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DEROutputStream out = new DEROutputStream(baos);
            out.writeObject(p12_SM2);
            byte[] encoding = baos.toByteArray();
            return encoding;
        } catch (IOException var7) {
            IOException e = var7;
            throw new PKIException("Encoding SM2File failure with IOException", e);
        }
    }
}
