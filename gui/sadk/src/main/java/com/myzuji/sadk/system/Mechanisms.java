package com.myzuji.sadk.system;

import com.myzuji.sadk.algorithm.common.GMObjectIdentifiers;
import com.myzuji.sadk.algorithm.common.Mechanism;
import com.myzuji.sadk.algorithm.common.PKCSObjectIdentifiers;
import com.myzuji.sadk.algorithm.common.PKIException;
import com.myzuji.sadk.algorithm.util.SM2OIDUtil;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Primitive;
import com.myzuji.sadk.org.bouncycastle.asn1.DERNull;
import com.myzuji.sadk.org.bouncycastle.asn1.DERSequence;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.x9.X9ECParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.Digest;
import com.myzuji.sadk.org.bouncycastle.crypto.digests.*;
import com.myzuji.sadk.org.bouncycastle.jcajce.provider.asymmetric.sm.SM2Params;
import com.myzuji.sadk.org.bouncycastle.jce.spec.ECParameterSpec;

import java.util.HashMap;
import java.util.Map;

public final class Mechanisms {
    public static final Mechanism K_RSA = new Mechanism("RSA");
    public static final Mechanism K_SM2 = new Mechanism("SM2");
    public static final Mechanism M_SM3_SM2 = new Mechanism("sm3WithSM2Encryption");
    public static final Mechanism M_MD5_RSA = new Mechanism("md5WithRSAEncryption");
    public static final Mechanism M_SHA1_RSA = new Mechanism("sha1WithRSAEncryption");
    public static final Mechanism M_SHA256_RSA = new Mechanism("sha256WithRSAEncryption");
    public static final Mechanism M_SHA512_RSA = new Mechanism("sha512WithRSAEncryption");
    private static final Map HASH_OID2NAME_TABLE = new HashMap();
    private static final Map signAlgMappings;
    private static final Map hashAlgMappings;

    private Mechanisms() {
    }

    public static String getDigestAlgorithmName(ASN1ObjectIdentifier oid) throws SecurityException {
        return (String) HASH_OID2NAME_TABLE.get(oid);
    }

    private static HashID getHashID(String algorithmName) {
        HashID hashID = null;
        if (algorithmName != null) {
            hashID = (HashID) hashAlgMappings.get(algorithmName.toUpperCase());
        }

        return hashID;
    }

    public static boolean isSM2WithSM3(Mechanism signAlg) {
        boolean smFlag = false;
        if (signAlg != null) {
            smFlag = isSM2WithSM3(signAlg.getMechanismType());
        }

        return smFlag;
    }

    public static boolean isSM2WithSM3(String signAlg) {
        boolean smFlag = false;
        if (signAlg != null) {
            signAlg = signAlg.toUpperCase();
            smFlag = "SM3WITHSM2ENCRYPTION".equals(signAlg) || "SM3WITHSM2".equals(signAlg) || "SM2WITHSM3ENCRYPTION".equals(signAlg) || "SM2WITHSM3".equals(signAlg) || "SM2".equals(signAlg) || M_SM3_SM2.getMechanismType().equalsIgnoreCase(signAlg);
        }

        return smFlag;
    }

    public static boolean isSM2WithSM3(ASN1ObjectIdentifier oid) {
        return SM2OIDUtil.isSm3WithSM2Encryption(oid);
    }

    public static boolean isValid(Mechanism mechanism) {
        boolean valid = false;
        if (mechanism != null) {
            valid = isValid(mechanism.getMechanismType());
        }

        return valid;
    }

    public static boolean isValid(String signAlg) {
        return getHashID(signAlg) != null;
    }

