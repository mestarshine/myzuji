package com.myzuji.sadk.org.bouncycastle.asn1;

import com.myzuji.sadk.org.bouncycastle.util.Arrays;
import com.myzuji.sadk.org.bouncycastle.util.Strings;

import java.io.IOException;

public class DERGeneralString extends ASN1Primitive implements ASN1String {
    private byte[] string;

    public static DERGeneralString getInstance(Object obj) {
        if (obj != null && !(obj instanceof DERGeneralString)) {
            if (obj instanceof byte[]) {
                try {
                    return (DERGeneralString) fromByteArray((byte[]) ((byte[]) obj));
                } catch (Exception var2) {
                    Exception e = var2;
                    throw new IllegalArgumentException("encoding error in getInstance: " + e.toString());
                }
            } else {
                throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
            }
        } else {
            return (DERGeneralString) obj;
        }
    }

    public static DERGeneralString getInstance(ASN1TaggedObject obj, boolean explicit) {
        ASN1Primitive o = obj.getObject();
        return !explicit && !(o instanceof DERGeneralString) ? new DERGeneralString(((ASN1OctetString) o).getOctets()) : getInstance(o);
    }

    DERGeneralString(byte[] string) {
        this.string = string;
    }

    public DERGeneralString(String string) {
        this.string = Strings.toByteArray(string);
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
        out.writeEncoded(27, this.string);
    }

    public int hashCode() {
        return Arrays.hashCode(this.string);
    }

    public boolean asn1Equals(ASN1Primitive o) {
        if (!(o instanceof DERGeneralString)) {
            return false;
        } else {
            DERGeneralString s = (DERGeneralString) o;
            return Arrays.areEqual(this.string, s.string);
        }
    }
}
