package com.myzuji.sadk.org.bouncycastle.jcajce.provider.asymmetric.util;

import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Encodable;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

public class KeyUtil {
    public KeyUtil() {
    }

    public static byte[] getEncodedSubjectPublicKeyInfo(AlgorithmIdentifier algId, ASN1Encodable keyData) {
        try {
            return getEncodedSubjectPublicKeyInfo(new SubjectPublicKeyInfo(algId, keyData));
        } catch (Exception var3) {
            return null;
        }
    }

    public static byte[] getEncodedSubjectPublicKeyInfo(AlgorithmIdentifier algId, byte[] keyData) {
        try {
            return getEncodedSubjectPublicKeyInfo(new SubjectPublicKeyInfo(algId, keyData));
        } catch (Exception var3) {
            return null;
        }
    }

    public static byte[] getEncodedSubjectPublicKeyInfo(SubjectPublicKeyInfo info) {
        try {
            return info.getEncoded("DER");
        } catch (Exception var2) {
            return null;
        }
    }

    public static byte[] getEncodedPrivateKeyInfo(AlgorithmIdentifier algId, ASN1Encodable privKey) {
        try {
            PrivateKeyInfo info = new PrivateKeyInfo(algId, privKey.toASN1Primitive());
            return getEncodedPrivateKeyInfo(info);
        } catch (Exception var3) {
            return null;
        }
    }

    public static byte[] getEncodedPrivateKeyInfo(PrivateKeyInfo info) {
        try {
            return info.getEncoded("DER");
        } catch (Exception var2) {
            return null;
        }
    }
}

