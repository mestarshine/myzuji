package com.myzuji.sadk.org.bouncycastle.crypto.generators;

import com.myzuji.sadk.org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import com.myzuji.sadk.org.bouncycastle.crypto.KeyGenerationParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ECDomainParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ECPublicKeyParameters;
import com.myzuji.sadk.org.bouncycastle.math.ec.ECPoint;
import com.myzuji.sadk.org.bouncycastle.math.ec.FixedPointCombMultiplier;
import com.myzuji.sadk.org.bouncycastle.math.ec.WNafUtil;

import java.math.BigInteger;
import java.security.SecureRandom;

public class SM2KeyPairGenerator extends ECKeyPairGenerator {
    private final int mBitlength = 249;
    private ECDomainParameters params;
    private SecureRandom random;

    public SM2KeyPairGenerator() {
    }

    public final void init(KeyGenerationParameters param) {
        ECKeyGenerationParameters ecP = (ECKeyGenerationParameters) param;
        this.random = ecP.getRandom();
        this.params = ecP.getDomainParameters();
        if (this.random == null) {
            this.random = new SecureRandom();
        }

    }

    public final AsymmetricCipherKeyPair generateKeyPair() {
        FixedPointCombMultiplier multiplier = new FixedPointCombMultiplier();
        BigInteger n = this.params.getN();
        ECPoint G = this.params.getG();
        int nBitLength = n.bitLength();
        int minWeight = nBitLength >>> 2;
        ECPoint Q = null;

        while (true) {
            while (true) {
                BigInteger d = new BigInteger(nBitLength, this.random);
                if (d.bitLength() >= 249 && d.compareTo(TWO) >= 0 && d.compareTo(n) <= 0 && WNafUtil.getNafWeight(d) >= minWeight) {
                    Q = multiplier.multiply(G, d).normalize();
                    if (Q.getXCoord().bitLength() >= 249 && Q.getYCoord().bitLength() >= 249) {
                        return new AsymmetricCipherKeyPair(new ECPublicKeyParameters(Q, this.params), new ECPrivateKeyParameters(d, this.params));
                    }
                }
            }
        }
    }
}
