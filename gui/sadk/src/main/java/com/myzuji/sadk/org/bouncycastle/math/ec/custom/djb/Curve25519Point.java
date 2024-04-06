package com.myzuji.sadk.org.bouncycastle.math.ec.custom.djb;

import com.myzuji.sadk.org.bouncycastle.math.ec.ECCurve;
import com.myzuji.sadk.org.bouncycastle.math.ec.ECFieldElement;
import com.myzuji.sadk.org.bouncycastle.math.ec.ECPoint;
import com.myzuji.sadk.org.bouncycastle.math.raw.Nat256;

public class Curve25519Point extends ECPoint.AbstractFp {

    public Curve25519Point(ECCurve curve, ECFieldElement x, ECFieldElement y) {
        this(curve, x, y, false);
    }


    public Curve25519Point(ECCurve curve, ECFieldElement x, ECFieldElement y, boolean withCompression) {
        super(curve, x, y);
        if (x == null != (y == null)) {
            throw new IllegalArgumentException("Exactly one of the field elements is null");
        } else {
            this.withCompression = withCompression;
        }
    }

    Curve25519Point(ECCurve curve, ECFieldElement x, ECFieldElement y, ECFieldElement[] zs, boolean withCompression) {
        super(curve, x, y, zs);
        this.withCompression = withCompression;
    }

    protected ECPoint detach() {
        return new Curve25519Point((ECCurve) null, this.getAffineXCoord(), this.getAffineYCoord());
    }

    public ECFieldElement getZCoord(int index) {
        return (ECFieldElement) (index == 1 ? this.getJacobianModifiedW() : super.getZCoord(index));
    }

    public ECPoint add(ECPoint b) {
        if (this.isInfinity()) {
            return b;
        } else if (b.isInfinity()) {
            return this;
        } else if (this == b) {
            return this.twice();
        } else {
            ECCurve curve = this.getCurve();
            Curve25519FieldElement X1 = (Curve25519FieldElement) this.x;
            Curve25519FieldElement Y1 = (Curve25519FieldElement) this.y;
            Curve25519FieldElement Z1 = (Curve25519FieldElement) this.zs[0];
            Curve25519FieldElement X2 = (Curve25519FieldElement) b.getXCoord();
            Curve25519FieldElement Y2 = (Curve25519FieldElement) b.getYCoord();
            Curve25519FieldElement Z2 = (Curve25519FieldElement) b.getZCoord(0);
            int[] tt1 = Nat256.createExt();
            int[] t2 = Nat256.create();
            int[] t3 = Nat256.create();
            int[] t4 = Nat256.create();
            boolean Z1IsOne = Z1.isOne();
            int[] U2;
            int[] S2;
            if (Z1IsOne) {
                U2 = X2.x;
                S2 = Y2.x;
            } else {
                S2 = t3;
                Curve25519Field.square(Z1.x, S2);
                U2 = t2;
                Curve25519Field.multiply(S2, X2.x, U2);
                Curve25519Field.multiply(S2, Z1.x, S2);
                Curve25519Field.multiply(S2, Y2.x, S2);
            }

            boolean Z2IsOne = Z2.isOne();
            int[] U1;
            int[] S1;
            if (Z2IsOne) {
                U1 = X1.x;
                S1 = Y1.x;
            } else {
                S1 = t4;
                Curve25519Field.square(Z2.x, S1);
                U1 = tt1;
                Curve25519Field.multiply(S1, X1.x, U1);
                Curve25519Field.multiply(S1, Z2.x, S1);
                Curve25519Field.multiply(S1, Y1.x, S1);
            }

            int[] H = Nat256.create();
            Curve25519Field.subtract(U1, U2, H);
            int[] R = t2;
            Curve25519Field.subtract(S1, S2, R);
            if (Nat256.isZero(H)) {
                return Nat256.isZero(R) ? this.twice() : curve.getInfinity();
            } else {
                int[] HSquared = Nat256.create();
                Curve25519Field.square(H, HSquared);
                int[] G = Nat256.create();
                Curve25519Field.multiply(HSquared, H, G);
                int[] V = t3;
                Curve25519Field.multiply(HSquared, U1, V);
                Curve25519Field.negate(G, G);
                Nat256.mul(S1, G, tt1);
                int c = Nat256.addBothTo(V, V, G);
                Curve25519Field.reduce27(c, G);
                Curve25519FieldElement X3 = new Curve25519FieldElement(t4);
                Curve25519Field.square(R, X3.x);
                Curve25519Field.subtract(X3.x, G, X3.x);
                Curve25519FieldElement Y3 = new Curve25519FieldElement(G);
                Curve25519Field.subtract(V, X3.x, Y3.x);
                Curve25519Field.multiplyAddToExt(Y3.x, R, tt1);
                Curve25519Field.reduce(tt1, Y3.x);
                Curve25519FieldElement Z3 = new Curve25519FieldElement(H);
                if (!Z1IsOne) {
                    Curve25519Field.multiply(Z3.x, Z1.x, Z3.x);
                }

                if (!Z2IsOne) {
                    Curve25519Field.multiply(Z3.x, Z2.x, Z3.x);
                }

                int[] Z3Squared = Z1IsOne && Z2IsOne ? HSquared : null;
                Curve25519FieldElement W3 = this.calculateJacobianModifiedW(Z3, Z3Squared);
                ECFieldElement[] zs = new ECFieldElement[]{Z3, W3};
                return new Curve25519Point(curve, X3, Y3, zs, this.withCompression);
            }
        }
    }

