package com.myzuji.sadk.org.bouncycastle.asn1;

import com.myzuji.sadk.org.bouncycastle.util.Arrays;

import java.io.IOException;

public class DERBMPString extends ASN1Primitive implements ASN1String {
    private char[] string;

    public static DERBMPString getInstance(Object obj) {
        if (obj != null && !(obj instanceof DERBMPString)) {
            if (obj instanceof byte[]) {
                try {
                    return (DERBMPString) fromByteArray((byte[]) ((byte[]) obj));
                } catch (Exception var2) {
                    Exception e = var2;
                    throw new IllegalArgumentException("encoding error in getInstance: " + e.toString());
                }
            } else {
                throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
            }
        } else {
            return (DERBMPString) obj;
        }
    }

    public static DERBMPString getInstance(ASN1TaggedObject obj, boolean explicit) {
        ASN1Primitive o = obj.getObject();
        return !explicit && !(o instanceof DERBMPString) ? new DERBMPString(ASN1OctetString.getInstance(o).getOctets()) : getInstance(o);
    }

    DERBMPString(byte[] string) {
        char[] cs = new char[string.length / 2];

        for (int i = 0; i != cs.length; ++i) {
            cs[i] = (char) (string[2 * i] << 8 | string[2 * i + 1] & 255);
        }

        this.string = cs;
    }

    DERBMPString(char[] string) {
        this.string = string;
    }

    public DERBMPString(String string) {
        this.string = string.toCharArray();
    }

    public String getString() {
        return new String(this.string);
    }

    public String toString() {
        return this.getString();
    }

    public int hashCode() {
        return Arrays.hashCode(this.string);
    }

    public boolean asn1Equals(ASN1Primitive o) {
        if (!(o instanceof DERBMPString)) {
            return false;
        } else {
            DERBMPString s = (DERBMPString) o;
            return Arrays.areEqual(this.string, s.string);
        }
    }

    public boolean isConstructed() {
        return false;
    }

    public int encodedLength() {
        return 1 + StreamUtil.calculateBodyLength(this.string.length * 2) + this.string.length * 2;
    }

    public void encode(ASN1OutputStream out) throws IOException {
        out.write(30);
        out.writeLength(this.string.length * 2);

        for (int i = 0; i != this.string.length; ++i) {
            char c = this.string[i];
            out.write((byte) (c >> 8));
            out.write((byte) c);
        }

    }
}
