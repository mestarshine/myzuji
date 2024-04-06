package com.myzuji.sadk.org.bouncycastle.cms;

import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.asn1.cms.Attribute;
import com.myzuji.sadk.org.bouncycastle.asn1.cms.AttributeTable;
import com.myzuji.sadk.org.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import com.myzuji.sadk.org.bouncycastle.asn1.cms.Time;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.DigestInfo;
import com.myzuji.sadk.org.bouncycastle.cert.X509CertificateHolder;
import com.myzuji.sadk.org.bouncycastle.operator.ContentVerifier;
import com.myzuji.sadk.org.bouncycastle.operator.DigestCalculator;
import com.myzuji.sadk.org.bouncycastle.operator.OperatorCreationException;
import com.myzuji.sadk.org.bouncycastle.operator.RawContentVerifier;
import com.myzuji.sadk.org.bouncycastle.util.Arrays;
import com.myzuji.sadk.org.bouncycastle.util.io.TeeOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class SignerInformation {
    private SignerId sid;
    private SignerInfo info;
    private AlgorithmIdentifier digestAlgorithm;
    private AlgorithmIdentifier encryptionAlgorithm;
    private final ASN1Set signedAttributeSet;
    private final ASN1Set unsignedAttributeSet;
    private CMSProcessable content;
    private byte[] signature;
    private ASN1ObjectIdentifier contentType;
    private byte[] resultDigest;
    private AttributeTable signedAttributeValues;
    private AttributeTable unsignedAttributeValues;
    private boolean isCounterSignature;

    SignerInformation(SignerInfo info, ASN1ObjectIdentifier contentType, CMSProcessable content, byte[] resultDigest) {
        this.info = info;
        this.contentType = contentType;
        this.isCounterSignature = contentType == null;
        SignerIdentifier s = info.getSID();
        if (s.isTagged()) {
            ASN1OctetString octs = ASN1OctetString.getInstance(s.getId());
            this.sid = new SignerId(octs.getOctets());
        } else {
            IssuerAndSerialNumber iAnds = IssuerAndSerialNumber.getInstance(s.getId());
            this.sid = new SignerId(iAnds.getName(), iAnds.getSerialNumber().getValue());
        }

        this.digestAlgorithm = info.getDigestAlgorithm();
        this.signedAttributeSet = info.getAuthenticatedAttributes();
        this.unsignedAttributeSet = info.getUnauthenticatedAttributes();
        this.encryptionAlgorithm = info.getDigestEncryptionAlgorithm();
        this.signature = info.getEncryptedDigest().getOctets();
        this.content = content;
        this.resultDigest = resultDigest;
    }

    public boolean isCounterSignature() {
        return this.isCounterSignature;
    }

    public ASN1ObjectIdentifier getContentType() {
        return this.contentType;
    }

    private byte[] encodeObj(ASN1Encodable obj) throws IOException {
        return obj != null ? obj.toASN1Primitive().getEncoded() : null;
    }

    public SignerId getSID() {
        return this.sid;
    }

    public int getVersion() {
        return this.info.getVersion().getValue().intValue();
    }

    public AlgorithmIdentifier getDigestAlgorithmID() {
        return this.digestAlgorithm;
    }

    public String getDigestAlgOID() {
        return this.digestAlgorithm.getAlgorithm().getId();
    }

    public byte[] getDigestAlgParams() {
        try {
            return this.encodeObj(this.digestAlgorithm.getParameters());
        } catch (Exception var2) {
            Exception e = var2;
            throw new RuntimeException("exception getting digest parameters " + e);
        }
    }

    public byte[] getContentDigest() {
        if (this.resultDigest == null) {
            throw new IllegalStateException("method can only be called after verify.");
        } else {
            return Arrays.clone(this.resultDigest);
        }
    }

    public String getEncryptionAlgOID() {
        return this.encryptionAlgorithm.getAlgorithm().getId();
    }

    public byte[] getEncryptionAlgParams() {
        try {
            return this.encodeObj(this.encryptionAlgorithm.getParameters());
        } catch (Exception var2) {
            Exception e = var2;
            throw new RuntimeException("exception getting encryption parameters " + e);
        }
    }

    public AttributeTable getSignedAttributes() {
        if (this.signedAttributeSet != null && this.signedAttributeValues == null) {
            this.signedAttributeValues = new AttributeTable(this.signedAttributeSet);
        }

        return this.signedAttributeValues;
    }

    public AttributeTable getUnsignedAttributes() {
        if (this.unsignedAttributeSet != null && this.unsignedAttributeValues == null) {
            this.unsignedAttributeValues = new AttributeTable(this.unsignedAttributeSet);
        }

        return this.unsignedAttributeValues;
    }

    public byte[] getSignature() {
        return Arrays.clone(this.signature);
    }

    public SignerInformationStore getCounterSignatures() {
        AttributeTable unsignedAttributeTable = this.getUnsignedAttributes();
        if (unsignedAttributeTable == null) {
            return new SignerInformationStore(new ArrayList(0));
        } else {
            List counterSignatures = new ArrayList();
            ASN1EncodableVector allCSAttrs = unsignedAttributeTable.getAll(CMSAttributes.counterSignature);

            for (int i = 0; i < allCSAttrs.size(); ++i) {
                Attribute counterSignatureAttribute = (Attribute) allCSAttrs.get(i);
                ASN1Set values = counterSignatureAttribute.getAttrValues();
                if (values.size() < 1) {
                }

                Enumeration en = values.getObjects();

                while (en.hasMoreElements()) {
                    SignerInfo si = SignerInfo.getInstance(en.nextElement());
                    counterSignatures.add(new SignerInformation(si, (ASN1ObjectIdentifier) null, new CMSProcessableByteArray(this.getSignature()), (byte[]) null));
                }
            }

            return new SignerInformationStore(counterSignatures);
        }
    }

    public byte[] getEncodedSignedAttributes() throws IOException {
        return this.signedAttributeSet != null ? this.signedAttributeSet.getEncoded() : null;
    }

    private boolean doVerify(SignerInformationVerifier verifier) throws CMSException {
        String encName = CMSSignedHelper.INSTANCE.getEncryptionAlgName(this.getEncryptionAlgOID());

        ContentVerifier contentVerifier;
        OperatorCreationException e;
        try {
            contentVerifier = verifier.getContentVerifier(this.encryptionAlgorithm, this.info.getDigestAlgorithm());
        } catch (OperatorCreationException var12) {
            e = var12;
            throw new CMSException("can't create content verifier: " + e.getMessage(), e);
        }

        try {
            OutputStream sigOut = contentVerifier.getOutputStream();
            if (this.resultDigest == null) {
                DigestCalculator calc = verifier.getDigestCalculator(this.getDigestAlgorithmID());
                if (this.content != null) {
                    OutputStream digOut = calc.getOutputStream();
                    if (this.signedAttributeSet == null) {
                        if (contentVerifier instanceof RawContentVerifier) {
                            this.content.write(digOut);
                        } else {
                            OutputStream cOut = new TeeOutputStream(digOut, sigOut);
                            this.content.write(cOut);
                            cOut.close();
                        }
                    } else {
                        this.content.write(digOut);
                        sigOut.write(this.getEncodedSignedAttributes());
                    }

                    digOut.close();
                } else {
                    if (this.signedAttributeSet == null) {
                        throw new CMSException("data not encapsulated in signature - use detached constructor.");
                    }

                    sigOut.write(this.getEncodedSignedAttributes());
                }

                this.resultDigest = calc.getDigest();
            } else if (this.signedAttributeSet == null) {
                if (this.content != null) {
                    this.content.write(sigOut);
                }
            } else {
                sigOut.write(this.getEncodedSignedAttributes());
            }

            sigOut.close();
        } catch (IOException var10) {
            throw new CMSException("can't process mime object to create signature.", var10);
        } catch (OperatorCreationException var11) {
            e = var11;
            throw new CMSException("can't create digest calculator: " + e.getMessage(), e);
        }

        ASN1Primitive validMessageDigest = this.getSingleValuedSignedAttribute(CMSAttributes.contentType, "content-type");
        if (validMessageDigest == null) {
            if (!this.isCounterSignature && this.signedAttributeSet != null) {
                throw new CMSException("The content-type attribute type MUST be present whenever signed attributes are present in signed-data");
            }
        } else {
            if (this.isCounterSignature) {
                throw new CMSException("[For counter signatures,] the signedAttributes field MUST NOT contain a content-type attribute");
            }

            if (!(validMessageDigest instanceof ASN1ObjectIdentifier)) {
                throw new CMSException("content-type attribute value not of ASN.1 type 'OBJECT IDENTIFIER'");
            }

            ASN1ObjectIdentifier signedContentType = (ASN1ObjectIdentifier) validMessageDigest;
            if (!signedContentType.equals(this.contentType)) {
                throw new CMSException("content-type attribute value does not match eContentType");
            }
        }

        validMessageDigest = this.getSingleValuedSignedAttribute(CMSAttributes.messageDigest, "message-digest");
        if (validMessageDigest == null) {
            if (this.signedAttributeSet != null) {
                throw new CMSException("the message-digest signed attribute type MUST be present when there are any signed attributes present");
            }
        } else {
            if (!(validMessageDigest instanceof ASN1OctetString)) {
                throw new CMSException("message-digest attribute value not of ASN.1 type 'OCTET STRING'");
            }

            ASN1OctetString signedMessageDigest = (ASN1OctetString) validMessageDigest;
            if (!Arrays.constantTimeAreEqual(this.resultDigest, signedMessageDigest.getOctets())) {
                throw new CMSSignerDigestMismatchException("message-digest attribute value does not match calculated value");
            }
        }

        AttributeTable signedAttrTable = this.getSignedAttributes();
        if (signedAttrTable != null && signedAttrTable.getAll(CMSAttributes.counterSignature).size() > 0) {
            throw new CMSException("A countersignature attribute MUST NOT be a signed attribute");
        } else {
            AttributeTable unsignedAttrTable = this.getUnsignedAttributes();
            if (unsignedAttrTable != null) {
                ASN1EncodableVector csAttrs = unsignedAttrTable.getAll(CMSAttributes.counterSignature);

                for (int i = 0; i < csAttrs.size(); ++i) {
                    Attribute csAttr = (Attribute) csAttrs.get(i);
                    if (csAttr.getAttrValues().size() < 1) {
                        throw new CMSException("A countersignature attribute MUST contain at least one AttributeValue");
                    }
                }
            }

            try {
                if (this.signedAttributeSet == null && this.resultDigest != null && contentVerifier instanceof RawContentVerifier) {
                    RawContentVerifier rawVerifier = (RawContentVerifier) contentVerifier;
                    if (encName.equals("RSA")) {
                        DigestInfo digInfo = new DigestInfo(new AlgorithmIdentifier(this.digestAlgorithm.getAlgorithm(), DERNull.INSTANCE), this.resultDigest);
                        return rawVerifier.verify(digInfo.getEncoded("DER"), this.getSignature());
                    } else {
                        return rawVerifier.verify(this.resultDigest, this.getSignature());
                    }
                } else {
                    return contentVerifier.verify(this.getSignature());
                }
            } catch (IOException var9) {
                throw new CMSException("can't process mime object to create signature.", var9);
            }
        }
    }

    public boolean verify(SignerInformationVerifier verifier) throws CMSException {
        Time signingTime = this.getSigningTime();
        if (verifier.hasAssociatedCertificate() && signingTime != null) {
            X509CertificateHolder dcv = verifier.getAssociatedCertificate();
            if (!dcv.isValidOn(signingTime.getDate())) {
                throw new CMSVerifierCertificateNotValidException("verifier not valid at signingTime");
            }
        }

        return this.doVerify(verifier);
    }

    public SignerInfo toASN1Structure() {
        return this.info;
    }

    private ASN1Primitive getSingleValuedSignedAttribute(ASN1ObjectIdentifier attrOID, String printableName) throws CMSException {
        AttributeTable unsignedAttrTable = this.getUnsignedAttributes();
        if (unsignedAttrTable != null && unsignedAttrTable.getAll(attrOID).size() > 0) {
            throw new CMSException("The " + printableName + " attribute MUST NOT be an unsigned attribute");
        } else {
            AttributeTable signedAttrTable = this.getSignedAttributes();
            if (signedAttrTable == null) {
                return null;
            } else {
                ASN1EncodableVector v = signedAttrTable.getAll(attrOID);
                switch (v.size()) {
                    case 0:
                        return null;
                    case 1:
                        Attribute t = (Attribute) v.get(0);
                        ASN1Set attrValues = t.getAttrValues();
                        if (attrValues.size() != 1) {
                            throw new CMSException("A " + printableName + " attribute MUST have a single attribute value");
                        }

                        return attrValues.getObjectAt(0).toASN1Primitive();
                    default:
                        throw new CMSException("The SignedAttributes in a signerInfo MUST NOT include multiple instances of the " + printableName + " attribute");
                }
            }
        }
    }

    private Time getSigningTime() throws CMSException {
        ASN1Primitive validSigningTime = this.getSingleValuedSignedAttribute(CMSAttributes.signingTime, "signing-time");
        if (validSigningTime == null) {
            return null;
        } else {
            try {
                return Time.getInstance(validSigningTime);
            } catch (IllegalArgumentException var3) {
                throw new CMSException("signing-time attribute value not a valid 'Time' structure");
            }
        }
    }

    public static SignerInformation replaceUnsignedAttributes(SignerInformation signerInformation, AttributeTable unsignedAttributes) {
        SignerInfo sInfo = signerInformation.info;
        ASN1Set unsignedAttr = null;
        if (unsignedAttributes != null) {
            unsignedAttr = new DERSet(unsignedAttributes.toASN1EncodableVector());
        }

        return new SignerInformation(new SignerInfo(sInfo.getSID(), sInfo.getDigestAlgorithm(), sInfo.getAuthenticatedAttributes(), sInfo.getDigestEncryptionAlgorithm(), sInfo.getEncryptedDigest(), unsignedAttr), signerInformation.contentType, signerInformation.content, (byte[]) null);
    }

    public static SignerInformation addCounterSigners(SignerInformation signerInformation, SignerInformationStore counterSigners) {
        SignerInfo sInfo = signerInformation.info;
        AttributeTable unsignedAttr = signerInformation.getUnsignedAttributes();
        ASN1EncodableVector v;
        if (unsignedAttr != null) {
            v = unsignedAttr.toASN1EncodableVector();
        } else {
            v = new ASN1EncodableVector();
        }

        ASN1EncodableVector sigs = new ASN1EncodableVector();
        Iterator it = counterSigners.getSigners().iterator();

        while (it.hasNext()) {
            sigs.add(((SignerInformation) it.next()).toASN1Structure());
        }

        v.add(new Attribute(CMSAttributes.counterSignature, new DERSet(sigs)));
        return new SignerInformation(new SignerInfo(sInfo.getSID(), sInfo.getDigestAlgorithm(), sInfo.getAuthenticatedAttributes(), sInfo.getDigestEncryptionAlgorithm(), sInfo.getEncryptedDigest(), new DERSet(v)), signerInformation.contentType, signerInformation.content, (byte[]) null);
    }
}
