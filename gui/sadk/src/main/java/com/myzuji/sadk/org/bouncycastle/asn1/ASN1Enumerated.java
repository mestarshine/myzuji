package com.myzuji.sadk.org.bouncycastle.asn1;

import com.myzuji.sadk.org.bouncycastle.util.Arrays;

import java.io.IOException;
import java.math.BigInteger;

public class ASN1Enumerated extends ASN1Primitive {
    byte[] bytes;
    private static ASN1Enumerated[] cache = new ASN1Enumerated[12];

    public static ASN1Enumerated getInstance(Object obj) {
        if (obj != null && !(obj instanceof ASN1Enumerated)) {
            if (obj instanceof byte[]) {
                try {
                    return (ASN1Enumerated) fromByteArray((byte[]) ((byte[]) obj));
                } catch (Exception var2) {
                    Exception e = var2;
                    throw new IllegalArgumentException("encoding error in getInstance: " + e.toString());
                }
            } else {
                throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
            }
        } else {
            return (ASN1Enumerated) obj;
        }
    }

    public static ASN1Enumerated getInstance(ASN1TaggedObject obj, boolean explicit) {
        ASN1Primitive o = obj.getObject();
        return !explicit && !(o instanceof ASN1Enumerated) ? fromOctetString(((ASN1OctetString) o).getOctets()) : getInstance(o);
    }

    public ASN1Enumerated(int value) {
        this.bytes = BigInteger.valueOf((long) value).toByteArray();
    }

    public ASN1Enumerated(BigInteger value) {
        this.bytes = value.toByteArray();
    }

    public ASN1Enumerated(byte[] bytes) {
        this.bytes = bytes;
    }

    public BigInteger getValue() {
        return new BigInteger(this.bytes);
    }

    public boolean isConstructed() {
        return false;
    }

    public int encodedLength() {
        return 1 + StreamUtil.calculateBodyLength(this.bytes.length) + this.bytes.length;
    }

    public void encode(ASN1OutputStream out) throws IOException {
        out.writeEncoded(10, this.bytes);
    }

    public boolean asn1Equals(ASN1Primitive o) {
        if (!(o instanceof ASN1Enumerated)) {
            return false;
        } else {
            ASN1Enumerated other = (ASN1Enumerated) o;
            return Arrays.areEqual(this.bytes, other.bytes);
        }
    }

    public int hashCode() {
        return Arrays.hashCode(this.bytes);
    }

    static ASN1Enumerated fromOctetString(byte[] enc) {
        if (enc.length > 1) {
            return new ASN1Enumerated(Arrays.clone(enc));
        } else if (enc.length == 0) {
            throw new IllegalArgumentException("ENUMERATED has zero length");
        } else {
            int value = enc[0] & 255;
            if (value >= cache.length) {
                return new ASN1Enumerated(Arrays.clone(enc));
            } else {
                ASN1Enumerated possibleMatch = cache[value];
                if (possibleMatch == null) {
                    possibleMatch = cache[value] = new ASN1Enumerated(Arrays.clone(enc));
                }

                return possibleMatch;
            }
        }
    }
}
