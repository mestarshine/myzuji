package com.myzuji.sadk.org.bouncycastle.cms;

import com.myzuji.sadk.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Set;
import com.myzuji.sadk.org.bouncycastle.asn1.DEROctetString;
import com.myzuji.sadk.org.bouncycastle.asn1.DERSet;
import com.myzuji.sadk.org.bouncycastle.asn1.cms.AttributeTable;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import com.myzuji.sadk.org.bouncycastle.cert.X509CertificateHolder;
import com.myzuji.sadk.org.bouncycastle.operator.*;
import com.myzuji.sadk.org.bouncycastle.util.Arrays;
import com.myzuji.sadk.org.bouncycastle.util.io.TeeOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SignerInfoGenerator {
    private final SignerIdentifier signerIdentifier;
    private final CMSAttributeTableGenerator sAttrGen;
    private final CMSAttributeTableGenerator unsAttrGen;
    private final ContentSigner signer;
    private final DigestCalculator digester;
    private final DigestAlgorithmIdentifierFinder digAlgFinder;
    private final CMSSignatureEncryptionAlgorithmFinder sigEncAlgFinder;
    private byte[] calculatedDigest;
    private X509CertificateHolder certHolder;

    SignerInfoGenerator(SignerIdentifier signerIdentifier, ContentSigner signer, DigestCalculatorProvider digesterProvider, CMSSignatureEncryptionAlgorithmFinder sigEncAlgFinder) throws OperatorCreationException {
        this(signerIdentifier, signer, digesterProvider, sigEncAlgFinder, false);
    }

    SignerInfoGenerator(SignerIdentifier signerIdentifier, ContentSigner signer, DigestCalculatorProvider digesterProvider, CMSSignatureEncryptionAlgorithmFinder sigEncAlgFinder, boolean isDirectSignature) throws OperatorCreationException {
        this.digAlgFinder = new DefaultDigestAlgorithmIdentifierFinder();
        this.calculatedDigest = null;
        this.signerIdentifier = signerIdentifier;
        this.signer = signer;
        if (digesterProvider != null) {
            this.digester = digesterProvider.get(this.digAlgFinder.find(signer.getAlgorithmIdentifier()));
        } else {
            this.digester = null;
        }

        if (isDirectSignature) {
            this.sAttrGen = null;
            this.unsAttrGen = null;
        } else {
            this.sAttrGen = new DefaultSignedAttributeTableGenerator();
            this.unsAttrGen = null;
        }

        this.sigEncAlgFinder = sigEncAlgFinder;
    }

    public SignerInfoGenerator(SignerInfoGenerator original, CMSAttributeTableGenerator sAttrGen, CMSAttributeTableGenerator unsAttrGen) {
        this.digAlgFinder = new DefaultDigestAlgorithmIdentifierFinder();
        this.calculatedDigest = null;
        this.signerIdentifier = original.signerIdentifier;
        this.signer = original.signer;
        this.digester = original.digester;
        this.sigEncAlgFinder = original.sigEncAlgFinder;
        this.sAttrGen = sAttrGen;
        this.unsAttrGen = unsAttrGen;
    }

    SignerInfoGenerator(SignerIdentifier signerIdentifier, ContentSigner signer, DigestCalculatorProvider digesterProvider, CMSSignatureEncryptionAlgorithmFinder sigEncAlgFinder, CMSAttributeTableGenerator sAttrGen, CMSAttributeTableGenerator unsAttrGen) throws OperatorCreationException {
        this.digAlgFinder = new DefaultDigestAlgorithmIdentifierFinder();
        this.calculatedDigest = null;
        this.signerIdentifier = signerIdentifier;
        this.signer = signer;
        if (digesterProvider != null) {
            this.digester = digesterProvider.get(this.digAlgFinder.find(signer.getAlgorithmIdentifier()));
        } else {
            this.digester = null;
        }

        this.sAttrGen = sAttrGen;
        this.unsAttrGen = unsAttrGen;
        this.sigEncAlgFinder = sigEncAlgFinder;
    }

    public SignerIdentifier getSID() {
        return this.signerIdentifier;
    }

    public int getGeneratedVersion() {
        return this.signerIdentifier.isTagged() ? 3 : 1;
    }

    public boolean hasAssociatedCertificate() {
        return this.certHolder != null;
    }

    public X509CertificateHolder getAssociatedCertificate() {
        return this.certHolder;
    }

    public AlgorithmIdentifier getDigestAlgorithm() {
        return this.digester != null ? this.digester.getAlgorithmIdentifier() : this.digAlgFinder.find(this.signer.getAlgorithmIdentifier());
    }

    public OutputStream getCalculatingOutputStream() {
        if (this.digester != null) {
            return (OutputStream) (this.sAttrGen == null ? new TeeOutputStream(this.digester.getOutputStream(), this.signer.getOutputStream()) : this.digester.getOutputStream());
        } else {
            return this.signer.getOutputStream();
        }
    }

    public com.myzuji.sadk.org.bouncycastle.cms.SignerInfo generate(ASN1ObjectIdentifier contentType) throws CMSException {
        try {
            ASN1Set signedAttr = null;
            AlgorithmIdentifier digestAlg = null;
            if (this.sAttrGen != null) {
                digestAlg = this.digester.getAlgorithmIdentifier();
                this.calculatedDigest = this.digester.getDigest();
                Map parameters = this.getBaseParameters(contentType, this.digester.getAlgorithmIdentifier(), this.calculatedDigest);
                AttributeTable signed = this.sAttrGen.getAttributes(Collections.unmodifiableMap(parameters));
                signedAttr = this.getAttributeSet(signed);
                OutputStream sOut = this.signer.getOutputStream();
                sOut.write(signedAttr.getEncoded("DER"));
                sOut.close();
            } else if (this.digester != null) {
                digestAlg = this.digester.getAlgorithmIdentifier();
                this.calculatedDigest = this.digester.getDigest();
            } else {
                digestAlg = this.digAlgFinder.find(this.signer.getAlgorithmIdentifier());
                this.calculatedDigest = null;
            }

            byte[] sigBytes = this.signer.getSignature();
            ASN1Set unsignedAttr = null;
            if (this.unsAttrGen != null) {
                Map parameters = this.getBaseParameters(contentType, digestAlg, this.calculatedDigest);
                parameters.put("encryptedDigest", Arrays.clone(sigBytes));
                AttributeTable unsigned = this.unsAttrGen.getAttributes(Collections.unmodifiableMap(parameters));
                unsignedAttr = this.getAttributeSet(unsigned);
            }

            AlgorithmIdentifier digestEncryptionAlgorithm = this.sigEncAlgFinder.findEncryptionAlgorithm(this.signer.getAlgorithmIdentifier());
            return new SignerInfo(this.signerIdentifier, digestAlg, signedAttr, digestEncryptionAlgorithm, new DEROctetString(sigBytes), unsignedAttr);
        } catch (IOException var8) {
            IOException e = var8;
            throw new CMSException("encoding error.", e);
        }
    }

    void setAssociatedCertificate(X509CertificateHolder certHolder) {
        this.certHolder = certHolder;
    }

    private ASN1Set getAttributeSet(AttributeTable attr) {
        return attr != null ? new DERSet(attr.toASN1EncodableVector()) : null;
    }

    private Map getBaseParameters(ASN1ObjectIdentifier contentType, AlgorithmIdentifier digAlgId, byte[] hash) {
        Map param = new HashMap();
        if (contentType != null) {
            param.put("contentType", contentType);
        }

        param.put("digestAlgID", digAlgId);
        param.put("digest", Arrays.clone(hash));
        return param;
    }

    public byte[] getCalculatedDigest() {
        return this.calculatedDigest != null ? Arrays.clone(this.calculatedDigest) : null;
    }

    public CMSAttributeTableGenerator getSignedAttributeTableGenerator() {
        return this.sAttrGen;
    }

    public CMSAttributeTableGenerator getUnsignedAttributeTableGenerator() {
        return this.unsAttrGen;
    }
}
