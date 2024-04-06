package com.myzuji.sadk.org.bouncycastle.cms;

import com.myzuji.sadk.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.myzuji.sadk.org.bouncycastle.pkcs.PKCSObjectIdentifiers;

public interface CMSAttributes {
    ASN1ObjectIdentifier contentType = PKCSObjectIdentifiers.pkcs_9_at_contentType;
    ASN1ObjectIdentifier messageDigest = PKCSObjectIdentifiers.pkcs_9_at_messageDigest;
    ASN1ObjectIdentifier signingTime = PKCSObjectIdentifiers.pkcs_9_at_signingTime;
    ASN1ObjectIdentifier counterSignature = PKCSObjectIdentifiers.pkcs_9_at_counterSignature;
    ASN1ObjectIdentifier contentHint = PKCSObjectIdentifiers.id_aa_contentHint;
}
