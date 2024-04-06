package com.myzuji.sadk.org.bouncycastle.asn1;

import com.myzuji.sadk.org.bouncycastle.util.Arrays;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DERApplicationSpecific extends ASN1Primitive {
    private final boolean isConstructed;
    private final int tag;
    private final byte[] octets;

    DERApplicationSpecific(boolean isConstructed, int tag, byte[] octets) {
        this.isConstructed = isConstructed;
        this.tag = tag;
        this.octets = octets;
    }

    public DERApplicationSpecific(int tag, byte[] octets) {
        this(false, tag, octets);
    }

    public DERApplicationSpecific(int tag, ASN1Encodable object) throws IOException {
        this(true, tag, object);
    }

    public DERApplicationSpecific(boolean explicit, int tag, ASN1Encodable object) throws IOException {
        ASN1Primitive primitive = object.toASN1Primitive();
        byte[] data = primitive.getEncoded("DER");
        this.isConstructed = explicit || primitive instanceof ASN1Set || primitive instanceof ASN1Sequence;
        this.tag = tag;
        if (explicit) {
            this.octets = data;
        } else {
            int lenBytes = this.getLengthOfHeader(data);
            byte[] tmp = new byte[data.length - lenBytes];
            System.arraycopy(data, lenBytes, tmp, 0, tmp.length);
            this.octets = tmp;
        }

    }

    public DERApplicationSpecific(int tagNo, ASN1EncodableVector vec) {
        this.tag = tagNo;
        this.isConstructed = true;
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();

        for (int i = 0; i != vec.size(); ++i) {
            try {
                bOut.write(((ASN1Object) vec.get(i)).getEncoded("DER"));
            } catch (IOException var6) {
                IOException e = var6;
                throw new ASN1ParsingException("malformed object: " + e, e);
            }
        }

        this.octets = bOut.toByteArray();
    }

    public static DERApplicationSpecific getInstance(Object obj) {
        if (obj != null && !(obj instanceof DERApplicationSpecific)) {
            if (obj instanceof byte[]) {
                try {
                    return getInstance(fromByteArray((byte[]) ((byte[]) obj)));
                } catch (IOException var2) {
                    IOException e = var2;
                    throw new IllegalArgumentException("failed to construct object from byte[]: " + e.getMessage());
                }
            } else {
                throw new IllegalArgumentException("unknown object in getInstance: " + obj.getClass().getName());
            }
        } else {
            return (DERApplicationSpecific) obj;
        }
    }

    private int getLengthOfHeader(byte[] data) {
        int length = data[1] & 255;
        if (length == 128) {
            return 2;
        } else if (length > 127) {
            int size = length & 127;
            if (size > 4) {
                throw new IllegalStateException("DER length more than 4 bytes: " + size);
            } else {
                return size + 2;
            }
        } else {
            return 2;
        }
    }

    public boolean isConstructed() {
        return this.isConstructed;
    }

    public byte[] getContents() {
        return this.octets;
    }

    public int getApplicationTag() {
        return this.tag;
    }

    public ASN1Primitive getObject() throws IOException {
        return (new ASN1InputStream(this.getContents())).readObject();
    }

    public ASN1Primitive getObject(int derTagNo) throws IOException {
        if (derTagNo >= 31) {
            throw new IOException("unsupported tag number");
        } else {
            byte[] orig = this.getEncoded();
            byte[] tmp = this.replaceTagNumber(derTagNo, orig);
            if ((orig[0] & 32) != 0) {
                tmp[0] = (byte) (tmp[0] | 32);
            }

            return (new ASN1InputStream(tmp)).readObject();
        }
    }

    public int encodedLength() throws IOException {
        return StreamUtil.calculateTagLength(this.tag) + StreamUtil.calculateBodyLength(this.octets.length) + this.octets.length;
    }

    public void encode(ASN1OutputStream out) throws IOException {
        int classBits = 64;
        if (this.isConstructed) {
            classBits |= 32;
        }

        out.writeEncoded(classBits, this.tag, this.octets);
    }

    public boolean asn1Equals(ASN1Primitive o) {
        if (!(o instanceof DERApplicationSpecific)) {
            return false;
        } else {
            DERApplicationSpecific other = (DERApplicationSpecific) o;
            return this.isConstructed == other.isConstructed && this.tag == other.tag && Arrays.areEqual(this.octets, other.octets);
        }
    }

    public int hashCode() {
        return (this.isConstructed ? 1 : 0) ^ this.tag ^ Arrays.hashCode(this.octets);
    }

    private byte[] replaceTagNumber(int newTag, byte[] input) throws IOException {
        int tagNo = input[0] & 31;
        int index = 1;
        if (tagNo == 31) {
            tagNo = 0;
            int b = input[index++] & 255;
            if ((b & 127) == 0) {
                throw new ASN1ParsingException("corrupted stream - invalid high tag number found");
            }

            while ((b & 128) != 0) {
                tagNo |= b & 127;
                tagNo <<= 7;
                b = input[index++] & 255;
            }

            int var10000 = tagNo | b & 127;
        }

        byte[] tmp = new byte[input.length - index + 1];
        System.arraycopy(input, index, tmp, 1, tmp.length - 1);
        tmp[0] = (byte) newTag;
        return tmp;
    }

}
