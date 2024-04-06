package com.myzuji.sadk.org.bouncycastle.asn1;

import com.myzuji.sadk.org.bouncycastle.util.Arrays;
import com.myzuji.sadk.org.bouncycastle.util.encoders.Hex;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class ASN1OctetString extends ASN1Primitive implements ASN1OctetStringParser {
    byte[] string;

    public static ASN1OctetString getInstance(ASN1TaggedObject obj, boolean explicit) {
        ASN1Primitive o = obj.getObject();
        return (ASN1OctetString) (!explicit && !(o instanceof ASN1OctetString) ? BEROctetString.fromSequence(ASN1Sequence.getInstance(o)) : getInstance(o));
    }

    public static ASN1OctetString getInstance(Object obj) {
        if (obj != null && !(obj instanceof ASN1OctetString)) {
            if (obj instanceof byte[]) {
                try {
                    return getInstance(fromByteArray((byte[]) ((byte[]) obj)));
                } catch (IOException var2) {
                    IOException e = var2;
                    throw new IllegalArgumentException("failed to construct OCTET STRING from byte[]: " + e.getMessage());
                }
            } else {
                if (obj instanceof ASN1Encodable) {
                    ASN1Primitive primitive = ((ASN1Encodable) obj).toASN1Primitive();
                    if (primitive instanceof ASN1OctetString) {
                        return (ASN1OctetString) primitive;
                    }
                }

                throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
            }
        } else {
            return (ASN1OctetString) obj;
        }
    }

    public ASN1OctetString(byte[] string) {
        if (string == null) {
            throw new NullPointerException("string cannot be null");
        } else {
            this.string = string;
        }
    }

    public InputStream getOctetStream() {
        return new ByteArrayInputStream(this.string);
    }

    public ASN1OctetStringParser parser() {
        return this;
    }

    public byte[] getOctets() {
        return this.string;
    }

    public int hashCode() {
        return Arrays.hashCode(this.getOctets());
    }

    public boolean asn1Equals(ASN1Primitive o) {
        if (!(o instanceof ASN1OctetString)) {
            return false;
        } else {
            ASN1OctetString other = (ASN1OctetString) o;
            return Arrays.areEqual(this.string, other.string);
        }
    }

    public ASN1Primitive getLoadedObject() {
        return this.toASN1Primitive();
    }

    public ASN1Primitive toDERObject() {
        return new DEROctetString(this.string);
    }

    public ASN1Primitive toDLObject() {
        return new DEROctetString(this.string);
    }

    public abstract void encode(ASN1OutputStream var1) throws IOException;

    public String toString() {
        return "#" + new String(Hex.encode(this.string));
    }
}
