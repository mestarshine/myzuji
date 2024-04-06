package com.myzuji.sadk.org.bouncycastle.asn1.pkcs;

import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.DigestInfo;

import java.math.BigInteger;

public class MacData extends ASN1Object {
    private static final BigInteger ONE = BigInteger.valueOf(1L);
    DigestInfo digInfo;
    byte[] salt;
    BigInteger iterationCount;

    public static MacData getInstance(Object obj) {
        if (obj instanceof MacData) {
            return (MacData) obj;
        } else {
            return obj != null ? new MacData(ASN1Sequence.getInstance(obj)) : null;
        }
    }

    private MacData(ASN1Sequence seq) {
        this.digInfo = DigestInfo.getInstance(seq.getObjectAt(0));
        this.salt = ((ASN1OctetString) seq.getObjectAt(1)).getOctets();
        if (seq.size() == 3) {
            this.iterationCount = ((ASN1Integer) seq.getObjectAt(2)).getValue();
        } else {
            this.iterationCount = ONE;
        }

    }

    public MacData(DigestInfo digInfo, byte[] salt, int iterationCount) {
        this.digInfo = digInfo;
        this.salt = salt;
        this.iterationCount = BigInteger.valueOf((long) iterationCount);
    }

    public DigestInfo getMac() {
        return this.digInfo;
    }

    public byte[] getSalt() {
        return this.salt;
    }

    public BigInteger getIterationCount() {
        return this.iterationCount;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(this.digInfo);
        v.add(new DEROctetString(this.salt));
        if (!this.iterationCount.equals(ONE)) {
            v.add(new ASN1Integer(this.iterationCount));
        }

        return new DERSequence(v);
    }
}
