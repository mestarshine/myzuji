package com.myzuji.sadk.org.bouncycastle.crypto.engines;

import com.myzuji.sadk.org.bouncycastle.crypto.AsymmetricBlockCipher;
import com.myzuji.sadk.org.bouncycastle.crypto.CipherParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ParametersWithRandom;
import com.myzuji.sadk.org.bouncycastle.crypto.params.RSAKeyParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import com.myzuji.sadk.org.bouncycastle.util.BigIntegers;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RSABlindedEngine implements AsymmetricBlockCipher {
    private static final BigInteger ONE = BigInteger.valueOf(1L);
    private RSACoreEngine core = new RSACoreEngine();
    private RSAKeyParameters key;
    private SecureRandom random;

    public RSABlindedEngine() {
    }

    public void init(boolean forEncryption, CipherParameters param) {
        this.core.init(forEncryption, param);
        if (param instanceof ParametersWithRandom) {
            ParametersWithRandom rParam = (ParametersWithRandom) param;
            this.key = (RSAKeyParameters) rParam.getParameters();
            this.random = rParam.getRandom();
        } else {
            this.key = (RSAKeyParameters) param;
            this.random = new SecureRandom();
        }

    }

    public int getInputBlockSize() {
        return this.core.getInputBlockSize();
    }

    public int getOutputBlockSize() {
        return this.core.getOutputBlockSize();
    }

    public byte[] processBlock(byte[] in, int inOff, int inLen) {
        if (this.key == null) {
            throw new IllegalStateException("RSA engine not initialised");
        } else {
            BigInteger input = this.core.convertInput(in, inOff, inLen);
            BigInteger result;
            if (this.key instanceof RSAPrivateCrtKeyParameters) {
                RSAPrivateCrtKeyParameters k = (RSAPrivateCrtKeyParameters) this.key;
                BigInteger e = k.getPublicExponent();
                if (e != null) {
                    BigInteger m = k.getModulus();
                    BigInteger r = BigIntegers.createRandomInRange(ONE, m.subtract(ONE), this.random);
                    BigInteger blindedInput = r.modPow(e, m).multiply(input).mod(m);
                    BigInteger blindedResult = this.core.processBlock(blindedInput);
                    BigInteger rInv = r.modInverse(m);
                    result = blindedResult.multiply(rInv).mod(m);
                } else {
                    result = this.core.processBlock(input);
                }
            } else {
                result = this.core.processBlock(input);
            }

            return this.core.convertOutput(result);
        }
    }
}
