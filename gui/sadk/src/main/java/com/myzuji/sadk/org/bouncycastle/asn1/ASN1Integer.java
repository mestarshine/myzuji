package com.myzuji.sadk.org.bouncycastle.asn1;

import com.myzuji.sadk.org.bouncycastle.util.Arrays;

import java.io.IOException;
import java.math.BigInteger;

public class ASN1Integer extends ASN1Primitive {
    byte[] bytes;

    public static ASN1Integer getInstance(Object obj) {
        if (obj != null && !(obj instanceof ASN1Integer)) {
            if (obj instanceof byte[]) {
                try {
                    return (ASN1Integer) fromByteArray((byte[]) ((byte[]) obj));
                } catch (Exception var2) {
                    Exception e = var2;
                    throw new IllegalArgumentException("encoding error in getInstance: " + e.toString());
                }
            } else {
                throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
            }
        } else {
            return (ASN1Integer) obj;
        }
    }

    public static ASN1Integer getInstance(ASN1TaggedObject obj, boolean explicit) {
        ASN1Primitive o = obj.getObject();
        return !explicit && !(o instanceof ASN1Integer) ? new ASN1Integer(ASN1OctetString.getInstance(obj.getObject()).getOctets()) : getInstance(o);
    }

    public ASN1Integer(long value) {
        this.bytes = BigInteger.valueOf(value).toByteArray();
    }

    public ASN1Integer(BigInteger value) {
        this.bytes = value.toByteArray();
    }

    public ASN1Integer(byte[] bytes) {
        this(bytes, true);
    }

    ASN1Integer(byte[] bytes, boolean clone) {
        this.bytes = clone ? Arrays.clone(bytes) : bytes;
    }

    public BigInteger getValue() {
        return new BigInteger(this.bytes);
    }

    public BigInteger getPositiveValue() {
        return new BigInteger(1, this.bytes);
    }

    public boolean isConstructed() {
        return false;
    }

    public int encodedLength() {
        return 1 + StreamUtil.calculateBodyLength(this.bytes.length) + this.bytes.length;
    }

    public void encode(ASN1OutputStream out) throws IOException {
        out.writeEncoded(2, this.bytes);
    }

    public int hashCode() {
        int value = 0;

        for (int i = 0; i != this.bytes.length; ++i) {
            value ^= (this.bytes[i] & 255) << i % 4;
        }

        return value;
    }

    public boolean asn1Equals(ASN1Primitive o) {
        if (!(o instanceof ASN1Integer)) {
            return false;
        } else {
            ASN1Integer other = (ASN1Integer) o;
            return Arrays.areEqual(this.bytes, other.bytes);
        }
    }

    public String toString() {
        return this.getValue().toString();
    }
}
