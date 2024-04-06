package com.myzuji.sadk.org.bouncycastle.asn1.pkcs;

import com.myzuji.sadk.algorithm.common.PKCSObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.*;

import java.util.Enumeration;

public class ContentInfo extends ASN1Object implements PKCSObjectIdentifiers {
    private ASN1ObjectIdentifier contentType;
    private ASN1Encodable content;
    private boolean isBer = true;

    public static ContentInfo getInstance(Object obj) {
        if (obj instanceof ContentInfo) {
            return (ContentInfo) obj;
        } else {
            return obj != null ? new ContentInfo(ASN1Sequence.getInstance(obj)) : null;
        }
    }

    private ContentInfo(ASN1Sequence seq) {
        Enumeration e = seq.getObjects();
        this.contentType = (ASN1ObjectIdentifier) e.nextElement();
        if (e.hasMoreElements()) {
            this.content = ((ASN1TaggedObject) e.nextElement()).getObject();
        }

        this.isBer = seq instanceof BERSequence;
    }

    public ContentInfo(ASN1ObjectIdentifier contentType, ASN1Encodable content) {
        this.contentType = contentType;
        this.content = content;
    }

    public ASN1ObjectIdentifier getContentType() {
        return this.contentType;
    }

    public ASN1Encodable getContent() {
        return this.content;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(this.contentType);
        if (this.content != null) {
            v.add(new BERTaggedObject(true, 0, this.content));
        }

        return (ASN1Primitive) (this.isBer ? new BERSequence(v) : new DLSequence(v));
    }
}
