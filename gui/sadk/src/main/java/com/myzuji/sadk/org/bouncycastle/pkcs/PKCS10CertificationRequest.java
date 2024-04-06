package com.myzuji.sadk.org.bouncycastle.pkcs;

import com.myzuji.sadk.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Primitive;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Set;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.Attribute;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.CertificationRequest;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.CertificationRequestInfo;
import com.myzuji.sadk.org.bouncycastle.asn1.x500.X500Name;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import com.myzuji.sadk.org.bouncycastle.operator.ContentVerifier;
import com.myzuji.sadk.org.bouncycastle.operator.ContentVerifierProvider;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class PKCS10CertificationRequest {
    private static Attribute[] EMPTY_ARRAY = new Attribute[0];
    private CertificationRequest certificationRequest;

    private static CertificationRequest parseBytes(byte[] encoding) throws IOException {
        try {
            return CertificationRequest.getInstance(ASN1Primitive.fromByteArray(encoding));
        } catch (ClassCastException var2) {
            ClassCastException e = var2;
            throw new PKCSIOException("malformed data: " + e.getMessage(), e);
        } catch (IllegalArgumentException var3) {
            IllegalArgumentException e = var3;
            throw new PKCSIOException("malformed data: " + e.getMessage(), e);
        }
    }

    public PKCS10CertificationRequest(CertificationRequest certificationRequest) {
        this.certificationRequest = certificationRequest;
    }

    public PKCS10CertificationRequest(byte[] encoded) throws IOException {
        this(parseBytes(encoded));
    }

    public CertificationRequest toASN1Structure() {
        return this.certificationRequest;
    }

    public X500Name getSubject() {
        return X500Name.getInstance(this.certificationRequest.getCertificationRequestInfo().getSubject());
    }

    public AlgorithmIdentifier getSignatureAlgorithm() {
        return this.certificationRequest.getSignatureAlgorithm();
    }

    public byte[] getSignature() {
        return this.certificationRequest.getSignature().getBytes();
    }

    public SubjectPublicKeyInfo getSubjectPublicKeyInfo() {
        return this.certificationRequest.getCertificationRequestInfo().getSubjectPublicKeyInfo();
    }

    public Attribute[] getAttributes() {
        ASN1Set attrSet = this.certificationRequest.getCertificationRequestInfo().getAttributes();
        if (attrSet == null) {
            return EMPTY_ARRAY;
        } else {
            Attribute[] attrs = new Attribute[attrSet.size()];

            for (int i = 0; i != attrSet.size(); ++i) {
                attrs[i] = Attribute.getInstance(attrSet.getObjectAt(i));
            }

            return attrs;
        }
    }

    public Attribute[] getAttributes(ASN1ObjectIdentifier type) {
        ASN1Set attrSet = this.certificationRequest.getCertificationRequestInfo().getAttributes();
        if (attrSet == null) {
            return EMPTY_ARRAY;
        } else {
            List list = new ArrayList();

            for (int i = 0; i != attrSet.size(); ++i) {
                Attribute attr = Attribute.getInstance(attrSet.getObjectAt(i));
                if (attr.getAttrType().equals(type)) {
                    list.add(attr);
                }
            }

            if (list.size() == 0) {
                return EMPTY_ARRAY;
            } else {
                return (Attribute[]) ((Attribute[]) list.toArray(new Attribute[list.size()]));
            }
        }
    }

    public byte[] getEncoded() throws IOException {
        return this.certificationRequest.getEncoded();
    }

    public boolean isSignatureValid(ContentVerifierProvider verifierProvider) throws PKCSException {
        CertificationRequestInfo requestInfo = this.certificationRequest.getCertificationRequestInfo();

        ContentVerifier verifier;
        try {
            verifier = verifierProvider.get(this.certificationRequest.getSignatureAlgorithm());
            OutputStream sOut = verifier.getOutputStream();
            sOut.write(requestInfo.getEncoded("DER"));
            sOut.close();
        } catch (Exception var5) {
            Exception e = var5;
            throw new PKCSException("unable to process signature: " + e.getMessage(), e);
        }

        return verifier.verify(this.certificationRequest.getSignature().getBytes());
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof PKCS10CertificationRequest)) {
            return false;
        } else {
            PKCS10CertificationRequest other = (PKCS10CertificationRequest) o;
            return this.toASN1Structure().equals(other.toASN1Structure());
        }
    }

    public int hashCode() {
        return this.toASN1Structure().hashCode();
    }
}
