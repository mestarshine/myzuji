package com.myzuji.sadk.org.bouncycastle.asn1.x9;

import com.myzuji.sadk.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.nist.NISTNamedCurves;
import com.myzuji.sadk.org.bouncycastle.asn1.sec.SECNamedCurves;
import com.myzuji.sadk.org.bouncycastle.asn1.teletrust.TeleTrusTNamedCurves;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class ECNamedCurveTable {
    public ECNamedCurveTable() {
    }

    public static X9ECParameters getByName(String name) {
        X9ECParameters ecP = X962NamedCurves.getByName(name);
        if (ecP == null) {
            ecP = SECNamedCurves.getByName(name);
        }

        if (ecP == null) {
            ecP = TeleTrusTNamedCurves.getByName(name);
        }

        if (ecP == null) {
            ecP = NISTNamedCurves.getByName(name);
        }

        return ecP;
    }

    public static ASN1ObjectIdentifier getOID(String name) {
        ASN1ObjectIdentifier oid = X962NamedCurves.getOID(name);
        if (oid == null) {
            oid = SECNamedCurves.getOID(name);
        }

        if (oid == null) {
            oid = TeleTrusTNamedCurves.getOID(name);
        }

        if (oid == null) {
            oid = NISTNamedCurves.getOID(name);
        }

        return oid;
    }

    public static X9ECParameters getByOID(ASN1ObjectIdentifier oid) {
        X9ECParameters ecP = X962NamedCurves.getByOID(oid);
        if (ecP == null) {
            ecP = SECNamedCurves.getByOID(oid);
        }

        if (ecP == null) {
            ecP = TeleTrusTNamedCurves.getByOID(oid);
        }

        return ecP;
    }

    public static Enumeration getNames() {
        return Collections.enumeration(getNameList());
    }

    public static List getNameList() {
        List values = new ArrayList();
        values.addAll(X962NamedCurves.getNameList());
        values.addAll(SECNamedCurves.getNameList());
        values.addAll(NISTNamedCurves.getNameList());
        values.addAll(TeleTrusTNamedCurves.getNameList());
        return values;
    }
}
