package com.myzuji.sadk.org.bouncycastle.jcajce.asymmetric.keypair;

import com.myzuji.sadk.algorithm.sm2.SM2PrivateKey;
import com.myzuji.sadk.algorithm.sm2.SM2PublicKey;
import com.myzuji.sadk.org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ECPublicKeyParameters;
import com.myzuji.sadk.org.bouncycastle.jcajce.provider.asymmetric.sm.SM2Params;

import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.KeyPairGeneratorSpi;
import java.security.SecureRandom;

public class SM2KeyPairGenerator extends KeyPairGeneratorSpi {
    private SecureRandom random = null;

    public SM2KeyPairGenerator() {
    }

    public void initialize(int keysize, SecureRandom random) {
        if (256 != keysize) {
            throw new InvalidParameterException("SM2 keysize must be 256 bytes!");
        } else {
            this.random = random;
        }
    }

    public KeyPair generateKeyPair() {
        if (this.random == null) {
            this.random = new SecureRandom();
        }

        com.myzuji.sadk.org.bouncycastle.crypto.generators.SM2KeyPairGenerator generator = new com.myzuji.sadk.org.bouncycastle.crypto.generators.SM2KeyPairGenerator();
        generator.init(new ECKeyGenerationParameters(SM2Params.sm2DomainParameters, this.random));
        AsymmetricCipherKeyPair pair = generator.generateKeyPair();
        SM2PublicKey pubKey = new SM2PublicKey((ECPublicKeyParameters) pair.getPublic());
        SM2PrivateKey priKey = new SM2PrivateKey((ECPrivateKeyParameters) pair.getPrivate(), (ECPublicKeyParameters) pair.getPublic());
        return new KeyPair(pubKey, priKey);
    }
}
