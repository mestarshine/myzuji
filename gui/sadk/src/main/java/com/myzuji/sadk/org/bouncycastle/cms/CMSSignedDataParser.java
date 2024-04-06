package com.myzuji.sadk.org.bouncycastle.cms;

import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.asn1.cms.CMSObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import com.myzuji.sadk.org.bouncycastle.operator.DigestCalculator;
import com.myzuji.sadk.org.bouncycastle.operator.DigestCalculatorProvider;
import com.myzuji.sadk.org.bouncycastle.operator.OperatorCreationException;
import com.myzuji.sadk.org.bouncycastle.util.Store;
import com.myzuji.sadk.org.bouncycastle.util.io.Streams;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class CMSSignedDataParser extends CMSContentInfoParser {
    private static final CMSSignedHelper HELPER;
    private SignedDataParser _signedData;
    private ASN1ObjectIdentifier _signedContentType;
    private CMSTypedStream _signedContent;
    private Map digests;
    private SignerInformationStore _signerInfoStore;
    private ASN1Set _certSet;
    private ASN1Set _crlSet;
    private boolean _isCertCrlParsed;

    public CMSSignedDataParser(DigestCalculatorProvider digestCalculatorProvider, byte[] sigBlock) throws CMSException {
        this(digestCalculatorProvider, (InputStream) (new ByteArrayInputStream(sigBlock)));
    }

    public CMSSignedDataParser(DigestCalculatorProvider digestCalculatorProvider, CMSTypedStream signedContent, byte[] sigBlock) throws CMSException {
        this(digestCalculatorProvider, signedContent, (InputStream) (new ByteArrayInputStream(sigBlock)));
    }

    public CMSSignedDataParser(DigestCalculatorProvider digestCalculatorProvider, InputStream sigData) throws CMSException {
        this(digestCalculatorProvider, (CMSTypedStream) null, (InputStream) sigData);
    }

    public CMSSignedDataParser(DigestCalculatorProvider digestCalculatorProvider, CMSTypedStream signedContent, InputStream sigData) throws CMSException {
        super(sigData);

        try {
            this._signedContent = signedContent;
            this._signedData = SignedDataParser.getInstance(this._contentInfo.getContent(16));
            this.digests = new HashMap();
            ASN1SetParser digAlgs = this._signedData.getDigestAlgorithms();

            ASN1Encodable o;
            while ((o = digAlgs.readObject()) != null) {
                AlgorithmIdentifier algId = AlgorithmIdentifier.getInstance(o);

                try {
                    DigestCalculator calculator = digestCalculatorProvider.get(algId);
                    if (calculator != null) {
                        this.digests.put(algId.getAlgorithm(), calculator);
                    }
                } catch (OperatorCreationException var9) {
                }
            }

            ContentInfoParser cont = this._signedData.getEncapContentInfo();
            ASN1OctetStringParser octs = (ASN1OctetStringParser) cont.getContent(4);
            if (octs != null) {
                CMSTypedStream ctStr = new CMSTypedStream(cont.getContentType().getId(), octs.getOctetStream());
                if (this._signedContent == null) {
                    this._signedContent = ctStr;
                } else {
                    ctStr.drain();
                }
            }

            if (signedContent == null) {
                this._signedContentType = cont.getContentType();
            } else {
                this._signedContentType = this._signedContent.getContentType();
            }

        } catch (IOException var10) {
            IOException e = var10;
            throw new CMSException("io exception: " + e.getMessage(), e);
        }
    }

    public int getVersion() {
        return this._signedData.getVersion().getValue().intValue();
    }

    public SignerInformationStore getSignerInfos() throws CMSException {
        if (this._signerInfoStore == null) {
            this.populateCertCrlSets();
            List signerInfos = new ArrayList();
            Map hashes = new HashMap();
            Iterator it = this.digests.keySet().iterator();

            while (it.hasNext()) {
                Object digestKey = it.next();
                hashes.put(digestKey, ((DigestCalculator) this.digests.get(digestKey)).getDigest());
            }

            try {
                ASN1SetParser s = this._signedData.getSignerInfos();

                ASN1Encodable o;
                while ((o = s.readObject()) != null) {
                    SignerInfo info = SignerInfo.getInstance(o.toASN1Primitive());
                    byte[] hash = (byte[]) ((byte[]) hashes.get(info.getDigestAlgorithm().getAlgorithm()));
                    signerInfos.add(new SignerInformation(info, this._signedContentType, (CMSProcessable) null, hash));
                }
            } catch (IOException var8) {
                IOException e = var8;
                throw new CMSException("io exception: " + e.getMessage(), e);
            }

            this._signerInfoStore = new SignerInformationStore(signerInfos);
        }

        return this._signerInfoStore;
    }

    public Store getCertificates() throws CMSException {
        this.populateCertCrlSets();
        return HELPER.getCertificates(this._certSet);
    }

    public Store getCRLs() throws CMSException {
        this.populateCertCrlSets();
        return HELPER.getCRLs(this._crlSet);
    }

    public Store getAttributeCertificates() throws CMSException {
        this.populateCertCrlSets();
        return HELPER.getAttributeCertificates(this._certSet);
    }

    public Store getOtherRevocationInfo(ASN1ObjectIdentifier otherRevocationInfoFormat) throws CMSException {
        this.populateCertCrlSets();
        return HELPER.getOtherRevocationInfo(otherRevocationInfoFormat, this._crlSet);
    }

    private void populateCertCrlSets() throws CMSException {
        if (!this._isCertCrlParsed) {
            this._isCertCrlParsed = true;

            try {
                this._certSet = getASN1Set(this._signedData.getCertificates());
                this._crlSet = getASN1Set(this._signedData.getCrls());
            } catch (IOException var2) {
                IOException e = var2;
                throw new CMSException("problem parsing cert/crl sets", e);
            }
        }
    }

    public String getSignedContentTypeOID() {
        return this._signedContentType.getId();
    }

    public CMSTypedStream getSignedContent() {
        if (this._signedContent == null) {
            return null;
        } else {
            InputStream digStream = CMSUtils.attachDigestsToInputStream(this.digests.values(), this._signedContent.getContentStream());
            return new CMSTypedStream(this._signedContent.getContentType(), digStream);
        }
    }

    public static OutputStream replaceSigners(InputStream original, SignerInformationStore signerInformationStore, OutputStream out) throws CMSException, IOException {
        ASN1StreamParser in = new ASN1StreamParser(original);
        ContentInfoParser contentInfo = new ContentInfoParser((ASN1SequenceParser) in.readObject());
        SignedDataParser signedData = SignedDataParser.getInstance(contentInfo.getContent(16));
        BERSequenceGenerator sGen = new BERSequenceGenerator(out);
        sGen.addObject(CMSObjectIdentifiers.signedData);
        BERSequenceGenerator sigGen = new BERSequenceGenerator(sGen.getRawOutputStream(), 0, true);
        sigGen.addObject(signedData.getVersion());
        signedData.getDigestAlgorithms().toASN1Primitive();
        ASN1EncodableVector digestAlgs = new ASN1EncodableVector();
        Iterator it = signerInformationStore.getSigners().iterator();

        while (it.hasNext()) {
            SignerInformation signer = (SignerInformation) it.next();
            digestAlgs.add(CMSSignedHelper.INSTANCE.fixAlgID(signer.getDigestAlgorithmID()));
        }

        sigGen.getRawOutputStream().write((new DERSet(digestAlgs)).getEncoded());
        ContentInfoParser encapContentInfo = signedData.getEncapContentInfo();
        BERSequenceGenerator eiGen = new BERSequenceGenerator(sigGen.getRawOutputStream());
        eiGen.addObject(encapContentInfo.getContentType());
        pipeEncapsulatedOctetString(encapContentInfo, eiGen.getRawOutputStream());
        eiGen.close();
        writeSetToGeneratorTagged(sigGen, signedData.getCertificates(), 0);
        writeSetToGeneratorTagged(sigGen, signedData.getCrls(), 1);
        ASN1EncodableVector signerInfos = new ASN1EncodableVector();
        it = signerInformationStore.getSigners().iterator();

        while (it.hasNext()) {
            SignerInformation signer = (SignerInformation) it.next();
            signerInfos.add(signer.toASN1Structure());
        }

        sigGen.getRawOutputStream().write((new DERSet(signerInfos)).getEncoded());
        sigGen.close();
        sGen.close();
        return out;
    }

    public static OutputStream replaceCertificatesAndCRLs(InputStream original, Store certs, Store crls, Store attrCerts, OutputStream out) throws CMSException, IOException {
        ASN1StreamParser in = new ASN1StreamParser(original);
        ContentInfoParser contentInfo = new ContentInfoParser((ASN1SequenceParser) in.readObject());
        SignedDataParser signedData = SignedDataParser.getInstance(contentInfo.getContent(16));
        BERSequenceGenerator sGen = new BERSequenceGenerator(out);
        sGen.addObject(CMSObjectIdentifiers.signedData);
        BERSequenceGenerator sigGen = new BERSequenceGenerator(sGen.getRawOutputStream(), 0, true);
        sigGen.addObject(signedData.getVersion());
        sigGen.getRawOutputStream().write(signedData.getDigestAlgorithms().toASN1Primitive().getEncoded());
        ContentInfoParser encapContentInfo = signedData.getEncapContentInfo();
        BERSequenceGenerator eiGen = new BERSequenceGenerator(sigGen.getRawOutputStream());
        eiGen.addObject(encapContentInfo.getContentType());
        pipeEncapsulatedOctetString(encapContentInfo, eiGen.getRawOutputStream());
        eiGen.close();
        getASN1Set(signedData.getCertificates());
        getASN1Set(signedData.getCrls());
        if (certs != null || attrCerts != null) {
            List certificates = new ArrayList();
            if (certs != null) {
                certificates.addAll(CMSUtils.getCertificatesFromStore(certs));
            }

            if (attrCerts != null) {
                certificates.addAll(CMSUtils.getAttributeCertificatesFromStore(attrCerts));
            }

            ASN1Set asn1Certs = CMSUtils.createBerSetFromList(certificates);
            if (asn1Certs.size() > 0) {
                sigGen.getRawOutputStream().write((new DERTaggedObject(false, 0, asn1Certs)).getEncoded());
            }
        }

        if (crls != null) {
            ASN1Set asn1Crls = CMSUtils.createBerSetFromList(CMSUtils.getCRLsFromStore(crls));
            if (asn1Crls.size() > 0) {
                sigGen.getRawOutputStream().write((new DERTaggedObject(false, 1, asn1Crls)).getEncoded());
            }
        }

        sigGen.getRawOutputStream().write(signedData.getSignerInfos().toASN1Primitive().getEncoded());
        sigGen.close();
        sGen.close();
        return out;
    }

    private static void writeSetToGeneratorTagged(ASN1Generator asn1Gen, ASN1SetParser asn1SetParser, int tagNo) throws IOException {
        ASN1Set asn1Set = getASN1Set(asn1SetParser);
        if (asn1Set != null) {
            if (asn1SetParser instanceof BERSetParser) {
                asn1Gen.getRawOutputStream().write((new BERTaggedObject(false, tagNo, asn1Set)).getEncoded());
            } else {
                asn1Gen.getRawOutputStream().write((new DERTaggedObject(false, tagNo, asn1Set)).getEncoded());
            }
        }

    }

    private static ASN1Set getASN1Set(ASN1SetParser asn1SetParser) {
        return asn1SetParser == null ? null : ASN1Set.getInstance(asn1SetParser.toASN1Primitive());
    }

    private static void pipeEncapsulatedOctetString(ContentInfoParser encapContentInfo, OutputStream rawOutputStream) throws IOException {
        ASN1OctetStringParser octs = (ASN1OctetStringParser) encapContentInfo.getContent(4);
        if (octs != null) {
            pipeOctetString(octs, rawOutputStream);
        }

    }

    private static void pipeOctetString(ASN1OctetStringParser octs, OutputStream output) throws IOException {
        OutputStream outOctets = CMSUtils.createBEROctetOutputStream(output, 0, true, 0);
        Streams.pipeAll(octs.getOctetStream(), outOctets);
        outOctets.close();
    }

    static {
        HELPER = CMSSignedHelper.INSTANCE;
    }
}

