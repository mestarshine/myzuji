package com.myzuji.sadk.org.bouncycastle.math.field;

import java.math.BigInteger;

public class PrimeField implements FiniteField {
    protected final BigInteger characteristic;

    PrimeField(BigInteger characteristic) {
        this.characteristic = characteristic;
    }

    public BigInteger getCharacteristic() {
        return this.characteristic;
    }

    public int getDimension() {
        return 1;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof PrimeField)) {
            return false;
        } else {
            PrimeField other = (PrimeField) obj;
            return this.characteristic.equals(other.characteristic);
        }
    }

    public int hashCode() {
        return this.characteristic.hashCode();
    }
}

