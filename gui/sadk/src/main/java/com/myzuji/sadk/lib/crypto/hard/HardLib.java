package com.myzuji.sadk.lib.crypto.hard;

import com.myzuji.sadk.algorithm.common.CBCParam;
import com.myzuji.sadk.algorithm.common.GenKeyAttribute;
import com.myzuji.sadk.algorithm.common.Mechanism;
import com.myzuji.sadk.algorithm.common.PKIException;
import com.myzuji.sadk.algorithm.util.HashEncoderUtil;
import com.myzuji.sadk.asn1.DERHeader;
import com.myzuji.sadk.lib.crypto.jni.Session;
import com.myzuji.sadk.org.bouncycastle.asn1.sm2.ASN1SM2Cipher;
import com.myzuji.sadk.org.bouncycastle.asn1.sm2.ASN1SM2Signature;
import com.myzuji.sadk.org.bouncycastle.jce.interfaces.ECPrivateKey;
import com.myzuji.sadk.org.bouncycastle.jce.interfaces.ECPublicKey;
import com.myzuji.sadk.signature.rsa.RSAPackageUtil;
import com.myzuji.sadk.system.Mechanisms;
import com.myzuji.sadk.system.global.FileAndBufferConfig;
import com.myzuji.sadk.util.KeyUtil;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class HardLib implements Session {
    final String signByHash_ALG;
    private String providerName;
    private Provider provider;
    final boolean sm2SigningASN1Format;
    final boolean sm2EncryptASN1Format;
    final String transformation_cbc_pkcs7_des3 = "DESede/CBC/PKCS7Padding";
    final String transformation_cbc_pkcs7_sm4 = "SM4/CBC/PKCS7Padding";
    final int buffsize = 16384;

    public HardLib(String providerPath) throws PKIException {
        if (providerPath == null || providerPath.trim().equals("")) {
            providerPath = "com.sansec.jce.provider.SwxaProvider";
        }

        Mechanism mechanism = null;
        try {
            this.provider = (Provider) Class.forName(providerPath).newInstance();
            this.providerName = this.provider.getName();
        } catch (Exception e) {
            throw new PKIException(PKIException.INIT, PKIException.INIT_DES + " " + this.providerName, e);
        }

        KeyPair keypair = null;

        try {
            mechanism = new Mechanism("SM2");
            keypair = this.generateKeyPair(mechanism, 256);
        } catch (Exception var13) {
        }

        String signByHash = "SM2";

        try {
            Signature.getInstance("SimuSM2", this.provider);
            signByHash = "SimuSM2";
        } catch (Exception var11) {
            signByHash = "SM2";
        } finally {
            this.signByHash_ALG = signByHash;
        }

        this.sm2SigningASN1Format = this.signingFormat(keypair);
        this.sm2EncryptASN1Format = this.encryptFormat(keypair);
    }

    public byte[] sign(Mechanism mechanism, PrivateKey priKey, byte[] sourceData) throws PKIException {
        PrivateKey privateKey = this.SM2HardPrivateKey(mechanism, priKey);
        String mType = mechanism.getMechanismType();
        byte[] signData = null;

        try {
            Signature signature = Signature.getInstance(mType, this.provider);
            signature.initSign(privateKey);
            signature.update(sourceData);
            signData = signature.sign();
            return this.signedOutFormat(mechanism, signData);
        } catch (Exception var8) {
            Exception ex = var8;
            throw new PKIException(PKIException.SIGN, PKIException.SIGN_DES, ex);
        }
    }

    public byte[] sign(Mechanism mechanism, PrivateKey priKey, InputStream sourceStream) throws PKIException {
        PrivateKey privateKey = this.SM2HardPrivateKey(mechanism, priKey);
        String mType = mechanism.getMechanismType();
        byte[] signData = null;
        InputStream sourceData = null;

        try {
            Signature signature = Signature.getInstance(mType, this.provider);
            signature.initSign(privateKey);
            byte[] buffer = new byte[FileAndBufferConfig.BIG_FILE_BUFFER];
            int i = 1;
            sourceData = new BufferedInputStream(sourceStream);

            while ((i = sourceData.read(buffer)) > 0) {
                signature.update(buffer, 0, i);
            }

            signData = signature.sign();
            byte[] var11 = this.signedOutFormat(mechanism, signData);
            return var11;
        } catch (Exception var20) {
            Exception ex = var20;
            throw new PKIException(PKIException.SIGN, PKIException.SIGN_DES, ex);
        } finally {
            if (sourceData != null) {
                try {
                    sourceData.close();
                } catch (Exception var19) {
                    Exception e = var19;
                    throw new PKIException("Signed failure", e);
                }
            }

        }
    }

    public boolean verify(Mechanism mechanism, PublicKey pubKey, byte[] sourceData, byte[] signData) throws PKIException {
        PublicKey publicKey = this.SM2HardPublicKey(mechanism, pubKey);

        try {
            Signature signature = Signature.getInstance(mechanism.getMechanismType(), this.provider);
            signature.initVerify(publicKey);
            byte[] signedData = this.signedInFormat(mechanism, signData);
            signature.update(sourceData);
            return signature.verify(signedData);
        } catch (Exception var8) {
            Exception ex = var8;
            throw new PKIException(PKIException.VERIFY_SIGN, PKIException.VERIFY_SIGN_DES, ex);
        }
    }

    public boolean verify(Mechanism mechanism, PublicKey pubKey, InputStream sourceStream, byte[] signData) throws PKIException {
        PublicKey publicKey = this.SM2HardPublicKey(mechanism, pubKey);
        InputStream sourceData = null;

        try {
            Signature signature = Signature.getInstance(mechanism.getMechanismType(), this.provider);
            signature.initVerify(publicKey);
            byte[] signedData = this.signedInFormat(mechanism, signData);
            byte[] buffer = new byte[FileAndBufferConfig.BIG_FILE_BUFFER];
            int i = 1;
            sourceData = new BufferedInputStream(sourceStream);

            while ((i = sourceData.read(buffer)) > 0) {
                signature.update(buffer, 0, i);
            }

            boolean var11 = signature.verify(signedData);
            return var11;
        } catch (Exception var20) {
            Exception ex = var20;
            throw new PKIException(PKIException.VERIFY_SIGN, PKIException.VERIFY_SIGN_DES, ex);
        } finally {
            if (sourceData != null) {
                try {
                    sourceData.close();
                } catch (Exception var19) {
                    Exception e = var19;
                    throw new PKIException("Verified failure", e);
                }
            }

        }
    }

    public byte[] encrypt(Mechanism mechanism, Key key, byte[] sourceData) throws PKIException {
        Key hard = key;
        boolean sm2Operation = false;
        if (Mechanisms.isSM2Type(mechanism)) {
            hard = this.SM2HardPublicKey(mechanism, (PublicKey) key);
            sm2Operation = true;
        }

        Cipher cipher = this.engine(mechanism, 1, (Key) hard);

        try {
            byte[] encryptedData = cipher.doFinal(sourceData);
            if (sm2Operation) {
                encryptedData = this.sm2EncryptOutFormat(encryptedData);
            }

            return encryptedData;
        } catch (Exception var8) {
            Exception e = var8;
            throw new PKIException(PKIException.ENCRYPT, PKIException.ENCRYPT_DES, e);
        }
    }

    public byte[] decrypt(Mechanism mechanism, Key key, byte[] encryptData) throws PKIException {
        Key hard = key;
        boolean sm2Operation = false;
        if (Mechanisms.isSM2Type(mechanism)) {
            sm2Operation = true;
            hard = this.SM2HardPrivateKey(mechanism, (PrivateKey) key);
        }

        Cipher cipher = this.engine(mechanism, 2, (Key) hard);

        try {
            byte[] encryptedData = encryptData;
            return sm2Operation ? this.SM2Decrypt(cipher, encryptData, (Key) hard) : cipher.doFinal(encryptedData);
        } catch (Exception var8) {
            Exception e = var8;
            throw new PKIException(PKIException.DECRYPT, PKIException.DECRYPT_DES, e);
        }
    }

    public KeyPair generateKeyPair(Mechanism mechanism, int keyLength) throws PKIException {
        String mType = mechanism.getMechanismType();
        boolean isExport = true;
        int keyNum = 0;
        Object object = mechanism.getParam();
        if (object != null) {
            GenKeyAttribute attr = (GenKeyAttribute) object;
            isExport = attr.isExport;
            if (!isExport) {
                keyNum = attr.keyNum;
            }
        }

        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(mType, this.provider);
            if (isExport) {
                keyPairGen.initialize(keyLength);
            } else {
                keyPairGen.initialize(keyNum << 16);
            }

            return keyPairGen.generateKeyPair();
        } catch (Exception var8) {
            Exception e = var8;
            throw new PKIException(PKIException.HARD_KEY_PAIR, PKIException.HARD_KEY_PAIR_DES, e);
        }
    }

    public byte[] signByHash(Mechanism mechanism, PrivateKey priKey, byte[] digest) throws PKIException {
        PrivateKey privateKey = this.SM2HardPrivateKey(mechanism, priKey);
        int cipherMode = 1;

        try {
            byte[] derDigest;
            if (Mechanisms.isSM2WithSM3(mechanism)) {
                Signature signature = Signature.getInstance(this.signByHash_ALG, this.provider);
                signature.initSign(privateKey);
                signature.update(digest);
                derDigest = signature.sign();
                return this.signedOutFormat(mechanism, derDigest);
            } else {
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING", this.provider);
                cipher.init(cipherMode, priKey);
                derDigest = HashEncoderUtil.derEncoder(mechanism.getMechanismType(), digest);
                return cipher.doFinal(derDigest);
            }
        } catch (Exception var8) {
            Exception e = var8;
            throw new PKIException(PKIException.ENCRYPT, PKIException.ENCRYPT_DES, e);
        }
    }

    public boolean verifyByHash(Mechanism mechanism, PublicKey pubKey, byte[] digest, byte[] signData) throws PKIException {
        PublicKey publicKey = this.SM2HardPublicKey(mechanism, pubKey);
        int cipherMode = 2;

        try {
            byte[] extractHash;
            if (Mechanisms.isSM2WithSM3(mechanism)) {
                Signature signature = Signature.getInstance(this.signByHash_ALG, this.provider);
                signature.initVerify(publicKey);
                signature.update(digest);
                extractHash = this.signedInFormat(mechanism, signData);
                return signature.verify(extractHash);
            } else {
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING", this.provider);
                cipher.init(cipherMode, pubKey);
                extractHash = cipher.doFinal(signData);
                byte[] derDigest = HashEncoderUtil.derEncoder(mechanism, digest);
                return RSAPackageUtil.isRSAHashEqual(extractHash, derDigest);
            }
        } catch (Exception var10) {
            Exception e = var10;
            throw new PKIException(PKIException.DECRYPT, PKIException.DECRYPT_DES, e);
        }
    }

    public void encrypt(Mechanism mechanism, Key key, InputStream in, OutputStream out) throws PKIException {
        if (this.isSM2Type(mechanism)) {
            byte[] message;
            int mLength;
            try {
                message = new byte[8192];
                mLength = in.read(message);
            } catch (Exception var24) {
                throw new PKIException(PKIException.ENCRYPT, "file read failure");
            }

            if (mLength > 4096) {
                throw new PKIException(PKIException.ENCRYPT, "file length limited with 4096");
            } else {
                byte[] encryptedData = this.encrypt(mechanism, key, message);

                try {
                    out.write(encryptedData, 0, encryptedData.length);
                } catch (Exception var23) {
                    throw new PKIException(PKIException.ENCRYPT, "file write failure");
                }
            }
        } else {
            Cipher cipher = this.engine(mechanism, 1, key);
            byte[] buffer = new byte[16384];
            BufferedOutputStream bufferOS = null;
            BufferedInputStream bufferIS = null;

            try {
                bufferOS = new BufferedOutputStream(new CipherOutputStream(out, cipher), 16384);
                bufferIS = new BufferedInputStream(in, 16384);
                int len = 1;

                while ((len = bufferIS.read(buffer)) > 0) {
                    bufferOS.write(buffer, 0, len);
                }
            } catch (IOException var25) {
                IOException e = var25;
                throw new PKIException("Encrypt failure", e);
            } catch (Exception var26) {
                Exception e = var26;
                throw new PKIException("Encrypt failure", e);
            } finally {
                if (bufferOS != null) {
                    try {
                        bufferOS.close();
                    } catch (IOException var22) {
                    }
                }

                if (bufferIS != null) {
                    try {
                        bufferIS.close();
                    } catch (IOException var21) {
                    }
                }

            }

        }
    }

    public void decrypt(Mechanism mechanism, Key key, InputStream in, OutputStream out) throws PKIException {
        if (this.isSM2Type(mechanism)) {
            byte[] message;
            int mLength;
            try {
                message = new byte[8192];
                mLength = in.read(message);
            } catch (Exception var24) {
                throw new PKIException(PKIException.DECRYPT, "file read failure");
            }

            if (mLength > 5120) {
                throw new PKIException(PKIException.DECRYPT, "file length limited with 4096");
            } else {
                byte[] decryptedData = this.decrypt(mechanism, key, message);

                try {
                    out.write(decryptedData, 0, decryptedData.length);
                } catch (Exception var23) {
                    throw new PKIException(PKIException.DECRYPT, "file write failure");
                }
            }
        } else {
            Cipher cipher = this.engine(mechanism, 2, key);
            byte[] buffer = new byte[16384];
            BufferedOutputStream bufferOS = null;
            BufferedInputStream bufferIS = null;

            try {
                bufferOS = new BufferedOutputStream(out, 16384);
                bufferIS = new BufferedInputStream(new CipherInputStream(in, cipher), 16384);
                int len = 1;

                while ((len = bufferIS.read(buffer)) > 0) {
                    bufferOS.write(buffer, 0, len);
                }
            } catch (IOException var25) {
                IOException e = var25;
                throw new PKIException("Decrypt failure", e);
            } catch (Exception var26) {
                Exception e = var26;
                throw new PKIException("Decrypt failure", e);
            } finally {
                if (bufferOS != null) {
                    try {
                        bufferOS.close();
                    } catch (IOException var22) {
                    }
                }

                if (bufferIS != null) {
                    try {
                        bufferIS.close();
                    } catch (IOException var21) {
                    }
                }

            }

        }
    }

    public Key generateKey(Mechanism keyType) throws PKIException {
        String type = keyType.getMechanismType();
        int len = 1;
        if (type.equals("RC4")) {
            len = 128;
        } else if (type.equals("DESede")) {
            len = 192;
        } else {
            if (!type.equals("SM4")) {
                throw new PKIException("do not support this key type:" + type);
            }

            len = 128;
        }

        KeyGenerator keyGen;
        try {
            keyGen = KeyGenerator.getInstance(type, this.provider);
            keyGen.init(len);
        } catch (Exception var6) {
            Exception e = var6;
            throw new PKIException("KeyGenerator init failure", e);
        }

        return keyGen.generateKey();
    }

    public Key generateKey(Mechanism keyType, byte[] keyData) throws PKIException {
        return KeyUtil.generateKey(keyType, keyData);
    }

    public Provider getProvider() {
        return this.provider;
    }

    public String getProviderName() {
        return this.providerName;
    }

    private final boolean isSM2Type(Mechanism mechanism) {
        boolean convertFlag = true;
        if (mechanism != null && mechanism.getMechanismType() != null) {
            convertFlag = mechanism.getMechanismType().toUpperCase().indexOf("SM2") != -1;
        } else {
            convertFlag = true;
        }

        return convertFlag;
    }

    public final PublicKey SM2HardPublicKey(Mechanism mechanism, PublicKey pubKey) throws PKIException {
        if (pubKey != null && this.isSM2Type(mechanism) && pubKey instanceof ECPublicKey) {
            try {
                KeyFactory kf = KeyFactory.getInstance("SM2", this.provider);
                X509EncodedKeySpec x509spec = new X509EncodedKeySpec(pubKey.getEncoded());
                return kf.generatePublic(x509spec);
            } catch (Exception var5) {
                Exception ex = var5;
                throw new PKIException(PKIException.COV_PUB_KEY, PKIException.COV_PUB_KEY_DES, ex);
            }
        } else {
            return pubKey;
        }
    }

    public final PrivateKey SM2HardPrivateKey(Mechanism mechanism, PrivateKey privKey) throws PKIException {
        if (privKey != null && this.isSM2Type(mechanism) && privKey instanceof ECPrivateKey) {
            try {
                KeyFactory kf = KeyFactory.getInstance("SM2", this.provider);
                PKCS8EncodedKeySpec x509spec = new PKCS8EncodedKeySpec(privKey.getEncoded());
                return kf.generatePrivate(x509spec);
            } catch (Exception var5) {
                Exception ex = var5;
                throw new PKIException(PKIException.COV_PRV_KEY, PKIException.COV_PRV_KEY_DES, ex);
            }
        } else {
            return privKey;
        }
    }

    private final boolean signingFormat(KeyPair keypair) {
        boolean asnFormat = false;
        if (keypair != null) {
            try {
                Signature signature = Signature.getInstance("SM2", this.provider);
                signature.initSign(keypair.getPrivate());
                signature.update(new byte[32]);
                byte[] signData = signature.sign();
                asnFormat = signData != null && signData.length > 64;
            } catch (Exception var5) {
                asnFormat = false;
            }
        }

        return asnFormat;
    }

    private final boolean encryptFormat(KeyPair keypair) {
        boolean asnFormat = false;
        if (keypair != null) {
            try {
                Cipher cipher = Cipher.getInstance("SM2", this.provider);
                cipher.init(1, keypair.getPublic());
                byte[] encrypedData = cipher.doFinal(new byte[32]);
                asnFormat = encrypedData.length > 128;
            } catch (Exception var5) {
                asnFormat = false;
            }
        }

        return asnFormat;
    }

    private final byte[] signedOutFormat(Mechanism mechanism, byte[] signData) {
        return signData != null && this.isSM2Type(mechanism) && signData.length != 64 ? (new ASN1SM2Signature(signData)).getRS() : signData;
    }

    private byte[] signedInFormat(Mechanism mechanism, byte[] signData) {
        if (signData != null && this.isSM2Type(mechanism)) {
            try {
                if (this.sm2SigningASN1Format) {
                    if (signData.length == 64) {
                        return (new ASN1SM2Signature(signData)).getEncoded();
                    }
                } else if (signData.length != 64) {
                    return (new ASN1SM2Signature(signData)).getRS();
                }
            } catch (Exception var4) {
                return signData;
            }
        }

        return signData;
    }

    private final byte[] sm2EncryptOutFormat(byte[] encryptedData) {
        return encryptedData;
    }

    private final byte[] SM2Decrypt(Cipher engine, byte[] encryptData, Key key) throws PKIException, Exception {
        if (encryptData != null && encryptData.length >= 96) {
            DERHeader der = null;

            try {
                der = new DERHeader(encryptData, 0);
            } catch (Exception var8) {
                der = null;
            }

            if (der != null && (der.getTag() == 48 || der.getDerLength() == encryptData.length)) {
                return engine.doFinal(encryptData);
            } else {
                try {
                    ASN1SM2Cipher asn = new ASN1SM2Cipher(encryptData, 4);
                    return engine.doFinal(asn.getEncoded());
                } catch (Exception var7) {
                    engine.init(2, key);
                    ASN1SM2Cipher asn = new ASN1SM2Cipher(encryptData, 16);
                    return engine.doFinal(asn.getEncoded());
                }
            }
        } else {
            throw new PKIException(PKIException.DECRYPT, "encryptData too shortage");
        }
    }

    private final Cipher engine(Mechanism mechanism, int opmode, Key key) throws PKIException {
        String mType = mechanism.getMechanismType();

        Cipher cipher;
        try {
            cipher = Cipher.getInstance(mType, this.provider);
        } catch (NoSuchAlgorithmException var10) {
            NoSuchAlgorithmException e = var10;
            throw new PKIException(PKIException.JHARDLIB + ": " + e.getMessage(), e);
        } catch (NoSuchPaddingException var11) {
            NoSuchPaddingException e = var11;
            throw new PKIException(PKIException.JHARDLIB + ": " + e.getMessage(), e);
        }

        try {
            if (mType.indexOf("CBC") != -1) {
                CBCParam cbcParam = (CBCParam) mechanism.getParam();
                if (cbcParam == null) {
                    throw new IllegalArgumentException("mechanism missing param");
                }

                IvParameterSpec iv = new IvParameterSpec(cbcParam.getIv());
                cipher.init(opmode, key, iv);
            } else {
                cipher.init(opmode, key);
            }

            return cipher;
        } catch (InvalidKeyException var8) {
            InvalidKeyException e = var8;
            throw new PKIException(PKIException.JHARDLIB + ": " + e.getMessage(), e);
        } catch (InvalidAlgorithmParameterException var9) {
            InvalidAlgorithmParameterException e = var9;
            throw new PKIException(PKIException.JHARDLIB + ": " + e.getMessage(), e);
        }
    }
}
