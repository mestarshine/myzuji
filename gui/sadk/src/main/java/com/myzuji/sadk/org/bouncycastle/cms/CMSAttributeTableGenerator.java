package com.myzuji.sadk.org.bouncycastle.cms;

import com.myzuji.sadk.org.bouncycastle.asn1.cms.AttributeTable;

import java.util.Map;

public interface CMSAttributeTableGenerator {
    String CONTENT_TYPE = "contentType";
    String DIGEST = "digest";
    String SIGNATURE = "encryptedDigest";
    String DIGEST_ALGORITHM_IDENTIFIER = "digestAlgID";

    AttributeTable getAttributes(Map var1) throws CMSAttributeTableGenerationException;
}
