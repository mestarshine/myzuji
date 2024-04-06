package com.myzuji.sadk.org.bouncycastle.cms;

import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.asn1.cms.CMSObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.ContentInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CMSSignedDataGenerator extends CMSSignedGenerator {
    private List signerInfs = new ArrayList();

    public CMSSignedDataGenerator() {
    }

    public CMSSignedData generate(CMSTypedData content) throws CMSException {
        return this.generate(content, false);
    }

    public CMSSignedData generate(CMSTypedData content, boolean encapsulate) throws CMSException {
        if (!this.signerInfs.isEmpty()) {
            throw new IllegalStateException("this method can only be used with SignerInfoGenerator");
        } else {
            ASN1EncodableVector digestAlgs = new ASN1EncodableVector();
            ASN1EncodableVector signerInfos = new ASN1EncodableVector();
            this.digests.clear();
            Iterator it = this._signers.iterator();

            while (it.hasNext()) {
                SignerInformation signer = (SignerInformation) it.next();
                digestAlgs.add(CMSSignedHelper.INSTANCE.fixAlgID(signer.getDigestAlgorithmID()));
                signerInfos.add(signer.toASN1Structure());
            }

            ASN1ObjectIdentifier contentTypeOID = content.getContentType();
            ASN1OctetString octs = null;
            if (content.getContent() != null) {
                ByteArrayOutputStream bOut = null;
                if (encapsulate) {
                    bOut = new ByteArrayOutputStream();
                }

                OutputStream cOut = CMSUtils.attachSignersToOutputStream(this.signerGens, bOut);
                cOut = CMSUtils.getSafeOutputStream(cOut);

                try {
                    content.write(cOut);
                    cOut.close();
                } catch (IOException var12) {
                    IOException e = var12;
                    throw new CMSException("data processing exception: " + e.getMessage(), e);
                }

                if (encapsulate) {
                    octs = new BEROctetString(bOut.toByteArray());
                }
            }

            it = this.signerGens.iterator();

            while (it.hasNext()) {
                SignerInfoGenerator sGen = (SignerInfoGenerator) it.next();
                SignerInfo inf = sGen.generate(contentTypeOID);
                digestAlgs.add(inf.getDigestAlgorithm());
                signerInfos.add(inf);
                byte[] calcDigest = sGen.getCalculatedDigest();
                if (calcDigest != null) {
                    this.digests.put(inf.getDigestAlgorithm().getAlgorithm().getId(), calcDigest);
                }
            }

            ASN1Set certificates = null;
            if (this.certs.size() != 0) {
                certificates = CMSUtils.createBerSetFromList(this.certs);
            }

            ASN1Set certrevlist = null;
            if (this.crls.size() != 0) {
                certrevlist = CMSUtils.createBerSetFromList(this.crls);
            }

            ContentInfo encInfo = new ContentInfo(contentTypeOID, octs);
            SignedData sd = new SignedData(new DERSet(digestAlgs), encInfo, certificates, certrevlist, new DERSet(signerInfos));
            ContentInfo contentInfo = new ContentInfo(CMSObjectIdentifiers.signedData, sd);
            return new CMSSignedData(content, contentInfo);
        }
    }

    public SignerInformationStore generateCounterSigners(SignerInformation signer) throws CMSException {
        return this.generate(new CMSProcessableByteArray((ASN1ObjectIdentifier) null, signer.getSignature()), false).getSignerInfos();
    }
}

