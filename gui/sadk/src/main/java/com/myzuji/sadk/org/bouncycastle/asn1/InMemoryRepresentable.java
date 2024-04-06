package com.myzuji.sadk.org.bouncycastle.asn1;

import java.io.IOException;

public interface InMemoryRepresentable {
    ASN1Primitive getLoadedObject() throws IOException;
}
