package com.myzuji.sadk.org.bouncycastle.cms;

import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.ContentInfo;
import com.myzuji.sadk.org.bouncycastle.operator.OperatorCreationException;
import com.myzuji.sadk.org.bouncycastle.util.Store;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class CMSSignedData {
    private static final CMSSignedHelper HELPER;
    SignedData signedData;
    ContentInfo contentInfo;
    CMSTypedData signedContent;
    SignerInformationStore signerInfoStore;
    private Map hashes;

    private CMSSignedData(CMSSignedData c) {
        this.signedData = c.signedData;
        this.contentInfo = c.contentInfo;
        this.signedContent = c.signedContent;
        this.signerInfoStore = c.signerInfoStore;
    }

    public CMSSignedData(byte[] sigBlock) throws CMSException {
        this(CMSUtils.readContentInfo(sigBlock));
    }

    public CMSSignedData(CMSProcessable signedContent, byte[] sigBlock) throws CMSException {
        this(signedContent, CMSUtils.readContentInfo(sigBlock));
    }

    public CMSSignedData(Map hashes, byte[] sigBlock) throws CMSException {
        this(hashes, CMSUtils.readContentInfo(sigBlock));
    }

    public CMSSignedData(CMSProcessable signedContent, InputStream sigData) throws CMSException {
        this(signedContent, CMSUtils.readContentInfo(new ASN1InputStream(sigData)));
    }

    public CMSSignedData(InputStream sigData) throws CMSException {
        this(CMSUtils.readContentInfo(sigData));
    }

    public CMSSignedData(final CMSProcessable signedContent, ContentInfo sigData) throws CMSException {
        if (signedContent instanceof CMSTypedData) {
            this.signedContent = (CMSTypedData) signedContent;
        } else {
            this.signedContent = new CMSTypedData() {
                public ASN1ObjectIdentifier getContentType() {
                    return CMSSignedData.this.signedData.getEncapContentInfo().getContentType();
                }

                public void write(OutputStream out) throws IOException, CMSException {
                    signedContent.write(out);
                }

                public Object getContent() {
                    return signedContent.getContent();
                }
            };
        }

        this.contentInfo = sigData;
        this.signedData = this.getSignedData();
    }

    public CMSSignedData(Map hashes, ContentInfo sigData) throws CMSException {
        this.hashes = hashes;
        this.contentInfo = sigData;
        this.signedData = this.getSignedData();
    }

    public CMSSignedData(ContentInfo sigData) throws CMSException {
        this.contentInfo = sigData;
        this.signedData = this.getSignedData();
        if (this.signedData.getEncapContentInfo().getContent() != null) {
            this.signedContent = new CMSProcessableByteArray(this.signedData.getEncapContentInfo().getContentType(), ((ASN1OctetString) ((ASN1OctetString) this.signedData.getEncapContentInfo().getContent())).getOctets());
        } else {
            this.signedContent = null;
        }

    }

    private SignedData getSignedData() throws CMSException {
        try {
            return SignedData.getInstance(this.contentInfo.getContent());
        } catch (ClassCastException var2) {
            ClassCastException e = var2;
            throw new CMSException("Malformed content.", e);
        } catch (IllegalArgumentException var3) {
            IllegalArgumentException e = var3;
            throw new CMSException("Malformed content.", e);
        }
    }

    public int getVersion() {
        return this.signedData.getVersion().getValue().intValue();
    }

    public SignerInformationStore getSignerInfos() {
        if (this.signerInfoStore == null) {
            ASN1Set s = this.signedData.getSignerInfos();
            List signerInfos = new ArrayList();

            for (int i = 0; i != s.size(); ++i) {
                SignerInfo info = SignerInfo.getInstance(s.getObjectAt(i));
                ASN1ObjectIdentifier contentType = this.signedData.getEncapContentInfo().getContentType();
                if (this.hashes == null) {
                    signerInfos.add(new SignerInformation(info, contentType, this.signedContent, (byte[]) null));
                } else {
                    Object obj = this.hashes.keySet().iterator().next();
                    byte[] hash = obj instanceof String ? (byte[]) ((byte[]) this.hashes.get(info.getDigestAlgorithm().getAlgorithm().getId())) : (byte[]) ((byte[]) this.hashes.get(info.getDigestAlgorithm().getAlgorithm()));
                    signerInfos.add(new SignerInformation(info, contentType, (CMSProcessable) null, hash));
                }
            }

            this.signerInfoStore = new SignerInformationStore(signerInfos);
        }

        return this.signerInfoStore;
    }

    public Store getCertificates() {
        return HELPER.getCertificates(this.signedData.getCertificates());
    }

    public Store getCRLs() {
        return HELPER.getCRLs(this.signedData.getCRLs());
    }

    public Store getAttributeCertificates() {
        return HELPER.getAttributeCertificates(this.signedData.getCertificates());
    }

    public Store getOtherRevocationInfo(ASN1ObjectIdentifier otherRevocationInfoFormat) {
        return HELPER.getOtherRevocationInfo(otherRevocationInfoFormat, this.signedData.getCRLs());
    }

    public String getSignedContentTypeOID() {
        return this.signedData.getEncapContentInfo().getContentType().getId();
    }

    public CMSTypedData getSignedContent() {
        return this.signedContent;
    }

    public ContentInfo toASN1Structure() {
        return this.contentInfo;
    }

    public byte[] getEncoded() throws IOException {
        return this.contentInfo.getEncoded();
    }

    public boolean verifySignatures(SignerInformationVerifierProvider verifierProvider) throws CMSException {
        return this.verifySignatures(verifierProvider, false);
    }

    public boolean verifySignatures(SignerInformationVerifierProvider verifierProvider, boolean ignoreCounterSignatures) throws CMSException {
        Collection signers = this.getSignerInfos().getSigners();
        Iterator it = signers.iterator();

        while (it.hasNext()) {
            SignerInformation signer = (SignerInformation) it.next();

            try {
                SignerInformationVerifier verifier = verifierProvider.get(signer.getSID());
                if (!signer.verify(verifier)) {
                    return false;
                }

                if (!ignoreCounterSignatures) {
                    Collection counterSigners = signer.getCounterSignatures().getSigners();
                    Iterator cIt = counterSigners.iterator();

                    while (cIt.hasNext()) {
                        SignerInformation counterSigner = (SignerInformation) cIt.next();
                        SignerInformationVerifier counterVerifier = verifierProvider.get(signer.getSID());
                        if (!counterSigner.verify(counterVerifier)) {
                            return false;
                        }
                    }
                }
            } catch (OperatorCreationException var11) {
                OperatorCreationException e = var11;
                throw new CMSException("failure in verifier provider: " + e.getMessage(), e);
            }
        }

        return true;
    }

    public static CMSSignedData replaceSigners(CMSSignedData signedData, SignerInformationStore signerInformationStore) {
        CMSSignedData cms = new CMSSignedData(signedData);
        cms.signerInfoStore = signerInformationStore;
        ASN1EncodableVector digestAlgs = new ASN1EncodableVector();
        ASN1EncodableVector vec = new ASN1EncodableVector();
        Iterator it = signerInformationStore.getSigners().iterator();

        while (it.hasNext()) {
            SignerInformation signer = (SignerInformation) it.next();
            digestAlgs.add(CMSSignedHelper.INSTANCE.fixAlgID(signer.getDigestAlgorithmID()));
            vec.add(signer.toASN1Structure());
        }

        ASN1Set digests = new DERSet(digestAlgs);
        ASN1Set signers = new DERSet(vec);
        ASN1Sequence sD = (ASN1Sequence) signedData.signedData.toASN1Primitive();
        vec = new ASN1EncodableVector();
        vec.add(sD.getObjectAt(0));
        vec.add(digests);

        for (int i = 2; i != sD.size() - 1; ++i) {
            vec.add(sD.getObjectAt(i));
        }

        vec.add(signers);
        cms.signedData = SignedData.getInstance(new BERSequence(vec));
        cms.contentInfo = new ContentInfo(cms.contentInfo.getContentType(), cms.signedData);
        return cms;
    }

    public static CMSSignedData replaceCertificatesAndCRLs(CMSSignedData signedData, Store certificates, Store attrCerts, Store revocations) throws CMSException {
        CMSSignedData cms = new CMSSignedData(signedData);
        ASN1Set certSet = null;
        ASN1Set crlSet = null;
        if (certificates != null || attrCerts != null) {
            List certs = new ArrayList();
            if (certificates != null) {
                certs.addAll(CMSUtils.getCertificatesFromStore(certificates));
            }

            if (attrCerts != null) {
                certs.addAll(CMSUtils.getAttributeCertificatesFromStore(attrCerts));
            }

            ASN1Set set = CMSUtils.createBerSetFromList(certs);
            if (set.size() != 0) {
                certSet = set;
            }
        }

        if (revocations != null) {
            ASN1Set set = CMSUtils.createBerSetFromList(CMSUtils.getCRLsFromStore(revocations));
            if (set.size() != 0) {
                crlSet = set;
            }
        }

        cms.signedData = new SignedData(signedData.signedData.getDigestAlgorithms(), signedData.signedData.getEncapContentInfo(), certSet, crlSet, signedData.signedData.getSignerInfos());
        cms.contentInfo = new ContentInfo(cms.contentInfo.getContentType(), cms.signedData);
        return cms;
    }

    static {
        HELPER = CMSSignedHelper.INSTANCE;
    }
}
