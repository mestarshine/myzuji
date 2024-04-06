package com.myzuji.sadk.org.bouncycastle.cert;

import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.*;
import com.myzuji.sadk.org.bouncycastle.operator.ContentSigner;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.*;

public class CertUtils {
    private static Set EMPTY_SET = Collections.unmodifiableSet(new HashSet());
    private static List EMPTY_LIST = Collections.unmodifiableList(new ArrayList());

    CertUtils() {
    }

    static X509CertificateHolder generateFullCert(ContentSigner signer, TBSCertificate tbsCert) {
        try {
            return new X509CertificateHolder(generateStructure(tbsCert, signer.getAlgorithmIdentifier(), generateSig(signer, tbsCert)));
        } catch (IOException var3) {
            throw new IllegalStateException("cannot produce certificate signature");
        }
    }

    static X509AttributeCertificateHolder generateFullAttrCert(ContentSigner signer, AttributeCertificateInfo attrInfo) {
        try {
            return new X509AttributeCertificateHolder(generateAttrStructure(attrInfo, signer.getAlgorithmIdentifier(), generateSig(signer, attrInfo)));
        } catch (IOException var3) {
            throw new IllegalStateException("cannot produce attribute certificate signature");
        }
    }

    static X509CRLHolder generateFullCRL(ContentSigner signer, TBSCertList tbsCertList) {
        try {
            return new X509CRLHolder(generateCRLStructure(tbsCertList, signer.getAlgorithmIdentifier(), generateSig(signer, tbsCertList)));
        } catch (IOException var3) {
            throw new IllegalStateException("cannot produce certificate signature");
        }
    }

    private static byte[] generateSig(ContentSigner signer, ASN1Encodable tbsObj) throws IOException {
        OutputStream sOut = signer.getOutputStream();
        DEROutputStream dOut = new DEROutputStream(sOut);
        dOut.writeObject(tbsObj);
        sOut.close();
        return signer.getSignature();
    }

    private static Certificate generateStructure(TBSCertificate tbsCert, AlgorithmIdentifier sigAlgId, byte[] signature) {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(tbsCert);
        v.add(sigAlgId);
        v.add(new DERBitString(signature));
        return Certificate.getInstance(new DERSequence(v));
    }

    private static AttributeCertificate generateAttrStructure(AttributeCertificateInfo attrInfo, AlgorithmIdentifier sigAlgId, byte[] signature) {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(attrInfo);
        v.add(sigAlgId);
        v.add(new DERBitString(signature));
        return AttributeCertificate.getInstance(new DERSequence(v));
    }

    private static CertificateList generateCRLStructure(TBSCertList tbsCertList, AlgorithmIdentifier sigAlgId, byte[] signature) {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(tbsCertList);
        v.add(sigAlgId);
        v.add(new DERBitString(signature));
        return CertificateList.getInstance(new DERSequence(v));
    }

    static Set getCriticalExtensionOIDs(Extensions extensions) {
        return extensions == null ? EMPTY_SET : Collections.unmodifiableSet(new HashSet(Arrays.asList(extensions.getCriticalExtensionOIDs())));
    }

    static Set getNonCriticalExtensionOIDs(Extensions extensions) {
        return extensions == null ? EMPTY_SET : Collections.unmodifiableSet(new HashSet(Arrays.asList(extensions.getNonCriticalExtensionOIDs())));
    }

    static List getExtensionOIDs(Extensions extensions) {
        return extensions == null ? EMPTY_LIST : Collections.unmodifiableList(Arrays.asList(extensions.getExtensionOIDs()));
    }

    static void addExtension(ExtensionsGenerator extGenerator, ASN1ObjectIdentifier oid, boolean isCritical, ASN1Encodable value) throws CertIOException {
        try {
            extGenerator.addExtension(oid, isCritical, value);
        } catch (IOException var5) {
            IOException e = var5;
            throw new CertIOException("cannot encode extension: " + e.getMessage(), e);
        }
    }

    static DERBitString booleanToBitString(boolean[] id) {
        byte[] bytes = new byte[(id.length + 7) / 8];

        int pad;
        for (pad = 0; pad != id.length; ++pad) {
            bytes[pad / 8] = (byte) (bytes[pad / 8] | (id[pad] ? 1 << 7 - pad % 8 : 0));
        }

        pad = id.length % 8;
        if (pad == 0) {
            return new DERBitString(bytes);
        } else {
            return new DERBitString(bytes, 8 - pad);
        }
    }

    static boolean[] bitStringToBoolean(DERBitString bitString) {
        if (bitString != null) {
            byte[] bytes = bitString.getBytes();
            boolean[] boolId = new boolean[bytes.length * 8 - bitString.getPadBits()];

            for (int i = 0; i != boolId.length; ++i) {
                boolId[i] = (bytes[i / 8] & 128 >>> i % 8) != 0;
            }

            return boolId;
        } else {
            return null;
        }
    }

    static Date recoverDate(ASN1GeneralizedTime time) {
        try {
            return time.getDate();
        } catch (ParseException var2) {
            ParseException e = var2;
            throw new IllegalStateException("unable to recover date: " + e.getMessage());
        }
    }

    static boolean isAlgIdEqual(AlgorithmIdentifier id1, AlgorithmIdentifier id2) {
        if (!id1.getAlgorithm().equals(id2.getAlgorithm())) {
            return false;
        } else if (id1.getParameters() == null) {
            return id2.getParameters() == null || id2.getParameters().equals(DERNull.INSTANCE);
        } else if (id2.getParameters() == null) {
            return id1.getParameters() == null || id1.getParameters().equals(DERNull.INSTANCE);
        } else {
            return id1.getParameters().equals(id2.getParameters());
        }
    }
}
