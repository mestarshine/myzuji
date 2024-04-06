package com.myzuji.sadk.org.bouncycastle.asn1;

import com.myzuji.sadk.org.bouncycastle.util.Arrays;
import com.myzuji.sadk.org.bouncycastle.util.Strings;

import java.io.IOException;

public class DERT61String extends ASN1Primitive implements ASN1String {
    private byte[] string;

    public static DERT61String getInstance(Object obj) {
        if (obj != null && !(obj instanceof DERT61String)) {
            if (obj instanceof byte[]) {
                try {
                    return (DERT61String) fromByteArray((byte[]) ((byte[]) obj));
                } catch (Exception var2) {
                    Exception e = var2;
                    throw new IllegalArgumentException("encoding error in getInstance: " + e.toString());
                }
            } else {
                throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
            }
        } else {
            return (DERT61String) obj;
        }
    }

    public static DERT61String getInstance(ASN1TaggedObject obj, boolean explicit) {
        ASN1Primitive o = obj.getObject();
        return !explicit && !(o instanceof DERT61String) ? new DERT61String(ASN1OctetString.getInstance(o).getOctets()) : getInstance(o);
    }

    public DERT61String(byte[] string) {
        this.string = string;
    }

    public DERT61String(String string) {
        this(Strings.toByteArray(string));
    }

    public String getString() {
        return Strings.fromByteArray(this.string);
    }

    public String toString() {
        return this.getString();
    }

    public boolean isConstructed() {
        return false;
    }

    public int encodedLength() {
        return 1 + StreamUtil.calculateBodyLength(this.string.length) + this.string.length;
    }

    public void encode(ASN1OutputStream out) throws IOException {
        out.writeEncoded(20, this.string);
    }

    public byte[] getOctets() {
        return Arrays.clone(this.string);
    }

    public boolean asn1Equals(ASN1Primitive o) {
        return !(o instanceof DERT61String) ? false : Arrays.areEqual(this.string, ((DERT61String) o).string);
    }

    public int hashCode() {
        return Arrays.hashCode(this.string);
    }
}
