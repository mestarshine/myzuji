package com.myzuji.sadk.org.bouncycastle.asn1;

import com.myzuji.sadk.org.bouncycastle.util.Arrays;
import com.myzuji.sadk.org.bouncycastle.util.Strings;

import java.io.IOException;

public class DERNumericString extends ASN1Primitive implements ASN1String {
    private byte[] string;

    public static DERNumericString getInstance(Object obj) {
        if (obj != null && !(obj instanceof DERNumericString)) {
            if (obj instanceof byte[]) {
                try {
                    return (DERNumericString) fromByteArray((byte[]) ((byte[]) obj));
                } catch (Exception var2) {
                    Exception e = var2;
                    throw new IllegalArgumentException("encoding error in getInstance: " + e.toString());
                }
            } else {
                throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
            }
        } else {
            return (DERNumericString) obj;
        }
    }

    public static DERNumericString getInstance(ASN1TaggedObject obj, boolean explicit) {
        ASN1Primitive o = obj.getObject();
        return !explicit && !(o instanceof DERNumericString) ? new DERNumericString(ASN1OctetString.getInstance(o).getOctets()) : getInstance(o);
    }

    DERNumericString(byte[] string) {
        this.string = string;
    }

    public DERNumericString(String string) {
        this(string, false);
    }

    public DERNumericString(String string, boolean validate) {
        if (validate && !isNumericString(string)) {
            throw new IllegalArgumentException("string contains illegal characters");
        } else {
            this.string = Strings.toByteArray(string);
        }
    }

    public String getString() {
        return Strings.fromByteArray(this.string);
    }

    public String toString() {
        return this.getString();
    }

    public byte[] getOctets() {
        return Arrays.clone(this.string);
    }

    public boolean isConstructed() {
        return false;
    }

    public int encodedLength() {
        return 1 + StreamUtil.calculateBodyLength(this.string.length) + this.string.length;
    }

    public void encode(ASN1OutputStream out) throws IOException {
        out.writeEncoded(18, this.string);
    }

    public int hashCode() {
        return Arrays.hashCode(this.string);
    }

    public boolean asn1Equals(ASN1Primitive o) {
        if (!(o instanceof DERNumericString)) {
            return false;
        } else {
            DERNumericString s = (DERNumericString) o;
            return Arrays.areEqual(this.string, s.string);
        }
    }

    public static boolean isNumericString(String str) {
        for (int i = str.length() - 1; i >= 0; --i) {
            char ch = str.charAt(i);
            if (ch > 127) {
                return false;
            }

            if (('0' > ch || ch > '9') && ch != ' ') {
                return false;
            }
        }

        return true;
    }
}
