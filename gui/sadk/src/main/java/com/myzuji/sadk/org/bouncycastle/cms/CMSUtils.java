package com.myzuji.sadk.org.bouncycastle.cms;

import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.asn1.cms.CMSObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.cms.OtherRevocationInfoFormat;
import com.myzuji.sadk.org.bouncycastle.asn1.ocsp.OCSPResponse;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.ContentInfo;
import com.myzuji.sadk.org.bouncycastle.cert.X509AttributeCertificateHolder;
import com.myzuji.sadk.org.bouncycastle.cert.X509CRLHolder;
import com.myzuji.sadk.org.bouncycastle.cert.X509CertificateHolder;
import com.myzuji.sadk.org.bouncycastle.operator.DigestCalculator;
import com.myzuji.sadk.org.bouncycastle.util.Selector;
import com.myzuji.sadk.org.bouncycastle.util.Store;
import com.myzuji.sadk.org.bouncycastle.util.Strings;
import com.myzuji.sadk.org.bouncycastle.util.io.Streams;
import com.myzuji.sadk.org.bouncycastle.util.io.TeeInputStream;
import com.myzuji.sadk.org.bouncycastle.util.io.TeeOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CMSUtils {
    CMSUtils() {
    }

    static ContentInfo readContentInfo(byte[] input) throws CMSException {
        return readContentInfo(new ASN1InputStream(input));
    }

    static ContentInfo readContentInfo(InputStream input) throws CMSException {
        return readContentInfo(new ASN1InputStream(input));
    }

    static List getCertificatesFromStore(Store certStore) throws CMSException {
        List certs = new ArrayList();

        try {
            Iterator it = certStore.getMatches((Selector) null).iterator();

            while (it.hasNext()) {
                X509CertificateHolder c = (X509CertificateHolder) it.next();
                certs.add(c.toASN1Structure());
            }

            return certs;
        } catch (ClassCastException var4) {
            ClassCastException e = var4;
            throw new CMSException("error processing certs", e);
        }
    }

    static List getAttributeCertificatesFromStore(Store attrStore) throws CMSException {
        List certs = new ArrayList();

        try {
            Iterator it = attrStore.getMatches((Selector) null).iterator();

            while (it.hasNext()) {
                X509AttributeCertificateHolder attrCert = (X509AttributeCertificateHolder) it.next();
                certs.add(new DERTaggedObject(false, 2, attrCert.toASN1Structure()));
            }

            return certs;
        } catch (ClassCastException var4) {
            ClassCastException e = var4;
            throw new CMSException("error processing certs", e);
        }
    }

    static List getCRLsFromStore(Store crlStore) throws CMSException {
        List crls = new ArrayList();

        try {
            Iterator it = crlStore.getMatches((Selector) null).iterator();

            while (it.hasNext()) {
                Object rev = it.next();
                if (rev instanceof X509CRLHolder) {
                    X509CRLHolder c = (X509CRLHolder) rev;
                    crls.add(c.toASN1Structure());
                } else if (rev instanceof OtherRevocationInfoFormat) {
                    OtherRevocationInfoFormat infoFormat = OtherRevocationInfoFormat.getInstance(rev);
                    validateInfoFormat(infoFormat);
                    crls.add(new DERTaggedObject(false, 1, infoFormat));
                } else if (rev instanceof ASN1TaggedObject) {
                    crls.add(rev);
                }
            }

            return crls;
        } catch (ClassCastException var5) {
            ClassCastException e = var5;
            throw new CMSException("error processing certs", e);
        }
    }

    private static void validateInfoFormat(OtherRevocationInfoFormat infoFormat) {
        if (CMSObjectIdentifiers.id_ri_ocsp_response.equals(infoFormat.getInfoFormat())) {
            OCSPResponse resp = OCSPResponse.getInstance(infoFormat.getInfo());
            if (resp.getResponseStatus().getValue().intValue() != 0) {
                throw new IllegalArgumentException("cannot add unsuccessful OCSP response to CMS SignedData");
            }
        }

    }

    static Collection getOthersFromStore(ASN1ObjectIdentifier otherRevocationInfoFormat, Store otherRevocationInfos) {
        List others = new ArrayList();
        Iterator it = otherRevocationInfos.getMatches((Selector) null).iterator();

        while (it.hasNext()) {
            ASN1Encodable info = (ASN1Encodable) it.next();
            OtherRevocationInfoFormat infoFormat = new OtherRevocationInfoFormat(otherRevocationInfoFormat, info);
            validateInfoFormat(infoFormat);
            others.add(new DERTaggedObject(false, 1, infoFormat));
        }

        return others;
    }

    static ASN1Set createBerSetFromList(List derObjects) {
        ASN1EncodableVector v = new ASN1EncodableVector();
        Iterator it = derObjects.iterator();

        while (it.hasNext()) {
            v.add((ASN1Encodable) it.next());
        }

        return new BERSet(v);
    }

    static ASN1Set createDerSetFromList(List derObjects) {
        ASN1EncodableVector v = new ASN1EncodableVector();
        Iterator it = derObjects.iterator();

        while (it.hasNext()) {
            v.add((ASN1Encodable) it.next());
        }

        return new DERSet(v);
    }

    static OutputStream createBEROctetOutputStream(OutputStream s, int tagNo, boolean isExplicit, int bufferSize) throws IOException {
        BEROctetStringGenerator octGen = new BEROctetStringGenerator(s, tagNo, isExplicit);
        return bufferSize != 0 ? octGen.getOctetOutputStream(new byte[bufferSize]) : octGen.getOctetOutputStream();
    }

    private static ContentInfo readContentInfo(ASN1InputStream in) throws CMSException {
        try {
            return ContentInfo.getInstance(in.readObject());
        } catch (IOException var2) {
            IOException e = var2;
            throw new CMSException("IOException reading content.", e);
        } catch (ClassCastException var3) {
            ClassCastException e = var3;
            throw new CMSException("Malformed content.", e);
        } catch (IllegalArgumentException var4) {
            IllegalArgumentException e = var4;
            throw new CMSException("Malformed content.", e);
        }
    }

    static byte[] getPasswordBytes(int scheme, char[] password) {
        return scheme == 0 ? PKCS5PasswordToBytes(password) : PKCS5PasswordToUTF8Bytes(password);
    }

    private static byte[] PKCS5PasswordToBytes(char[] password) {
        if (password == null) {
            return new byte[0];
        } else {
            byte[] bytes = new byte[password.length];

            for (int i = 0; i != bytes.length; ++i) {
                bytes[i] = (byte) password[i];
            }

            return bytes;
        }
    }

    private static byte[] PKCS5PasswordToUTF8Bytes(char[] password) {
        return password != null ? Strings.toUTF8ByteArray(password) : new byte[0];
    }

    public static byte[] streamToByteArray(InputStream in) throws IOException {
        return Streams.readAll(in);
    }

    public static byte[] streamToByteArray(InputStream in, int limit) throws IOException {
        return Streams.readAllLimited(in, limit);
    }

    static InputStream attachDigestsToInputStream(Collection digests, InputStream s) {
        InputStream result = s;

        DigestCalculator digest;
        for (Iterator it = digests.iterator(); it.hasNext(); result = new TeeInputStream((InputStream) result, digest.getOutputStream())) {
            digest = (DigestCalculator) it.next();
        }

        return (InputStream) result;
    }

    static OutputStream attachSignersToOutputStream(Collection signers, OutputStream s) {
        OutputStream result = s;

        SignerInfoGenerator signerGen;
        for (Iterator it = signers.iterator(); it.hasNext(); result = getSafeTeeOutputStream(result, signerGen.getCalculatingOutputStream())) {
            signerGen = (SignerInfoGenerator) it.next();
        }

        return result;
    }

    static OutputStream getSafeOutputStream(OutputStream s) {
        return (OutputStream) (s == null ? new NullOutputStream() : s);
    }

    static OutputStream getSafeTeeOutputStream(OutputStream s1, OutputStream s2) {
        return (OutputStream) (s1 == null ? getSafeOutputStream(s2) : (s2 == null ? getSafeOutputStream(s1) : new TeeOutputStream(s1, s2)));
    }
}
