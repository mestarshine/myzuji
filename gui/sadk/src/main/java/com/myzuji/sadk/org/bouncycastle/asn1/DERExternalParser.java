package com.myzuji.sadk.org.bouncycastle.asn1;

import java.io.IOException;

public class DERExternalParser implements ASN1Encodable, InMemoryRepresentable {
    private ASN1StreamParser _parser;

    public DERExternalParser(ASN1StreamParser parser) {
        this._parser = parser;
    }

    public ASN1Encodable readObject() throws IOException {
        return this._parser.readObject();
    }

    public ASN1Primitive getLoadedObject() throws IOException {
        try {
            return new DERExternal(this._parser.readVector());
        } catch (IllegalArgumentException var2) {
            IllegalArgumentException e = var2;
            throw new ASN1Exception(e.getMessage(), e);
        }
    }

    public ASN1Primitive toASN1Primitive() {
        try {
            return this.getLoadedObject();
        } catch (IOException var2) {
            IOException ioe = var2;
            throw new ASN1ParsingException("unable to get DER object", ioe);
        } catch (IllegalArgumentException var3) {
            IllegalArgumentException ioe = var3;
            throw new ASN1ParsingException("unable to get DER object", ioe);
        }
    }
}
