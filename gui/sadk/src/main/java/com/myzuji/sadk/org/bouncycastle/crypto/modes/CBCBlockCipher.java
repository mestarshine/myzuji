package com.myzuji.sadk.org.bouncycastle.crypto.modes;

import com.myzuji.sadk.org.bouncycastle.crypto.BlockCipher;
import com.myzuji.sadk.org.bouncycastle.crypto.CipherParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.DataLengthException;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ParametersWithIV;
import com.myzuji.sadk.org.bouncycastle.util.Arrays;

public class CBCBlockCipher implements BlockCipher {
    private byte[] IV;
    private byte[] cbcV;
    private byte[] cbcNextV;
    private int blockSize;
    private BlockCipher cipher = null;
    private boolean encrypting;

    public CBCBlockCipher(BlockCipher cipher) {
        this.cipher = cipher;
        this.blockSize = cipher.getBlockSize();
        this.IV = new byte[this.blockSize];
        this.cbcV = new byte[this.blockSize];
        this.cbcNextV = new byte[this.blockSize];
    }

    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }

    public void init(boolean encrypting, CipherParameters params) throws IllegalArgumentException {
        boolean oldEncrypting = this.encrypting;
        this.encrypting = encrypting;
        if (params instanceof ParametersWithIV) {
            ParametersWithIV ivParam = (ParametersWithIV) params;
            byte[] iv = ivParam.getIV();
            if (iv.length != this.blockSize) {
                throw new IllegalArgumentException("initialisation vector must be the same length as block size");
            }

            System.arraycopy(iv, 0, this.IV, 0, iv.length);
            this.reset();
            if (ivParam.getParameters() != null) {
                this.cipher.init(encrypting, ivParam.getParameters());
            } else if (oldEncrypting != encrypting) {
                throw new IllegalArgumentException("cannot change encrypting state without providing key.");
            }
        } else {
            this.reset();
            if (params != null) {
                this.cipher.init(encrypting, params);
            } else if (oldEncrypting != encrypting) {
                throw new IllegalArgumentException("cannot change encrypting state without providing key.");
            }
        }

    }

    public String getAlgorithmName() {
        return this.cipher.getAlgorithmName() + "/CBC";
    }

    public int getBlockSize() {
        return this.cipher.getBlockSize();
    }

    public int processBlock(byte[] in, int inOff, byte[] out, int outOff) throws DataLengthException, IllegalStateException {
        return this.encrypting ? this.encryptBlock(in, inOff, out, outOff) : this.decryptBlock(in, inOff, out, outOff);
    }

    public void reset() {
        System.arraycopy(this.IV, 0, this.cbcV, 0, this.IV.length);
        Arrays.fill(this.cbcNextV, (byte) 0);
        this.cipher.reset();
    }

    private int encryptBlock(byte[] in, int inOff, byte[] out, int outOff) throws DataLengthException, IllegalStateException {
        if (inOff + this.blockSize > in.length) {
            throw new DataLengthException("input buffer too short");
        } else {
            int length;
            for (length = 0; length < this.blockSize; ++length) {
                byte[] var10000 = this.cbcV;
                var10000[length] ^= in[inOff + length];
            }

            length = this.cipher.processBlock(this.cbcV, 0, out, outOff);
            System.arraycopy(out, outOff, this.cbcV, 0, this.cbcV.length);
            return length;
        }
    }

    private int decryptBlock(byte[] in, int inOff, byte[] out, int outOff) throws DataLengthException, IllegalStateException {
        if (inOff + this.blockSize > in.length) {
            throw new DataLengthException("input buffer too short");
        } else {
            System.arraycopy(in, inOff, this.cbcNextV, 0, this.blockSize);
            int length = this.cipher.processBlock(in, inOff, out, outOff);

            for (int i = 0; i < this.blockSize; ++i) {
                out[outOff + i] ^= this.cbcV[i];
            }

            byte[] tmp = this.cbcV;
            this.cbcV = this.cbcNextV;
            this.cbcNextV = tmp;
            return length;
        }
    }
}
