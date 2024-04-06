package com.myzuji.sadk.util;

import com.myzuji.sadk.algorithm.common.Mechanism;
import com.myzuji.sadk.algorithm.common.PKCSObjectIdentifiers;
import com.myzuji.sadk.algorithm.common.PKIException;
import com.myzuji.sadk.algorithm.sm2.SM2HashZValue;
import com.myzuji.sadk.lib.crypto.jni.JNIDigest;
import com.myzuji.sadk.lib.crypto.jni.JNISM2;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.DERNull;
import com.myzuji.sadk.org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.teletrust.TeleTrusTObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.DigestInfo;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.crypto.Digest;
import com.myzuji.sadk.org.bouncycastle.crypto.digests.SM3Digest;
import com.myzuji.sadk.system.Mechanisms;
import com.myzuji.sadk.system.global.FileAndBufferConfig;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Hashtable;

public class HashUtil {
    public static final Hashtable ALGOIDMAP = new Hashtable();

    public HashUtil() {
    }

    public static byte[] RSAHashMessageByBC(byte[] sourceData, Mechanism mechanism, boolean ifDEREncoding) throws PKIException {
        byte[] outData = null;
        if (sourceData != null && sourceData.length != 0) {
            try {
                Digest extDigest = getDigestByBC(mechanism);
                extDigest.update(sourceData, 0, sourceData.length);
                outData = new byte[extDigest.getDigestSize()];
                extDigest.doFinal(outData, 0);
                if (!ifDEREncoding) {
                    return outData;
                } else {
                    outData = getDigestEncoded(mechanism, outData);
                    return outData;
                }
            } catch (PKIException var5) {
                PKIException e = var5;
                throw e;
            } catch (Exception var6) {
                Exception e = var6;
                throw new PKIException("RSAHashMessage Failure", e);
            }
        } else {
            throw new PKIException("the source data is null or empty!");
        }
    }

    public static byte[] RSAHashMessageByJNI(byte[] sourceData, Mechanism mechanism, boolean ifDEREncoding) throws Exception {
        byte[] outData = null;
        if (sourceData != null && sourceData.length != 0) {
            try {
                JNIDigest jniDigest = getDigestByJNI(mechanism);
                jniDigest.update(sourceData);
                outData = new byte[jniDigest.getDigestSize()];
                jniDigest.doFinal(outData);
                if (!ifDEREncoding) {
                    return outData;
                } else {
                    outData = getDigestEncoded(mechanism, outData);
                    return outData;
                }
            } catch (PKIException var5) {
                PKIException e = var5;
                throw e;
            } catch (Exception var6) {
                Exception e = var6;
                throw new PKIException("RSAHashMessage Failure", e);
            }
        } else {
            throw new PKIException("the source data is null or empty!");
        }
    }

    public static byte[] RSAHashFileByBC(InputStream sourceStream, Mechanism mechanism, boolean ifDEREncoding) throws PKIException {
        BufferedInputStream bufferedInputStream = null;
        Digest digest = getDigestByBC(mechanism);
        bufferedInputStream = new BufferedInputStream(sourceStream);
        int buffer_size = FileAndBufferConfig.BIG_FILE_BUFFER;
        byte[] buffer = new byte[buffer_size];

        try {
            int i = bufferedInputStream.read(buffer);
            if (i == -1) {
                throw new PKIException("the source data is null!");
            }

            do {
                digest.update(buffer, 0, i);
                i = bufferedInputStream.read(buffer);
            } while (i != -1);
        } catch (IOException var12) {
            IOException e = var12;
            throw new PKIException("RSAHashMessage Failure", e);
        }

        byte[] out = new byte[digest.getDigestSize()];
        digest.doFinal(out, 0);
        if (!ifDEREncoding) {
            return out;
        } else {
            try {
                out = getDigestEncoded(mechanism, out);
                return out;
            } catch (PKIException var10) {
                PKIException e = var10;
                throw e;
            } catch (Exception var11) {
                Exception e = var11;
                throw new PKIException("RSAHashMessage Failure", e);
            }
        }
    }

