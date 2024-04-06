package com.myzuji.sadk.org.bouncycastle.asn1;

import com.myzuji.sadk.org.bouncycastle.util.Arrays;
import com.myzuji.sadk.org.bouncycastle.util.Strings;

import java.io.IOException;

public class DERUTF8String extends ASN1Primitive implements ASN1String {
    private byte[] string;

    public static DERUTF8String getInstance(Object obj) {
        if (obj != null && !(obj instanceof DERUTF8String)) {
            if (obj instanceof byte[]) {
                try {
                    return (DERUTF8String) fromByteArray((byte[]) ((byte[]) obj));
                } catch (Exception var2) {
                    Exception e = var2;
                    throw new IllegalArgumentException("encoding error in getInstance: " + e.toString());
                }
            } else {
                throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
            }
        } else {
            return (DERUTF8String) obj;
        }
    }

    public static DERUTF8String getInstance(ASN1TaggedObject obj, boolean explicit) {
        ASN1Primitive o = obj.getObject();
        return !explicit && !(o instanceof DERUTF8String) ? new DERUTF8String(ASN1OctetString.getInstance(o).getOctets()) : getInstance(o);
    }

    DERUTF8String(byte[] string) {
        this.string = string;
    }

    public DERUTF8String(String string) {
        this.string = Strings.toUTF8ByteArray(string);
    }

    public String getString() {
        return Strings.fromUTF8ByteArray(this.string);
    }

    public String toString() {
        return this.getString();
    }

    public int hashCode() {
        return Arrays.hashCode(this.string);
    }

    public boolean asn1Equals(ASN1Primitive o) {
        if (!(o instanceof DERUTF8String)) {
            return false;
        } else {
            DERUTF8String s = (DERUTF8String) o;
            return Arrays.areEqual(this.string, s.string);
        }
    }

    public boolean isConstructed() {
        return false;
    }

    public int encodedLength() throws IOException {
        return 1 + StreamUtil.calculateBodyLength(this.string.length) + this.string.length;
    }

    public void encode(ASN1OutputStream out) throws IOException {
        out.writeEncoded(12, this.string);
    }
}
