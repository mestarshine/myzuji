package com.myzuji.sadk.org.bouncycastle.crypto.params;

import com.myzuji.sadk.org.bouncycastle.crypto.CipherParameters;

public class ParametersWithSBox implements CipherParameters {
    private CipherParameters parameters;
    private byte[] sBox;

    public ParametersWithSBox(CipherParameters parameters, byte[] sBox) {
        this.parameters = parameters;
        this.sBox = sBox;
    }

    public byte[] getSBox() {
        return this.sBox;
    }

    public CipherParameters getParameters() {
        return this.parameters;
    }
}
