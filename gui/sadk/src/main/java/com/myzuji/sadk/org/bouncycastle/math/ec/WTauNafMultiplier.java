package com.myzuji.sadk.org.bouncycastle.math.ec;

import java.math.BigInteger;

public class WTauNafMultiplier extends AbstractECMultiplier {
    static final String PRECOMP_NAME = "bc_wtnaf";

    public WTauNafMultiplier() {
    }

    protected ECPoint multiplyPositive(ECPoint point, BigInteger k) {
        if (!(point instanceof ECPoint.F2m)) {
            throw new IllegalArgumentException("Only ECPoint.F2m can be used in WTauNafMultiplier");
        } else {
            ECPoint.F2m p = (ECPoint.F2m) point;
            ECCurve.F2m curve = (ECCurve.F2m) p.getCurve();
            int m = curve.getM();
            byte a = curve.getA().toBigInteger().byteValue();
            byte mu = curve.getMu();
            BigInteger[] s = curve.getSi();
            ZTauElement rho = Tnaf.partModReduction(k, m, a, s, mu, (byte) 10);
            return this.multiplyWTnaf(p, rho, curve.getPreCompInfo(p, "bc_wtnaf"), a, mu);
        }
    }

    private ECPoint.F2m multiplyWTnaf(ECPoint.F2m p, ZTauElement lambda, PreCompInfo preCompInfo, byte a, byte mu) {
        ZTauElement[] alpha = a == 0 ? Tnaf.alpha0 : Tnaf.alpha1;
        BigInteger tw = Tnaf.getTw(mu, 4);
        byte[] u = Tnaf.tauAdicWNaf(mu, lambda, (byte) 4, BigInteger.valueOf(16L), tw, alpha);
        return multiplyFromWTnaf(p, u, preCompInfo);
    }

    private static ECPoint.F2m multiplyFromWTnaf(ECPoint.F2m p, byte[] u, PreCompInfo preCompInfo) {
        ECCurve.F2m curve = (ECCurve.F2m) p.getCurve();
        byte a = curve.getA().toBigInteger().byteValue();
        ECPoint.F2m[] pu;
        if (preCompInfo != null && preCompInfo instanceof WTauNafPreCompInfo) {
            pu = ((WTauNafPreCompInfo) preCompInfo).getPreComp();
        } else {
            pu = Tnaf.getPreComp(p, a);
            WTauNafPreCompInfo pre = new WTauNafPreCompInfo();
            pre.setPreComp(pu);
            curve.setPreCompInfo(p, "bc_wtnaf", pre);
        }

        ECPoint.F2m q = (ECPoint.F2m) p.getCurve().getInfinity();

        for (int i = u.length - 1; i >= 0; --i) {
            q = Tnaf.tau(q);
            byte ui = u[i];
            if (ui != 0) {
                if (ui > 0) {
                    q = q.addSimple(pu[ui]);
                } else {
                    q = q.subtractSimple(pu[-ui]);
                }
            }
        }

        return q;
    }
}
