package com.myzuji.sadk.org.bouncycastle.crypto;

public interface AsymmetricCipherKeyPairGenerator {
    void init(KeyGenerationParameters var1);

    AsymmetricCipherKeyPair generateKeyPair();
}
