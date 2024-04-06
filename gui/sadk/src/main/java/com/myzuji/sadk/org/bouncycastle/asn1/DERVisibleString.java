package com.myzuji.sadk.org.bouncycastle.asn1;

import com.myzuji.sadk.org.bouncycastle.util.Arrays;
import com.myzuji.sadk.org.bouncycastle.util.Strings;

import java.io.IOException;

public class DERVisibleString extends ASN1Primitive implements ASN1String {
    private byte[] string;

    public static DERVisibleString getInstance(Object obj) {
        if (obj != null && !(obj instanceof DERVisibleString)) {
            if (obj instanceof byte[]) {
                try {
                    return (DERVisibleString) fromByteArray((byte[]) ((byte[]) obj));
                } catch (Exception var2) {
                    Exception e = var2;
                    throw new IllegalArgumentException("encoding error in getInstance: " + e.toString());
                }
            } else {
                throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
            }
        } else {
            return (DERVisibleString) obj;
        }
    }

    public static DERVisibleString getInstance(ASN1TaggedObject obj, boolean explicit) {
        ASN1Primitive o = obj.getObject();
        return !explicit && !(o instanceof DERVisibleString) ? new DERVisibleString(ASN1OctetString.getInstance(o).getOctets()) : getInstance(o);
    }

    DERVisibleString(byte[] string) {
        this.string = string;
    }

    public DERVisibleString(String string) {
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
        out.writeEncoded(26, this.string);
    }

    public boolean asn1Equals(ASN1Primitive o) {
        return !(o instanceof DERVisibleString) ? false : Arrays.areEqual(this.string, ((DERVisibleString) o).string);
    }

    public int hashCode() {
        return Arrays.hashCode(this.string);
    }
}
