package com.myzuji.sadk.util;

import com.myzuji.sadk.algorithm.common.PKIException;
import com.myzuji.sadk.algorithm.util.InitKeyStore;
import com.myzuji.sadk.algorithm.util.P7BParser;
import com.myzuji.sadk.algorithm.util.SM2OIDUtil;
import com.myzuji.sadk.asn1.pkcs.PKCS12;
import com.myzuji.sadk.asn1.pkcs.PKCS12_SM2;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.BasicConstraints;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.Extensions;
import com.myzuji.sadk.system.FileHelper;
import com.myzuji.sadk.x509.certificate.X509Cert;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;

public class CertUtil {
    public CertUtil() {
    }

    public static byte[] getCertExtensionData(X509Cert x509Cert, String oid) throws PKIException {
        if (x509Cert == null) {
            throw new IllegalArgumentException("x509Cert should not be null");
        } else if (oid == null) {
            throw new IllegalArgumentException("oid should not be null");
        } else {
            try {
                byte[] extension = x509Cert.getExtensionByteData(new ASN1ObjectIdentifier(oid));
                if (extension == null) {
                    return null;
                } else {
                    byte b = extension[1];
                    int blen = b & 128;
                    byte[] ret;
                    if (blen == 0) {
                        ret = new byte[extension.length - 2];
                        System.arraycopy(extension, 2, ret, 0, extension.length - 2);
                        return ret;
                    } else {
                        blen = b & 127;
                        ret = new byte[extension.length - 2 - blen];
                        System.arraycopy(extension, 2 + blen, ret, 0, extension.length - 2 - blen);
                        return ret;
                    }
                }
            } catch (PKIException var6) {
                PKIException e = var6;
                throw e;
            } catch (Exception var7) {
                Exception e = var7;
                throw new PKIException("GetExtension failure", e);
            }
        }
    }

    public static Extensions getCertExtensionsData(X509Cert x509Cert) {
        if (x509Cert == null) {
            throw new IllegalArgumentException("x509Cert should not be null");
        } else {
            return x509Cert.getExtensionsData();
        }
    }

    public static X509Cert getCertFromSM2(String sm2FilePath) throws PKIException {
        if (sm2FilePath == null) {
            throw new PKIException("SM2File sm2FilePath should not be null");
        } else {
            byte[] encoding;
            try {
                encoding = FileHelper.read(sm2FilePath);
            } catch (Exception var3) {
                Exception e = var3;
                throw new PKIException("SM2File read failure", e);
            }

            return getCertFromSM2(encoding);
        }
    }

    public static X509Cert getCertFromSM2(InputStream sm2FileInputStream) throws PKIException {
        if (sm2FileInputStream == null) {
            throw new PKIException("SM2File sm2FileInputStream should not be null");
        } else {
            int dLength;
            try {
                dLength = sm2FileInputStream.available();
            } catch (IOException var5) {
                IOException e = var5;
                throw new PKIException("SM2File sm2FileInputStream invalid", e);
            }

            if (dLength > 1024000) {
                throw new PKIException("SM2File sm2FileInputStream too large");
            } else {
                byte[] encoding;
                try {
                    encoding = new byte[dLength];
                    sm2FileInputStream.read(encoding);
                } catch (Exception var4) {
                    Exception e = var4;
                    throw new PKIException("SM2File sm2FileInputStream read failure", e);
                }

                return getCertFromSM2(encoding);
            }
        }
    }

    public static X509Cert getCertFromSM2(byte[] sm2FileData) throws PKIException {
        if (sm2FileData == null) {
            throw new PKIException("SM2File sm2FileData should not be null");
        } else {
            PKCS12_SM2 P12 = new PKCS12_SM2(sm2FileData);
            return P12.getPublicCert()[0];
        }
    }

