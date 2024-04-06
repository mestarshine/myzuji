package com.myzuji.sadk.org.bouncycastle.crypto;

import com.myzuji.sadk.org.bouncycastle.crypto.params.AsymmetricKeyParameter;

public class AsymmetricCipherKeyPair {
    private AsymmetricKeyParameter publicParam;
    private AsymmetricKeyParameter privateParam;

    public AsymmetricCipherKeyPair(AsymmetricKeyParameter publicParam, AsymmetricKeyParameter privateParam) {
        this.publicParam = publicParam;
        this.privateParam = privateParam;
    }


    public AsymmetricCipherKeyPair(CipherParameters publicParam, CipherParameters privateParam) {
        this.publicParam = (AsymmetricKeyParameter) publicParam;
        this.privateParam = (AsymmetricKeyParameter) privateParam;
    }

    public AsymmetricKeyParameter getPublic() {
        return this.publicParam;
    }

    public AsymmetricKeyParameter getPrivate() {
        return this.privateParam;
    }
}
