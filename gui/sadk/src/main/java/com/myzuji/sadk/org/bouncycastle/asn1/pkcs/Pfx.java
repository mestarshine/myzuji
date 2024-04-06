package com.myzuji.sadk.org.bouncycastle.asn1.pkcs;

import com.myzuji.sadk.algorithm.common.PKCSObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.*;

import java.math.BigInteger;

public class Pfx extends ASN1Object implements PKCSObjectIdentifiers {
    private ContentInfo contentInfo;
    private MacData macData = null;

    private Pfx(ASN1Sequence seq) {
        BigInteger version = ((ASN1Integer) seq.getObjectAt(0)).getValue();
        if (version.intValue() != 3) {
            throw new IllegalArgumentException("wrong version for PFX PDU");
        } else {
            this.contentInfo = ContentInfo.getInstance(seq.getObjectAt(1));
            if (seq.size() == 3) {
                this.macData = MacData.getInstance(seq.getObjectAt(2));
            }

        }
    }

    public static Pfx getInstance(Object obj) {
        if (obj instanceof Pfx) {
            return (Pfx) obj;
        } else {
            return obj != null ? new Pfx(ASN1Sequence.getInstance(obj)) : null;
        }
    }

    public Pfx(ContentInfo contentInfo, MacData macData) {
        this.contentInfo = contentInfo;
        this.macData = macData;
    }

    public ContentInfo getAuthSafe() {
        return this.contentInfo;
    }

    public MacData getMacData() {
        return this.macData;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(new ASN1Integer(3L));
        v.add(this.contentInfo);
        if (this.macData != null) {
            v.add(this.macData);
        }

        return new BERSequence(v);
    }
}
