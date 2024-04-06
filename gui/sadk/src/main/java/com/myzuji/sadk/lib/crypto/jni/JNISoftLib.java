package com.myzuji.sadk.lib.crypto.jni;

import com.myzuji.sadk.algorithm.common.Mechanism;
import com.myzuji.sadk.algorithm.common.PKIException;
import com.myzuji.sadk.algorithm.sm2.SM2PrivateKey;
import com.myzuji.sadk.algorithm.sm2.SM2PublicKey;
import com.myzuji.sadk.algorithm.util.BigFileCipherUtil;
import com.myzuji.sadk.algorithm.util.HashEncoderUtil;
import com.myzuji.sadk.algorithm.util.RSAAndItsCloseSymAlgUtil;
import com.myzuji.sadk.algorithm.util.SM2AndItsCloseSymAlgUtil;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ECDomainParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ECPublicKeyParameters;
import com.myzuji.sadk.org.bouncycastle.jcajce.provider.asymmetric.sm.SM2Params;
import com.myzuji.sadk.org.bouncycastle.math.ec.ECPoint;
import com.myzuji.sadk.signature.rsa.RSAPackageUtil;
import com.myzuji.sadk.signature.sm2.SM2PackageUtil;
import com.myzuji.sadk.system.CompatibleAlgorithm;
import com.myzuji.sadk.system.Mechanisms;
import com.myzuji.sadk.system.global.SM2ContextConfig;
import com.myzuji.sadk.util.HashUtil;
import com.myzuji.sadk.util.KeyUtil;

import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.*;

public class JNISoftLib implements Session {
    public JNISoftLib() {
    }

    public KeyPair generateKeyPair(Mechanism mechanism, int keyLength) throws PKIException {
        try {
            if (Mechanisms.isSM2Type(mechanism)) {
                byte[] dBytes = new byte[32];
                byte[] xBytes = new byte[32];
                byte[] yBytes = new byte[32];

                try {
                    JNISM2.generateKeypair(dBytes, xBytes, yBytes);
                } catch (PKIException var11) {
                    PKIException e = var11;
                    throw e;
                } catch (Exception var12) {
                    Exception e = var12;
                    throw new PKIException("Generate KeyPair failure", e);
                }

                ECDomainParameters spec = SM2Params.sm2DomainParameters;
                ECPoint pubPoint = spec.getCurve().createPoint(new BigInteger(1, xBytes), new BigInteger(1, yBytes));
                ECPublicKeyParameters pubParams = new ECPublicKeyParameters(pubPoint, spec);
                SM2PublicKey pubKey = new SM2PublicKey(pubParams);
                SM2PrivateKey priKey = new SM2PrivateKey(new ECPrivateKeyParameters(new BigInteger(1, dBytes), spec), pubParams);
                return new KeyPair(pubKey, priKey);
            } else if (Mechanisms.isRSAType(mechanism)) {

                KeyPairGenerator keyPairGen;
                try {
                    keyPairGen = KeyPairGenerator.getInstance("RSA");
                } catch (Exception var13) {
                    return null;
                }

                if (keyLength != 1024 && keyLength != 2048 && keyLength != 4096) {
                    keyLength = 1024;
                }

                keyPairGen.initialize(keyLength);
                KeyPair keyPair = keyPairGen.generateKeyPair();
                return keyPair;
            } else {
                throw new PKIException(PKIException.JNI_KEY_PAIR, PKIException.JNI_KEY_PAIR_DES + " " + PKIException.NOT_SUP_DES + mechanism.getMechanismType());
            }
        } catch (PKIException var14) {
            PKIException e = var14;
            throw e;
        } catch (Exception e) {
            throw new PKIException("Generate KeyPair failure", e);
        }
    }

