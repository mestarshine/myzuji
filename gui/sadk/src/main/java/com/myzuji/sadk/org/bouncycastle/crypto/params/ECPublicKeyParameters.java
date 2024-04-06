package com.myzuji.sadk.org.bouncycastle.crypto.params;

import com.myzuji.sadk.org.bouncycastle.math.ec.ECPoint;

public class ECPublicKeyParameters extends ECKeyParameters {
    ECPoint Q;

    public ECPublicKeyParameters(ECPoint Q, ECDomainParameters params) {
        super(false, params);
        this.Q = Q.normalize();
    }

    public ECPoint getQ() {
        return this.Q;
    }
}
