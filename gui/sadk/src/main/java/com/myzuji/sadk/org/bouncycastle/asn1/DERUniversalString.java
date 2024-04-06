package com.myzuji.sadk.org.bouncycastle.asn1;

import com.myzuji.sadk.org.bouncycastle.util.Arrays;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DERUniversalString extends ASN1Primitive implements ASN1String {
    private static final char[] table = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private byte[] string;

    public static DERUniversalString getInstance(Object obj) {
        if (obj != null && !(obj instanceof DERUniversalString)) {
            if (obj instanceof byte[]) {
                try {
                    return (DERUniversalString) fromByteArray((byte[]) ((byte[]) obj));
                } catch (Exception var2) {
                    Exception e = var2;
                    throw new IllegalArgumentException("encoding error getInstance: " + e.toString());
                }
            } else {
                throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
            }
        } else {
            return (DERUniversalString) obj;
        }
    }

    public static DERUniversalString getInstance(ASN1TaggedObject obj, boolean explicit) {
        ASN1Primitive o = obj.getObject();
        return !explicit && !(o instanceof DERUniversalString) ? new DERUniversalString(((ASN1OctetString) o).getOctets()) : getInstance(o);
    }

    public DERUniversalString(byte[] string) {
        this.string = string;
    }

    public String getString() {
        StringBuffer buf = new StringBuffer("#");
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        ASN1OutputStream aOut = new ASN1OutputStream(bOut);

        try {
            aOut.writeObject(this);
        } catch (IOException var6) {
            throw new RuntimeException("internal error encoding BitString");
        }

        byte[] string = bOut.toByteArray();

        for (int i = 0; i != string.length; ++i) {
            buf.append(table[string[i] >>> 4 & 15]);
            buf.append(table[string[i] & 15]);
        }

        return buf.toString();
    }

    public String toString() {
        return this.getString();
    }

    public byte[] getOctets() {
        return this.string;
    }

    public boolean isConstructed() {
        return false;
    }

    public int encodedLength() {
        return 1 + StreamUtil.calculateBodyLength(this.string.length) + this.string.length;
    }

    public void encode(ASN1OutputStream out) throws IOException {
        out.writeEncoded(28, this.getOctets());
    }

    public boolean asn1Equals(ASN1Primitive o) {
        return !(o instanceof DERUniversalString) ? false : Arrays.areEqual(this.string, ((DERUniversalString) o).string);
    }

    public int hashCode() {
        return Arrays.hashCode(this.string);
    }
}
