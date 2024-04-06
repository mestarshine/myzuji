package com.myzuji.sadk.org.bouncycastle.crypto.engines;

import com.myzuji.sadk.org.bouncycastle.crypto.CipherParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.DataLengthException;
import com.myzuji.sadk.org.bouncycastle.crypto.OutputLengthException;
import com.myzuji.sadk.org.bouncycastle.crypto.params.KeyParameter;

public class DESedeEngine extends DESEngine {
    protected static final int BLOCK_SIZE = 8;
    private int[] workingKey1 = null;
    private int[] workingKey2 = null;
    private int[] workingKey3 = null;
    private boolean forEncryption;

    public DESedeEngine() {
    }

    public void init(boolean encrypting, CipherParameters params) {
        if (!(params instanceof KeyParameter)) {
            throw new IllegalArgumentException("invalid parameter passed to DESede init - " + params.getClass().getName());
        } else {
            byte[] keyMaster = ((KeyParameter) params).getKey();
            if (keyMaster.length != 24 && keyMaster.length != 16) {
                throw new IllegalArgumentException("key size must be 16 or 24 bytes.");
            } else {
                this.forEncryption = encrypting;
                byte[] key1 = new byte[8];
                System.arraycopy(keyMaster, 0, key1, 0, key1.length);
                this.workingKey1 = this.generateWorkingKey(encrypting, key1);
                byte[] key2 = new byte[8];
                System.arraycopy(keyMaster, 8, key2, 0, key2.length);
                this.workingKey2 = this.generateWorkingKey(!encrypting, key2);
                if (keyMaster.length == 24) {
                    byte[] key3 = new byte[8];
                    System.arraycopy(keyMaster, 16, key3, 0, key3.length);
                    this.workingKey3 = this.generateWorkingKey(encrypting, key3);
                } else {
                    this.workingKey3 = this.workingKey1;
                }

            }
        }
    }

    public String getAlgorithmName() {
        return "DESede";
    }

    public int getBlockSize() {
        return 8;
    }

    public int processBlock(byte[] in, int inOff, byte[] out, int outOff) {
        if (this.workingKey1 == null) {
            throw new IllegalStateException("DESede engine not initialised");
        } else if (inOff + 8 > in.length) {
            throw new DataLengthException("input buffer too short");
        } else if (outOff + 8 > out.length) {
            throw new OutputLengthException("output buffer too short");
        } else {
            byte[] temp = new byte[8];
            if (this.forEncryption) {
                this.desFunc(this.workingKey1, in, inOff, temp, 0);
                this.desFunc(this.workingKey2, temp, 0, temp, 0);
                this.desFunc(this.workingKey3, temp, 0, out, outOff);
            } else {
                this.desFunc(this.workingKey3, in, inOff, temp, 0);
                this.desFunc(this.workingKey2, temp, 0, temp, 0);
                this.desFunc(this.workingKey1, temp, 0, out, outOff);
            }

            return 8;
        }
    }

    public void reset() {
    }
}