    public ECPoint twice() {
        if (this.isInfinity()) {
            return this;
        } else {
            ECCurve curve = this.getCurve();
            ECFieldElement Y1 = this.y;
            return (ECPoint) (Y1.isZero() ? curve.getInfinity() : this.twiceJacobianModified(true));
        }
    }

    public ECPoint twicePlus(ECPoint b) {
        if (this == b) {
            return this.threeTimes();
        } else if (this.isInfinity()) {
            return b;
        } else if (b.isInfinity()) {
            return this.twice();
        } else {
            ECFieldElement Y1 = this.y;
            return Y1.isZero() ? b : this.twiceJacobianModified(false).add(b);
        }
    }

    public ECPoint threeTimes() {
        if (this.isInfinity()) {
            return this;
        } else {
            ECFieldElement Y1 = this.y;
            return (ECPoint) (Y1.isZero() ? this : this.twiceJacobianModified(false).add(this));
        }
    }

    public ECPoint negate() {
        return this.isInfinity() ? this : new Curve25519Point(this.getCurve(), this.x, this.y.negate(), this.zs, this.withCompression);
    }

    protected Curve25519FieldElement calculateJacobianModifiedW(Curve25519FieldElement Z, int[] ZSquared) {
        Curve25519FieldElement a4 = (Curve25519FieldElement) this.getCurve().getA();
        if (Z.isOne()) {
            return a4;
        } else {
            Curve25519FieldElement W = new Curve25519FieldElement();
            if (ZSquared == null) {
                ZSquared = W.x;
                Curve25519Field.square(Z.x, ZSquared);
            }

            Curve25519Field.square(ZSquared, W.x);
            Curve25519Field.multiply(W.x, a4.x, W.x);
            return W;
        }
    }

    protected Curve25519FieldElement getJacobianModifiedW() {
        Curve25519FieldElement W = (Curve25519FieldElement) this.zs[1];
        if (W == null) {
            this.zs[1] = W = this.calculateJacobianModifiedW((Curve25519FieldElement) this.zs[0], (int[]) null);
        }

        return W;
    }

    protected Curve25519Point twiceJacobianModified(boolean calculateW) {
        Curve25519FieldElement X1 = (Curve25519FieldElement) this.x;
        Curve25519FieldElement Y1 = (Curve25519FieldElement) this.y;
        Curve25519FieldElement Z1 = (Curve25519FieldElement) this.zs[0];
        Curve25519FieldElement W1 = this.getJacobianModifiedW();
        int[] M = Nat256.create();
        Curve25519Field.square(X1.x, M);
        int c = Nat256.addBothTo(M, M, M);
        c += Nat256.addTo(W1.x, M);
        Curve25519Field.reduce27(c, M);
        int[] _2Y1 = Nat256.create();
        Curve25519Field.twice(Y1.x, _2Y1);
        int[] _2Y1Squared = Nat256.create();
        Curve25519Field.multiply(_2Y1, Y1.x, _2Y1Squared);
        int[] S = Nat256.create();
        Curve25519Field.multiply(_2Y1Squared, X1.x, S);
        Curve25519Field.twice(S, S);
        int[] _8T = Nat256.create();
        Curve25519Field.square(_2Y1Squared, _8T);
        Curve25519Field.twice(_8T, _8T);
        Curve25519FieldElement X3 = new Curve25519FieldElement(_2Y1Squared);
        Curve25519Field.square(M, X3.x);
        Curve25519Field.subtract(X3.x, S, X3.x);
        Curve25519Field.subtract(X3.x, S, X3.x);
        Curve25519FieldElement Y3 = new Curve25519FieldElement(S);
        Curve25519Field.subtract(S, X3.x, Y3.x);
        Curve25519Field.multiply(Y3.x, M, Y3.x);
        Curve25519Field.subtract(Y3.x, _8T, Y3.x);
        Curve25519FieldElement Z3 = new Curve25519FieldElement(_2Y1);
        if (!Nat256.isOne(Z1.x)) {
            Curve25519Field.multiply(Z3.x, Z1.x, Z3.x);
        }

        Curve25519FieldElement W3 = null;
        if (calculateW) {
            W3 = new Curve25519FieldElement(_8T);
            Curve25519Field.multiply(W3.x, W1.x, W3.x);
            Curve25519Field.twice(W3.x, W3.x);
        }

        return new Curve25519Point(this.getCurve(), X3, Y3, new ECFieldElement[]{Z3, W3}, this.withCompression);
    }
}
