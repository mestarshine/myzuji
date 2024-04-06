package com.myzuji.sadk.org.bouncycastle.asn1.x500.style;

import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Encodable;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.x500.AttributeTypeAndValue;
import com.myzuji.sadk.org.bouncycastle.asn1.x500.RDN;
import com.myzuji.sadk.org.bouncycastle.asn1.x500.X500Name;
import com.myzuji.sadk.org.bouncycastle.asn1.x500.X500NameStyle;

import java.util.Vector;

public class X500NameBuilder {
    private X500NameStyle template;
    private Vector rdns;

    public X500NameBuilder() {
        this(BCStyle.INSTANCE);
    }

    public X500NameBuilder(X500NameStyle template) {
        this.rdns = new Vector();
        this.template = template;
    }

    public X500NameBuilder addRDN(ASN1ObjectIdentifier oid, String value) {
        this.addRDN(oid, this.template.stringToValue(oid, value));
        return this;
    }

    public X500NameBuilder addRDN(ASN1ObjectIdentifier oid, ASN1Encodable value) {
        this.rdns.addElement(new RDN(oid, value));
        return this;
    }

    public X500NameBuilder addRDN(AttributeTypeAndValue attrTAndV) {
        this.rdns.addElement(new RDN(attrTAndV));
        return this;
    }

    public X500NameBuilder addMultiValuedRDN(ASN1ObjectIdentifier[] oids, String[] values) {
        ASN1Encodable[] vals = new ASN1Encodable[values.length];

        for (int i = 0; i != vals.length; ++i) {
            vals[i] = this.template.stringToValue(oids[i], values[i]);
        }

        return this.addMultiValuedRDN(oids, vals);
    }

    public X500NameBuilder addMultiValuedRDN(ASN1ObjectIdentifier[] oids, ASN1Encodable[] values) {
        AttributeTypeAndValue[] avs = new AttributeTypeAndValue[oids.length];

        for (int i = 0; i != oids.length; ++i) {
            avs[i] = new AttributeTypeAndValue(oids[i], values[i]);
        }

        return this.addMultiValuedRDN(avs);
    }

    public X500NameBuilder addMultiValuedRDN(AttributeTypeAndValue[] attrTAndVs) {
        this.rdns.addElement(new RDN(attrTAndVs));
        return this;
    }

    public X500Name build() {
        RDN[] vals = new RDN[this.rdns.size()];

        for (int i = 0; i != vals.length; ++i) {
            vals[i] = (RDN) this.rdns.elementAt(i);
        }

        return new X500Name(this.template, vals);
    }
}