    public byte[] sign(Mechanism mechanism, PrivateKey priKey, byte[] sourceData) throws PKIException {
        if (!Mechanisms.isValid(mechanism)) {
            throw new PKIException(PKIException.SIGN, PKIException.SIGN_DES + " " + PKIException.NOT_SUP_DES + " " + mechanism);
        } else if (sourceData == null) {
            throw new PKIException("the source data is null!");
        } else {
            try {
                if (Mechanisms.isSM2WithSM3(mechanism)) {
                    SM2PublicKey sm2PubKey = null;
                    SM2PrivateKey sm2priKey = this.SM2PrivateKeyFrom(priKey);
                    byte[] hash = new byte[32];
                    if (SM2ContextConfig.getUseZValue()) {
                        sm2PubKey = sm2priKey.getSM2PublicKey();
                        this.SM2HashMessage(sm2PubKey, true, sourceData, hash);
                    } else {
                        this.SM2HashMessage(sm2PubKey, false, sourceData, hash);
                    }

                    return SM2PackageUtil.encryptByJNI(hash, sm2priKey.dBigInteger());
                } else {
                    byte[] out = HashUtil.RSAHashMessageByJNI(sourceData, mechanism, true);
                    return RSAPackageUtil.encryptByJNI(out, priKey);
                }
            } catch (Exception e) {
                throw new PKIException(PKIException.SIGN, PKIException.SIGN_DES, e);
            }
        }
    }

    public byte[] sign(Mechanism mechanism, PrivateKey priKey, InputStream sourceStream) throws PKIException {
        if (!Mechanisms.isValid(mechanism)) {
            throw new PKIException(PKIException.SIGN, PKIException.SIGN_DES + " " + PKIException.NOT_SUP_DES + " " + mechanism);
        } else {
            try {
                if (Mechanisms.isSM2WithSM3(mechanism)) {
                    SM2PublicKey sm2PubKey = null;
                    SM2PrivateKey sm2priKey = this.SM2PrivateKeyFrom(priKey);
                    byte[] hash = new byte[32];
                    if (SM2ContextConfig.getUseZValue()) {
                        sm2PubKey = sm2priKey.getSM2PublicKey();
                        this.SM2HashFile(sm2PubKey, true, sourceStream, hash);
                    } else {
                        this.SM2HashFile(sm2PubKey, false, sourceStream, hash);
                    }

                    return SM2PackageUtil.encryptByJNI(hash, sm2priKey.dBigInteger());
                } else {
                    byte[] digestData = HashUtil.RSAHashFileByJNI(sourceStream, mechanism, true);
                    return RSAPackageUtil.encryptByJNI(digestData, priKey);
                }
            } catch (Exception e) {
                throw new PKIException(PKIException.SIGN, PKIException.SIGN_DES, e);
            }
        }
    }

    public boolean verify(Mechanism mechanism, PublicKey pubKey, byte[] sourceData, byte[] signData) throws PKIException {
        if (!Mechanisms.isValid(mechanism)) {
            throw new PKIException(PKIException.SIGN, PKIException.SIGN_DES + " " + PKIException.NOT_SUP_DES + " " + mechanism);
        } else {
            try {
                byte[] pubX;
                if (Mechanisms.isSM2WithSM3(mechanism)) {
                    SM2PublicKey sm2PubKey = this.SM2PublicKeyFrom(pubKey);
                    pubX = sm2PubKey.xBytes();
                    byte[] pubY = sm2PubKey.yBytes();
                    byte[] hash = new byte[32];
                    this.SM2HashMessage(sm2PubKey, true, sourceData, hash);
                    boolean verifyResult = SM2PackageUtil.verifyByJNI(hash, signData, pubX, pubY);
                    boolean supportedWithoutZ = CompatibleAlgorithm.isCompatibleSM2WithoutZ();
                    if (supportedWithoutZ && !verifyResult) {
                        this.SM2HashMessage(sm2PubKey, false, sourceData, hash);
                        verifyResult = SM2PackageUtil.verifyByJNI(hash, signData, pubX, pubY);
                    }

                    return verifyResult;
                } else {
                    byte[] hashData = HashUtil.RSAHashMessageByJNI(sourceData, mechanism, true);
                    pubX = RSAPackageUtil.decryptByJNI(signData, pubKey);
                    return RSAPackageUtil.isRSAHashEqual(pubX, hashData);
                }
            } catch (Exception var11) {
                Exception e = var11;
                throw new PKIException(PKIException.VERIFY_SIGN, PKIException.VERIFY_SIGN_DES, e);
            }
        }
    }

