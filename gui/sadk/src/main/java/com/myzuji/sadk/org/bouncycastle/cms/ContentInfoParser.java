package com.myzuji.sadk.org.bouncycastle.cms;

import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Encodable;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1SequenceParser;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1TaggedObjectParser;

import java.io.IOException;

public class ContentInfoParser {
    private ASN1ObjectIdentifier contentType;
    private ASN1TaggedObjectParser content;

    public ContentInfoParser(ASN1SequenceParser seq) throws IOException {
        this.contentType = (ASN1ObjectIdentifier) seq.readObject();
        this.content = (ASN1TaggedObjectParser) seq.readObject();
    }

    public ASN1ObjectIdentifier getContentType() {
        return this.contentType;
    }

    public ASN1Encodable getContent(int tag) throws IOException {
        return this.content != null ? this.content.getObjectParser(tag, true) : null;
    }
}
