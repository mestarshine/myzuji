package com.myzuji.sadk.algorithm.common;

import com.myzuji.sadk.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.DERNull;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import com.myzuji.sadk.system.Mechanisms;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MechanismKit {
    public static final String RSA = "RSA";
    public static final String RSA_PKCS = "RSA/ECB/PKCS1PADDING";
    public static final String SM2 = "SM2";
    public static final String SM4_KEY = "SM4";
    public static final String SM4_CBC = "SM4/CBC/PKCS7Padding";
    public static final String SM4_ECB = "SM4/ECB/PKCS7Padding";
    public static final String RC4 = "RC4";
    public static final String RC4_KEY = "RC4";
    public static final String DES3_KEY = "DESede";
    public static final String DES3_ECB = "DESede/ECB/PKCS7Padding";
    public static final String DES3_CBC = "DESede/CBC/PKCS7Padding";
    public static final String AES_KEY = "AES";
    public static final String AES_ECB = "AES/ECB/PKCS7Padding";
    public static final String AES_CBC = "AES/CBC/PKCS7Padding";
    public static final String PBE_KEY = "PBEWithMD5AndDES";
    public static final String PBE_MD5_RC2_KEY = "PBEWithMD5AndRC2";
    public static final String PBE_SHA1_RC2_KEY = "PBEWithSHA1AndRC2";
    public static final String PBE_SHA1_DES_KEY = "PBEWithSHA1AndDES";
    public static final String PBE_2KEY = "PBEWITHSHAAND2-KEYTRIPLEDES-CBC";
    public static final String PBE_3KEY = "PBEWITHSHAAND3-KEYTRIPLEDES-CBC";
    public static final String PBE_40BITRC4KEY = "PBEWITHSHAAND40BITRC4";
    public static final String PBE_128BITRC4KEY = "PBEWITHSHAAND128BITRC4";
    public static final String PBE_MD5_DES = "PBEWithMD5AndDES";
    public static final String PBE_MD5_RC2 = "PBEWithMD5AndRC2";
    public static final String PBE_SHA1_DES = "PBEWithSHA1AndDES";
    public static final String PBE_SHA1_2DES = "PBEWITHSHAAND2-KEYTRIPLEDES-CBC";
    public static final String PBE_SHA1_3DES = "PBEWITHSHAAND3-KEYTRIPLEDES-CBC";
    public static final String PBE_SHA1_RC2 = "PBEWithSHA1AndRC2";
    public static final String PBE_SHA1_40BITRC4 = "PBEWITHSHAAND40BITRC4";
    public static final String PBE_SHA1_128BITRC4 = "PBEWITHSHAAND128BITRC4";
    public static final String MD2 = "MD2";
    public static final String MD4 = "MD4";
    public static final String MD5 = "MD5";
    public static final String RIPEMD128 = "RIPEMD128";
    public static final String RIPEMD160 = "RIPEMD160";
    public static final String RIPEMD256 = "RIPEMD256";
    public static final String SHA1 = "SHA-1";
    public static final String SHA244 = "SHA-244";
    public static final String SHA256 = "SHA-256";
    public static final String SHA384 = "SHA-384";
    public static final String SHA512 = "SHA-512";
    public static final String SM3 = "SM3";
    public static final String MD5_RSA = "md5WithRSAEncryption";
    public static final String SHA1_RSA = "sha1WithRSAEncryption";
    public static final String SHA256_RSA = "sha256WithRSAEncryption";
    public static final String SHA384_RSA = "sha384WithRSAEncryption";
    public static final String SHA512_RSA = "sha512WithRSAEncryption";
    public static final String SM3_SM2 = "sm3WithSM2Encryption";
    public static final String SM3_SM2_OLD = "sm3WithSM2Encryption_OLD";
    public static final String MD2_RSA = "md2WithRSAEncryption";
    private static final Map signAlgMappings;
    public static Map OIDALGMap = new HashMap();
    public static Map ALGOIDMap = new HashMap();

    static {
        OIDALGMap.put(PKCSObjectIdentifiers.md5WithRSAEncryption, "md5WithRSAEncryption");
        OIDALGMap.put(PKCSObjectIdentifiers.sha1WithRSAEncryption, "sha1WithRSAEncryption");
        OIDALGMap.put(PKCSObjectIdentifiers.sha256WithRSAEncryption, "sha256WithRSAEncryption");
        OIDALGMap.put(PKCSObjectIdentifiers.sha512WithRSAEncryption, "sha512WithRSAEncryption");
        OIDALGMap.put(X9ObjectIdentifiers.sm3WithSM2Encryption, "sm3WithSM2Encryption");
        OIDALGMap.put(X9ObjectIdentifiers.sm3WithSM2Encryption_OLD, "sm3WithSM2Encryption_OLD");
        Collections.unmodifiableMap(OIDALGMap);
        ALGOIDMap.put("md5WithRSAEncryption", PKCSObjectIdentifiers.md5WithRSAEncryption);
        ALGOIDMap.put("sha1WithRSAEncryption", PKCSObjectIdentifiers.sha1WithRSAEncryption);
        ALGOIDMap.put("sha256WithRSAEncryption", PKCSObjectIdentifiers.sha256WithRSAEncryption);
        ALGOIDMap.put("sha512WithRSAEncryption", PKCSObjectIdentifiers.sha512WithRSAEncryption);
        ALGOIDMap.put("sm3WithSM2Encryption", X9ObjectIdentifiers.sm3WithSM2Encryption);
        ALGOIDMap.put("sm3WithSM2Encryption_OLD", X9ObjectIdentifiers.sm3WithSM2Encryption_OLD);
        Collections.unmodifiableMap(ALGOIDMap);
        signAlgMappings = new HashMap();
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
    }

    protected String mechanismType;
    protected Object param;

    public MechanismKit(String mechanismType, Object param) {
        this.mechanismType = mechanismType;
        this.param = param;
    }

    public MechanismKit(String mechanismType) {
        this.mechanismType = mechanismType;
        this.param = null;
    }

    public static MechanismKit getMechanismFromDigestAlgotithm(String digestAlgorithm) throws PKIException {
        MechanismKit mech = Mechanisms.signMechanismFrom(digestAlgorithm);
        if (mech == null) {
            throw new PKIException("can not support this degest algorithm:" + digestAlgorithm);
        } else {
            return mech;
        }
    }

    public static boolean isDigestAlgorithmValid(String digestAlgorithm) {
        return Mechanisms.isValidDigestAlgorithm(digestAlgorithm);
    }

    public static boolean isSymmetricAlgorithmValid(String symmetricAlgorithm) {
        if (symmetricAlgorithm == null) {
            return false;
        } else {
            return symmetricAlgorithm.equals("SM2") || symmetricAlgorithm.equals("RSA/ECB/PKCS1PADDING") || symmetricAlgorithm.equals("RC4") || symmetricAlgorithm.equals("DESede/ECB/PKCS7Padding") || symmetricAlgorithm.equals("DESede/CBC/PKCS7Padding") || symmetricAlgorithm.equals("SM4/CBC/PKCS7Padding") || symmetricAlgorithm.equals("SM4/ECB/PKCS7Padding");
        }
    }

    public static boolean isValid(MechanismKit signAlg) {
        boolean valid = false;
        if (signAlg != null && signAlg.getMechanismType() != null) {
            valid = signAlgMappings.containsKey(signAlg.getMechanismType().toUpperCase());
        } else {
            valid = false;
        }

        return valid;
    }

    public static String getSignatureAlgName(AlgorithmIdentifier signatureAlgorithm) {
        ASN1ObjectIdentifier oid = signatureAlgorithm.getAlgorithm();
        String sigAlgName = (String) OIDALGMap.get(oid);
        if (sigAlgName == null) {
            sigAlgName = oid.getId();
        }

        return sigAlgName;
    }

    public static boolean isValid(AlgorithmIdentifier signatureAlgorithm) {
        ASN1ObjectIdentifier oid = signatureAlgorithm.getAlgorithm();
        return OIDALGMap.containsKey(oid);
    }

    public static ASN1ObjectIdentifier getObjectIdentifier(String signatureAlgorithm) {
        return (ASN1ObjectIdentifier) ALGOIDMap.get(signatureAlgorithm);
    }

    public static AlgorithmIdentifier getAlgorithmIdentifier(String signatureAlgorithm) {
        ASN1ObjectIdentifier oid = (ASN1ObjectIdentifier) ALGOIDMap.get(signatureAlgorithm);
        return new AlgorithmIdentifier(oid, DERNull.INSTANCE);
    }

    public String getMechanismType() {
        return this.mechanismType;
    }

    public void setMechanismType(String mechanismType) {
        this.mechanismType = mechanismType;
    }

    public Object getParam() {
        return this.param;
    }

    public void setParam(Object param) {
        this.param = param;
    }

    public int hashCode() {
        int prime = 0;
        int result = 1;
        result = 31 * result + (this.mechanismType == null ? 0 : this.mechanismType.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            MechanismKit other = (MechanismKit) obj;
            if (this.mechanismType == null) {
                if (other.mechanismType != null) {
                    return false;
                }
            } else if (!this.mechanismType.equalsIgnoreCase(other.mechanismType)) {
                return false;
            }

            return true;
        }
    }

    public String toString() {
        return this.mechanismType;
    }
}
