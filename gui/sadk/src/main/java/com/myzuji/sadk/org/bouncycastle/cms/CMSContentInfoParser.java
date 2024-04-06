package com.myzuji.sadk.org.bouncycastle.cms;

import com.myzuji.sadk.org.bouncycastle.asn1.ASN1SequenceParser;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1StreamParser;

import java.io.IOException;
import java.io.InputStream;

public class CMSContentInfoParser {
    protected ContentInfoParser _contentInfo;
    protected InputStream _data;

    protected CMSContentInfoParser(InputStream data) throws CMSException {
        this._data = data;

        try {
            ASN1StreamParser in = new ASN1StreamParser(data);
            this._contentInfo = new ContentInfoParser((ASN1SequenceParser) in.readObject());
        } catch (IOException var3) {
            IOException e = var3;
            throw new CMSException("IOException reading content.", e);
        } catch (ClassCastException var4) {
            ClassCastException e = var4;
            throw new CMSException("Unexpected object reading content.", e);
        }
    }

    public void close() throws IOException {
        this._data.close();
    }
}
