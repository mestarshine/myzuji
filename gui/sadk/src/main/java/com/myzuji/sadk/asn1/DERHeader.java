package com.myzuji.sadk.asn1;

import java.io.EOFException;
import java.io.IOException;

public class DERHeader {
    private int tag;
    private int length;
    private int size;

    public DERHeader(byte[] encoded, int offset) throws IOException {
        this(encoded, offset, 0, Integer.MAX_VALUE);
    }

    public DERHeader(byte[] encoded, int offset, int tag) throws IOException {
        this(encoded, offset, tag, Integer.MAX_VALUE);
    }

    public DERHeader(byte[] encoded, int offset, int tag, int limited) throws IOException {
        this.tag = 0;
        this.length = 0;
        this.size = 0;
        this.decodedDERHeader(encoded, offset, tag, limited);
    }

    public int getTag() {
        return this.tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getDerLength() {
        return this.size + this.length;
    }

    public String toString() {
        StringBuffer builder = new StringBuffer();
        builder.append("DERHeader [tag=").append(this.tag).append(", length=").append(this.length).append(", size=").append(this.size).append("]");
        return builder.toString();
    }

    private final void decodedDERHeader(byte[] encoded, int offset, int tag, int limited) throws IOException {
        if (encoded != null && encoded.length >= 1 + offset) {
            this.tag = encoded[offset++] & 255;
            this.size = 1;
            if (tag != 0 && this.tag != tag) {
                throw new EOFException("decodedTag - Tag unexpected value");
            } else {
                this.length = encoded[offset++] & 255;
                ++this.size;
                if (this.length == 128) {
                    throw new EOFException("readLength - BER found");
                } else {
                    if (this.length > 127) {
                        int size = this.length & 127;
                        if (size > 4) {
                            throw new IOException("readLength - DER length more than 4 bytes: " + size);
                        }

                        if (encoded.length < offset + size) {
                            throw new EOFException("readLength - EOF found reading length");
                        }

                        this.size += size;
                        this.length = 0;
                        int next = 1;

                        for (int i = 0; i < size; ++i) {
                            next = encoded[offset++] & 255;
                            this.length = (this.length << 8) + next;
                        }

                        if (this.length < 0) {
                            throw new IOException("readLength - negative length found");
                        }
                    }

                    if (encoded.length < offset + this.length) {
                        throw new IOException("readLength - out of bounds length found");
                    } else if (this.length > limited) {
                        throw new IOException("readLength - out of bounds length limited");
                    }
                }
            }
        } else {
            throw new EOFException("decodedTag - EOF found");
        }
    }

    public static final boolean checkedASN1Sequence(byte[] encoded) {
        boolean checked = false;
        if (encoded != null) {
            try {
                DERHeader der = new DERHeader(encoded, 0);
                checked = der.getTag() == 48 || der.getDerLength() == encoded.length;
            } catch (Exception var3) {
                checked = false;
            }
        }

        return checked;
    }
}
