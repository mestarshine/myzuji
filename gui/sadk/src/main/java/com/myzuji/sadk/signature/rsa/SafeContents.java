package com.myzuji.sadk.signature.rsa;

import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.SafeBag;

public class SafeContents implements ASN1Encodable {
    private SafeBag[] safeBag;

    public static SafeContents getInstance(Object o) {
        if (o != null && !(o instanceof SafeContents)) {
            if (o instanceof ASN1Sequence) {
                return new SafeContents((ASN1Sequence) o);
            } else {
                throw new IllegalArgumentException("unknown object in factory " + o.getClass().getName());
            }
        } else {
            return (SafeContents) o;
        }
    }

    public SafeContents(SafeBag[] _safeBag) {
        this.safeBag = _safeBag;
    }

    public SafeContents(ASN1Sequence seq) {
        this.safeBag = new SafeBag[seq.size()];

        for (int i = 0; i < this.safeBag.length; ++i) {
            this.safeBag[i] = SafeBag.getInstance(seq.getObjectAt(i));
        }

    }

    public SafeBag[] getSafeBag() {
        return this.safeBag;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();

        for (int i = 0; i < this.safeBag.length; ++i) {
            v.add(this.safeBag[i]);
        }

        return new BERSequence(v);
    }
}
