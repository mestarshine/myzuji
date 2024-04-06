package com.myzuji.sadk.org.bouncycastle.operator;

import com.myzuji.sadk.org.bouncycastle.asn1.x509.AlgorithmIdentifier;

import java.io.OutputStream;

public interface ContentVerifier {
    AlgorithmIdentifier getAlgorithmIdentifier();

    OutputStream getOutputStream();

    boolean verify(byte[] var1);
}
