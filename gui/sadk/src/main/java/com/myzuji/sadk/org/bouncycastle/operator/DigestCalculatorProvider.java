package com.myzuji.sadk.org.bouncycastle.operator;

import com.myzuji.sadk.org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public interface DigestCalculatorProvider {
    DigestCalculator get(AlgorithmIdentifier var1) throws OperatorCreationException;
}
