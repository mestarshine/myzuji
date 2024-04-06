package com.myzuji.sadk.org.bouncycastle.asn1.pkcs;

import com.myzuji.sadk.org.bouncycastle.asn1.*;

public class AuthenticatedSafe extends ASN1Object {
    private ContentInfo[] info;
    private boolean isBer = true;

    private AuthenticatedSafe(ASN1Sequence seq) {
        this.info = new ContentInfo[seq.size()];

        for (int i = 0; i != this.info.length; ++i) {
            this.info[i] = ContentInfo.getInstance(seq.getObjectAt(i));
        }

        this.isBer = seq instanceof BERSequence;
    }

    public static AuthenticatedSafe getInstance(Object o) {
        if (o instanceof AuthenticatedSafe) {
            return (AuthenticatedSafe) o;
        } else {
            return o != null ? new AuthenticatedSafe(ASN1Sequence.getInstance(o)) : null;
        }
    }

    public AuthenticatedSafe(ContentInfo[] info) {
        this.info = info;
    }

    public ContentInfo[] getContentInfo() {
        return this.info;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();

        for (int i = 0; i != this.info.length; ++i) {
            v.add(this.info[i]);
        }

        return (ASN1Primitive) (this.isBer ? new BERSequence(v) : new DLSequence(v));
    }
}
