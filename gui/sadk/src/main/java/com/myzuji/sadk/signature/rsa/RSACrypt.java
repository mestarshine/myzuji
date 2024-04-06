package com.myzuji.sadk.signature.rsa;

import com.myzuji.sadk.org.bouncycastle.crypto.AsymmetricBlockCipher;
import com.myzuji.sadk.org.bouncycastle.crypto.CipherParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.encodings.PKCS1Encoding;
import com.myzuji.sadk.org.bouncycastle.crypto.engines.RSABlindedEngine;
import com.myzuji.sadk.org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ParametersWithRandom;

public class RSACrypt {
    private final AsymmetricBlockCipher rsaEngine = new PKCS1Encoding(new RSABlindedEngine());
    private boolean forSigning;

    RSACrypt() {
    }

    public byte[] encrypt(byte[] hash) throws Exception {
        if (!this.forSigning) {
            throw new IllegalStateException("RSADigestSigner not initialised for signature generation.");
        } else {
            return this.rsaEngine.processBlock(hash, 0, hash.length);
        }
    }

    public void init(boolean forSigning, CipherParameters parameters) {
        this.forSigning = forSigning;
        AsymmetricKeyParameter k;
        if (parameters instanceof ParametersWithRandom) {
            k = (AsymmetricKeyParameter) ((ParametersWithRandom) parameters).getParameters();
        } else {
            k = (AsymmetricKeyParameter) parameters;
        }

        if (forSigning && !k.isPrivate()) {
            throw new IllegalArgumentException("signing requires private key");
        } else if (!forSigning && k.isPrivate()) {
            throw new IllegalArgumentException("verification requires public key");
        } else {
            this.rsaEngine.init(forSigning, parameters);
        }
    }

    public byte[] decrypt(byte[] encryptedData) throws Exception {
        if (this.forSigning) {
            throw new IllegalStateException("RSADigestSigner not initialised for verification");
        } else if (encryptedData == null) {
            throw new Exception("encrypt data is null,can not decrypt!");
        } else {
            byte[] sig = this.rsaEngine.processBlock(encryptedData, 0, encryptedData.length);
            return sig;
        }
    }
}
