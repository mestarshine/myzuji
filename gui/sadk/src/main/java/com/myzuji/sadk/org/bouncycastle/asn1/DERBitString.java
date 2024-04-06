package com.myzuji.sadk.org.bouncycastle.asn1;

import com.myzuji.sadk.org.bouncycastle.util.Arrays;
import com.myzuji.sadk.org.bouncycastle.util.io.Streams;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class DERBitString extends ASN1Primitive implements ASN1String {
    private static final char[] table = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    protected byte[] data;
    protected int padBits;

    protected static int getPadBits(int bitString) {
        int val = 0;

        int bits;
        for (bits = 3; bits >= 0; --bits) {
            if (bits != 0) {
                if (bitString >> bits * 8 != 0) {
                    val = bitString >> bits * 8 & 255;
                    break;
                }
            } else if (bitString != 0) {
                val = bitString & 255;
                break;
            }
        }

        if (val == 0) {
            return 7;
        } else {
            for (bits = 1; ((val <<= 1) & 255) != 0; ++bits) {
            }

            return 8 - bits;
        }
    }

    protected static byte[] getBytes(int bitString) {
        int bytes = 4;

        for (int i = 3; i >= 1 && (bitString & 255 << i * 8) == 0; --i) {
            --bytes;
        }

        byte[] result = new byte[bytes];

        for (int i = 0; i < bytes; ++i) {
            result[i] = (byte) (bitString >> i * 8 & 255);
        }

        return result;
    }

    public static DERBitString getInstance(Object obj) {
        if (obj != null && !(obj instanceof DERBitString)) {
            throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
        } else {
            return (DERBitString) obj;
        }
    }

    public static DERBitString getInstance(ASN1TaggedObject obj, boolean explicit) {
        ASN1Primitive o = obj.getObject();
        return !explicit && !(o instanceof DERBitString) ? fromOctetString(((ASN1OctetString) o).getOctets()) : getInstance(o);
    }

    protected DERBitString(byte data, int padBits) {
        this.data = new byte[1];
        this.data[0] = data;
        this.padBits = padBits;
    }

    public DERBitString(byte[] data, int padBits) {
        this.data = data;
        this.padBits = padBits;
    }

    public DERBitString(byte[] data) {
        this(data, 0);
    }

    public DERBitString(int value) {
        this.data = getBytes(value);
        this.padBits = getPadBits(value);
    }

    public DERBitString(ASN1Encodable obj) throws IOException {
        this.data = obj.toASN1Primitive().getEncoded("DER");
        this.padBits = 0;
    }

    public byte[] getBytes() {
        return this.data;
    }

    public int getPadBits() {
        return this.padBits;
    }

    public int intValue() {
        int value = 0;

        for (int i = 0; i != this.data.length && i != 4; ++i) {
            value |= (this.data[i] & 255) << 8 * i;
        }

        return value;
    }

    public boolean isConstructed() {
        return false;
    }

    public int encodedLength() {
        return 1 + StreamUtil.calculateBodyLength(this.data.length + 1) + this.data.length + 1;
    }

    public void encode(ASN1OutputStream out) throws IOException {
        byte[] bytes = new byte[this.getBytes().length + 1];
        bytes[0] = (byte) this.getPadBits();
        System.arraycopy(this.getBytes(), 0, bytes, 1, bytes.length - 1);
        out.writeEncoded(3, bytes);
    }

    public int hashCode() {
        return this.padBits ^ Arrays.hashCode(this.data);
    }

    public boolean asn1Equals(ASN1Primitive o) {
        if (!(o instanceof DERBitString)) {
            return false;
        } else {
            DERBitString other = (DERBitString) o;
            return this.padBits == other.padBits && Arrays.areEqual(this.data, other.data);
        }
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

    static DERBitString fromOctetString(byte[] bytes) {
        if (bytes.length < 1) {
            throw new IllegalArgumentException("truncated BIT STRING detected");
        } else {
            int padBits = bytes[0];
            byte[] data = new byte[bytes.length - 1];
            if (data.length != 0) {
                System.arraycopy(bytes, 1, data, 0, bytes.length - 1);
            }

            return new DERBitString(data, padBits);
        }
    }

    static DERBitString fromInputStream(int length, InputStream stream) throws IOException {
        if (length < 1) {
            throw new IllegalArgumentException("truncated BIT STRING detected");
        } else {
            int padBits = stream.read();
            byte[] data = new byte[length - 1];
            if (data.length != 0 && Streams.readFully(stream, data) != data.length) {
                throw new EOFException("EOF encountered in middle of BIT STRING");
            } else {
                return new DERBitString(data, padBits);
            }
        }
    }
}
