package com.myzuji.sadk.algorithm.util;

import com.myzuji.sadk.algorithm.common.PKCSObjectIdentifiers;
import com.myzuji.sadk.algorithm.common.PKIException;
import com.myzuji.sadk.asn1.parser.ASN1Parser;
import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.ContentInfo;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.SignedData;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.Certificate;
import com.myzuji.sadk.x509.certificate.X509Cert;

import java.io.*;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Vector;

public class P7BParser {
    public P7BParser() {
    }

    private static ContentInfo generateP7B(X509Cert[] certs) {
        ASN1EncodableVector v = new ASN1EncodableVector();
        if (certs != null) {
            for (int i = 0; i < certs.length; ++i) {
                Certificate certStruc = certs[i].getCertStructure();
                v.add(certStruc);
            }
        }

        DERSet certSet = new DERSet(v);
        DERSet algs = new DERSet();
        DERSet signerInfos = new DERSet();
        ContentInfo ci = new ContentInfo(PKCSObjectIdentifiers.data, (ASN1Encodable) null);
        SignedData signedData = new SignedData(new ASN1Integer(1L), algs, ci, certSet, (ASN1Set) null, signerInfos);
        ContentInfo contentInfo = new ContentInfo(PKCSObjectIdentifiers.signedData, signedData);
        return contentInfo;
    }

    public static void generateP7BFile(X509Cert[] certs, String filePath) throws PKIException {
        ContentInfo contentInfo = generateP7B(certs);
        FileOutputStream fos = null;
        DEROutputStream dos = null;

        try {
            fos = new FileOutputStream(filePath);
            dos = new DEROutputStream(fos);
            dos.writeObject(contentInfo.toASN1Primitive());
        } catch (Exception var9) {
            Exception ex = var9;
            throw new PKIException(PKIException.GEN_P7B_ERR, PKIException.GEN_P7B_ERR_DES, ex);
        } finally {
            close((ASN1OutputStream) dos);
            close((OutputStream) fos);
        }

    }

    public static void generateP7BFile(X509Cert[] x509Certs, OutputStream os) throws PKIException {
        ContentInfo contentInfo = generateP7B(x509Certs);
        DEROutputStream dos = null;

        try {
            dos = new DEROutputStream(os);
            dos.writeObject(contentInfo.toASN1Primitive());
        } catch (Exception var8) {
            Exception ex = var8;
            throw new PKIException(PKIException.GEN_P7B_ERR, PKIException.GEN_P7B_ERR_DES, ex);
        } finally {
            close((ASN1OutputStream) dos);
            close(os);
        }

    }

    public static byte[] generateP7BData(X509Cert[] certs) throws PKIException {
        ContentInfo contentInfo = generateP7B(certs);
        ByteArrayOutputStream bos = null;
        DEROutputStream dos = null;

        byte[] var6;
        try {
            bos = new ByteArrayOutputStream();
            dos = new DEROutputStream(bos);
            dos.writeObject(contentInfo.toASN1Primitive());
            byte[] noB64Data = bos.toByteArray();
            byte[] b64Data = Base64.getEncoder().encode(noB64Data);
            var6 = b64Data;
        } catch (Exception var10) {
            Exception ex = var10;
            throw new PKIException(PKIException.GEN_P7B_ERR, PKIException.GEN_P7B_ERR_DES, ex);
        } finally {
            close((ASN1OutputStream) dos);
            close((OutputStream) bos);
        }

        return var6;
    }

    public static X509Cert[] parseP7B(byte[] p7bData) throws PKIException {
        if (p7bData == null) {
            throw new PKIException("P7BFile data should not be null");
        } else {
            ASN1Sequence sequence;
            try {
                sequence = ASN1Parser.getDERSequenceFrom(p7bData);
            } catch (Exception var9) {
                Exception e = var9;
                throw new PKIException("P7BFile data decoding failure", e);
            }

            ContentInfo contentInfo = ContentInfo.getInstance(sequence);
            if (!contentInfo.getContentType().equals(PKCSObjectIdentifiers.signedData)) {
                throw new PKIException(PKIException.PARSE_P7B_ERR, PKIException.PARSE_P7B_ERR_DES + " " + PKIException.PARSE_P7B_ERR_FORMAT_DES + " " + contentInfo.getContentType().getId());
            } else {
                SignedData signedData = SignedData.getInstance(contentInfo.getContent());
                ASN1Set certSet = signedData.getCertificates();
                Enumeration enumeration = certSet.getObjects();
                Vector v = new Vector();
                X509Cert cert = null;

                while (enumeration.hasMoreElements()) {
                    Certificate certStruc = Certificate.getInstance(enumeration.nextElement());
                    cert = new X509Cert(certStruc);
                    v.add(cert);
                }

                X509Cert[] certs = new X509Cert[v.size()];
                v.toArray(certs);
                return certs;
            }
        }
    }

    public static X509Cert[] parseP7B(String p7bFilePath) throws IOException, PKIException {
        FileInputStream fis = new FileInputStream(p7bFilePath);
        return parseP7B((InputStream) fis);
    }

    public static X509Cert[] parseP7B(InputStream is) throws IOException, PKIException {
        byte[] p7bData = new byte[is.available()];
        is.read(p7bData);
        is.close();
        return parseP7B(p7bData);
    }

    private static final void close(OutputStream out) {
        if (out != null) {
            try {
                out.close();
            } catch (Exception var2) {
            }
        }

    }

    private static final void close(ASN1OutputStream out) {
        if (out != null) {
            try {
                out.close();
            } catch (Exception var2) {
            }
        }

    }
}
