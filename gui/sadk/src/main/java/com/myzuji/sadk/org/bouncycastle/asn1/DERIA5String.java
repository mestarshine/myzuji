package com.myzuji.sadk.org.bouncycastle.asn1;

import com.myzuji.sadk.org.bouncycastle.util.Arrays;
import com.myzuji.sadk.org.bouncycastle.util.Strings;

import java.io.IOException;

public class DERIA5String extends ASN1Primitive implements ASN1String {
    private byte[] string;

    public static DERIA5String getInstance(Object obj) {
        if (obj != null && !(obj instanceof DERIA5String)) {
            if (obj instanceof byte[]) {
                try {
                    return (DERIA5String) fromByteArray((byte[]) ((byte[]) obj));
                } catch (Exception var2) {
                    Exception e = var2;
                    throw new IllegalArgumentException("encoding error in getInstance: " + e.toString());
                }
            } else {
                throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
            }
        } else {
            return (DERIA5String) obj;
        }
    }

    public static DERIA5String getInstance(ASN1TaggedObject obj, boolean explicit) {
        ASN1Primitive o = obj.getObject();
        return !explicit && !(o instanceof DERIA5String) ? new DERIA5String(((ASN1OctetString) o).getOctets()) : getInstance(o);
    }

    DERIA5String(byte[] string) {
        this.string = string;
    }

    public DERIA5String(String string) {
        this(string, false);
    }

    public DERIA5String(String string, boolean validate) {
        if (string == null) {
            throw new NullPointerException("string cannot be null");
        } else if (validate && !isIA5String(string)) {
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
        out.writeEncoded(22, this.string);
    }

    public int hashCode() {
        return Arrays.hashCode(this.string);
    }

    public boolean asn1Equals(ASN1Primitive o) {
        if (!(o instanceof DERIA5String)) {
            return false;
        } else {
            DERIA5String s = (DERIA5String) o;
            return Arrays.areEqual(this.string, s.string);
        }
    }

    public static boolean isIA5String(String str) {
        for (int i = str.length() - 1; i >= 0; --i) {
            char ch = str.charAt(i);
            if (ch > 127) {
                return false;
            }
        }

        return true;
    }
}
