package com.myzuji.sadk.org.bouncycastle.crypto.params;

public class DHKeyParameters extends AsymmetricKeyParameter {
    private DHParameters params;

    protected DHKeyParameters(boolean isPrivate, DHParameters params) {
        super(isPrivate);
        this.params = params;
    }

    public DHParameters getParameters() {
        return this.params;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DHKeyParameters)) {
            return false;
        } else {
            DHKeyParameters dhKey = (DHKeyParameters) obj;
            if (this.params == null) {
                return dhKey.getParameters() == null;
            } else {
                return this.params.equals(dhKey.getParameters());
            }
        }
    }

    public int hashCode() {
        int code = this.isPrivate() ? 0 : 1;
        if (this.params != null) {
            code ^= this.params.hashCode();
        }

        return code;
    }
}
