package com.myzuji.sadk.org.bouncycastle.asn1;

import java.io.IOException;

public class BERApplicationSpecificParser implements ASN1ApplicationSpecificParser {
    private final int tag;
    private final ASN1StreamParser parser;

    BERApplicationSpecificParser(int tag, ASN1StreamParser parser) {
        this.tag = tag;
        this.parser = parser;
    }

    public ASN1Encodable readObject() throws IOException {
        return this.parser.readObject();
    }

    public ASN1Primitive getLoadedObject() throws IOException {
        return new BERApplicationSpecific(this.tag, this.parser.readVector());
    }

    public ASN1Primitive toASN1Primitive() {
        try {
            return this.getLoadedObject();
        } catch (IOException var2) {
            IOException e = var2;
            throw new ASN1ParsingException(e.getMessage(), e);
        }
    }
}