    public static AlgorithmIdentifier getDigestAlgIdentifier(String signAlg) {
        HashID hashID = getHashID(signAlg);
        AlgorithmIdentifier digestAlgIdentifier = null;
        if (hashID == null) {
            digestAlgIdentifier = null;
        } else if (HashID.SHA1.equals(hashID)) {
            digestAlgIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.sha1, DERNull.INSTANCE);
        } else if (HashID.SHA256.equals(hashID)) {
            digestAlgIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.sha256, DERNull.INSTANCE);
        } else if (HashID.SHA384.equals(hashID)) {
            digestAlgIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.sha384, DERNull.INSTANCE);
        } else if (HashID.SHA512.equals(hashID)) {
            digestAlgIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.sha512, DERNull.INSTANCE);
        } else if (HashID.MD5.equals(hashID)) {
            digestAlgIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.md5, DERNull.INSTANCE);
        }

        return digestAlgIdentifier;
    }

    public static AlgorithmIdentifier getDigestAlgIdentifier(Mechanism mechanism) {
        AlgorithmIdentifier aid = null;
        if (mechanism != null) {
            aid = getDigestAlgIdentifier(mechanism.getMechanismType());
        }

        return aid;
    }

    public static Digest getDigest(Mechanism mechanism) {
        Digest engine = null;
        if (mechanism != null) {
            engine = getDigest(mechanism.getMechanismType());
        }

        return engine;
    }

    public static Digest getDigest(String digestAlgorithm) {
        HashID hashID = getHashID(digestAlgorithm);
        Digest engine = null;
        if (hashID == null) {
            engine = null;
        } else if (HashID.SHA1.equals(hashID)) {
            engine = new SHA1Digest();
        } else if (HashID.SHA256.equals(hashID)) {
            engine = new SHA256Digest();
        } else if (HashID.SHA384.equals(hashID)) {
            engine = new SHA384Digest();
        } else if (HashID.SHA512.equals(hashID)) {
            engine = new SHA512Digest();
        } else if (HashID.MD5.equals(hashID)) {
            engine = new MD5Digest();
        }

        return (Digest) engine;
    }

    public static int getHashID(Mechanism mechanism) {
        HashID hashID = null;
        if (mechanism != null) {
            hashID = getHashID(mechanism.getMechanismType());
        }

        int jniDigest = 0;
        if (hashID == null) {
            jniDigest = 0;
        } else if (HashID.SHA1.equals(hashID)) {
            jniDigest = 64;
        } else if (HashID.SHA256.equals(hashID)) {
            jniDigest = 672;
        } else if (HashID.SHA384.equals(hashID)) {
            jniDigest = 673;
        } else if (HashID.SHA512.equals(hashID)) {
            jniDigest = 674;
        } else if (HashID.MD5.equals(hashID)) {
            jniDigest = 4;
        }

        return jniDigest;
    }

    public static Mechanism signMechanismFrom(String signAlgOrDigestAlgorithm) {
        HashID hashID = getHashID(signAlgOrDigestAlgorithm);
        Mechanism mechanism = null;
        if (hashID == null) {
            mechanism = null;
        } else if (HashID.SM3.equals(hashID)) {
            mechanism = new Mechanism("sm3WithSM2Encryption");
        } else if (HashID.SHA1.equals(hashID)) {
            mechanism = new Mechanism("sha1WithRSAEncryption");
        } else if (HashID.SHA256.equals(hashID)) {
            mechanism = new Mechanism("sha256WithRSAEncryption");
        } else if (HashID.SHA384.equals(hashID)) {
            mechanism = new Mechanism("sha384WithRSAEncryption");
        } else if (HashID.SHA512.equals(hashID)) {
            mechanism = new Mechanism("sha512WithRSAEncryption");
        } else if (HashID.MD5.equals(hashID)) {
            mechanism = new Mechanism("md5WithRSAEncryption");
        }

        return mechanism;
    }

    public static Mechanism signMechanismRSAFrom(ASN1ObjectIdentifier digestId) {
        Mechanism signM = null;
        if (digestId == null) {
            signM = null;
        } else if (digestId.equals(PKCSObjectIdentifiers.md5)) {
            signM = new Mechanism("md5WithRSAEncryption");
        } else if (digestId.equals(PKCSObjectIdentifiers.sha1)) {
            signM = new Mechanism("sha1WithRSAEncryption");
        } else if (digestId.equals(PKCSObjectIdentifiers.sha256)) {
            signM = new Mechanism("sha256WithRSAEncryption");
        } else if (digestId.equals(PKCSObjectIdentifiers.sha384)) {
            signM = new Mechanism("sha384WithRSAEncryption");
        } else if (digestId.equals(PKCSObjectIdentifiers.sha512)) {
            signM = new Mechanism("sha512WithRSAEncryption");
        }

        return signM;
    }

    public static boolean isValidDigestAlgorithm(String degestAlgorithm) {
        return isValid(degestAlgorithm);
    }

    public static Mechanism encryptMechanismFrom(Mechanism mechanism) {
        Mechanism enMechanism = null;
        if (isSM2WithSM3(mechanism)) {
            enMechanism = new Mechanism("SM2");
        } else if (isValid(mechanism)) {
            enMechanism = new Mechanism("RSA");
        }

        return enMechanism;
    }

    public static boolean isSM2PublicKey(AlgorithmIdentifier algorithm) throws PKIException {
        try {
            boolean smFlag = false;
            if (!SM2OIDUtil.isSm3WithSM2Encryption(algorithm.getAlgorithm()) && !SM2OIDUtil.isSM2PublicKeyOID(algorithm.getParameters())) {
                if (GMObjectIdentifiers.ecPubKey.equals(algorithm.getAlgorithm())) {
                    ASN1Primitive obj = algorithm.getParameters().toASN1Primitive();
                    ECParameterSpec smParams = SM2Params.sm2ParameterSpec;
                    X9ECParameters params = X9ECParameters.getInstance(DERSequence.getInstance(obj));
                    smFlag = params.getG().equals(smParams.getG()) && params.getCurve().equals(smParams.getCurve());
                } else {
                    smFlag = false;
                }
            } else {
                smFlag = true;
            }

            return smFlag;
        } catch (Exception var5) {
            Exception e = var5;
            throw new PKIException("Invalid SM2PublicKey AlgorithmIdentifier Parameters", e);
        }
    }

    public static boolean isRSAType(Mechanism mechanism) {
        return "RSA".equals(mechanism.getMechanismType());
    }

    public static boolean isSM2Type(Mechanism mechanism) {
        return "SM2".equals(mechanism.getMechanismType());
    }

    static {
        HASH_OID2NAME_TABLE.put(PKCSObjectIdentifiers.md5WithRSAEncryption, "MD5");
        HASH_OID2NAME_TABLE.put(PKCSObjectIdentifiers.md5, "MD5");
        HASH_OID2NAME_TABLE.put(PKCSObjectIdentifiers.sha1WithRSAEncryption, "SHA-1");
        HASH_OID2NAME_TABLE.put(PKCSObjectIdentifiers.sha1, "SHA-1");
        HASH_OID2NAME_TABLE.put(PKCSObjectIdentifiers.sha256WithRSAEncryption, "SHA-256");
        HASH_OID2NAME_TABLE.put(PKCSObjectIdentifiers.sha256, "SHA-256");
        HASH_OID2NAME_TABLE.put(PKCSObjectIdentifiers.sha512WithRSAEncryption, "SHA-512");
        HASH_OID2NAME_TABLE.put(PKCSObjectIdentifiers.sha512, "SHA-512");
        signAlgMappings = new HashMap();
        hashAlgMappings = new HashMap();
        signAlgMappings.put("MD2WITHRSAENCRYPTION", "MD2withRSAEncryption");
        signAlgMappings.put("MD5WITHRSAENCRYPTION", "MD5withRSAEncryption");
        signAlgMappings.put("SHA1WITHRSAENCRYPTION", "SHA1withRSAEncryption");
        signAlgMappings.put("SHA256WITHRSAENCRYPTION", "SHA256withRSAEncryption");
        signAlgMappings.put("SHA384WITHRSAENCRYPTION", "SHA384withRSAEncryption");
        signAlgMappings.put("SHA512WITHRSAENCRYPTION", "SHA512withRSAEncryption");
        signAlgMappings.put("SM3WITHSM2ENCRYPTION", "SM3withSM2");
        signAlgMappings.put("SM3WITHSM2ENCRYPTION_OLD", "SM3withSM2");
        signAlgMappings.put("SM3WITHSM2", "SM3withSM2");
        signAlgMappings.put("RSAWITHMD2ENCRYPTION", "MD2withRSAEncryption");
        signAlgMappings.put("RSAWITHMD5ENCRYPTION", "MD5withRSAEncryption");
        signAlgMappings.put("RSAWITHSHA1ENCRYPTION", "SHA1withRSAEncryption");
        signAlgMappings.put("RSAWITHSHA256ENCRYPTION", "SHA256withRSAEncryption");
        signAlgMappings.put("RSAWITHSHA384ENCRYPTION", "SHA384withRSAEncryption");
        signAlgMappings.put("RSAWITHSHA512ENCRYPTION", "SHA512withRSAEncryption");
        signAlgMappings.put("SM2WITHSM3ENCRYPTION", "SM3withSM2");
        signAlgMappings.put("SM2WITHSM3ENCRYPTION_OLD", "SM3withSM2");
        signAlgMappings.put("SM2WITHSM3", "SM3withSM2");
        hashAlgMappings.put("MD2WITHRSAENCRYPTION", HashID.MD2);
        hashAlgMappings.put("MD5WITHRSAENCRYPTION", HashID.MD5);
        hashAlgMappings.put("SHA1WITHRSAENCRYPTION", HashID.SHA1);
        hashAlgMappings.put("SHA256WITHRSAENCRYPTION", HashID.SHA256);
        hashAlgMappings.put("SHA384WITHRSAENCRYPTION", HashID.SHA384);
        hashAlgMappings.put("SHA512WITHRSAENCRYPTION", HashID.SHA512);
        hashAlgMappings.put("SM3WITHSM2ENCRYPTION", HashID.SM3);
        hashAlgMappings.put("SM3WITHSM2ENCRYPTION_OLD", HashID.SM3);
        hashAlgMappings.put("SM3WITHSM2", HashID.SM3);
        hashAlgMappings.put("RSAWITHMD2ENCRYPTION", HashID.MD2);
        hashAlgMappings.put("RSAWITHMD5ENCRYPTION", HashID.MD5);
        hashAlgMappings.put("RSAWITHSHA1ENCRYPTION", HashID.SHA1);
        hashAlgMappings.put("RSAWITHSHA256ENCRYPTION", HashID.SHA256);
        hashAlgMappings.put("RSAWITHSHA384ENCRYPTION", HashID.SHA384);
        hashAlgMappings.put("RSAWITHSHA512ENCRYPTION", HashID.SHA512);
        hashAlgMappings.put("SM2WITHSM3ENCRYPTION", HashID.SM3);
        hashAlgMappings.put("SM2WITHSM3ENCRYPTION_OLD", HashID.SM3);
        hashAlgMappings.put("SM2WITHSM3", HashID.SM3);
        hashAlgMappings.put("MD2", HashID.MD2);
        hashAlgMappings.put("MD5", HashID.MD5);
        hashAlgMappings.put("SHA1", HashID.SHA1);
        hashAlgMappings.put("SHA256", HashID.SHA256);
        hashAlgMappings.put("SHA384", HashID.SHA384);
        hashAlgMappings.put("SHA512", HashID.SHA512);
        hashAlgMappings.put("SM3", HashID.SM3);
        hashAlgMappings.put("MD-2", HashID.MD2);
        hashAlgMappings.put("MD-5", HashID.MD5);
        hashAlgMappings.put("SHA-1", HashID.SHA1);
        hashAlgMappings.put("SHA-256", HashID.SHA256);
        hashAlgMappings.put("SHA-384", HashID.SHA384);
        hashAlgMappings.put("SHA-512", HashID.SHA512);
        hashAlgMappings.put("SM-3", HashID.SM3);
    }
}
