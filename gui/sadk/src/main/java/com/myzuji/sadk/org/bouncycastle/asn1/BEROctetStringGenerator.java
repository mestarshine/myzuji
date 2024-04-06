package com.myzuji.sadk.org.bouncycastle.asn1;

import java.io.IOException;
import java.io.OutputStream;

public class BEROctetStringGenerator extends BERGenerator {
    public BEROctetStringGenerator(OutputStream out) throws IOException {
        super(out);
        this.writeBERHeader(36);
    }

    public BEROctetStringGenerator(OutputStream out, int tagNo, boolean isExplicit) throws IOException {
        super(out, tagNo, isExplicit);
        this.writeBERHeader(36);
    }

    public OutputStream getOctetOutputStream() {
        return this.getOctetOutputStream(new byte[1000]);
    }

    public OutputStream getOctetOutputStream(byte[] buf) {
        return new BufferedBEROctetStream(buf);
    }

    private class BufferedBEROctetStream extends OutputStream {
        private byte[] _buf;
        private int _off;
        private DEROutputStream _derOut;

        BufferedBEROctetStream(byte[] buf) {
            this._buf = buf;
            this._off = 0;
            this._derOut = new DEROutputStream(BEROctetStringGenerator.this._out);
        }

        public void write(int b) throws IOException {
            this._buf[this._off++] = (byte) b;
            if (this._off == this._buf.length) {
                DEROctetString.encode(this._derOut, this._buf);
                this._off = 0;
            }

        }

        public void write(byte[] b, int off, int len) throws IOException {
            while (true) {
                if (len > 0) {
                    int numToCopy = Math.min(len, this._buf.length - this._off);
                    System.arraycopy(b, off, this._buf, this._off, numToCopy);
                    this._off += numToCopy;
                    if (this._off >= this._buf.length) {
                        DEROctetString.encode(this._derOut, this._buf);
                        this._off = 0;
                        off += numToCopy;
                        len -= numToCopy;
                        continue;
                    }
                }

                return;
            }
        }

        public void close() throws IOException {
            if (this._off != 0) {
                byte[] bytes = new byte[this._off];
                System.arraycopy(this._buf, 0, bytes, 0, this._off);
                DEROctetString.encode(this._derOut, bytes);
            }

            BEROctetStringGenerator.this.writeBEREnd();
        }
    }
}
