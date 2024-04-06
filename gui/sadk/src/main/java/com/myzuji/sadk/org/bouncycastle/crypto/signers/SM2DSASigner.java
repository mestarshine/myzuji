package com.myzuji.sadk.org.bouncycastle.crypto.signers;

import com.myzuji.sadk.org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import com.myzuji.sadk.org.bouncycastle.crypto.CipherParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.DSA;
import com.myzuji.sadk.org.bouncycastle.crypto.generators.SM2KeyPairGenerator;
import com.myzuji.sadk.org.bouncycastle.crypto.params.*;
import com.myzuji.sadk.org.bouncycastle.jcajce.provider.asymmetric.sm.SM2Params;
import com.myzuji.sadk.org.bouncycastle.math.ec.ECConstants;
import com.myzuji.sadk.org.bouncycastle.math.ec.ECMultiplier;
import com.myzuji.sadk.org.bouncycastle.math.ec.ECPoint;
import com.myzuji.sadk.org.bouncycastle.math.ec.FixedPointCombMultiplier;

import java.math.BigInteger;
import java.security.SecureRandom;

public final class SM2DSASigner implements ECConstants, DSA {
    private ECKeyParameters key;
    private SM2KeyPairGenerator generator;

    public SM2DSASigner() {
    }

    public void initSign(BigInteger d, SecureRandom random) {
        if (d == null) {
            throw new SecurityException("null not allowed for d");
        } else {
            if (random == null) {
                random = new SecureRandom();
            }

            this.key = new ECPrivateKeyParameters(d, SM2Params.sm2DomainParameters);
            this.generator = new SM2KeyPairGenerator();
            this.generator.init(new ECKeyGenerationParameters(this.key.getParameters(), random));
        }
    }

    public void initVerify(ECPoint point) {
        if (point == null) {
            throw new SecurityException("null not allowed for point");
        } else {
            this.key = new ECPublicKeyParameters(point, SM2Params.sm2DomainParameters);
        }
    }

    public void init(boolean forSigning, CipherParameters param) {
        if (param == null) {
            throw new SecurityException("null not allowed for param");
        } else {
            if (forSigning) {
                SecureRandom random = null;
                if (param instanceof ParametersWithRandom) {
                    ParametersWithRandom rParam = (ParametersWithRandom) param;
                    this.key = (ECPrivateKeyParameters) rParam.getParameters();
                    random = rParam.getRandom();
                } else {
                    this.key = (ECPrivateKeyParameters) param;
                }

                if (random == null) {
                    random = new SecureRandom();
                }

                this.generator = new SM2KeyPairGenerator();
                this.generator.init(new ECKeyGenerationParameters(this.key.getParameters(), random));
            } else {
                this.key = (ECPublicKeyParameters) param;
            }

        }
    }

    public BigInteger[] generateSignature(byte[] hashMessage) {
        if (hashMessage == null) {
            throw new SecurityException("null not allowed for message");
        } else if (this.key == null) {
            throw new SecurityException("not Initialization");
        } else if (!(this.key instanceof ECPrivateKeyParameters)) {
            throw new SecurityException("key not ECPrivateKeyParameters");
        } else {
            ECDomainParameters ec = this.key.getParameters();
            BigInteger n = ec.getN();
            BigInteger e = this.calculateE(n, hashMessage);
            BigInteger d = ((ECPrivateKeyParameters) this.key).getD();
            ECPoint Q = null;
            AsymmetricCipherKeyPair keypair = null;
            BigInteger k = null;
            BigInteger x1 = null;

            while (true) {
                BigInteger r;
                do {
                    keypair = this.generator.generateKeyPair();
                    k = ((ECPrivateKeyParameters) keypair.getPrivate()).getD();
                    Q = ((ECPublicKeyParameters) keypair.getPublic()).getQ();
                    x1 = Q.normalize().getXCoord().toBigInteger();
                    r = e.add(x1).mod(n);
                } while (r.equals(ZERO));

                if (!r.add(k).equals(n)) {
                    BigInteger s = ONE.add(d).modInverse(n).multiply(k.subtract(r.multiply(d)).mod(n)).mod(n);
                    if (!s.equals(ZERO)) {
                        return new BigInteger[]{r, s};
                    }
                }
            }
        }
    }

    public boolean verifySignature(byte[] message, BigInteger r, BigInteger s) {
        if (message == null) {
            throw new SecurityException("null not allowed for message");
        } else if (r != null && s != null) {
            if (this.key == null) {
                throw new SecurityException("not Initialization");
            } else if (!(this.key instanceof ECPublicKeyParameters)) {
                throw new SecurityException("key not ECPublicKeyParameters");
            } else {
                ECDomainParameters ec = this.key.getParameters();
                BigInteger n = ec.getN();
                if (r.compareTo(ONE) > 0 && r.compareTo(n) <= 0) {
                    if (s.compareTo(ONE) > 0 && s.compareTo(n) <= 0) {
                        BigInteger e = this.calculateE(n, message);
                        BigInteger t = r.add(s).mod(n);
                        if (t.equals(ZERO)) {
                            return false;
                        } else {
                            ECPoint P = ((ECPublicKeyParameters) this.key).getQ();
                            ECPoint point = ec.getG().multiply(s).add(P.multiply(t));
                            if (point.isInfinity()) {
                                return false;
                            } else {
                                BigInteger v = e.add(point.normalize().getXCoord().toBigInteger()).mod(n);
                                return v.equals(r);
                            }
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } else {
            throw new SecurityException("null not allowed for r/s");
        }
    }

    protected BigInteger calculateE(BigInteger n, byte[] message) {
        BigInteger e = new BigInteger(1, message);
        int messageBitLength = message.length * 8;
        int log2n = n.bitLength();
        if (log2n < messageBitLength) {
            e = e.shiftRight(messageBitLength - log2n);
        }

        return e;
    }

    protected ECMultiplier createBasePointMultiplier() {
        return new FixedPointCombMultiplier();
    }

    protected SecureRandom initSecureRandom(boolean needed, SecureRandom provided) {
        return !needed ? null : (provided != null ? provided : new SecureRandom());
    }
}
