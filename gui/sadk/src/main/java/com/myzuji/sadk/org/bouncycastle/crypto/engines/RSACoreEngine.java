package com.myzuji.sadk.org.bouncycastle.crypto.engines;

import com.myzuji.sadk.org.bouncycastle.crypto.CipherParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.DataLengthException;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ParametersWithRandom;
import com.myzuji.sadk.org.bouncycastle.crypto.params.RSAKeyParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;

import java.math.BigInteger;

public class RSACoreEngine {
    private RSAKeyParameters key;
    private boolean forEncryption;

    RSACoreEngine() {
    }

    public void init(boolean forEncryption, CipherParameters param) {
        if (param instanceof ParametersWithRandom) {
            ParametersWithRandom rParam = (ParametersWithRandom) param;
            this.key = (RSAKeyParameters) rParam.getParameters();
        } else {
            this.key = (RSAKeyParameters) param;
        }

        this.forEncryption = forEncryption;
    }

    public int getInputBlockSize() {
        int bitSize = this.key.getModulus().bitLength();
        return this.forEncryption ? (bitSize + 7) / 8 - 1 : (bitSize + 7) / 8;
    }

    public int getOutputBlockSize() {
        int bitSize = this.key.getModulus().bitLength();
        return this.forEncryption ? (bitSize + 7) / 8 : (bitSize + 7) / 8 - 1;
    }

    public BigInteger convertInput(byte[] in, int inOff, int inLen) {
        if (inLen > this.getInputBlockSize() + 1) {
            throw new DataLengthException("input too large for RSA cipher.");
        } else if (inLen == this.getInputBlockSize() + 1 && !this.forEncryption) {
            throw new DataLengthException("input too large for RSA cipher.");
        } else {
            byte[] block;
            if (inOff == 0 && inLen == in.length) {
                block = in;
            } else {
                block = new byte[inLen];
                System.arraycopy(in, inOff, block, 0, inLen);
            }

            BigInteger res = new BigInteger(1, block);
            if (res.compareTo(this.key.getModulus()) >= 0) {
                throw new DataLengthException("input too large for RSA cipher.");
            } else {
                return res;
            }
        }
    }

    public byte[] convertOutput(BigInteger result) {
        byte[] output = result.toByteArray();
        byte[] tmp;
        if (this.forEncryption) {
            if (output[0] == 0 && output.length > this.getOutputBlockSize()) {
                tmp = new byte[output.length - 1];
                System.arraycopy(output, 1, tmp, 0, tmp.length);
                return tmp;
            }

            if (output.length < this.getOutputBlockSize()) {
                tmp = new byte[this.getOutputBlockSize()];
                System.arraycopy(output, 0, tmp, tmp.length - output.length, output.length);
                return tmp;
            }
        } else if (output[0] == 0) {
            tmp = new byte[output.length - 1];
            System.arraycopy(output, 1, tmp, 0, tmp.length);
            return tmp;
        }

        return output;
    }

    public BigInteger processBlock(BigInteger input) {
        if (this.key instanceof RSAPrivateCrtKeyParameters) {
            RSAPrivateCrtKeyParameters crtKey = (RSAPrivateCrtKeyParameters) this.key;
            BigInteger p = crtKey.getP();
            BigInteger q = crtKey.getQ();
            BigInteger dP = crtKey.getDP();
            BigInteger dQ = crtKey.getDQ();
            BigInteger qInv = crtKey.getQInv();
            BigInteger mP = input.remainder(p).modPow(dP, p);
            BigInteger mQ = input.remainder(q).modPow(dQ, q);
            BigInteger h = mP.subtract(mQ);
            h = h.multiply(qInv);
            h = h.mod(p);
            BigInteger m = h.multiply(q);
            m = m.add(mQ);
            return m;
        } else {
            return input.modPow(this.key.getExponent(), this.key.getModulus());
        }
    }
}
