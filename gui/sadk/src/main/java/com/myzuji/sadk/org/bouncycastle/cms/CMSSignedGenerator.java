package com.myzuji.sadk.org.bouncycastle.cms;

import com.myzuji.sadk.algorithm.common.X9ObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Encodable;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.DERTaggedObject;
import com.myzuji.sadk.org.bouncycastle.asn1.cms.CMSObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.cms.OtherRevocationInfoFormat;
import com.myzuji.sadk.org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.teletrust.TeleTrusTObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import com.myzuji.sadk.org.bouncycastle.cert.X509AttributeCertificateHolder;
import com.myzuji.sadk.org.bouncycastle.cert.X509CRLHolder;
import com.myzuji.sadk.org.bouncycastle.cert.X509CertificateHolder;
import com.myzuji.sadk.org.bouncycastle.pkcs.PKCSObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.util.Arrays;
import com.myzuji.sadk.org.bouncycastle.util.Store;

import java.util.*;


public class CMSSignedGenerator {
    public static final String DATA;
    public static final String DIGEST_SHA1;
    public static final String DIGEST_SHA224;
    public static final String DIGEST_SHA256;
    public static final String DIGEST_SHA384;
    public static final String DIGEST_SHA512;
    public static final String DIGEST_MD5;
    public static final String DIGEST_GOST3411;
    public static final String DIGEST_RIPEMD128;
    public static final String DIGEST_RIPEMD160;
    public static final String DIGEST_RIPEMD256;
    public static final String ENCRYPTION_RSA;
    public static final String ENCRYPTION_DSA;
    public static final String ENCRYPTION_ECDSA;
    public static final String ENCRYPTION_RSA_PSS;
    public static final String ENCRYPTION_GOST3410;
    public static final String ENCRYPTION_ECGOST3410;
    private static final String ENCRYPTION_ECDSA_WITH_SHA1;
    private static final String ENCRYPTION_ECDSA_WITH_SHA224;
    private static final String ENCRYPTION_ECDSA_WITH_SHA256;
    private static final String ENCRYPTION_ECDSA_WITH_SHA384;
    private static final String ENCRYPTION_ECDSA_WITH_SHA512;
    private static final Set NO_PARAMS;
    private static final Map EC_ALGORITHMS;
    protected List certs = new ArrayList();
    protected List crls = new ArrayList();
    protected List _signers = new ArrayList();
    protected List signerGens = new ArrayList();
    protected Map digests = new HashMap();

    protected CMSSignedGenerator() {
    }

    protected Map getBaseParameters(ASN1ObjectIdentifier contentType, AlgorithmIdentifier digAlgId, byte[] hash) {
        Map param = new HashMap();
        param.put("contentType", contentType);
        param.put("digestAlgID", digAlgId);
        param.put("digest", Arrays.clone(hash));
        return param;
    }

    public void addCertificate(X509CertificateHolder certificate) throws CMSException {
        this.certs.add(certificate.toASN1Structure());
    }

    public void addCertificates(Store certStore) throws CMSException {
        this.certs.addAll(CMSUtils.getCertificatesFromStore(certStore));
    }

    public void addCRL(X509CRLHolder crl) {
        this.crls.add(crl.toASN1Structure());
    }

    public void addCRLs(Store crlStore) throws CMSException {
        this.crls.addAll(CMSUtils.getCRLsFromStore(crlStore));
    }

    public void addAttributeCertificate(X509AttributeCertificateHolder attrCert) throws CMSException {
        this.certs.add(new DERTaggedObject(false, 2, attrCert.toASN1Structure()));
    }

    public void addAttributeCertificates(Store attrStore) throws CMSException {
        this.certs.addAll(CMSUtils.getAttributeCertificatesFromStore(attrStore));
    }

    public void addOtherRevocationInfo(ASN1ObjectIdentifier otherRevocationInfoFormat, ASN1Encodable otherRevocationInfo) {
        this.crls.add(new DERTaggedObject(false, 1, new OtherRevocationInfoFormat(otherRevocationInfoFormat, otherRevocationInfo)));
    }

    public void addOtherRevocationInfo(ASN1ObjectIdentifier otherRevocationInfoFormat, Store otherRevocationInfos) {
        this.crls.addAll(CMSUtils.getOthersFromStore(otherRevocationInfoFormat, otherRevocationInfos));
    }

