package com.myzuji.sadk.algorithm.util;

import com.myzuji.sadk.algorithm.common.X9ObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Encodable;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.Certificate;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

public class SM2OIDUtil {
    public SM2OIDUtil() {
    }

    public static boolean isSM2PublicKeyOID(ASN1ObjectIdentifier OID) {
        boolean isSM2PublicKeyOID = false;
        if (OID != null) {
            isSM2PublicKeyOID = OID.equals(X9ObjectIdentifiers.sm2PubKey) || OID.equals(X9ObjectIdentifiers.sm3WithSM2Encryption) || OID.equals(X9ObjectIdentifiers.sm2PubKey_OLD) || OID.equals(X9ObjectIdentifiers.sm3WithSM2Encryption_OLD);
        }

        return isSM2PublicKeyOID;
    }

    public static boolean isSM2PublicKeyOID(ASN1Encodable OID) {
        boolean isSM2PublicKeyOID = false;
        if (OID != null) {
            isSM2PublicKeyOID = OID.equals(X9ObjectIdentifiers.sm2PubKey) || OID.equals(X9ObjectIdentifiers.sm2PubKey_OLD);
        }

        return isSM2PublicKeyOID;
    }

    public static boolean isSM2PublicKey(SubjectPublicKeyInfo spki) {
        boolean isSM2PublicKeyOID = false;
        if (spki != null) {
            ASN1ObjectIdentifier OID = spki.getAlgorithm().getAlgorithm();
            isSM2PublicKeyOID = OID.equals(X9ObjectIdentifiers.sm2PubKey) || OID.equals(X9ObjectIdentifiers.sm2PubKey_OLD);
        }

        return isSM2PublicKeyOID;
    }

    public static boolean isSm3WithSM2Encryption(ASN1ObjectIdentifier OID) {
        boolean isSM2PublicKeyOID = false;
        if (OID != null) {
            isSM2PublicKeyOID = OID.equals(X9ObjectIdentifiers.sm3WithSM2Encryption) || OID.equals(X9ObjectIdentifiers.sm3WithSM2Encryption_OLD);
        }

        return isSM2PublicKeyOID;
    }

    public static boolean isSm3WithSM2Encryption(Certificate cert) {
        boolean isSM2PublicKeyOID = false;
        if (cert != null) {
            ASN1ObjectIdentifier OID = cert.getSignatureAlgorithm().getAlgorithm();
            isSM2PublicKeyOID = OID.equals(X9ObjectIdentifiers.sm3WithSM2Encryption) || OID.equals(X9ObjectIdentifiers.sm3WithSM2Encryption_OLD);
        }

        return isSM2PublicKeyOID;
    }
}