    public static X509Cert getCertFromPFX(String pfxFilePath, String pfxFilePwd) throws PKIException {
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

            return getCertFromPFX(encoding, pfxFilePwd);
        }
    }

    public static X509Cert getCertFromPFX(InputStream pfxFileInputStream, String pfxFilePwd) throws PKIException {
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

                return getCertFromPFX(encoding, pfxFilePwd);
            }
        }
    }

    public static X509Cert getCertFromPFX(byte[] pfxFileData, String pfxFilePwd) throws PKIException {
        if (pfxFileData == null) {
            throw new PKIException("PFXFile pfxFileData should not be null");
        } else if (pfxFilePwd == null) {
            throw new PKIException("PFXFile pfxFilePwd should not be null");
        } else {
            PKCS12 P12 = new PKCS12(pfxFileData);

            try {
                P12.decrypt(pfxFilePwd.toCharArray());
            } catch (Exception var4) {
                Exception e = var4;
                throw new PKIException("PFXFile pfxFileData decrypting failure", e);
            }

            return P12.getCerts()[0];
        }
    }

    public static X509Cert getCertFromJKS(String jksFilePath, String jksFilePwd, String alias) throws PKIException {
        if (jksFilePath == null) {
            throw new PKIException("JKSFile jksFilePath should not be null");
        } else if (jksFilePwd == null) {
            throw new PKIException("JKSFile jksFilePwd should not be null");
        } else if (alias == null) {
            throw new PKIException("JKSFile alias should not be null");
        } else {
            try {
                KeyStore keyStore = InitKeyStore.initJKSKeyStore(jksFilePath, jksFilePwd);
                Certificate certificate = keyStore.getCertificate(alias);
                if (certificate != null) {
                    return new X509Cert(certificate.getEncoded());
                } else {
                    throw new PKIException("no such alias cert!");
                }
            } catch (Exception var5) {
                Exception e = var5;
                throw new PKIException("JKSFile parsed failure", e);
            }
        }
    }

    public static X509Cert[] parseP7B(String p7bFilePath) throws PKIException {
        if (p7bFilePath == null) {
            throw new PKIException("P7BFile p7bFilePath should not be null");
        } else {
            byte[] encoding;
            try {
                encoding = FileHelper.read(p7bFilePath);
            } catch (Exception var3) {
                Exception e = var3;
                throw new PKIException("P7BFile read failure", e);
            }

            return parseP7B(encoding);
        }
    }

    public static X509Cert[] parseP7B(byte[] p7bData) throws PKIException {
        if (p7bData == null) {
            throw new PKIException("P7BFile p7bData should not be null");
        } else {
            try {
                return P7BParser.parseP7B(p7bData);
            } catch (Exception var2) {
                Exception e = var2;
                throw new PKIException("P7BFile parsed failure", e);
            }
        }
    }

    public static void generateP7BFile(X509Cert[] certs, String outP7bFilePath) throws PKIException {
        if (certs == null) {
            throw new PKIException("P7BFile certs should not be null");
        } else if (outP7bFilePath == null) {
            throw new PKIException("P7BFile outP7bFilePath should not be null");
        } else {
            try {
                P7BParser.generateP7BFile(certs, outP7bFilePath);
            } catch (Exception var3) {
                Exception e = var3;
                throw new PKIException("P7BFile Generated failure", e);
            }
        }
    }

    public static void generateP7BFile(X509Cert[] certs, OutputStream out) throws PKIException {
        if (certs == null) {
            throw new PKIException("P7BFile certs should not be null");
        } else if (out == null) {
            throw new PKIException("P7BFile OutputStream should not be null");
        } else {
            try {
                P7BParser.generateP7BFile(certs, out);
            } catch (Exception var3) {
                Exception e = var3;
                throw new PKIException("P7BFile Generated failure", e);
            }
        }
    }

    public static byte[] generateP7BData(X509Cert[] certs) throws PKIException {
        if (certs == null) {
            throw new PKIException("P7BFile certs should not be null");
        } else {
            try {
                byte[] base64Data = P7BParser.generateP7BData(certs);
                return base64Data;
            } catch (Exception var2) {
                Exception e = var2;
                throw new PKIException("P7BFile Generated failure", e);
            }
        }
    }

    public static boolean isSM2Cert(X509Cert x509Cert) {
        if (x509Cert == null) {
            throw new IllegalArgumentException("x509Cert should not be null");
        } else {
            return SM2OIDUtil.isSm3WithSM2Encryption(x509Cert.getCertStructure().getSignatureAlgorithm().getAlgorithm());
        }
    }

    public static boolean isCACert(X509Cert x509Cert) throws PKIException {
        if (x509Cert == null) {
            throw new IllegalArgumentException("x509Cert should not be null");
        } else {
            boolean isCA = false;
            BasicConstraints bcs = x509Cert.getBasicConstraints();
            if (bcs != null) {
                isCA = bcs.isCA();
            }

            return isCA;
        }
    }
}
