package com.myzuji.sadk.org.bouncycastle.cms;

import com.myzuji.sadk.org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface CMSTypedData extends CMSProcessable {
    ASN1ObjectIdentifier getContentType();
}
