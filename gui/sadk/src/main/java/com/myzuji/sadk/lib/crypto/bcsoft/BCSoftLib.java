package com.myzuji.sadk.lib.crypto.bcsoft;

import com.myzuji.sadk.algorithm.common.Mechanism;
import com.myzuji.sadk.algorithm.common.PKIException;
import com.myzuji.sadk.algorithm.sm2.SM2PrivateKey;
import com.myzuji.sadk.algorithm.sm2.SM2PublicKey;
import com.myzuji.sadk.algorithm.util.BigFileCipherUtil;
import com.myzuji.sadk.algorithm.util.HashEncoderUtil;
import com.myzuji.sadk.algorithm.util.RSAAndItsCloseSymAlgUtil;
import com.myzuji.sadk.algorithm.util.SM2AndItsCloseSymAlgUtil;
import com.myzuji.sadk.jcajce.asymmetric.keypairt.SM2KeyPairGenerator;
import com.myzuji.sadk.lib.crypto.jni.Session;
import com.myzuji.sadk.org.bouncycastle.crypto.digests.SM3Digest;
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
import java.security.*;

public class BCSoftLib implements Session {
    public BCSoftLib() {
    }

    public KeyPair generateKeyPair(Mechanism keyType, int keyLength) throws PKIException {
        try {
            if (Mechanisms.isSM2Type(keyType)) {
                SM2KeyPairGenerator generator = new SM2KeyPairGenerator();
                generator.initialize(256, new SecureRandom());
                return generator.generateKeyPair();
            } else if (Mechanisms.isRSAType(keyType)) {

                KeyPairGenerator keyPairGen;
                try {
                    keyPairGen = KeyPairGenerator.getInstance("RSA");
                } catch (Exception var5) {
                    return null;
                }

                if (keyLength > 0 && keyLength <= 4096) {
                    keyPairGen.initialize(keyLength);
                    KeyPair keyPair = keyPairGen.generateKeyPair();
                    return keyPair;
                } else {
                    throw new PKIException("key length is illgal:" + keyLength);
                }
            } else {
                throw new PKIException(PKIException.BC_KEY_PAIR, PKIException.BC_KEY_PAIR_DES + " " + PKIException.NOT_SUP_DES + keyType.getMechanismType());
            }
        } catch (Exception e) {
            throw new PKIException("GenerateKeyPair Failure", e);
        }
    }

    public byte[] sign(Mechanism mechanism, PrivateKey priKey, byte[] sourceData) throws PKIException {
        if (!Mechanisms.isValid(mechanism)) {
            throw new PKIException(PKIException.SIGN, PKIException.SIGN_DES + " " + PKIException.NOT_SUP_DES + " " + mechanism);
        } else if (sourceData == null) {
            throw new PKIException("the source data is null!");
        } else {
            SM2PublicKey sm2PubKey;
            try {
                if (Mechanisms.isSM2WithSM3(mechanism)) {
                    sm2PubKey = null;
                    SM2PrivateKey sm2priKey = this.SM2PrivateKeyFrom(priKey);
                    byte[] hash = new byte[32];
                    if (SM2ContextConfig.getUseZValue()) {
                        sm2PubKey = sm2priKey.getSM2PublicKey();
                        this.SM2HashMessage(sm2PubKey, true, sourceData, hash);
                    } else {
                        this.SM2HashMessage(sm2PubKey, false, sourceData, hash);
                    }

                    return SM2PackageUtil.encryptByBC(hash, sm2priKey);
                } else {
                    byte[] out = HashUtil.RSAHashMessageByBC(sourceData, mechanism, true);
                    return RSAPackageUtil.encrypt(out, priKey);
                }
            } catch (Exception var7) {
                throw new PKIException(PKIException.SIGN, PKIException.SIGN_DES, var7);
            }
        }
    }

