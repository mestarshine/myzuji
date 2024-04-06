package com.myzuji.sadk.org.bouncycastle.crypto.encodings;

import com.myzuji.sadk.org.bouncycastle.crypto.AsymmetricBlockCipher;
import com.myzuji.sadk.org.bouncycastle.crypto.CipherParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.InvalidCipherTextException;
import com.myzuji.sadk.org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ParametersWithRandom;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.SecureRandom;

public class PKCS1Encoding implements AsymmetricBlockCipher {
    public static final String STRICT_LENGTH_ENABLED_PROPERTY = "com.myzuji.sadk.org.bouncycastle.pkcs1.strict";
    private static final int HEADER_LENGTH = 10;
    private SecureRandom random;
    private AsymmetricBlockCipher engine;
    private boolean forEncryption;
    private boolean forPrivateKey;
    private boolean useStrictLength;
    private int pLen = -1;
    private byte[] fallback = null;

    public PKCS1Encoding(AsymmetricBlockCipher cipher) {
        this.engine = cipher;
        this.useStrictLength = this.useStrict();
    }

    public PKCS1Encoding(AsymmetricBlockCipher cipher, int pLen) {
        this.engine = cipher;
        this.useStrictLength = this.useStrict();
        this.pLen = pLen;
    }

    public PKCS1Encoding(AsymmetricBlockCipher cipher, byte[] fallback) {
        this.engine = cipher;
        this.useStrictLength = this.useStrict();
        this.fallback = fallback;
        this.pLen = fallback.length;
    }

    private boolean useStrict() {
        return false;
    }

    public AsymmetricBlockCipher getUnderlyingCipher() {
        return this.engine;
    }

    public void init(boolean forEncryption, CipherParameters param) {
        AsymmetricKeyParameter kParam;
        if (param instanceof ParametersWithRandom) {
            ParametersWithRandom rParam = (ParametersWithRandom) param;
            this.random = rParam.getRandom();
            kParam = (AsymmetricKeyParameter) rParam.getParameters();
        } else {
            this.random = new SecureRandom();
            kParam = (AsymmetricKeyParameter) param;
        }

        this.engine.init(forEncryption, param);
        this.forPrivateKey = kParam.isPrivate();
        this.forEncryption = forEncryption;
    }

    public int getInputBlockSize() {
        int baseBlockSize = this.engine.getInputBlockSize();
        return this.forEncryption ? baseBlockSize - 10 : baseBlockSize;
    }

    public int getOutputBlockSize() {
        int baseBlockSize = this.engine.getOutputBlockSize();
        return this.forEncryption ? baseBlockSize : baseBlockSize - 10;
    }

    public byte[] processBlock(byte[] in, int inOff, int inLen) throws InvalidCipherTextException {
        return this.forEncryption ? this.encodeBlock(in, inOff, inLen) : this.decodeBlock(in, inOff, inLen);
    }

    private byte[] encodeBlock(byte[] in, int inOff, int inLen) throws InvalidCipherTextException {
        if (inLen > this.getInputBlockSize()) {
            throw new IllegalArgumentException("input data too large");
        } else {
            byte[] block = new byte[this.engine.getInputBlockSize()];
            int i;
            if (this.forPrivateKey) {
                block[0] = 1;

                for (i = 1; i != block.length - inLen - 1; ++i) {
                    block[i] = -1;
                }
            } else {
                this.random.nextBytes(block);
                block[0] = 2;

                for (i = 1; i != block.length - inLen - 1; ++i) {
                    while (block[i] == 0) {
                        block[i] = (byte) this.random.nextInt();
                    }
                }
            }

            block[block.length - inLen - 1] = 0;
            System.arraycopy(in, inOff, block, block.length - inLen, inLen);
            return this.engine.processBlock(block, 0, block.length);
        }
    }

    private static int checkPkcs1Encoding(byte[] encoded, int pLen) {
        int correct = 0;
        correct |= encoded[0] ^ 2;
        int plen = encoded.length - (pLen + 1);

        for (int i = 1; i < plen; ++i) {
            int tmp = encoded[i];
            tmp |= tmp >> 1;
            tmp |= tmp >> 2;
            tmp |= tmp >> 4;
            correct |= (tmp & 1) - 1;
        }

        correct |= encoded[encoded.length - (pLen + 1)];
        correct |= correct >> 1;
        correct |= correct >> 2;
        correct |= correct >> 4;
        return ~((correct & 1) - 1);
    }

    private byte[] decodeBlockOrRandom(byte[] in, int inOff, int inLen) throws InvalidCipherTextException {
        if (!this.forPrivateKey) {
            throw new InvalidCipherTextException("sorry, this method is only for decryption, not for signing");
        } else {
            byte[] block = this.engine.processBlock(in, inOff, inLen);
            byte[] random = null;
            if (this.fallback == null) {
                random = new byte[this.pLen];
                this.random.nextBytes(random);
            } else {
                random = this.fallback;
            }

            if (block.length < this.getOutputBlockSize()) {
                throw new InvalidCipherTextException("block truncated");
            } else if (this.useStrictLength && block.length != this.engine.getOutputBlockSize()) {
                throw new InvalidCipherTextException("block incorrect size");
            } else {
                int correct = checkPkcs1Encoding(block, this.pLen);
                byte[] result = new byte[this.pLen];

                for (int i = 0; i < this.pLen; ++i) {
                    result[i] = (byte) (block[i + (block.length - this.pLen)] & ~correct | random[i] & correct);
                }

                return result;
            }
        }
    }

    private byte[] decodeBlock(byte[] in, int inOff, int inLen) throws InvalidCipherTextException {
        if (this.pLen != -1) {
            return this.decodeBlockOrRandom(in, inOff, inLen);
        } else {
            byte[] block = this.engine.processBlock(in, inOff, inLen);
            if (block.length < this.getOutputBlockSize()) {
                throw new InvalidCipherTextException("block truncated");
            } else {
                byte type = block[0];
                if (this.forPrivateKey) {
                    if (type != 2) {
                        throw new InvalidCipherTextException("unknown block type");
                    }
                } else if (type != 1) {
                    throw new InvalidCipherTextException("unknown block type");
                }

                if (this.useStrictLength && block.length != this.engine.getOutputBlockSize()) {
                    throw new InvalidCipherTextException("block incorrect size");
                } else {
                    int start;
                    for (start = 1; start != block.length; ++start) {
                        byte pad = block[start];
                        if (pad == 0) {
                            break;
                        }

                        if (type == 1 && pad != -1) {
                            throw new InvalidCipherTextException("block padding incorrect");
                        }
                    }

                    ++start;
                    if (start <= block.length && start >= 10) {
                        byte[] result = new byte[block.length - start];
                        System.arraycopy(block, start, result, 0, result.length);
                        return result;
                    } else {
                        throw new InvalidCipherTextException("no data in block");
                    }
                }
            }
        }
    }
}
