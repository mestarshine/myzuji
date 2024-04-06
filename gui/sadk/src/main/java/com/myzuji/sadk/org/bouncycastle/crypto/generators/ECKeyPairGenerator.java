package com.myzuji.sadk.org.bouncycastle.crypto.generators;

import com.myzuji.sadk.org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import com.myzuji.sadk.org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import com.myzuji.sadk.org.bouncycastle.crypto.KeyGenerationParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ECDomainParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ECPublicKeyParameters;
import com.myzuji.sadk.org.bouncycastle.math.ec.*;

import java.math.BigInteger;
import java.security.SecureRandom;

public class ECKeyPairGenerator implements AsymmetricCipherKeyPairGenerator, ECConstants {
    ECDomainParameters params;
    SecureRandom random;

    public ECKeyPairGenerator() {
    }

    public void init(KeyGenerationParameters param) {
        ECKeyGenerationParameters ecP = (ECKeyGenerationParameters) param;
        this.random = ecP.getRandom();
        this.params = ecP.getDomainParameters();
        if (this.random == null) {
            this.random = new SecureRandom();
        }

    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        BigInteger n = this.params.getN();
        int nBitLength = n.bitLength();
        int minWeight = nBitLength >>> 2;

        BigInteger d;
        do {
            do {
                d = new BigInteger(nBitLength, this.random);
            } while (d.compareTo(TWO) < 0);
        } while (d.compareTo(n) >= 0 || WNafUtil.getNafWeight(d) < minWeight);

        ECPoint Q = this.createBasePointMultiplier().multiply(this.params.getG(), d);
        return new AsymmetricCipherKeyPair(new ECPublicKeyParameters(Q, this.params), new ECPrivateKeyParameters(d, this.params));
    }

    protected ECMultiplier createBasePointMultiplier() {
        return new FixedPointCombMultiplier();
    }
}