    public boolean verify(Mechanism mechanism, PublicKey pubKey, InputStream sourceStream, byte[] signData) throws PKIException {
        if (!Mechanisms.isValid(mechanism)) {
            throw new PKIException(PKIException.SIGN, PKIException.SIGN_DES + " " + PKIException.NOT_SUP_DES + " " + mechanism);
        } else {
            try {
                byte[] pubX;
                if (Mechanisms.isSM2WithSM3(mechanism)) {
                    SM2PublicKey sm2PubKey = this.SM2PublicKeyFrom(pubKey);
                    pubX = sm2PubKey.xBytes();
                    byte[] pubY = sm2PubKey.yBytes();
                    boolean compatible = CompatibleAlgorithm.isCompatibleSM2WithoutZ();
                    byte[] hashWithZ = new byte[32];
                    byte[] hashWithoutZ = new byte[32];
                    this.SM2HashFile(sm2PubKey, compatible, sourceStream, hashWithZ, hashWithoutZ);
                    boolean verifyResult = SM2PackageUtil.verifyByJNI(hashWithZ, signData, pubX, pubY);
                    if (compatible && !verifyResult) {
                        verifyResult = SM2PackageUtil.verifyByJNI(hashWithoutZ, signData, pubX, pubY);
                    }

                    return verifyResult;
                } else {
                    byte[] hashData = HashUtil.RSAHashFileByJNI(sourceStream, mechanism, true);
                    pubX = RSAPackageUtil.decryptByJNI(signData, pubKey);
                    return RSAPackageUtil.isRSAHashEqual(pubX, hashData);
                }
            } catch (Exception var12) {
                Exception e = var12;
                throw new PKIException(PKIException.VERIFY_SIGN, PKIException.VERIFY_SIGN_DES, e);
            }
        }
    }

    public byte[] encrypt(Mechanism mechanism, Key key, byte[] sourceData) throws PKIException {
        try {
            String mType = mechanism.getMechanismType();
            if (mType.equals("SM2")) {
                SM2PublicKey sm2PubKey = this.SM2PublicKeyFrom(key);
                return SM2AndItsCloseSymAlgUtil.sm2EncryptByJNI(true, sm2PubKey, sourceData);
            } else if (mType.equals("RSA/ECB/PKCS1PADDING")) {
                return RSAAndItsCloseSymAlgUtil.rsaEncryptByJNI(true, key, sourceData);
            } else if (!mType.equals("DESede/CBC/PKCS7Padding") && !mType.equals("DESede/ECB/PKCS7Padding") && !mType.equals("RC4")) {
                if (!mType.equals("SM4/ECB/PKCS7Padding") && !mType.equals("SM4/CBC/PKCS7Padding")) {
                    throw new PKIException(PKIException.ENCRYPT, PKIException.ENCRYPT_DES + " " + PKIException.NOT_SUP_DES + mType);
                } else {
                    return SM2AndItsCloseSymAlgUtil.crypto(true, true, key.getEncoded(), sourceData, mechanism);
                }
            } else {
                return RSAAndItsCloseSymAlgUtil.crypto(true, true, key.getEncoded(), sourceData, mechanism);
            }
        } catch (Exception var6) {
            Exception e = var6;
            throw new PKIException(PKIException.ENCRYPT, PKIException.ENCRYPT_DES, e);
        }
    }

