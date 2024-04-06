package com.myzuji.sadk.org.bouncycastle.asn1;

import java.io.IOException;

public class DERSequenceParser implements ASN1SequenceParser {
    private ASN1StreamParser _parser;

    DERSequenceParser(ASN1StreamParser parser) {
        this._parser = parser;
    }

    public ASN1Encodable readObject() throws IOException {
        return this._parser.readObject();
    }

    public ASN1Primitive getLoadedObject() throws IOException {
        return new DERSequence(this._parser.readVector());
    }

    public ASN1Primitive toASN1Primitive() {
        try {
            return this.getLoadedObject();
        } catch (IOException var2) {
            IOException e = var2;
            throw new IllegalStateException(e.getMessage());
        }
    }
}