    public static byte[] RSAHashFileByJNI(InputStream sourceStream, Mechanism mechanism, boolean ifDEREncoding) throws Exception {
        byte[] outData = null;
        BufferedInputStream bufferedInputStream = null;
        JNIDigest jniDigest = getDigestByJNI(mechanism);
        bufferedInputStream = new BufferedInputStream(sourceStream);
        int buffer_size = FileAndBufferConfig.BIG_FILE_BUFFER;
        byte[] buffer = new byte[buffer_size];

        try {
            int i = bufferedInputStream.read(buffer);
            if (i == -1) {
                throw new PKIException("the source data is null!");
            } else {
                do {
                    if (i < buffer_size) {
                        byte[] temp = new byte[i];
                        System.arraycopy(buffer, 0, temp, 0, i);
                        jniDigest.update(temp);
                        break;
                    }

                    jniDigest.update(buffer);
                    i = bufferedInputStream.read(buffer);
                } while (i != -1);

                outData = new byte[jniDigest.getDigestSize()];
                jniDigest.doFinal(outData);
                if (!ifDEREncoding) {
                    return outData;
                } else {
                    outData = getDigestEncoded(mechanism, outData);
                    return outData;
                }
            }
        } catch (PKIException var10) {
            PKIException e = var10;
            throw e;
        } catch (Exception var11) {
            Exception e = var11;
            throw new PKIException("RSAHashMessage Failure", e);
        }
    }

    private static Digest getDigestByBC(Mechanism mechanism) throws PKIException {
        Digest engine = Mechanisms.getDigest(mechanism);
        if (engine == null) {
            throw new PKIException("can not support this algorithm:" + mechanism);
        } else {
            return engine;
        }
    }

    private static JNIDigest getDigestByJNI(Mechanism mechanism) throws Exception {
        int hashID = Mechanisms.getHashID(mechanism);
        if (hashID == 0) {
            throw new PKIException("can not support this algorithm:" + mechanism);
        } else {
            JNIDigest engine = new JNIDigest();
            engine.init(hashID);
            return engine;
        }
    }

    public static byte[] SM2HashMessageByBCWithoutZValue(byte[] sourceData) throws Exception {
        byte[] out = new byte[32];
        SM3Digest digest = new SM3Digest();
        digest.update(sourceData, 0, sourceData.length);
        digest.doFinal(out, 0);
        return out;
    }

    public static byte[] SM2HashMessageByBCWithZValue(byte[] userId, byte[] sourceData, BigInteger pubX, BigInteger pubY) throws PKIException {
        if (sourceData != null && sourceData.length != 0) {
            byte[] out = new byte[32];
            byte[] z = SM2HashZValue.getZa(pubX, pubY, userId);
            SM3Digest digest = new SM3Digest();
            digest.update(z, 0, z.length);
            digest.update(sourceData, 0, sourceData.length);
            digest.doFinal(out, 0);
            return out;
        } else {
            throw new PKIException("the source data is null or empty!");
        }
    }

    public static byte[] SM2HashMessageByJNIWithZValue(byte[] userId, byte[] sourceData, byte[] pubX, byte[] pubY) throws PKIException {
        if (sourceData != null && sourceData.length != 0) {
            byte[] out = new byte[32];
            byte[] z = new byte[32];

            try {
                JNISM2.calculateZValue(pubX, pubY, userId, z);
                JNIDigest sm3_jni = new JNIDigest();
                sm3_jni.init(922);
                sm3_jni.update(z);
                sm3_jni.update(sourceData);
                sm3_jni.doFinal(out);
                return out;
            } catch (PKIException var7) {
                PKIException e = var7;
                throw e;
            } catch (Exception var8) {
                Exception e = var8;
                throw new PKIException("SM3 done Failure", e);
            }
        } else {
            throw new PKIException("the source data is null or empty!");
        }
    }