    public void addSigners(SignerInformationStore signerStore) {
        Iterator it = signerStore.getSigners().iterator();

        while (it.hasNext()) {
            this._signers.add(it.next());
        }

    }

    public void addSignerInfoGenerator(SignerInfoGenerator infoGen) {
        this.signerGens.add(infoGen);
    }

    public Map getGeneratedDigests() {
        return new HashMap(this.digests);
    }

    static {
        DATA = CMSObjectIdentifiers.data.getId();
        DIGEST_SHA1 = OIWObjectIdentifiers.idSHA1.getId();
        DIGEST_SHA224 = NISTObjectIdentifiers.id_sha224.getId();
        DIGEST_SHA256 = NISTObjectIdentifiers.id_sha256.getId();
        DIGEST_SHA384 = NISTObjectIdentifiers.id_sha384.getId();
        DIGEST_SHA512 = NISTObjectIdentifiers.id_sha512.getId();
        DIGEST_MD5 = PKCSObjectIdentifiers.md5.getId();
        DIGEST_GOST3411 = CryptoProObjectIdentifiers.gostR3411.getId();
        DIGEST_RIPEMD128 = TeleTrusTObjectIdentifiers.ripemd128.getId();
        DIGEST_RIPEMD160 = TeleTrusTObjectIdentifiers.ripemd160.getId();
        DIGEST_RIPEMD256 = TeleTrusTObjectIdentifiers.ripemd256.getId();
        ENCRYPTION_RSA = PKCSObjectIdentifiers.rsaEncryption.getId();
        ENCRYPTION_DSA = X9ObjectIdentifiers.id_dsa_with_sha1.getId();
        ENCRYPTION_ECDSA = X9ObjectIdentifiers.ecdsa_with_SHA1.getId();
        ENCRYPTION_RSA_PSS = PKCSObjectIdentifiers.id_RSASSA_PSS.getId();
        ENCRYPTION_GOST3410 = CryptoProObjectIdentifiers.gostR3410_94.getId();
        ENCRYPTION_ECGOST3410 = CryptoProObjectIdentifiers.gostR3410_2001.getId();
        ENCRYPTION_ECDSA_WITH_SHA1 = X9ObjectIdentifiers.ecdsa_with_SHA1.getId();
        ENCRYPTION_ECDSA_WITH_SHA224 = X9ObjectIdentifiers.ecdsa_with_SHA224.getId();
        ENCRYPTION_ECDSA_WITH_SHA256 = X9ObjectIdentifiers.ecdsa_with_SHA256.getId();
        ENCRYPTION_ECDSA_WITH_SHA384 = X9ObjectIdentifiers.ecdsa_with_SHA384.getId();
        ENCRYPTION_ECDSA_WITH_SHA512 = X9ObjectIdentifiers.ecdsa_with_SHA512.getId();
        NO_PARAMS = new HashSet();
        EC_ALGORITHMS = new HashMap();
        NO_PARAMS.add(ENCRYPTION_DSA);
        NO_PARAMS.add(ENCRYPTION_ECDSA);
        NO_PARAMS.add(ENCRYPTION_ECDSA_WITH_SHA1);
        NO_PARAMS.add(ENCRYPTION_ECDSA_WITH_SHA224);
        NO_PARAMS.add(ENCRYPTION_ECDSA_WITH_SHA256);
        NO_PARAMS.add(ENCRYPTION_ECDSA_WITH_SHA384);
        NO_PARAMS.add(ENCRYPTION_ECDSA_WITH_SHA512);
        EC_ALGORITHMS.put(DIGEST_SHA1, ENCRYPTION_ECDSA_WITH_SHA1);
        EC_ALGORITHMS.put(DIGEST_SHA224, ENCRYPTION_ECDSA_WITH_SHA224);
        EC_ALGORITHMS.put(DIGEST_SHA256, ENCRYPTION_ECDSA_WITH_SHA256);
        EC_ALGORITHMS.put(DIGEST_SHA384, ENCRYPTION_ECDSA_WITH_SHA384);
        EC_ALGORITHMS.put(DIGEST_SHA512, ENCRYPTION_ECDSA_WITH_SHA512);
    }
}
