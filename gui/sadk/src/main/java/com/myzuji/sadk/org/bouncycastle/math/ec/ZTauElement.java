package com.myzuji.sadk.org.bouncycastle.math.ec;

import java.math.BigInteger;

public class ZTauElement {
    public final BigInteger u;
    public final BigInteger v;

    public ZTauElement(BigInteger u, BigInteger v) {
        this.u = u;
        this.v = v;
    }
}
