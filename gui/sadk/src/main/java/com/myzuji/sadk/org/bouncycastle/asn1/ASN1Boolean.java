package com.myzuji.sadk.org.bouncycastle.asn1;

import com.myzuji.sadk.org.bouncycastle.util.Arrays;

import java.io.IOException;

public class ASN1Boolean extends ASN1Primitive {
    private static final byte[] TRUE_VALUE = new byte[]{-1};
    private static final byte[] FALSE_VALUE = new byte[]{0};
    private byte[] value;
    public static final ASN1Boolean FALSE = new ASN1Boolean(false);
    public static final ASN1Boolean TRUE = new ASN1Boolean(true);

    public static ASN1Boolean getInstance(Object obj) {
        if (obj != null && !(obj instanceof ASN1Boolean)) {
            if (obj instanceof byte[]) {
                byte[] enc = (byte[]) ((byte[]) obj);

                try {
                    return (ASN1Boolean) fromByteArray(enc);
                } catch (IOException var3) {
                    IOException e = var3;
                    throw new IllegalArgumentException("failed to construct boolean from byte[]: " + e.getMessage());
                }
            } else {
                throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
            }
        } else {
            return (ASN1Boolean) obj;
        }
    }

    public static ASN1Boolean getInstance(boolean value) {
        return value ? TRUE : FALSE;
    }

    public static ASN1Boolean getInstance(int value) {
        return value != 0 ? TRUE : FALSE;
    }

    public static ASN1Boolean getInstance(ASN1TaggedObject obj, boolean explicit) {
        ASN1Primitive o = obj.getObject();
        return !explicit && !(o instanceof ASN1Boolean) ? fromOctetString(((ASN1OctetString) o).getOctets()) : getInstance(o);
    }

    ASN1Boolean(byte[] value) {
        if (value.length != 1) {
            throw new IllegalArgumentException("byte value should have 1 byte in it");
        } else {
            if (value[0] == 0) {
                this.value = FALSE_VALUE;
            } else if ((value[0] & 255) == 255) {
                this.value = TRUE_VALUE;
            } else {
                this.value = Arrays.clone(value);
            }

        }
    }


    public ASN1Boolean(boolean value) {
        this.value = value ? TRUE_VALUE : FALSE_VALUE;
    }

    public boolean isTrue() {
        return this.value[0] != 0;
    }

    public boolean isConstructed() {
        return false;
    }

    public int encodedLength() {
        return 3;
    }

    public void encode(ASN1OutputStream out) throws IOException {
        out.writeEncoded(1, this.value);
    }

    public boolean asn1Equals(ASN1Primitive o) {
        if (o instanceof ASN1Boolean) {
            return this.value[0] == ((ASN1Boolean) o).value[0];
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.value[0];
    }

    public String toString() {
        return this.value[0] != 0 ? "TRUE" : "FALSE";
    }

    static ASN1Boolean fromOctetString(byte[] value) {
        if (value.length != 1) {
            throw new IllegalArgumentException("BOOLEAN value should have 1 byte in it");
        } else if (value[0] == 0) {
            return FALSE;
        } else {
            return (value[0] & 255) == 255 ? TRUE : new ASN1Boolean(value);
        }
    }
}
