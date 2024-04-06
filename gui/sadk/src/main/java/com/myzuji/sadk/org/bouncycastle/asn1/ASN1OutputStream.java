package com.myzuji.sadk.org.bouncycastle.asn1;

import java.io.IOException;
import java.io.OutputStream;

public class ASN1OutputStream {

    private OutputStream os;

    public ASN1OutputStream(OutputStream os) {
        this.os = os;
    }

    public void writeLength(int length) throws IOException {
        if (length > 127) {
            int size = 1;

            for (int val = length; (val >>>= 8) != 0; ++size) {
            }

            this.write((byte) (size | 128));

            for (int i = (size - 1) * 8; i >= 0; i -= 8) {
                this.write((byte) (length >> i));
            }
        } else {
            this.write((byte) length);
        }

    }

    public void write(int b) throws IOException {
        this.os.write(b);
    }

    public void write(byte[] bytes) throws IOException {
        this.os.write(bytes);
    }

    public void write(byte[] bytes, int off, int len) throws IOException {
        this.os.write(bytes, off, len);
    }

    public void writeEncoded(int tag, byte[] bytes) throws IOException {
        this.write(tag);
        this.writeLength(bytes.length);
        this.write(bytes);
    }

    public void writeTag(int flags, int tagNo) throws IOException {
        if (tagNo < 31) {
            this.write(flags | tagNo);
        } else {
            this.write(flags | 31);
            if (tagNo < 128) {
                this.write(tagNo);
            } else {
                byte[] stack = new byte[5];
                int pos = stack.length;
                --pos;
                stack[pos] = (byte) (tagNo & 127);

                do {
                    tagNo >>= 7;
                    --pos;
                    stack[pos] = (byte) (tagNo & 127 | 128);
                } while (tagNo > 127);

                this.write(stack, pos, stack.length - pos);
            }
        }

    }

    public void writeEncoded(int flags, int tagNo, byte[] bytes) throws IOException {
        this.writeTag(flags, tagNo);
        this.writeLength(bytes.length);
        this.write(bytes);
    }

    public void writeNull() throws IOException {
        this.os.write(5);
        this.os.write(0);
    }

    public void writeObject(ASN1Encodable obj) throws IOException {
        if (obj != null) {
            obj.toASN1Primitive().encode(this);
        } else {
            throw new IOException("null object detected");
        }
    }

    public void writeImplicitObject(ASN1Primitive obj) throws IOException {
        if (obj != null) {
            obj.encode(new ImplicitOutputStream(this.os));
        } else {
            throw new IOException("null object detected");
        }
    }

    public void close() throws IOException {
        this.os.close();
    }

    public void flush() throws IOException {
        this.os.flush();
    }

    public ASN1OutputStream getDERSubStream() {
        return new DEROutputStream(this.os);
    }

    public ASN1OutputStream getDLSubStream() {
        return new DLOutputStream(this.os);
    }

    private class ImplicitOutputStream extends ASN1OutputStream {
        private boolean first = true;

        public ImplicitOutputStream(OutputStream os) {
            super(os);
        }

        public void write(int b) throws IOException {
            if (this.first) {
                this.first = false;
            } else {
                super.write(b);
            }

        }
    }
}
