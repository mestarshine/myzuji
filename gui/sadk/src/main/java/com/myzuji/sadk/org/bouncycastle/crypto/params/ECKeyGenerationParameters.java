package com.myzuji.sadk.org.bouncycastle.crypto.params;

import com.myzuji.sadk.org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

public class ECKeyGenerationParameters extends KeyGenerationParameters {
    private ECDomainParameters domainParams;

    public ECKeyGenerationParameters(ECDomainParameters domainParams, SecureRandom random) {
        super(random, domainParams.getN().bitLength());
        this.domainParams = domainParams;
    }

    public ECDomainParameters getDomainParameters() {
        return this.domainParams;
    }
}
