package com.myzuji.sadk.org.bouncycastle.math.field;

import com.myzuji.sadk.org.bouncycastle.util.Arrays;

public class GF2Polynomial implements Polynomial {
    protected final int[] exponents;

    GF2Polynomial(int[] exponents) {
        this.exponents = Arrays.clone(exponents);
    }

    public int getDegree() {
        return this.exponents[this.exponents.length - 1];
    }

    public int[] getExponentsPresent() {
        return Arrays.clone(this.exponents);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof GF2Polynomial)) {
            return false;
        } else {
            GF2Polynomial other = (GF2Polynomial) obj;
            return Arrays.areEqual(this.exponents, other.exponents);
        }
    }

    public int hashCode() {
        return Arrays.hashCode(this.exponents);
    }
}

