package com.myzuji.sadk.org.bouncycastle.jce.interfaces;

import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Encodable;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1ObjectIdentifier;

import java.util.Enumeration;

public interface PKCS12BagAttributeCarrier {
    void setBagAttribute(ASN1ObjectIdentifier var1, ASN1Encodable var2);

    ASN1Encodable getBagAttribute(ASN1ObjectIdentifier var1);

    Enumeration getBagAttributeKeys();
}
