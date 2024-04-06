package com.myzuji.sadk.util;

import com.myzuji.sadk.algorithm.common.Mechanism;
import com.myzuji.sadk.algorithm.common.PKIException;
import com.myzuji.sadk.algorithm.sm2.SM2PrivateKey;
import com.myzuji.sadk.algorithm.sm2.SM2PublicKey;
import com.myzuji.sadk.algorithm.util.InitKeyStore;
import com.myzuji.sadk.asn1.pkcs.PKCS12;
import com.myzuji.sadk.asn1.pkcs.PKCS12_SM2;
import com.myzuji.sadk.lib.crypto.jni.Session;
import com.myzuji.sadk.system.FileHelper;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;

public class KeyUtil {
    public KeyUtil() {
    }

    public static SM2PrivateKey getPrivateKeyFromSM2(String sm2FilePath, String sm2FilePwd) throws PKIException {
        if (sm2FilePath == null) {
            throw new PKIException("SM2File sm2FilePath should not be null");
        } else if (sm2FilePwd == null) {
            throw new PKIException("SM2File sm2FilePwd should not be null");
        } else {
            byte[] encoding;
            try {
                encoding = FileHelper.read(sm2FilePath);
            } catch (Exception var4) {
                Exception e = var4;
                throw new PKIException("SM2File read failure", e);
            }

            return getPrivateKeyFromSM2(encoding, sm2FilePwd);
        }
    }

    public static SM2PrivateKey getPrivateKeyFromSM2(InputStream sm2FileInputStream, String sm2FilePwd) throws PKIException {
        if (sm2FileInputStream == null) {
            throw new PKIException("SM2File sm2FileInputStream should not be null");
        } else {
            int dLength;
            try {
                dLength = sm2FileInputStream.available();
            } catch (IOException var6) {
                IOException e = var6;
                throw new PKIException("SM2File sm2FileInputStream invalid", e);
            }

            if (dLength > 1024000) {
                throw new PKIException("SM2File sm2FileInputStream too large");
            } else {
                byte[] encoding;
                try {
                    encoding = new byte[dLength];
                    sm2FileInputStream.read(encoding);
                } catch (Exception var5) {
                    Exception e = var5;
                    throw new PKIException("SM2File sm2FileInputStream read failure", e);
                }

                return getPrivateKeyFromSM2(encoding, sm2FilePwd);
            }
        }
    }

    public static SM2PrivateKey getPrivateKeyFromSM2(byte[] sm2FileData, String sm2FilePwd) throws PKIException {
        if (sm2FileData == null) {
            throw new PKIException("SM2File sm2FileData should not be null");
        } else if (sm2FilePwd == null) {
            throw new PKIException("SM2File sm2FilePwd should not be null");
        } else {
            PKCS12_SM2 P12 = new PKCS12_SM2(sm2FileData);
            return P12.getPrivateKey(sm2FilePwd);
        }
    }

    public static PrivateKey getPrivateKeyFromPFX(String pfxFilePath, String pfxFilePwd) throws PKIException {
        if (pfxFilePath == null) {
            throw new PKIException("PFXFile pfxFilePath should not be null");
        } else {
            byte[] encoding;
            try {
                encoding = FileHelper.read(pfxFilePath);
            } catch (Exception var4) {
                Exception e = var4;
                throw new PKIException("PFXFile read failure", e);
            }

            return getPrivateKeyFromPFX(encoding, pfxFilePwd);
        }
    }

    public static PrivateKey getPrivateKeyFromPFX(InputStream pfxFileInputStream, String pfxFilePwd) throws PKIException {
        if (pfxFileInputStream == null) {
            throw new PKIException("PFXFile pfxFileInputStream should not be null");
        } else {
            int dLength;
            try {
                dLength = pfxFileInputStream.available();
            } catch (IOException var6) {
                IOException e = var6;
                throw new PKIException("PFXFile pfxFileInputStream invalid", e);
            }

            if (dLength > 1024000) {
                throw new PKIException("PFXFile pfxFileInputStream too large");
            } else {
                byte[] encoding;
                try {
                    encoding = new byte[dLength];
                    pfxFileInputStream.read(encoding);
                } catch (Exception var5) {
                    Exception e = var5;
                    throw new PKIException("PFXFile pfxFileInputStream read failure", e);
                }

                return getPrivateKeyFromPFX(encoding, pfxFilePwd);
            }
        }
    }

