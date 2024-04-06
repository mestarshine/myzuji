package com.myzuji.sadk.org.bouncycastle.asn1.nist;

import com.myzuji.sadk.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.ecc.NamedCurves;
import com.myzuji.sadk.org.bouncycastle.asn1.sec.SECNamedCurves;
import com.myzuji.sadk.org.bouncycastle.asn1.sec.SECObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.x9.X9ECParameters;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

public class NISTNamedCurves {
    private static final HashMap objIds = new HashMap();
    private static final HashMap names = new HashMap();

    public NISTNamedCurves() {
    }

    private static void defineCurve(String name, ASN1ObjectIdentifier oid) {
        objIds.put(name, oid);
        names.put(oid, name);
    }

    public static X9ECParameters getByName(String name) {
        ASN1ObjectIdentifier oid = NamedCurves.getOID(objIds, name);
        X9ECParameters param = null;
        if (oid != null) {
            param = getByOID(oid);
        }

        return param;
    }

    public static X9ECParameters getByOID(ASN1ObjectIdentifier oid) {
        return SECNamedCurves.getByOID(oid);
    }

    public static ASN1ObjectIdentifier getOID(String name) {
        return NamedCurves.getOID(objIds, name);
    }

    public static String getName(ASN1ObjectIdentifier oid) {
        return NamedCurves.getName(names, oid);
    }

    public static Enumeration getNames() {
        return NamedCurves.getNames(objIds);
    }

    public static List getNameList() {
        return NamedCurves.getNameList(objIds);
    }

    static {
        defineCurve("B-571", SECObjectIdentifiers.sect571r1);
        defineCurve("B-409", SECObjectIdentifiers.sect409r1);
        defineCurve("B-283", SECObjectIdentifiers.sect283r1);
        defineCurve("B-233", SECObjectIdentifiers.sect233r1);
        defineCurve("B-163", SECObjectIdentifiers.sect163r2);
        defineCurve("K-571", SECObjectIdentifiers.sect571k1);
        defineCurve("K-409", SECObjectIdentifiers.sect409k1);
        defineCurve("K-283", SECObjectIdentifiers.sect283k1);
        defineCurve("K-233", SECObjectIdentifiers.sect233k1);
        defineCurve("K-163", SECObjectIdentifiers.sect163k1);
        defineCurve("P-521", SECObjectIdentifiers.secp521r1);
        defineCurve("P-384", SECObjectIdentifiers.secp384r1);
        defineCurve("P-256", SECObjectIdentifiers.secp256r1);
        defineCurve("P-224", SECObjectIdentifiers.secp224r1);
        defineCurve("P-192", SECObjectIdentifiers.secp192r1);
    }
}

