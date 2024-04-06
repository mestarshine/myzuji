package com.myzuji.sadk.org.bouncycastle.asn1;

import java.io.IOException;

public abstract class ASN1Null extends ASN1Primitive {
    public ASN1Null() {
    }

    public static ASN1Null getInstance(Object o) {
        if (o instanceof ASN1Null) {
            return (ASN1Null) o;
        } else if (o != null) {
            try {
                return getInstance(fromByteArray((byte[]) ((byte[]) o)));
            } catch (IOException var2) {
                IOException e = var2;
                throw new IllegalArgumentException("failed to construct NULL from byte[]: " + e.getMessage());
            } catch (ClassCastException var3) {
                throw new IllegalArgumentException("unknown object in getInstance(): " + o.getClass().getName());
            }
        } else {
            return null;
        }
    }

    public int hashCode() {
        return -1;
    }

    public boolean asn1Equals(ASN1Primitive o) {
        return o instanceof ASN1Null;
    }

    public abstract void encode(ASN1OutputStream var1) throws IOException;

    public String toString() {
        return "NULL";
    }
}
