package com.myzuji.sadk.org.bouncycastle.asn1;

import com.myzuji.sadk.org.bouncycastle.util.Arrays;
import com.myzuji.sadk.org.bouncycastle.util.Strings;

import java.io.IOException;

public class DERPrintableString extends ASN1Primitive implements ASN1String {
    private byte[] string;

    public static DERPrintableString getInstance(Object obj) {
        if (obj != null && !(obj instanceof DERPrintableString)) {
            if (obj instanceof byte[]) {
                try {
                    return (DERPrintableString) fromByteArray((byte[]) ((byte[]) obj));
                } catch (Exception var2) {
                    Exception e = var2;
                    throw new IllegalArgumentException("encoding error in getInstance: " + e.toString());
                }
            } else {
                throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
            }
        } else {
            return (DERPrintableString) obj;
        }
    }

    public static DERPrintableString getInstance(ASN1TaggedObject obj, boolean explicit) {
        ASN1Primitive o = obj.getObject();
        return !explicit && !(o instanceof DERPrintableString) ? new DERPrintableString(ASN1OctetString.getInstance(o).getOctets()) : getInstance(o);
    }

    DERPrintableString(byte[] string) {
        this.string = string;
    }

    public DERPrintableString(String string) {
        this(string, false);
    }

    public DERPrintableString(String string, boolean validate) {
        if (validate && !isPrintableString(string)) {
            throw new IllegalArgumentException("string contains illegal characters");
        } else {
            this.string = Strings.toByteArray(string);
        }
    }

    public String getString() {
        return Strings.fromByteArray(this.string);
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
        out.writeEncoded(19, this.string);
    }

    public int hashCode() {
        return Arrays.hashCode(this.string);
    }

    public boolean asn1Equals(ASN1Primitive o) {
        if (!(o instanceof DERPrintableString)) {
            return false;
        } else {
            DERPrintableString s = (DERPrintableString) o;
            return Arrays.areEqual(this.string, s.string);
        }
    }

    public String toString() {
        return this.getString();
    }

    public static boolean isPrintableString(String str) {
        for (int i = str.length() - 1; i >= 0; --i) {
            char ch = str.charAt(i);
            if (ch > 127) {
                return false;
            }

            if (('a' > ch || ch > 'z') && ('A' > ch || ch > 'Z') && ('0' > ch || ch > '9')) {
                switch (ch) {
                    case ' ':
                    case '\'':
                    case '(':
                    case ')':
                    case '+':
                    case ',':
                    case '-':
                    case '.':
                    case '/':
                    case ':':
                    case '=':
                    case '?':
                        break;
                    case '!':
                    case '"':
                    case '#':
                    case '$':
                    case '%':
                    case '&':
                    case '*':
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                    case ';':
                    case '<':
                    case '>':
                    default:
                        return false;
                }
            }
        }

        return true;
    }
}
