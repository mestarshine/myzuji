package com.myzuji.sadk.org.bouncycastle.cms;

import com.myzuji.sadk.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.cms.CMSObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.util.Arrays;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CMSProcessableByteArray implements CMSTypedData, CMSReadable {
    private final ASN1ObjectIdentifier type;
    private final byte[] bytes;

    public CMSProcessableByteArray(byte[] bytes) {
        this(new ASN1ObjectIdentifier(CMSObjectIdentifiers.data.getId()), bytes);
    }

    public CMSProcessableByteArray(ASN1ObjectIdentifier type, byte[] bytes) {
        this.type = type;
        this.bytes = bytes;
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.bytes);
    }

    public void write(OutputStream zOut) throws IOException, CMSException {
        zOut.write(this.bytes);
    }

    public Object getContent() {
        return Arrays.clone(this.bytes);
    }

    public ASN1ObjectIdentifier getContentType() {
        return this.type;
    }
}
