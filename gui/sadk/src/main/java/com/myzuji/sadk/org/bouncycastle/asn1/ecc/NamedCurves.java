package com.myzuji.sadk.org.bouncycastle.asn1.ecc;

import com.myzuji.sadk.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.x9.X9ECParameters;
import com.myzuji.sadk.org.bouncycastle.asn1.x9.X9ECParametersHolder;
import com.myzuji.sadk.org.bouncycastle.util.Strings;

import java.util.*;

public class NamedCurves {
    private NamedCurves() {
    }

    public static final X9ECParameters getByName(HashMap curves, HashMap objIds, String name) {
        X9ECParameters param = null;
        if (name != null) {
            ASN1ObjectIdentifier oid = (ASN1ObjectIdentifier) objIds.get(Strings.toLowerCase(name));
            if (oid != null) {
                param = getByOID(curves, oid);
            }
        }

        return param;
    }

    public static final X9ECParameters getByOID(HashMap curves, ASN1ObjectIdentifier oid) {
        X9ECParameters param = null;
        if (oid != null) {
            X9ECParametersHolder holder = (X9ECParametersHolder) curves.get(oid);
            if (holder != null) {
                param = holder.getParameters();
            }
        }

        return param;
    }

    public static final ASN1ObjectIdentifier getOID(HashMap objIds, String name) {
        ASN1ObjectIdentifier oid = null;
        if (name != null) {
            oid = (ASN1ObjectIdentifier) objIds.get(Strings.toLowerCase(name));
        }

        return oid;
    }

    public static final String getName(HashMap names, ASN1ObjectIdentifier oid) {
        String name = null;
        if (names != null) {
            name = (String) names.get(oid);
        }

        return name;
    }

    public static final Enumeration getNames(HashMap objIds) {
        return Collections.enumeration(getNameList(objIds));
    }

    public static final List getNameList(HashMap objIds) {
        List values = new ArrayList();
        if (objIds != null) {
            values.addAll(objIds.keySet());
        }

        return values;
    }
}
