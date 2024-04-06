package com.myzuji.sadk.org.bouncycastle.crypto.params;

public class ElGamalKeyParameters extends AsymmetricKeyParameter {
    private ElGamalParameters params;

    protected ElGamalKeyParameters(boolean isPrivate, ElGamalParameters params) {
        super(isPrivate);
        this.params = params;
    }

    public ElGamalParameters getParameters() {
        return this.params;
    }

    public int hashCode() {
        return this.params != null ? this.params.hashCode() : 0;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ElGamalKeyParameters)) {
            return false;
        } else {
            ElGamalKeyParameters dhKey = (ElGamalKeyParameters) obj;
            if (this.params == null) {
                return dhKey.getParameters() == null;
            } else {
                return this.params.equals(dhKey.getParameters());
            }
        }
    }
}