    public byte[] sign(Mechanism mechanism, PrivateKey priKey, InputStream sourceStream) throws PKIException {
        if (!Mechanisms.isValid(mechanism)) {
            throw new PKIException(PKIException.SIGN, PKIException.SIGN_DES + " " + PKIException.NOT_SUP_DES + " " + mechanism);
        } else {
            SM2PublicKey sm2PubKey;
            try {
                if (Mechanisms.isSM2WithSM3(mechanism)) {
                    sm2PubKey = null;
                    SM2PrivateKey sm2priKey = this.SM2PrivateKeyFrom(priKey);
                    byte[] hash = new byte[32];
                    if (SM2ContextConfig.getUseZValue()) {
                        sm2PubKey = sm2priKey.getSM2PublicKey();
                        this.SM2HashFile(sm2PubKey, true, sourceStream, hash);
                    } else {
                        this.SM2HashFile(sm2PubKey, false, sourceStream, hash);
                    }

                    return SM2PackageUtil.encryptByBC(hash, sm2priKey);
                } else {
                    byte[] digestData = HashUtil.RSAHashFileByBC(sourceStream, mechanism, true);
                    return RSAPackageUtil.encrypt(digestData, priKey);
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
                byte[] hash;
                if (Mechanisms.isSM2WithSM3(mechanism)) {
                    SM2PublicKey sm2PubKey = this.SM2PublicKeyFrom(pubKey);
                    hash = new byte[32];
                    this.SM2HashMessage(sm2PubKey, true, sourceData, hash);
                    boolean verifyResult = SM2PackageUtil.verifyByBC(hash, signData, pubKey);
                    boolean compatible = CompatibleAlgorithm.isCompatibleSM2WithoutZ();
                    if (compatible && !verifyResult) {
                        this.SM2HashMessage(sm2PubKey, false, sourceData, hash);
                        verifyResult = SM2PackageUtil.verifyByBC(hash, signData, pubKey);
                    }

                    return verifyResult;
                } else {
                    byte[] hashData = HashUtil.RSAHashMessageByBC(sourceData, mechanism, true);
                    hash = RSAPackageUtil.decrypt(signData, pubKey);
                    return RSAPackageUtil.isRSAHashEqual(hash, hashData);
                }
            } catch (Exception var9) {
                Exception e = var9;
                e.printStackTrace();
                throw new PKIException(PKIException.VERIFY_SIGN, PKIException.VERIFY_SIGN_DES, e);
            }
        }
    }

    public boolean verify(Mechanism mechanism, PublicKey pubKey, InputStream sourceStream, byte[] signData) throws PKIException {
        if (!Mechanisms.isValid(mechanism)) {
            throw new PKIException(PKIException.SIGN, PKIException.SIGN_DES + " " + PKIException.NOT_SUP_DES + " " + mechanism);
        } else {
            try {
                if (Mechanisms.isSM2WithSM3(mechanism)) {
                    SM2PublicKey sm2PubKey = this.SM2PublicKeyFrom(pubKey);
                    boolean compatible = CompatibleAlgorithm.isCompatibleSM2WithoutZ();
                    byte[] hashWithZ = new byte[32];
                    byte[] hashWithoutZ = new byte[32];
                    this.SM2HashFile(sm2PubKey, compatible, sourceStream, hashWithZ, hashWithoutZ);
                    boolean verifyResult = SM2PackageUtil.verifyByBC(hashWithZ, signData, pubKey);
                    if (compatible && !verifyResult) {
                        verifyResult = SM2PackageUtil.verifyByBC(hashWithoutZ, signData, pubKey);
                    }

                    return verifyResult;
                } else {
                    byte[] hashData = HashUtil.RSAHashFileByBC(sourceStream, mechanism, true);
                    byte[] sig = RSAPackageUtil.decrypt(signData, pubKey);
                    return RSAPackageUtil.isRSAHashEqual(sig, hashData);
                }
            } catch (Exception var10) {
                Exception e = var10;
                throw new PKIException(PKIException.VERIFY_SIGN, PKIException.VERIFY_SIGN_DES, e);
            }
        }
    }

    public byte[] encrypt(Mechanism mechanism, Key key, byte[] sourceData) throws PKIException {
        try {
            String mType = mechanism.getMechanismType();
            if (mType.equals("SM2")) {
                SM2PublicKey sm2PubKey = this.SM2PublicKeyFrom(key);
                return SM2AndItsCloseSymAlgUtil.sm2Encrypt(true, sm2PubKey, sourceData);
            } else if (mType.equals("RSA/ECB/PKCS1PADDING")) {
                return RSAAndItsCloseSymAlgUtil.rsaEncrypt(true, key, sourceData);
            } else if (!mType.equals("DESede/CBC/PKCS7Padding") && !mType.equals("DESede/ECB/PKCS7Padding") && !mType.equals("RC4")) {
                if (!mType.equals("SM4/ECB/PKCS7Padding") && !mType.equals("SM4/CBC/PKCS7Padding")) {
                    throw new PKIException(PKIException.ENCRYPT, PKIException.ENCRYPT_DES + " " + PKIException.NOT_SUP_DES + mType);
                } else {
                    return SM2AndItsCloseSymAlgUtil.crypto(false, true, key.getEncoded(), sourceData, mechanism);
                }
            } else {
                return RSAAndItsCloseSymAlgUtil.crypto(false, true, key.getEncoded(), sourceData, mechanism);
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
                return SM2AndItsCloseSymAlgUtil.sm2Encrypt(false, sm2priKey, encryptData);
            } else if (mType.equals("RSA/ECB/PKCS1PADDING")) {
                return RSAAndItsCloseSymAlgUtil.rsaEncrypt(false, key, encryptData);
            } else if (!mType.equals("DESede/CBC/PKCS7Padding") && !mType.equals("DESede/ECB/PKCS7Padding") && !mType.equals("RC4")) {
                if (!mType.equals("SM4/ECB/PKCS7Padding") && !mType.equals("SM4/CBC/PKCS7Padding")) {
                    throw new PKIException(PKIException.DECRYPT, PKIException.DECRYPT_DES + " " + PKIException.NOT_SUP_DES + mType);
                } else {
                    return SM2AndItsCloseSymAlgUtil.crypto(false, false, key.getEncoded(), encryptData, mechanism);
                }
            } else {
                return RSAAndItsCloseSymAlgUtil.crypto(false, false, key.getEncoded(), encryptData, mechanism);
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
                    return SM2PackageUtil.encryptByBC(digest, sm2priKey);
                } else {
                    byte[] derDigest = HashEncoderUtil.derEncoder(mechanism, digest);
                    return RSAPackageUtil.encrypt(derDigest, priKey);
                }
            } catch (Exception var5) {
                Exception e = var5;
                throw new PKIException(PKIException.SIGN, PKIException.SIGN_DES, e);
            }
        }
    }

