package com.myzuji.sadk.org.bouncycastle.math.ec;

public class WTauNafPreCompInfo implements PreCompInfo {
    protected ECPoint.F2m[] preComp = null;

    public WTauNafPreCompInfo() {
    }

    public ECPoint.F2m[] getPreComp() {
        return this.preComp;
    }

    public void setPreComp(ECPoint.F2m[] preComp) {
        this.preComp = preComp;
    }
}