    public static byte[] SM2HashFileByBCWithZValue(byte[] userId, InputStream sourceStream, BigInteger pubX, BigInteger pubY) throws PKIException {
        BufferedInputStream bufferedInputStream = null;
        byte[] out = new byte[32];

        try {
            byte[] z = new byte[32];
            bufferedInputStream = new BufferedInputStream(sourceStream);
            int buffer_size = FileAndBufferConfig.BIG_FILE_BUFFER;
            byte[] buffer = new byte[buffer_size];
            int i = bufferedInputStream.read(buffer);
            z = SM2HashZValue.getZa(pubX, pubY, userId);
            SM3Digest digest = new SM3Digest();
            digest.update(z, 0, z.length);
            if (i == -1) {
                throw new Exception("the source data is null!");
            } else {
                do {
                    digest.update(buffer, 0, i);
                    i = bufferedInputStream.read(buffer);
                } while (i != -1);

                digest.doFinal(out, 0);
                return out;
            }
        } catch (PKIException var19) {
            PKIException e = var19;
            throw e;
        } catch (Exception var20) {
            Exception e = var20;
            throw new PKIException("SM3 done Failure", e);
        } finally {
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (Exception var18) {
                    Exception e = var18;
                    throw new PKIException("SM3 done Failure", e);
                }
            }

        }
    }

    public static byte[] SM2HashFileByJNIWithZValue(byte[] userId, InputStream sourceStream, byte[] pubX, byte[] pubY) throws PKIException {
        BufferedInputStream bfis = null;
        byte[] out = new byte[32];

        try {
            byte[] z = new byte[32];
            bfis = new BufferedInputStream(sourceStream);
            int buffer_size = FileAndBufferConfig.BIG_FILE_BUFFER;
            byte[] buffer = new byte[buffer_size];
            int i = bfis.read(buffer);
            JNISM2.calculateZValue(pubX, pubY, userId, z);
            JNIDigest sm3_jni = new JNIDigest();
            sm3_jni.init(922);
            sm3_jni.update(z);
            if (i == -1) {
                throw new Exception("the source data is null!");
            } else {
                while (true) {
                    if (i < buffer_size) {
                        byte[] temp = new byte[i];
                        System.arraycopy(buffer, 0, temp, 0, i);
                        sm3_jni.update(temp);
                    } else {
                        sm3_jni.update(buffer);
                        i = bfis.read(buffer);
                        if (i != -1) {
                            continue;
                        }
                    }

                    sm3_jni.doFinal(out);
                    return out;
                }
            }
        } catch (PKIException var20) {
            PKIException e = var20;
            throw e;
        } catch (Exception var21) {
            Exception e = var21;
            throw new PKIException("SM3 done Failure", e);
        } finally {
            if (bfis != null) {
                try {
                    bfis.close();
                } catch (Exception var19) {
                    Exception e = var19;
                    throw new PKIException("SM3 done Failure", e);
                }
            }

        }
    }

    public static byte[] SM2HashMessageByJNIWithoutZValue(byte[] sourceData) throws Exception {
        byte[] out = new byte[32];
        JNIDigest sm3_jni = new JNIDigest();
        sm3_jni.init(922);
        sm3_jni.update(sourceData);
        sm3_jni.doFinal(out);
        return out;
    }

    public static byte[] SM2HashFileByBCWithoutZValue(InputStream sourceStream) throws Exception {
        byte[] out = new byte[32];
        BufferedInputStream bfis = null;

        try {
            bfis = new BufferedInputStream(sourceStream);
            int buffer_size = FileAndBufferConfig.BIG_FILE_BUFFER;
            byte[] buffer = new byte[buffer_size];
            int i = bfis.read(buffer);
            SM3Digest digest = null;
            if (i == -1) {
                throw new Exception("the source data is null!");
            } else {
                digest = new SM3Digest();

                do {
                    digest.update(buffer, 0, i);
                    i = bfis.read(buffer);
                } while (i != -1);

                digest.doFinal(out, 0);
                byte[] var7 = out;
                return var7;
            }
        } catch (PKIException var12) {
            PKIException e = var12;
            throw e;
        } catch (Exception var13) {
            Exception e = var13;
            throw new PKIException("SM3 done Failure", e);
        } finally {
            if (bfis != null) {
                bfis.close();
            }

        }
    }

    public static byte[] SM2HashFileByJNIWithoutZValue(InputStream sourceStream) throws Exception {
        byte[] out = new byte[32];
        BufferedInputStream bfis = null;

        try {
            bfis = new BufferedInputStream(sourceStream);
            int buffer_size = FileAndBufferConfig.BIG_FILE_BUFFER;
            byte[] buffer = new byte[buffer_size];
            int i = bfis.read(buffer);
            JNIDigest sm3_jni = null;
            if (i == -1) {
                throw new Exception("the source data is null!");
            } else {
                sm3_jni = new JNIDigest();
                sm3_jni.init(922);

                while (true) {
                    byte[] temp;
                    if (i < buffer_size) {
                        temp = new byte[i];
                        System.arraycopy(buffer, 0, temp, 0, i);
                        sm3_jni.update(temp);
                    } else {
                        sm3_jni.update(buffer);
                        i = bfis.read(buffer);
                        if (i != -1) {
                            continue;
                        }
                    }

                    sm3_jni.doFinal(out);
                    temp = out;
                    return temp;
                }
            }
        } catch (PKIException var12) {
            PKIException e = var12;
            throw e;
        } catch (Exception var13) {
            Exception e = var13;
            throw new PKIException("SM3 done Failure", e);
        } finally {
            if (bfis != null) {
                bfis.close();
            }

        }
    }

    private static final byte[] getDigestEncoded(Mechanism mechanism, byte[] hash) throws PKIException {
        try {
            AlgorithmIdentifier algId = Mechanisms.getDigestAlgIdentifier(mechanism);
            if (algId == null) {
                algId = getRIPEMDIdentifier(mechanism);
            }

            if (algId == null) {
                throw new PKIException("invalid digest mechanism: " + mechanism);
            } else {
                DigestInfo dInfo = new DigestInfo(algId, hash);
                return dInfo.getEncoded("DER");
            }
        } catch (IOException var4) {
            IOException e = var4;
            throw new PKIException("Digest EncodedFailure", e);
        }
    }

    private static final AlgorithmIdentifier getRIPEMDIdentifier(Mechanism mechanism) {
        ASN1ObjectIdentifier oid = null;
        if (mechanism != null && mechanism.getMechanismType() != null) {
            String type = mechanism.getMechanismType().toUpperCase();
            if (type.equals("RIPEMD128")) {
                oid = TeleTrusTObjectIdentifiers.ripemd128;
            } else if (type.equals("RIPEMD160")) {
                oid = TeleTrusTObjectIdentifiers.ripemd160;
            } else if (type.equals("RIPEMD256")) {
                oid = TeleTrusTObjectIdentifiers.ripemd256;
            }
        } else {
            oid = null;
        }

        AlgorithmIdentifier digestAlgIdentifier = null;
        if (oid != null) {
            digestAlgIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.sha1, DERNull.INSTANCE);
        }

        return digestAlgIdentifier;
    }

    static {
        ALGOIDMAP.put("RIPEMD128", TeleTrusTObjectIdentifiers.ripemd128);
        ALGOIDMAP.put("RIPEMD160", TeleTrusTObjectIdentifiers.ripemd160);
        ALGOIDMAP.put("RIPEMD256", TeleTrusTObjectIdentifiers.ripemd256);
        ALGOIDMAP.put("SHA-1", X509ObjectIdentifiers.id_SHA1);
        ALGOIDMAP.put("sha1WithRSAEncryption", X509ObjectIdentifiers.id_SHA1);
        ALGOIDMAP.put("SHA-244", NISTObjectIdentifiers.id_sha224);
        ALGOIDMAP.put("SHA-256", NISTObjectIdentifiers.id_sha256);
        ALGOIDMAP.put("sha256WithRSAEncryption", NISTObjectIdentifiers.id_sha256);
        ALGOIDMAP.put("SHA-384", NISTObjectIdentifiers.id_sha384);
        ALGOIDMAP.put("SHA-512", NISTObjectIdentifiers.id_sha512);
        ALGOIDMAP.put("sha512WithRSAEncryption", NISTObjectIdentifiers.id_sha512);
        ALGOIDMAP.put("MD2", PKCSObjectIdentifiers.md2);
        ALGOIDMAP.put("MD4", PKCSObjectIdentifiers.md4);
        ALGOIDMAP.put("MD5", PKCSObjectIdentifiers.md5);
        ALGOIDMAP.put("md5WithRSAEncryption", PKCSObjectIdentifiers.md5);
        ALGOIDMAP.put("SM3", PKCSObjectIdentifiers.sm3);
    }
}