    public boolean verifyByHash(Mechanism mechanism, PublicKey pubKey, byte[] digest, byte[] signData) throws PKIException {
        if (!Mechanisms.isValid(mechanism)) {
            throw new PKIException(PKIException.SIGN, PKIException.SIGN_DES + " " + PKIException.NOT_SUP_DES + " " + mechanism);
        } else {
            try {
                if (Mechanisms.isSM2WithSM3(mechanism)) {
                    SM2PublicKey sm2PubKey = this.SM2PublicKeyFrom(pubKey);
                    return SM2PackageUtil.verifyByBC(digest, signData, sm2PubKey);
                } else {
                    byte[] sig = RSAPackageUtil.decrypt(signData, pubKey);
                    byte[] derDigest = HashEncoderUtil.derEncoder(mechanism, digest);
                    return RSAPackageUtil.isRSAHashEqual(sig, derDigest);
                }
            } catch (Exception var7) {
                Exception e = var7;
                throw new PKIException(PKIException.VERIFY_SIGN, PKIException.VERIFY_SIGN_DES, e);
            }
        }
    }

    public void encrypt(Mechanism mechanism, Key key, InputStream sourceStream, OutputStream encryptStream) throws PKIException {
        try {
            BigFileCipherUtil.bigFileBlockCipher(true, mechanism, key.getEncoded(), sourceStream, encryptStream);
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

    public Key generateKey(Mechanism keyType, byte[] keyData) throws PKIException {
        return KeyUtil.generateKey(keyType, keyData);
    }

    private final void SM2HashFile(SM2PublicKey sm2PubKey, boolean supportedWithoutZ, InputStream stream, byte[] hashWithZ, byte[] hashWithoutZ) throws Exception {
        byte[] buffer = new byte[65536];
        SM3Digest engineWithZ = new SM3Digest();
        engineWithZ.update(sm2PubKey.getDefaultZ(), 0, 32);
        SM3Digest engineWithoutZ = null;
        if (supportedWithoutZ) {
            engineWithoutZ = new SM3Digest();
        }

        int rLength = 1;

        while ((rLength = stream.read(buffer, 0, buffer.length)) != -1) {
            engineWithZ.update(buffer, 0, rLength);
            if (supportedWithoutZ) {
                engineWithoutZ.update(buffer, 0, rLength);
            }
        }

        engineWithZ.doFinal(hashWithZ, 0);
        if (supportedWithoutZ) {
            engineWithoutZ.doFinal(hashWithoutZ, 0);
        }

    }

    private final void SM2HashFile(SM2PublicKey sm2PubKey, boolean withZ, InputStream stream, byte[] hash) throws Exception {
        byte[] buffer = new byte[65536];
        SM3Digest engine = new SM3Digest();
        if (withZ) {
            engine.update(sm2PubKey.getDefaultZ(), 0, 32);
        }

        int rLength = 1;

        while ((rLength = stream.read(buffer, 0, buffer.length)) != -1) {
            engine.update(buffer, 0, rLength);
        }

        engine.doFinal(hash, 0);
    }

    private final void SM2HashMessage(SM2PublicKey sm2PubKey, boolean withZ, byte[] message, byte[] hash) {
        SM3Digest engine = new SM3Digest();
        if (withZ) {
            engine.update(sm2PubKey.getDefaultZ(), 0, 32);
        }

        engine.update(message, 0, message.length);
        engine.doFinal(hash, 0);
    }

    private final SM2PublicKey SM2PublicKeyFrom(Key key) {
        SM2PublicKey sm2Key = null;
        if (key instanceof SM2PublicKey) {
            sm2Key = (SM2PublicKey) key;
        } else {
            sm2Key = new SM2PublicKey(key.getEncoded());
        }

        return sm2Key;
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

    public Provider getProvider() {
        return null;
    }

    public String getProviderName() {
        return null;
    }
}
