package com.myzuji.sadk.org.bouncycastle.cms;

import com.myzuji.sadk.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.myzuji.sadk.org.bouncycastle.pkcs.PKCSObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.util.io.Streams;

import java.io.BufferedInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CMSTypedStream {
    private static final int BUF_SIZ = 32768;
    private final ASN1ObjectIdentifier _oid;
    private final InputStream _in;

    public CMSTypedStream(InputStream in) {
        this((String) PKCSObjectIdentifiers.data.getId(), in, 32768);
    }

    public CMSTypedStream(String oid, InputStream in) {
        this((ASN1ObjectIdentifier) (new ASN1ObjectIdentifier(oid)), in, 32768);
    }

    public CMSTypedStream(String oid, InputStream in, int bufSize) {
        this(new ASN1ObjectIdentifier(oid), in, bufSize);
    }

    public CMSTypedStream(ASN1ObjectIdentifier oid, InputStream in) {
        this((ASN1ObjectIdentifier) oid, in, 32768);
    }

    public CMSTypedStream(ASN1ObjectIdentifier oid, InputStream in, int bufSize) {
        this._oid = oid;
        this._in = new FullReaderStream(new BufferedInputStream(in, bufSize));
    }

    public ASN1ObjectIdentifier getContentType() {
        return this._oid;
    }

    public InputStream getContentStream() {
        return this._in;
    }

    public void drain() throws IOException {
        Streams.drain(this._in);
        this._in.close();
    }

    private static class FullReaderStream extends FilterInputStream {
        FullReaderStream(InputStream in) {
            super(in);
        }

        public int read(byte[] buf, int off, int len) throws IOException {
            int totalRead = Streams.readFully(super.in, buf, off, len);
            return totalRead > 0 ? totalRead : -1;
        }
    }
}
