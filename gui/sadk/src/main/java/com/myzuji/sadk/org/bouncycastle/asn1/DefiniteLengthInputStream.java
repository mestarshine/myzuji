package com.myzuji.sadk.org.bouncycastle.asn1;

import com.myzuji.sadk.org.bouncycastle.util.io.Streams;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class DefiniteLengthInputStream extends LimitedInputStream {
    private static final byte[] EMPTY_BYTES = new byte[0];
    private final int _originalLength;
    private int _remaining;

    DefiniteLengthInputStream(InputStream in, int length) {
        super(in, length);
        if (length < 0) {
            throw new IllegalArgumentException("negative lengths not allowed");
        } else {
            this._originalLength = length;
            this._remaining = length;
            if (length == 0) {
                this.setParentEofDetect(true);
            }

        }
    }

    int getRemaining() {
        return this._remaining;
    }

    public int read() throws IOException {
        if (this._remaining == 0) {
            return -1;
        } else {
            int b = this._in.read();
            if (b < 0) {
                throw new EOFException("DEF length " + this._originalLength + " object truncated by " + this._remaining);
            } else {
                if (--this._remaining == 0) {
                    this.setParentEofDetect(true);
                }

                return b;
            }
        }
    }

    public int read(byte[] buf, int off, int len) throws IOException {
        if (this._remaining == 0) {
            return -1;
        } else {
            int toRead = Math.min(len, this._remaining);
            int numRead = this._in.read(buf, off, toRead);
            if (numRead < 0) {
                throw new EOFException("DEF length " + this._originalLength + " object truncated by " + this._remaining);
            } else {
                if ((this._remaining -= numRead) == 0) {
                    this.setParentEofDetect(true);
                }

                return numRead;
            }
        }
    }

    byte[] toByteArray() throws IOException {
        if (this._remaining == 0) {
            return EMPTY_BYTES;
        } else {
            byte[] bytes = new byte[this._remaining];
            if ((this._remaining -= Streams.readFully(this._in, bytes)) != 0) {
                throw new EOFException("DEF length " + this._originalLength + " object truncated by " + this._remaining);
            } else {
                this.setParentEofDetect(true);
                return bytes;
            }
        }
    }
}
