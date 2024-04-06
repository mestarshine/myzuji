package com.myzuji.sadk.org.bouncycastle.crypto.params;

import com.myzuji.sadk.org.bouncycastle.util.Arrays;

public class DSAValidationParameters {
    private int usageIndex;
    private byte[] seed;
    private int counter;

    public DSAValidationParameters(byte[] seed, int counter) {
        this(seed, counter, -1);
    }

    public DSAValidationParameters(byte[] seed, int counter, int usageIndex) {
        this.seed = seed;
        this.counter = counter;
        this.usageIndex = usageIndex;
    }

    public int getCounter() {
        return this.counter;
    }

    public byte[] getSeed() {
        return this.seed;
    }

    public int getUsageIndex() {
        return this.usageIndex;
    }

    public int hashCode() {
        return this.counter ^ Arrays.hashCode(this.seed);
    }

    public boolean equals(Object o) {
        if (!(o instanceof DSAValidationParameters)) {
            return false;
        } else {
            DSAValidationParameters other = (DSAValidationParameters) o;
            return other.counter != this.counter ? false : Arrays.areEqual(this.seed, other.seed);
        }
    }
}