    public byte[] decrypt(Mechanism mechanism, Key key, byte[] encryptData) throws PKIException {
        try {
            String mType = mechanism.getMechanismType();
            if (mType.equals("SM2")) {
                SM2PrivateKey sm2priKey = this.SM2PrivateKeyFrom(key);
                return SM2AndItsCloseSymAlgUtil.sm2EncryptByJNI(false, sm2priKey, encryptData);
            } else if (mType.equals("RSA/ECB/PKCS1PADDING")) {
                return RSAAndItsCloseSymAlgUtil.rsaEncryptByJNI(false, key, encryptData);
            } else if (!mType.equals("DESede/CBC/PKCS7Padding") && !mType.equals("DESede/ECB/PKCS7Padding") && !mType.equals("RC4")) {
                if (!mType.equals("SM4/ECB/PKCS7Padding") && !mType.equals("SM4/CBC/PKCS7Padding")) {
                    throw new PKIException(PKIException.DECRYPT, PKIException.DECRYPT_DES + " " + PKIException.NOT_SUP_DES + mType);
                } else {
                    return SM2AndItsCloseSymAlgUtil.crypto(true, false, key.getEncoded(), encryptData, mechanism);
                }
            } else {
                return RSAAndItsCloseSymAlgUtil.crypto(true, false, key.getEncoded(), encryptData, mechanism);
            }
        } catch (Exception var6) {
            Exception e = var6;
            throw new PKIException(PKIException.DECRYPT, PKIException.DECRYPT_DES, e);
        }
    }

    public byte[] signByHash(Mechanism mechanism, PrivateKey priKey, byte[] digest) throws PKIException {
        if (!Mechanisms.isValid(mechanism)) {
            throw new PKIException(PKIException.SIGN, PKIException.SIGN_DES + " " + PKIException.NOT_SUP_DES + " " + mechanism);
        } else if (digest == null) {
            throw new PKIException("the hash data is null!");
        } else {
            try {
                if (Mechanisms.isSM2WithSM3(mechanism)) {
                    SM2PrivateKey sm2priKey = this.SM2PrivateKeyFrom(priKey);
                    return SM2PackageUtil.encryptByJNI(digest, sm2priKey.dBigInteger());
                } else {
                    byte[] derDigest = HashEncoderUtil.derEncoder(mechanism, digest);
                    return RSAPackageUtil.encryptByJNI(derDigest, priKey);
                }
            } catch (Exception var5) {
                Exception e = var5;
                throw new PKIException(PKIException.SIGN, PKIException.SIGN_DES, e);
            }
        }
    }

    public boolean verifyByHash(Mechanism mechanism, PublicKey pubKey, byte[] hash, byte[] signData) throws PKIException {
        if (!Mechanisms.isValid(mechanism)) {
            throw new PKIException(PKIException.SIGN, PKIException.SIGN_DES + " " + PKIException.NOT_SUP_DES + " " + mechanism);
        } else {
            try {
                if (Mechanisms.isSM2WithSM3(mechanism)) {
                    SM2PublicKey sm2PubKey = this.SM2PublicKeyFrom(pubKey);
                    return SM2PackageUtil.verifyByJNI(hash, signData, sm2PubKey.xBytes(), sm2PubKey.yBytes());
                } else {
                    byte[] sig = RSAPackageUtil.decryptByJNI(signData, pubKey);
                    byte[] derDigest = HashEncoderUtil.derEncoder(mechanism, hash);
                    return RSAPackageUtil.isRSAHashEqual(sig, derDigest);
                }
            } catch (Exception var7) {
                Exception e = var7;
                throw new PKIException(PKIException.VERIFY_SIGN, PKIException.VERIFY_SIGN_DES, e);
            }
        }
    }

    public void encrypt(Mechanism encryptAlg, Key key, InputStream sourceStream, OutputStream encryptStream) throws PKIException {
        try {
            BigFileCipherUtil.bigFileBlockCipher(true, encryptAlg, key.getEncoded(), sourceStream, encryptStream);
        } catch (PKIException var6) {
            PKIException e = var6;
            throw e;
        } catch (Exception var7) {
            Exception e = var7;
            throw new PKIException("Encrypt failure", e);
        }
    }

    public void decrypt(Mechanism encryptAlg, Key key, InputStream encryptStream, OutputStream plainTextStream) throws PKIException {
        try {
            BigFileCipherUtil.bigFileBlockCipher(false, encryptAlg, key.getEncoded(), encryptStream, plainTextStream);
        } catch (PKIException var6) {
            PKIException e = var6;
            throw e;
        } catch (Exception var7) {
            Exception e = var7;
            throw new PKIException("Decrypt failure", e);
        }
    }

