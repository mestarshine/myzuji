package com.myzuji.sadk.org.bouncycastle.asn1.x9;

import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Object;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1OctetString;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Primitive;
import com.myzuji.sadk.org.bouncycastle.asn1.DEROctetString;
import com.myzuji.sadk.org.bouncycastle.math.ec.ECCurve;
import com.myzuji.sadk.org.bouncycastle.math.ec.ECPoint;

public class X9ECPoint extends ASN1Object {
    ECPoint p;

    public X9ECPoint(ECPoint p) {
        this.p = p.normalize();
    }

    public X9ECPoint(ECCurve c, ASN1OctetString s) {
        this.p = c.decodePoint(s.getOctets());
    }

    public ECPoint getPoint() {
        return this.p;
    }

    public ASN1Primitive toASN1Primitive() {
        return new DEROctetString(this.p.getEncoded());
    }
}
