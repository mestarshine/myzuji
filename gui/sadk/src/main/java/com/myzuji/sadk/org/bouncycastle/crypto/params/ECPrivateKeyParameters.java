package com.myzuji.sadk.org.bouncycastle.crypto.params;

import java.math.BigInteger;

public class ECPrivateKeyParameters extends ECKeyParameters {
    BigInteger d;

    public ECPrivateKeyParameters(BigInteger d, ECDomainParameters params) {
        super(true, params);
        this.d = d;
    }

    public BigInteger getD() {
        return this.d;
    }

    public int hashCode() {
        int prime = 0;
        int result = 1;
        result = 31 * result + (this.d == null ? 0 : this.d.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            ECPrivateKeyParameters other = (ECPrivateKeyParameters) obj;
            if (this.d == null) {
                if (other.d != null) {
                    return false;
                }
            } else if (!this.d.equals(other.d)) {
                return false;
            }

            return true;
        }
    }
}