    public Key generateKey(Mechanism keyType) throws PKIException {
        String type = keyType.getMechanismType();
        SecureRandom random = new SecureRandom();
        byte[] keyData;
        SecretKeySpec key;
        if (type.equals("DESede")) {
            keyData = new byte[24];
            random.nextBytes(keyData);
            key = new SecretKeySpec(keyData, type);
            return key;
        } else if (type.equals("SM4")) {
            keyData = new byte[16];
            random.nextBytes(keyData);
            key = new SecretKeySpec(keyData, type);
            return key;
        } else if (type.equals("RC4")) {
            keyData = new byte[16];
            random.nextBytes(keyData);
            key = new SecretKeySpec(keyData, type);
            return key;
        } else {
            throw new PKIException("do not support this key type:" + type);
        }
    }

    private final void SM2HashFile(SM2PublicKey sm2PubKey, boolean supportedWithoutZ, InputStream stream, byte[] hashWithZ, byte[] hashWithoutZ) throws Exception {
        byte[] buffer = new byte[65536];
        JNIDigest engineWithZ = new JNIDigest();
        engineWithZ.init(922);
        engineWithZ.update(sm2PubKey.getDefaultZ());
        JNIDigest engineWithoutZ = null;
        if (supportedWithoutZ) {
            engineWithoutZ = new JNIDigest();
            engineWithoutZ.init(922);
        }

        int rLength = 1;
        byte[] data = null;

        while ((rLength = stream.read(buffer, 0, buffer.length)) != -1) {
            if (rLength < buffer.length) {
                data = new byte[rLength];
                System.arraycopy(buffer, 0, data, 0, data.length);
            } else {
                data = buffer;
            }

            engineWithZ.update(data);
            if (supportedWithoutZ) {
                engineWithoutZ.update(data);
            }
        }

        engineWithZ.doFinal(hashWithZ);
        if (supportedWithoutZ) {
            engineWithoutZ.doFinal(hashWithoutZ);
        }

    }

    private final void SM2HashFile(SM2PublicKey sm2PubKey, boolean withZ, InputStream stream, byte[] hash) throws Exception {
        byte[] buffer = new byte[65536];
        JNIDigest engine = new JNIDigest();
        engine.init(922);
        if (withZ) {
            engine.update(sm2PubKey.getDefaultZ());
        }

        int rLength = 1;

        byte[] data;
        for (; (rLength = stream.read(buffer, 0, buffer.length)) != -1; engine.update(data)) {
            if (rLength < buffer.length) {
                data = new byte[rLength];
                System.arraycopy(buffer, 0, data, 0, data.length);
            } else {
                data = buffer;
            }
        }

        engine.doFinal(hash);
    }

    private final void SM2HashMessage(SM2PublicKey sm2PubKey, boolean withZ, byte[] message, byte[] hash) throws Exception {
        JNIDigest engine = new JNIDigest();
        engine.init(922);
        if (withZ) {
            engine.update(sm2PubKey.getDefaultZ());
        }

        engine.update(message);
        engine.doFinal(hash);
    }

    private final SM2PublicKey SM2PublicKeyFrom(Key key) {
        SM2PublicKey sm2PubKey = null;
        if (key instanceof SM2PublicKey) {
            sm2PubKey = (SM2PublicKey) key;
        } else {
            sm2PubKey = new SM2PublicKey(key.getEncoded());
        }

        return sm2PubKey;
    }

    private final SM2PrivateKey SM2PrivateKeyFrom(Key key) {
        SM2PrivateKey sm2Key = null;
        if (key instanceof SM2PrivateKey) {
            sm2Key = (SM2PrivateKey) key;
        } else {
            sm2Key = new SM2PrivateKey(key.getEncoded());
        }

        return sm2Key;
    }

    public Key generateKey(Mechanism keyType, byte[] keyData) throws PKIException {
        return KeyUtil.generateKey(keyType, keyData);
    }

    public Provider getProvider() {
        return null;
    }

    public String getProviderName() {
        return null;
    }
}
