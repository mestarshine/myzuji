package com.myzuji.sadk.org.bouncycastle.crypto.params;

import com.myzuji.sadk.org.bouncycastle.util.Arrays;

public class DHValidationParameters {
    private byte[] seed;
    private int counter;

    public DHValidationParameters(byte[] seed, int counter) {
        this.seed = seed;
        this.counter = counter;
    }

    public int getCounter() {
        return this.counter;
    }

    public byte[] getSeed() {
        return this.seed;
    }

    public boolean equals(Object o) {
        if (!(o instanceof DHValidationParameters)) {
            return false;
        } else {
            DHValidationParameters other = (DHValidationParameters) o;
            return other.counter != this.counter ? false : Arrays.areEqual(this.seed, other.seed);
        }
    }

    public int hashCode() {
        return this.counter ^ Arrays.hashCode(this.seed);
    }
}

