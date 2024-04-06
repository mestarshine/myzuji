package com.myzuji.sadk.org.bouncycastle.crypto.generators;


import com.myzuji.sadk.org.bouncycastle.crypto.CipherKeyGenerator;
import com.myzuji.sadk.org.bouncycastle.crypto.KeyGenerationParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.params.DESParameters;

public class DESKeyGenerator extends CipherKeyGenerator {
    public DESKeyGenerator() {
    }

    public void init(KeyGenerationParameters param) {
        super.init(param);
        if (this.strength != 0 && this.strength != 7) {
            if (this.strength != 8) {
                throw new IllegalArgumentException("DES key must be 64 bits long.");
            }
        } else {
            this.strength = 8;
        }

    }

    public byte[] generateKey() {
        byte[] newKey = new byte[8];

        do {
            this.random.nextBytes(newKey);
            DESParameters.setOddParity(newKey);
        } while (DESParameters.isWeakKey(newKey, 0));

        return newKey;
    }
}
