package com.myzuji.sadk.org.bouncycastle.asn1.x509;

import com.myzuji.sadk.org.bouncycastle.asn1.*;

import java.io.IOException;

public class X509DefaultEntryConverter extends X509NameEntryConverter {
    public X509DefaultEntryConverter() {
    }

    public ASN1Primitive getConvertedValue(ASN1ObjectIdentifier oid, String value) {
        if (value.length() != 0 && value.charAt(0) == '#') {
            try {
                return this.convertHexEncoded(value, 1);
            } catch (IOException var4) {
                throw new RuntimeException("can't recode value for oid " + oid.getId());
            }
        } else {
            if (value.length() != 0 && value.charAt(0) == '\\') {
                value = value.substring(1);
            }

            if (!oid.equals(X509Name.EmailAddress) && !oid.equals(X509Name.DC)) {
                if (oid.equals(X509Name.DATE_OF_BIRTH)) {
                    return new DERGeneralizedTime(value);
                } else {
                    return (ASN1Primitive) (!oid.equals(X509Name.C) && !oid.equals(X509Name.SN) && !oid.equals(X509Name.DN_QUALIFIER) && !oid.equals(X509Name.TELEPHONE_NUMBER) ? new DERUTF8String(value) : new DERPrintableString(value));
                }
            } else {
                return new DERIA5String(value);
            }
        }
    }
}
