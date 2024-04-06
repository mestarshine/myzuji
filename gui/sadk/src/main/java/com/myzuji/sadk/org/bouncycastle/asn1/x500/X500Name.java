package com.myzuji.sadk.org.bouncycastle.asn1.x500;

import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.asn1.x500.style.BCStyle;

import java.util.Enumeration;

public class X500Name extends ASN1Object implements ASN1Choice {
    private static X500NameStyle defaultStyle;
    private boolean isHashCodeCalculated;
    private int hashCodeValue;
    private X500NameStyle style;
    private RDN[] rdns;

    public X500Name(X500NameStyle style, X500Name name) {
        this.rdns = name.rdns;
        this.style = style;
    }

    public static X500Name getInstance(ASN1TaggedObject obj, boolean explicit) {
        return getInstance(ASN1Sequence.getInstance(obj, true));
    }

    public static X500Name getInstance(Object obj) {
        if (obj instanceof X500Name) {
            return (X500Name) obj;
        } else {
            return obj != null ? new X500Name(ASN1Sequence.getInstance(obj)) : null;
        }
    }

    public static X500Name getInstance(X500NameStyle style, Object obj) {
        if (obj instanceof X500Name) {
            return getInstance(style, ((X500Name) obj).toASN1Primitive());
        } else {
            return obj != null ? new X500Name(style, ASN1Sequence.getInstance(obj)) : null;
        }
    }

    private X500Name(ASN1Sequence seq) {
        this(defaultStyle, seq);
    }

    private X500Name(X500NameStyle style, ASN1Sequence seq) {
        this.style = style;
        this.rdns = new RDN[seq.size()];
        int index = 0;

        for (Enumeration e = seq.getObjects(); e.hasMoreElements(); this.rdns[index++] = RDN.getInstance(e.nextElement())) {
        }

    }

    public X500Name(RDN[] rDNs) {
        this(defaultStyle, rDNs);
    }

    public X500Name(X500NameStyle style, RDN[] rDNs) {
        this.rdns = rDNs;
        this.style = style;
    }

    public X500Name(String dirName) {
        this(defaultStyle, dirName);
    }

    public X500Name(X500NameStyle style, String dirName) {
        this(style.fromString(dirName));
        this.style = style;
    }

    public RDN[] getRDNs() {
        RDN[] tmp = new RDN[this.rdns.length];
        System.arraycopy(this.rdns, 0, tmp, 0, tmp.length);
        return tmp;
    }

    public ASN1ObjectIdentifier[] getAttributeTypes() {
        int count = 0;

        for (int i = 0; i != this.rdns.length; ++i) {
            RDN rdn = this.rdns[i];
            count += rdn.size();
        }

        ASN1ObjectIdentifier[] res = new ASN1ObjectIdentifier[count];
        count = 0;

        for (int i = 0; i != this.rdns.length; ++i) {
            RDN rdn = this.rdns[i];
            if (rdn.isMultiValued()) {
                AttributeTypeAndValue[] attr = rdn.getTypesAndValues();

                for (int j = 0; j != attr.length; ++j) {
                    res[count++] = attr[j].getType();
                }
            } else if (rdn.size() != 0) {
                res[count++] = rdn.getFirst().getType();
            }
        }

        return res;
    }

    public RDN[] getRDNs(ASN1ObjectIdentifier attributeType) {
        RDN[] res = new RDN[this.rdns.length];
        int count = 0;

        for (int i = 0; i != this.rdns.length; ++i) {
            RDN rdn = this.rdns[i];
            if (rdn.isMultiValued()) {
                AttributeTypeAndValue[] attr = rdn.getTypesAndValues();

                for (int j = 0; j != attr.length; ++j) {
                    if (attr[j].getType().equals(attributeType)) {
                        res[count++] = rdn;
                        break;
                    }
                }
            } else if (rdn.getFirst().getType().equals(attributeType)) {
                res[count++] = rdn;
            }
        }

        RDN[] tmp = new RDN[count];
        System.arraycopy(res, 0, tmp, 0, tmp.length);
        return tmp;
    }

    public ASN1Primitive toASN1Primitive() {
        return new DERSequence(this.rdns);
    }

    public int hashCode() {
        if (this.isHashCodeCalculated) {
            return this.hashCodeValue;
        } else {
            this.isHashCodeCalculated = true;
            this.hashCodeValue = this.style.calculateHashCode(this);
            return this.hashCodeValue;
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof X500Name) && !(obj instanceof ASN1Encodable)) {
            return false;
        } else {
            ASN1Primitive derO = ((ASN1Encodable) obj).toASN1Primitive();
            if (this.toASN1Primitive().equals(derO)) {
                return true;
            } else {
                try {
                    return this.style.areEqual(this, new X500Name(ASN1Sequence.getInstance(((ASN1Encodable) obj).toASN1Primitive())));
                } catch (Exception var4) {
                    return false;
                }
            }
        }
    }

    public String toString() {
        return this.style.toString(this);
    }

    public static void setDefaultStyle(X500NameStyle style) {
        if (style == null) {
            throw new NullPointerException("cannot set style to null");
        } else {
            defaultStyle = style;
        }
    }

    public static X500NameStyle getDefaultStyle() {
        return defaultStyle;
    }

    static {
        defaultStyle = BCStyle.INSTANCE;
    }
}