    public static PrivateKey getPrivateKeyFromPFX(byte[] pfxFileData, String pfxFilePwd) throws PKIException {
        if (pfxFileData == null) {
            throw new PKIException("PFXFile pfxFileData should not be null");
        } else if (pfxFilePwd == null) {
            throw new PKIException("PFXFile pfxFilePwd should not be null");
        } else {
            PKCS12 pkcs12 = new PKCS12(pfxFileData);
            return pkcs12.decrypt(pfxFilePwd.toCharArray());
        }
    }

    public static PrivateKey getPrivateKeyFromJKS(String jksFilePath, String jksFilePwd, String alias) throws PKIException {
        if (jksFilePath == null) {
            throw new PKIException("JKSFile jksFilePath should not be null");
        } else if (jksFilePwd == null) {
            throw new PKIException("JKSFile jksFilePwd should not be null");
        } else if (alias == null) {
            throw new PKIException("JKSFile alias should not be null");
        } else {
            try {
                KeyStore ks = InitKeyStore.initJKSKeyStore(jksFilePath, jksFilePwd);
                char[] password = jksFilePwd.toCharArray();
                return (PrivateKey) ks.getKey(alias, password);
            } catch (PKIException var5) {
                PKIException e = var5;
                throw e;
            } catch (Exception var6) {
                Exception e = var6;
                throw new PKIException("JKSFile Parsed Failure", e);
            }
        }
    }

    public static PrivateKey getPrivateKeyFromJKS(InputStream jksInputStream, String jksFilePwd, String alias) throws PKIException {
        if (jksInputStream == null) {
            throw new PKIException("JKSFile jksInputStream should not be null");
        } else if (jksFilePwd == null) {
            throw new PKIException("JKSFile jksFilePwd should not be null");
        } else if (alias == null) {
            throw new PKIException("JKSFile alias should not be null");
        } else {
            try {
                KeyStore ks = InitKeyStore.initJKSKeyStore(jksInputStream, jksFilePwd);
                char[] password = jksFilePwd.toCharArray();
                return (PrivateKey) ks.getKey(alias, password);
            } catch (PKIException var5) {
                PKIException e = var5;
                throw e;
            } catch (Exception var6) {
                Exception e = var6;
                throw new PKIException("JKSFile Parsed Failure", e);
            }
        }
    }

    public static KeyPair generateKeyPair(Mechanism keypairType, int bitLength, Session session) throws PKIException {
        if ("SM2".equals(keypairType.getMechanismType())) {
            bitLength = 256;
        }

        return session.generateKeyPair(keypairType, bitLength);
    }

    public static Key generateKey(Mechanism symmetricKeyType, Session session) throws PKIException {
        return session.generateKey(symmetricKeyType);
    }

    public static Key generateKey(Mechanism symmetricKeyType, byte[] keyData, Session session) throws PKIException {
        return session.generateKey(symmetricKeyType, keyData);
    }

    public static SM2PublicKey getSM2PublicKey(byte[] pubX, byte[] pubY) {
        return new SM2PublicKey(pubX, pubY);
    }

    public static SM2PrivateKey getSM2PrivateKey(byte[] da, byte[] pubX, byte[] pubY) {
        return new SM2PrivateKey(da, pubX, pubY);
    }

    public static final Key generateKey(Mechanism keyType, byte[] keyData) throws PKIException {
        String type = keyType.getMechanismType();
        if ("DESede".equals(type)) {
            if (keyData.length != 24) {
                throw new PKIException("DES3 KEY must be 24 bytes");
            } else {
                return new SecretKeySpec(keyData, type);
            }
        } else if ("SM4".equals(type)) {
            if (keyData.length != 16) {
                throw new PKIException("SM4 KEY must be 16 bytes");
            } else {
                return new SecretKeySpec(keyData, type);
            }
        } else if (type.equals("RC4")) {
            if (keyData.length != 16) {
                throw new PKIException("RC4 KEY must be 16 bytes");
            } else {
                return new SecretKeySpec(keyData, type);
            }
        } else {
            throw new PKIException("do not support this key type:" + type);
        }
    }
}
