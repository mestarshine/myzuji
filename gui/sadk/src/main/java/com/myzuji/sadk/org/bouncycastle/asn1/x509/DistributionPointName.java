package com.myzuji.sadk.org.bouncycastle.asn1.x509;

import com.myzuji.sadk.org.bouncycastle.asn1.*;

public class DistributionPointName extends ASN1Object implements ASN1Choice {
    ASN1Encodable name;
    int type;
    public static final int FULL_NAME = 0;
    public static final int NAME_RELATIVE_TO_CRL_ISSUER = 1;

    public static DistributionPointName getInstance(ASN1TaggedObject obj, boolean explicit) {
        return getInstance(ASN1TaggedObject.getInstance(obj, true));
    }

    public static DistributionPointName getInstance(Object obj) {
        if (obj != null && !(obj instanceof DistributionPointName)) {
            if (obj instanceof ASN1TaggedObject) {
                return new DistributionPointName((ASN1TaggedObject) obj);
            } else {
                throw new IllegalArgumentException("unknown object in factory: " + obj.getClass().getName());
            }
        } else {
            return (DistributionPointName) obj;
        }
    }

    public DistributionPointName(int type, ASN1Encodable name) {
        this.type = type;
        this.name = name;
    }

    public DistributionPointName(GeneralNames name) {
        this(0, name);
    }

    public int getType() {
        return this.type;
    }

    public ASN1Encodable getName() {
        return this.name;
    }

    public DistributionPointName(ASN1TaggedObject obj) {
        this.type = obj.getTagNo();
        if (this.type == 0) {
            this.name = GeneralNames.getInstance(obj, false);
        } else {
            this.name = ASN1Set.getInstance(obj, false);
        }

    }

    public ASN1Primitive toASN1Primitive() {
        return new DERTaggedObject(false, this.type, this.name);
    }

    public String toString() {
        String sep = System.getProperty("line.separator");
        StringBuffer buf = new StringBuffer();
        buf.append("DistributionPointName: [");
        buf.append(sep);
        if (this.type == 0) {
            this.appendObject(buf, sep, "fullName", this.name.toString());
        } else {
            this.appendObject(buf, sep, "nameRelativeToCRLIssuer", this.name.toString());
        }

        buf.append("]");
        buf.append(sep);
        return buf.toString();
    }

    private void appendObject(StringBuffer buf, String sep, String name, String value) {
        String indent = "    ";
        buf.append(indent);
        buf.append(name);
        buf.append(":");
        buf.append(sep);
        buf.append(indent);
        buf.append(indent);
        buf.append(value);
        buf.append(sep);
    }
}
