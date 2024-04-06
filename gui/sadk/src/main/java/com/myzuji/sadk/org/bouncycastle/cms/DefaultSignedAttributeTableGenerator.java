package com.myzuji.sadk.org.bouncycastle.cms;

import com.myzuji.sadk.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.DEROctetString;
import com.myzuji.sadk.org.bouncycastle.asn1.DERSet;
import com.myzuji.sadk.org.bouncycastle.asn1.cms.Attribute;
import com.myzuji.sadk.org.bouncycastle.asn1.cms.AttributeTable;
import com.myzuji.sadk.org.bouncycastle.asn1.cms.Time;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

public class DefaultSignedAttributeTableGenerator implements CMSAttributeTableGenerator {
    private final Hashtable table;

    public DefaultSignedAttributeTableGenerator() {
        this.table = new Hashtable();
    }

    public DefaultSignedAttributeTableGenerator(AttributeTable attributeTable) {
        if (attributeTable != null) {
            this.table = attributeTable.toHashtable();
        } else {
            this.table = new Hashtable();
        }

    }

    protected Hashtable createStandardAttributeTable(Map parameters) {
        Hashtable std = copyHashTable(this.table);
        Attribute attr;
        if (!std.containsKey(CMSAttributes.contentType)) {
            ASN1ObjectIdentifier contentType = ASN1ObjectIdentifier.getInstance(parameters.get("contentType"));
            if (contentType != null) {
                attr = new Attribute(CMSAttributes.contentType, new DERSet(contentType));
                std.put(attr.getAttrType(), attr);
            }
        }

        if (!std.containsKey(CMSAttributes.signingTime)) {
            Date signingTime = new Date();
            attr = new Attribute(CMSAttributes.signingTime, new DERSet(new Time(signingTime)));
            std.put(attr.getAttrType(), attr);
        }

        if (!std.containsKey(CMSAttributes.messageDigest)) {
            byte[] messageDigest = (byte[]) ((byte[]) parameters.get("digest"));
            attr = new Attribute(CMSAttributes.messageDigest, new DERSet(new DEROctetString(messageDigest)));
            std.put(attr.getAttrType(), attr);
        }

        return std;
    }

    public AttributeTable getAttributes(Map parameters) {
        return new AttributeTable(this.createStandardAttributeTable(parameters));
    }

    private static Hashtable copyHashTable(Hashtable paramsMap) {
        Hashtable newTable = new Hashtable();
        Enumeration keys = paramsMap.keys();

        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            newTable.put(key, paramsMap.get(key));
        }

        return newTable;
    }
}
