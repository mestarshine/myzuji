package com.myzuji.sadk.org.bouncycastle.math.ec;

import java.math.BigInteger;
import java.util.Hashtable;

public abstract class ECPoint {
    protected static ECFieldElement[] EMPTY_ZS = new ECFieldElement[0];
    protected ECCurve curve;
    protected ECFieldElement x;
    protected ECFieldElement y;
    protected ECFieldElement[] zs;
    protected boolean withCompression;
    protected Hashtable preCompTable;

    protected static ECFieldElement[] getInitialZCoords(ECCurve curve) {
        int coord = null == curve ? 0 : curve.getCoordinateSystem();
        switch (coord) {
            case 0:
            case 5:
                return EMPTY_ZS;
            default:
                ECFieldElement one = curve.fromBigInteger(ECConstants.ONE);
                switch (coord) {
                    case 1:
                    case 2:
                    case 6:
                        return new ECFieldElement[]{one};
                    case 3:
                        return new ECFieldElement[]{one, one, one};
                    case 4:
                        return new ECFieldElement[]{one, curve.getA()};
                    case 5:
                    default:
                        throw new IllegalArgumentException("unknown coordinate system");
                }
        }
    }

    protected ECPoint(ECCurve curve, ECFieldElement x, ECFieldElement y) {
        this(curve, x, y, getInitialZCoords(curve));
    }

    protected ECPoint(ECCurve curve, ECFieldElement x, ECFieldElement y, ECFieldElement[] zs) {
        this.preCompTable = null;
        this.curve = curve;
        this.x = x;
        this.y = y;
        this.zs = zs;
    }

    protected boolean satisfiesCofactor() {
        BigInteger h = this.curve.getCofactor();
        return h == null || h.equals(ECConstants.ONE) || !ECAlgorithms.referenceMultiply(this, h).isInfinity();
    }

    protected abstract boolean satisfiesCurveEquation();

    public final ECPoint getDetachedPoint() {
        return this.normalize().detach();
    }

    public ECCurve getCurve() {
        return this.curve;
    }

    protected abstract ECPoint detach();

    protected int getCurveCoordinateSystem() {
        return null == this.curve ? 0 : this.curve.getCoordinateSystem();
    }


    public ECFieldElement getX() {
        return this.normalize().getXCoord();
    }


    public ECFieldElement getY() {
        return this.normalize().getYCoord();
    }

    public ECFieldElement getAffineXCoord() {
        this.checkNormalized();
        return this.getXCoord();
    }

    public ECFieldElement getAffineYCoord() {
        this.checkNormalized();
        return this.getYCoord();
    }

    public ECFieldElement getXCoord() {
        return this.x;
    }

    public ECFieldElement getYCoord() {
        return this.y;
    }

    public ECFieldElement getZCoord(int index) {
        return index >= 0 && index < this.zs.length ? this.zs[index] : null;
    }

    public ECFieldElement[] getZCoords() {
        int zsLen = this.zs.length;
        if (zsLen == 0) {
            return this.zs;
        } else {
            ECFieldElement[] copy = new ECFieldElement[zsLen];
            System.arraycopy(this.zs, 0, copy, 0, zsLen);
            return copy;
        }
    }

    protected final ECFieldElement getRawXCoord() {
        return this.x;
    }

    protected final ECFieldElement getRawYCoord() {
        return this.y;
    }

    protected final ECFieldElement[] getRawZCoords() {
        return this.zs;
    }

    protected void checkNormalized() {
        if (!this.isNormalized()) {
            throw new IllegalStateException("point not in normal form");
        }
    }

    public boolean isNormalized() {
        int coord = this.getCurveCoordinateSystem();
        return coord == 0 || coord == 5 || this.isInfinity() || this.zs[0].isOne();
    }

    public ECPoint normalize() {
        if (this.isInfinity()) {
            return this;
        } else {
            switch (this.getCurveCoordinateSystem()) {
                case 0:
                case 5:
                    return this;
                default:
                    ECFieldElement Z1 = this.getZCoord(0);
                    return Z1.isOne() ? this : this.normalize(Z1.invert());
            }
        }
    }

    ECPoint normalize(ECFieldElement zInv) {
        switch (this.getCurveCoordinateSystem()) {
            case 1:
            case 6:
                return this.createScaledPoint(zInv, zInv);
            case 2:
            case 3:
            case 4:
                ECFieldElement zInv2 = zInv.square();
                ECFieldElement zInv3 = zInv2.multiply(zInv);
                return this.createScaledPoint(zInv2, zInv3);
            case 5:
            default:
                throw new IllegalStateException("not a projective coordinate system");
        }
    }

    protected ECPoint createScaledPoint(ECFieldElement sx, ECFieldElement sy) {
        return this.getCurve().createRawPoint(this.getRawXCoord().multiply(sx), this.getRawYCoord().multiply(sy), this.withCompression);
    }

    public boolean isInfinity() {
        return this.x == null || this.y == null || this.zs.length > 0 && this.zs[0].isZero();
    }

    public boolean isCompressed() {
        return this.withCompression;
    }

    public boolean isValid() {
        if (this.isInfinity()) {
            return true;
        } else {
            ECCurve curve = this.getCurve();
            if (curve != null) {
                if (!this.satisfiesCurveEquation()) {
                    return false;
                }

                if (!this.satisfiesCofactor()) {
                    return false;
                }
            }

            return true;
        }
    }

    public ECPoint scaleX(ECFieldElement scale) {
        return this.isInfinity() ? this : this.getCurve().createRawPoint(this.getRawXCoord().multiply(scale), this.getRawYCoord(), this.getRawZCoords(), this.withCompression);
    }

    public ECPoint scaleY(ECFieldElement scale) {
        return this.isInfinity() ? this : this.getCurve().createRawPoint(this.getRawXCoord(), this.getRawYCoord().multiply(scale), this.getRawZCoords(), this.withCompression);
    }

    public boolean equals(ECPoint other) {
        if (null == other) {
            return false;
        } else {
            ECCurve c1 = this.getCurve();
            ECCurve c2 = other.getCurve();
            boolean n1 = null == c1;
            boolean n2 = null == c2;
            boolean i1 = this.isInfinity();
            boolean i2 = other.isInfinity();
            if (!i1 && !i2) {
                ECPoint p1 = this;
                ECPoint p2 = other;
                if (!n1 || !n2) {
                    if (n1) {
                        p2 = p2.normalize();
                    } else if (n2) {
                        p1 = p1.normalize();
                    } else {
                        if (!c1.equals(c2)) {
                            return false;
                        }

                        ECPoint[] points = new ECPoint[]{this, c1.importPoint(p2)};
                        c1.normalizeAll(points);
                        p1 = points[0];
                        p2 = points[1];
                    }
                }

                return p1.getXCoord().equals(p2.getXCoord()) && p1.getYCoord().equals(p2.getYCoord());
            } else {
                return i1 && i2 && (n1 || n2 || c1.equals(c2));
            }
        }
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else {
            return !(other instanceof ECPoint) ? false : this.equals((ECPoint) other);
        }
    }

    public int hashCode() {
        ECCurve c = this.getCurve();
        int hc = null == c ? 0 : ~c.hashCode();
        if (!this.isInfinity()) {
            ECPoint p = this.normalize();
            hc ^= p.getXCoord().hashCode() * 17;
            hc ^= p.getYCoord().hashCode() * 257;
        }

        return hc;
    }

    public String toString() {
        if (this.isInfinity()) {
            return "INF";
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append('(');
            sb.append(this.getRawXCoord());
            sb.append(',');
            sb.append(this.getRawYCoord());

            for (int i = 0; i < this.zs.length; ++i) {
                sb.append(',');
                sb.append(this.zs[i]);
            }

            sb.append(')');
            return sb.toString();
        }
    }

    public byte[] getEncoded() {
        return this.getEncoded(this.withCompression);
    }

    public byte[] getEncoded(boolean compressed) {
        if (this.isInfinity()) {
            return new byte[1];
        } else {
            ECPoint normed = this.normalize();
            byte[] X = normed.getXCoord().getEncoded();
            byte[] Y;
            if (compressed) {
                Y = new byte[X.length + 1];
                Y[0] = (byte) (normed.getCompressionYTilde() ? 3 : 2);
                System.arraycopy(X, 0, Y, 1, X.length);
                return Y;
            } else {
                Y = normed.getYCoord().getEncoded();
                byte[] PO = new byte[X.length + Y.length + 1];
                PO[0] = 4;
                System.arraycopy(X, 0, PO, 1, X.length);
                System.arraycopy(Y, 0, PO, X.length + 1, Y.length);
                return PO;
            }
        }
    }

    protected abstract boolean getCompressionYTilde();

    public abstract ECPoint add(ECPoint var1);

    public abstract ECPoint negate();

    public abstract ECPoint subtract(ECPoint var1);

    public ECPoint timesPow2(int e) {
        if (e < 0) {
            throw new IllegalArgumentException("'e' cannot be negative");
        } else {
            ECPoint p = this;

            while (true) {
                --e;
                if (e < 0) {
                    return p;
                }

                p = p.twice();
            }
        }
    }

    public abstract ECPoint twice();

    public ECPoint twicePlus(ECPoint b) {
        return this.twice().add(b);
    }

    public ECPoint threeTimes() {
        return this.twicePlus(this);
    }

    public ECPoint multiply(BigInteger k) {
        return this.getCurve().getMultiplier().multiply(this, k);
    }

    public static class F2m extends AbstractF2m {

        public F2m(ECCurve curve, ECFieldElement x, ECFieldElement y) {
            this(curve, x, y, false);
        }


        public F2m(ECCurve curve, ECFieldElement x, ECFieldElement y, boolean withCompression) {
            super(curve, x, y);
            if (x == null != (y == null)) {
                throw new IllegalArgumentException("Exactly one of the field elements is null");
            } else {
                if (x != null) {
                    ECFieldElement.F2m.checkFieldElements(this.x, this.y);
                    if (curve != null) {
                        ECFieldElement.F2m.checkFieldElements(this.x, this.curve.getA());
                    }
                }

                this.withCompression = withCompression;
            }
        }

        F2m(ECCurve curve, ECFieldElement x, ECFieldElement y, ECFieldElement[] zs, boolean withCompression) {
            super(curve, x, y, zs);
            this.withCompression = withCompression;
        }

        protected ECPoint detach() {
            return new F2m((ECCurve) null, this.getAffineXCoord(), this.getAffineYCoord());
        }

        public ECFieldElement getYCoord() {
            int coord = this.getCurveCoordinateSystem();
            switch (coord) {
                case 5:
                case 6:
                    ECFieldElement X = this.x;
                    ECFieldElement L = this.y;
                    if (!this.isInfinity() && !X.isZero()) {
                        ECFieldElement Y = L.add(X).multiply(X);
                        if (6 == coord) {
                            ECFieldElement Z = this.zs[0];
                            if (!Z.isOne()) {
                                Y = Y.divide(Z);
                            }
                        }

                        return Y;
                    }

                    return L;
                default:
                    return this.y;
            }
        }

        public ECPoint scaleX(ECFieldElement scale) {
            if (this.isInfinity()) {
                return this;
            } else {
                int coord = this.getCurveCoordinateSystem();
                ECFieldElement X;
                ECFieldElement L;
                ECFieldElement Z;
                ECFieldElement X2;
                switch (coord) {
                    case 5:
                        X = this.getRawXCoord();
                        L = this.getRawYCoord();
                        Z = X.multiply(scale);
                        X2 = L.add(X).divide(scale).add(Z);
                        return this.getCurve().createRawPoint(X, X2, this.getRawZCoords(), this.withCompression);
                    case 6:
                        X = this.getRawXCoord();
                        L = this.getRawYCoord();
                        Z = this.getRawZCoords()[0];
                        X2 = X.multiply(scale.square());
                        ECFieldElement L2 = L.add(X).add(X2);
                        ECFieldElement Z2 = Z.multiply(scale);
                        return this.getCurve().createRawPoint(X2, L2, new ECFieldElement[]{Z2}, this.withCompression);
                    default:
                        return super.scaleX(scale);
                }
            }
        }

        public ECPoint scaleY(ECFieldElement scale) {
            if (this.isInfinity()) {
                return this;
            } else {
                int coord = this.getCurveCoordinateSystem();
                switch (coord) {
                    case 5:
                    case 6:
                        ECFieldElement X = this.getRawXCoord();
                        ECFieldElement L = this.getRawYCoord();
                        ECFieldElement L2 = L.add(X).multiply(scale).add(X);
                        return this.getCurve().createRawPoint(X, L2, this.getRawZCoords(), this.withCompression);
                    default:
                        return super.scaleY(scale);
                }
            }
        }

        protected boolean getCompressionYTilde() {
            ECFieldElement X = this.getRawXCoord();
            if (X.isZero()) {
                return false;
            } else {
                ECFieldElement Y = this.getRawYCoord();
                switch (this.getCurveCoordinateSystem()) {
                    case 5:
                    case 6:
                        return Y.testBitZero() != X.testBitZero();
                    default:
                        return Y.divide(X).testBitZero();
                }
            }
        }

        private static void checkPoints(ECPoint a, ECPoint b) {
            if (a.curve != b.curve) {
                throw new IllegalArgumentException("Only points on the same curve can be added or subtracted");
            }
        }

        public ECPoint add(ECPoint b) {
            checkPoints(this, b);
            return this.addSimple((F2m) b);
        }

        public F2m addSimple(F2m b) {
            if (this.isInfinity()) {
                return b;
            } else if (b.isInfinity()) {
                return this;
            } else {
                ECCurve curve = this.getCurve();
                int coord = curve.getCoordinateSystem();
                ECFieldElement X1 = this.x;
                ECFieldElement X2 = b.x;
                ECFieldElement L1;
                ECFieldElement Z1;
                ECFieldElement L2;
                ECFieldElement Z2;
                boolean Z1IsOne;
                ECFieldElement U2;
                ECFieldElement S2;
                ECFieldElement U1;
                ECFieldElement S1;
                ECFieldElement A;
                ECFieldElement B;
                ECFieldElement X3;
                ECFieldElement L3;
                ECFieldElement Z3;
                ECFieldElement AU2;
                ECFieldElement ABZ2;
                ECFieldElement L;
                ECFieldElement Y3;
                ECFieldElement AU1;
                switch (coord) {
                    case 0:
                        L1 = this.y;
                        Z1 = b.y;
                        L2 = X1.add(X2);
                        Z2 = L1.add(Z1);
                        if (L2.isZero()) {
                            if (Z2.isZero()) {
                                return (F2m) this.twice();
                            }

                            return (F2m) curve.getInfinity();
                        }

                        L = Z2.divide(L2);
                        U2 = L.square().add(L).add(L2).add(curve.getA());
                        S2 = L.multiply(X1.add(U2)).add(U2).add(L1);
                        return new F2m(curve, U2, S2, this.withCompression);
                    case 1:
                        L1 = this.y;
                        Z1 = this.zs[0];
                        L2 = b.y;
                        Z2 = b.zs[0];
                        Z1IsOne = Z2.isOne();
                        U2 = Z1.multiply(L2);
                        S2 = Z1IsOne ? L1 : L1.multiply(Z2);
                        ECFieldElement U = U2.add(S2);
                        U1 = Z1.multiply(X2);
                        S1 = Z1IsOne ? X1 : X1.multiply(Z2);
                        A = U1.add(S1);
                        if (A.isZero()) {
                            if (U.isZero()) {
                                return (F2m) this.twice();
                            }

                            return (F2m) curve.getInfinity();
                        }

                        B = A.square();
                        X3 = B.multiply(A);
                        L3 = Z1IsOne ? Z1 : Z1.multiply(Z2);
                        Z3 = U.add(A);
                        AU1 = Z3.multiplyPlusProduct(U, B, curve.getA()).multiply(L3).add(X3);
                        AU2 = A.multiply(AU1);
                        ABZ2 = Z1IsOne ? B : B.multiply(Z2);
                        L = U.multiplyPlusProduct(X1, A, L1).multiplyPlusProduct(ABZ2, Z3, AU1);
                        Y3 = X3.multiply(L3);
                        return new F2m(curve, AU2, L, new ECFieldElement[]{Y3}, this.withCompression);
                    case 6:
                        if (X1.isZero()) {
                            if (X2.isZero()) {
                                return (F2m) curve.getInfinity();
                            }

                            return b.addSimple(this);
                        } else {
                            L1 = this.y;
                            Z1 = this.zs[0];
                            L2 = b.y;
                            Z2 = b.zs[0];
                            Z1IsOne = Z1.isOne();
                            U2 = X2;
                            S2 = L2;
                            if (!Z1IsOne) {
                                U2 = U2.multiply(Z1);
                                S2 = S2.multiply(Z1);
                            }

                            boolean Z2IsOne = Z2.isOne();
                            U1 = X1;
                            S1 = L1;
                            if (!Z2IsOne) {
                                U1 = U1.multiply(Z2);
                                S1 = S1.multiply(Z2);
                            }

                            A = S1.add(S2);
                            B = U1.add(U2);
                            if (B.isZero()) {
                                if (A.isZero()) {
                                    return (F2m) this.twice();
                                }

                                return (F2m) curve.getInfinity();
                            } else {
                                if (X2.isZero()) {
                                    ECPoint p = this.normalize();
                                    X1 = p.getXCoord();
                                    AU2 = p.getYCoord();
                                    ABZ2 = L2;
                                    L = AU2.add(ABZ2).divide(X1);
                                    X3 = L.square().add(L).add(X1).add(curve.getA());
                                    if (X3.isZero()) {
                                        return new F2m(curve, X3, curve.getB().sqrt(), this.withCompression);
                                    }

                                    Y3 = L.multiply(X1.add(X3)).add(X3).add(AU2);
                                    L3 = Y3.divide(X3).add(X3);
                                    Z3 = curve.fromBigInteger(ECConstants.ONE);
                                } else {
                                    B = B.square();
                                    AU1 = A.multiply(U1);
                                    AU2 = A.multiply(U2);
                                    X3 = AU1.multiply(AU2);
                                    if (X3.isZero()) {
                                        return new F2m(curve, X3, curve.getB().sqrt(), this.withCompression);
                                    }

                                    ABZ2 = A.multiply(B);
                                    if (!Z2IsOne) {
                                        ABZ2 = ABZ2.multiply(Z2);
                                    }

                                    L3 = AU2.add(B).squarePlusProduct(ABZ2, L1.add(Z1));
                                    Z3 = ABZ2;
                                    if (!Z1IsOne) {
                                        Z3 = Z3.multiply(Z1);
                                    }
                                }

                                return new F2m(curve, X3, L3, new ECFieldElement[]{Z3}, this.withCompression);
                            }
                        }
                    default:
                        throw new IllegalStateException("unsupported coordinate system");
                }
            }
        }

        public ECPoint subtract(ECPoint b) {
            checkPoints(this, b);
            return this.subtractSimple((F2m) b);
        }

        public F2m subtractSimple(F2m b) {
            return b.isInfinity() ? this : this.addSimple((F2m) b.negate());
        }

        public F2m tau() {
            if (this.isInfinity()) {
                return this;
            } else {
                ECCurve curve = this.getCurve();
                int coord = curve.getCoordinateSystem();
                ECFieldElement X1 = this.x;
                ECFieldElement Y1;
                switch (coord) {
                    case 0:
                    case 5:
                        Y1 = this.y;
                        return new F2m(curve, X1.square(), Y1.square(), this.withCompression);
                    case 1:
                    case 6:
                        Y1 = this.y;
                        ECFieldElement Z1 = this.zs[0];
                        return new F2m(curve, X1.square(), Y1.square(), new ECFieldElement[]{Z1.square()}, this.withCompression);
                    case 2:
                    case 3:
                    case 4:
                    default:
                        throw new IllegalStateException("unsupported coordinate system");
                }
            }
        }

        public ECPoint twice() {
            if (this.isInfinity()) {
                return this;
            } else {
                ECCurve curve = this.getCurve();
                ECFieldElement X1 = this.x;
                if (X1.isZero()) {
                    return curve.getInfinity();
                } else {
                    int coord = curve.getCoordinateSystem();
                    ECFieldElement L1;
                    ECFieldElement Z1;
                    boolean Z1IsOne;
                    ECFieldElement L1Z1;
                    ECFieldElement Z1Sq;
                    ECFieldElement a;
                    ECFieldElement aZ1Sq;
                    ECFieldElement T;
                    ECFieldElement X3;
                    ECFieldElement Z3;
                    ECFieldElement b;
                    ECFieldElement L3;
                    ECFieldElement t1;
                    ECFieldElement t2;
                    switch (coord) {
                        case 0:
                            L1 = this.y;
                            Z1 = L1.divide(X1).add(X1);
                            X3 = Z1.square().add(Z1).add(curve.getA());
                            L1Z1 = X1.squarePlusProduct(X3, Z1.addOne());
                            return new F2m(curve, X3, L1Z1, this.withCompression);
                        case 1:
                            L1 = this.y;
                            Z1 = this.zs[0];
                            Z1IsOne = Z1.isOne();
                            L1Z1 = Z1IsOne ? X1 : X1.multiply(Z1);
                            Z1Sq = Z1IsOne ? L1 : L1.multiply(Z1);
                            a = X1.square();
                            aZ1Sq = a.add(Z1Sq);
                            T = L1Z1;
                            X3 = T.square();
                            Z3 = aZ1Sq.add(T);
                            b = Z3.multiplyPlusProduct(aZ1Sq, X3, curve.getA());
                            L3 = T.multiply(b);
                            t1 = a.square().multiplyPlusProduct(T, b, Z3);
                            t2 = T.multiply(X3);
                            return new F2m(curve, L3, t1, new ECFieldElement[]{t2}, this.withCompression);
                        case 6:
                            L1 = this.y;
                            Z1 = this.zs[0];
                            Z1IsOne = Z1.isOne();
                            L1Z1 = Z1IsOne ? L1 : L1.multiply(Z1);
                            Z1Sq = Z1IsOne ? Z1 : Z1.square();
                            a = curve.getA();
                            aZ1Sq = Z1IsOne ? a : a.multiply(Z1Sq);
                            T = L1.square().add(L1Z1).add(aZ1Sq);
                            if (T.isZero()) {
                                return new F2m(curve, T, curve.getB().sqrt(), this.withCompression);
                            }

                            X3 = T.square();
                            Z3 = Z1IsOne ? T : T.multiply(Z1Sq);
                            b = curve.getB();
                            if (b.bitLength() < curve.getFieldSize() >> 1) {
                                t1 = L1.add(X1).square();
                                if (b.isOne()) {
                                    t2 = aZ1Sq.add(Z1Sq).square();
                                } else {
                                    t2 = aZ1Sq.squarePlusProduct(b, Z1Sq.square());
                                }

                                L3 = t1.add(T).add(Z1Sq).multiply(t1).add(t2).add(X3);
                                if (a.isZero()) {
                                    L3 = L3.add(Z3);
                                } else if (!a.isOne()) {
                                    L3 = L3.add(a.addOne().multiply(Z3));
                                }
                            } else {
                                t1 = Z1IsOne ? X1 : X1.multiply(Z1);
                                L3 = t1.squarePlusProduct(T, L1Z1).add(X3).add(Z3);
                            }

                            return new F2m(curve, X3, L3, new ECFieldElement[]{Z3}, this.withCompression);
                        default:
                            throw new IllegalStateException("unsupported coordinate system");
                    }
                }
            }
        }

        public ECPoint twicePlus(ECPoint b) {
            if (this.isInfinity()) {
                return b;
            } else if (b.isInfinity()) {
                return this.twice();
            } else {
                ECCurve curve = this.getCurve();
                ECFieldElement X1 = this.x;
                if (X1.isZero()) {
                    return b;
                } else {
                    int coord = curve.getCoordinateSystem();
                    switch (coord) {
                        case 6:
                            ECFieldElement X2 = b.x;
                            ECFieldElement Z2 = b.zs[0];
                            if (!X2.isZero() && Z2.isOne()) {
                                ECFieldElement L1 = this.y;
                                ECFieldElement Z1 = this.zs[0];
                                ECFieldElement L2 = b.y;
                                ECFieldElement X1Sq = X1.square();
                                ECFieldElement L1Sq = L1.square();
                                ECFieldElement Z1Sq = Z1.square();
                                ECFieldElement L1Z1 = L1.multiply(Z1);
                                ECFieldElement T = curve.getA().multiply(Z1Sq).add(L1Sq).add(L1Z1);
                                ECFieldElement L2plus1 = L2.addOne();
                                ECFieldElement A = curve.getA().add(L2plus1).multiply(Z1Sq).add(L1Sq).multiplyPlusProduct(T, X1Sq, Z1Sq);
                                ECFieldElement X2Z1Sq = X2.multiply(Z1Sq);
                                ECFieldElement B = X2Z1Sq.add(T).square();
                                if (B.isZero()) {
                                    if (A.isZero()) {
                                        return b.twice();
                                    }

                                    return curve.getInfinity();
                                }

                                if (A.isZero()) {
                                    return new F2m(curve, A, curve.getB().sqrt(), this.withCompression);
                                }

                                ECFieldElement X3 = A.square().multiply(X2Z1Sq);
                                ECFieldElement Z3 = A.multiply(B).multiply(Z1Sq);
                                ECFieldElement L3 = A.add(B).square().multiplyPlusProduct(T, L2plus1, Z3);
                                return new F2m(curve, X3, L3, new ECFieldElement[]{Z3}, this.withCompression);
                            }

                            return this.twice().add(b);
                        default:
                            return this.twice().add(b);
                    }
                }
            }
        }

        public ECPoint negate() {
            if (this.isInfinity()) {
                return this;
            } else {
                ECFieldElement X = this.x;
                if (X.isZero()) {
                    return this;
                } else {
                    ECFieldElement L;
                    ECFieldElement Z;
                    switch (this.getCurveCoordinateSystem()) {
                        case 0:
                            L = this.y;
                            return new F2m(this.curve, X, L.add(X), this.withCompression);
                        case 1:
                            L = this.y;
                            Z = this.zs[0];
                            return new F2m(this.curve, X, L.add(X), new ECFieldElement[]{Z}, this.withCompression);
                        case 2:
                        case 3:
                        case 4:
                        default:
                            throw new IllegalStateException("unsupported coordinate system");
                        case 5:
                            L = this.y;
                            return new F2m(this.curve, X, L.addOne(), this.withCompression);
                        case 6:
                            L = this.y;
                            Z = this.zs[0];
                            return new F2m(this.curve, X, L.add(Z), new ECFieldElement[]{Z}, this.withCompression);
                    }
                }
            }
        }
    }

    public abstract static class AbstractF2m extends ECPoint {
        protected AbstractF2m(ECCurve curve, ECFieldElement x, ECFieldElement y) {
            super(curve, x, y);
        }

        protected AbstractF2m(ECCurve curve, ECFieldElement x, ECFieldElement y, ECFieldElement[] zs) {
            super(curve, x, y, zs);
        }

        protected boolean satisfiesCurveEquation() {
            ECCurve curve = this.getCurve();
            ECFieldElement X = this.x;
            ECFieldElement A = curve.getA();
            ECFieldElement B = curve.getB();
            int coord = curve.getCoordinateSystem();
            ECFieldElement Z;
            ECFieldElement X2;
            ECFieldElement lhs;
            if (coord == 6) {
                Z = this.zs[0];
                boolean ZIsOne = Z.isOne();
                if (X.isZero()) {
                    Z = this.y;
                    X2 = Z.square();
                    lhs = B;
                    if (!ZIsOne) {
                        lhs = lhs.multiply(Z.square());
                    }

                    return X2.equals(lhs);
                } else {
                    Z = this.y;
                    X2 = X.square();
                    ECFieldElement rhs;
                    if (ZIsOne) {
                        lhs = Z.square().add(Z).add(A);
                        rhs = X2.square().add(B);
                    } else {
                        ECFieldElement Z2 = Z.square();
                        ECFieldElement Z4 = Z2.square();
                        lhs = Z.add(Z).multiplyPlusProduct(Z, A, Z2);
                        rhs = X2.squarePlusProduct(B, Z4);
                    }

                    lhs = lhs.multiply(X2);
                    return lhs.equals(rhs);
                }
            } else {
                Z = this.y;
                lhs = Z.add(X).multiply(Z);
                switch (coord) {
                    case 1:
                        Z = this.zs[0];
                        if (!Z.isOne()) {
                            X2 = Z.square();
                            lhs = Z.multiply(X2);
                            lhs = lhs.multiply(Z);
                            A = A.multiply(Z);
                            B = B.multiply(lhs);
                        }
                    case 0:
                        Z = X.add(A).multiply(X.square()).add(B);
                        return lhs.equals(Z);
                    default:
                        throw new IllegalStateException("unsupported coordinate system");
                }
            }
        }
    }

    public static class Fp extends AbstractFp {

        public Fp(ECCurve curve, ECFieldElement x, ECFieldElement y) {
            this(curve, x, y, false);
        }


        public Fp(ECCurve curve, ECFieldElement x, ECFieldElement y, boolean withCompression) {
            super(curve, x, y);
            if (x == null != (y == null)) {
                throw new IllegalArgumentException("Exactly one of the field elements is null");
            } else {
                this.withCompression = withCompression;
            }
        }

        Fp(ECCurve curve, ECFieldElement x, ECFieldElement y, ECFieldElement[] zs, boolean withCompression) {
            super(curve, x, y, zs);
            this.withCompression = withCompression;
        }

        protected ECPoint detach() {
            return new Fp((ECCurve) null, this.getAffineXCoord(), this.getAffineYCoord());
        }

        public ECFieldElement getZCoord(int index) {
            return index == 1 && 4 == this.getCurveCoordinateSystem() ? this.getJacobianModifiedW() : super.getZCoord(index);
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
                int coord = curve.getCoordinateSystem();
                ECFieldElement X1 = this.x;
                ECFieldElement Y1 = this.y;
                ECFieldElement X2 = b.x;
                ECFieldElement Y2 = b.y;
                ECFieldElement Z1;
                ECFieldElement Z2;
                boolean Z1IsOne;
                ECFieldElement X3;
                ECFieldElement Y3;
                ECFieldElement Z3;
                ECFieldElement Z3Squared;
                ECFieldElement Z1Squared;
                ECFieldElement W3;
                ECFieldElement S2;
                ECFieldElement Z1Cubed;
                ECFieldElement Z2Squared;
                ECFieldElement U1;
                ECFieldElement S1;
                ECFieldElement H;
                ECFieldElement R;
                ECFieldElement HSquared;
                ECFieldElement G;
                switch (coord) {
                    case 0:
                        Z1 = X2.subtract(X1);
                        Z2 = Y2.subtract(Y1);
                        if (Z1.isZero()) {
                            if (Z2.isZero()) {
                                return this.twice();
                            }

                            return curve.getInfinity();
                        }

                        ECFieldElement gamma = Z2.divide(Z1);
                        X3 = gamma.square().subtract(X1).subtract(X2);
                        Y3 = gamma.multiply(X1.subtract(X3)).subtract(Y1);
                        return new Fp(curve, X3, Y3, this.withCompression);
                    case 1:
                        Z1 = this.zs[0];
                        Z2 = b.zs[0];
                        Z1IsOne = Z1.isOne();
                        boolean Z2IsOne = Z2.isOne();
                        Y3 = Z1IsOne ? Y2 : Y2.multiply(Z1);
                        Z3 = Z2IsOne ? Y1 : Y1.multiply(Z2);
                        Z3Squared = Y3.subtract(Z3);
                        Z1Squared = Z1IsOne ? X2 : X2.multiply(Z1);
                        W3 = Z2IsOne ? X1 : X1.multiply(Z2);
                        S2 = Z1Squared.subtract(W3);
                        if (S2.isZero()) {
                            if (Z3Squared.isZero()) {
                                return this.twice();
                            }

                            return curve.getInfinity();
                        }

                        Z1Cubed = Z1IsOne ? Z2 : (Z2IsOne ? Z1 : Z1.multiply(Z2));
                        Z2Squared = S2.square();
                        U1 = Z2Squared.multiply(S2);
                        S1 = Z2Squared.multiply(W3);
                        H = Z3Squared.square().multiply(Z1Cubed).subtract(U1).subtract(this.two(S1));
                        R = S2.multiply(H);
                        HSquared = S1.subtract(H).multiplyMinusProduct(Z3Squared, Z3, U1);
                        G = U1.multiply(Z1Cubed);
                        return new Fp(curve, R, HSquared, new ECFieldElement[]{G}, this.withCompression);
                    case 2:
                    case 4:
                        Z1 = this.zs[0];
                        Z2 = b.zs[0];
                        Z1IsOne = Z1.isOne();
                        Z3Squared = null;
                        if (!Z1IsOne && Z1.equals(Z2)) {
                            Z1Squared = X1.subtract(X2);
                            W3 = Y1.subtract(Y2);
                            if (Z1Squared.isZero()) {
                                if (W3.isZero()) {
                                    return this.twice();
                                }

                                return curve.getInfinity();
                            }

                            S2 = Z1Squared.square();
                            Z1Cubed = X1.multiply(S2);
                            Z2Squared = X2.multiply(S2);
                            U1 = Z1Cubed.subtract(Z2Squared).multiply(Y1);
                            X3 = W3.square().subtract(Z1Cubed).subtract(Z2Squared);
                            Y3 = Z1Cubed.subtract(X3).multiply(W3).subtract(U1);
                            Z3 = Z1Squared;
                            if (Z1IsOne) {
                                Z3Squared = S2;
                            } else {
                                Z3 = Z3.multiply(Z1);
                            }
                        } else {
                            if (Z1IsOne) {
                                W3 = X2;
                                S2 = Y2;
                            } else {
                                Z1Squared = Z1.square();
                                W3 = Z1Squared.multiply(X2);
                                Z1Cubed = Z1Squared.multiply(Z1);
                                S2 = Z1Cubed.multiply(Y2);
                            }

                            Z2IsOne = Z2.isOne();
                            if (Z2IsOne) {
                                U1 = X1;
                                S1 = Y1;
                            } else {
                                Z2Squared = Z2.square();
                                U1 = Z2Squared.multiply(X1);
                                H = Z2Squared.multiply(Z2);
                                S1 = H.multiply(Y1);
                            }

                            H = U1.subtract(W3);
                            R = S1.subtract(S2);
                            if (H.isZero()) {
                                if (R.isZero()) {
                                    return this.twice();
                                }

                                return curve.getInfinity();
                            }

                            HSquared = H.square();
                            G = HSquared.multiply(H);
                            ECFieldElement V = HSquared.multiply(U1);
                            X3 = R.square().add(G).subtract(this.two(V));
                            Y3 = V.subtract(X3).multiplyMinusProduct(R, G, S1);
                            Z3 = H;
                            if (!Z1IsOne) {
                                Z3 = Z3.multiply(Z1);
                            }

                            if (!Z2IsOne) {
                                Z3 = Z3.multiply(Z2);
                            }

                            if (Z3 == H) {
                                Z3Squared = HSquared;
                            }
                        }

                        ECFieldElement[] zs;
                        if (coord == 4) {
                            W3 = this.calculateJacobianModifiedW(Z3, Z3Squared);
                            zs = new ECFieldElement[]{Z3, W3};
                        } else {
                            zs = new ECFieldElement[]{Z3};
                        }

                        return new Fp(curve, X3, Y3, zs, this.withCompression);
                    case 3:
                    default:
                        throw new IllegalStateException("unsupported coordinate system");
                }
            }
        }

        public ECPoint twice() {
            if (this.isInfinity()) {
                return this;
            } else {
                ECCurve curve = this.getCurve();
                ECFieldElement Y1 = this.y;
                if (Y1.isZero()) {
                    return curve.getInfinity();
                } else {
                    int coord = curve.getCoordinateSystem();
                    ECFieldElement X1 = this.x;
                    ECFieldElement Z1;
                    boolean Z1IsOne;
                    ECFieldElement Y1Squared;
                    ECFieldElement T;
                    ECFieldElement a4;
                    ECFieldElement a4Neg;
                    ECFieldElement M;
                    ECFieldElement S;
                    ECFieldElement X3;
                    ECFieldElement Z1Squared;
                    ECFieldElement Z1Pow4;
                    switch (coord) {
                        case 0:
                            Z1 = X1.square();
                            ECFieldElement gamma = this.three(Z1).add(this.getCurve().getA()).divide(this.two(Y1));
                            Y1Squared = gamma.square().subtract(this.two(X1));
                            T = gamma.multiply(X1.subtract(Y1Squared)).subtract(Y1);
                            return new Fp(curve, Y1Squared, T, this.withCompression);
                        case 1:
                            Z1 = this.zs[0];
                            Z1IsOne = Z1.isOne();
                            Y1Squared = curve.getA();
                            if (!Y1Squared.isZero() && !Z1IsOne) {
                                Y1Squared = Y1Squared.multiply(Z1.square());
                            }

                            Y1Squared = Y1Squared.add(this.three(X1.square()));
                            T = Z1IsOne ? Y1 : Y1.multiply(Z1);
                            a4 = Z1IsOne ? Y1.square() : T.multiply(Y1);
                            a4Neg = X1.multiply(a4);
                            M = this.four(a4Neg);
                            S = Y1Squared.square().subtract(this.two(M));
                            X3 = this.two(T);
                            Z1Squared = S.multiply(X3);
                            Z1Pow4 = this.two(a4);
                            ECFieldElement Y3 = M.subtract(S).multiply(Y1Squared).subtract(this.two(Z1Pow4.square()));
                            ECFieldElement _4sSquared = Z1IsOne ? this.two(Z1Pow4) : X3.square();
                            ECFieldElement Z3 = this.two(_4sSquared).multiply(T);
                            return new Fp(curve, Z1Squared, Y3, new ECFieldElement[]{Z3}, this.withCompression);
                        case 2:
                            Z1 = this.zs[0];
                            Z1IsOne = Z1.isOne();
                            Y1Squared = Y1.square();
                            T = Y1Squared.square();
                            a4 = curve.getA();
                            a4Neg = a4.negate();
                            if (a4Neg.toBigInteger().equals(BigInteger.valueOf(3L))) {
                                X3 = Z1IsOne ? Z1 : Z1.square();
                                M = this.three(X1.add(X3).multiply(X1.subtract(X3)));
                                S = this.four(Y1Squared.multiply(X1));
                            } else {
                                X3 = X1.square();
                                M = this.three(X3);
                                if (Z1IsOne) {
                                    M = M.add(a4);
                                } else if (!a4.isZero()) {
                                    Z1Squared = Z1IsOne ? Z1 : Z1.square();
                                    Z1Pow4 = Z1Squared.square();
                                    if (a4Neg.bitLength() < a4.bitLength()) {
                                        M = M.subtract(Z1Pow4.multiply(a4Neg));
                                    } else {
                                        M = M.add(Z1Pow4.multiply(a4));
                                    }
                                }

                                S = this.four(X1.multiply(Y1Squared));
                            }

                            X3 = M.square().subtract(this.two(S));
                            Z1Squared = S.subtract(X3).multiply(M).subtract(this.eight(T));
                            Z1Pow4 = this.two(Y1);
                            if (!Z1IsOne) {
                                Z1Pow4 = Z1Pow4.multiply(Z1);
                            }

                            return new Fp(curve, X3, Z1Squared, new ECFieldElement[]{Z1Pow4}, this.withCompression);
                        case 3:
                        default:
                            throw new IllegalStateException("unsupported coordinate system");
                        case 4:
                            return this.twiceJacobianModified(true);
                    }
                }
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
                if (Y1.isZero()) {
                    return b;
                } else {
                    ECCurve curve = this.getCurve();
                    int coord = curve.getCoordinateSystem();
                    switch (coord) {
                        case 0:
                            ECFieldElement X1 = this.x;
                            ECFieldElement X2 = b.x;
                            ECFieldElement Y2 = b.y;
                            ECFieldElement dx = X2.subtract(X1);
                            ECFieldElement dy = Y2.subtract(Y1);
                            if (dx.isZero()) {
                                if (dy.isZero()) {
                                    return this.threeTimes();
                                }

                                return this;
                            } else {
                                ECFieldElement X = dx.square();
                                ECFieldElement Y = dy.square();
                                ECFieldElement d = X.multiply(this.two(X1).add(X2)).subtract(Y);
                                if (d.isZero()) {
                                    return curve.getInfinity();
                                }

                                ECFieldElement D = d.multiply(dx);
                                ECFieldElement I = D.invert();
                                ECFieldElement L1 = d.multiply(I).multiply(dy);
                                ECFieldElement L2 = this.two(Y1).multiply(X).multiply(dx).multiply(I).subtract(L1);
                                ECFieldElement X4 = L2.subtract(L1).multiply(L1.add(L2)).add(X2);
                                ECFieldElement Y4 = X1.subtract(X4).multiply(L2).subtract(Y1);
                                return new Fp(curve, X4, Y4, this.withCompression);
                            }
                        case 4:
                            return this.twiceJacobianModified(false).add(b);
                        default:
                            return this.twice().add(b);
                    }
                }
            }
        }

        public ECPoint threeTimes() {
            if (this.isInfinity()) {
                return this;
            } else {
                ECFieldElement Y1 = this.y;
                if (Y1.isZero()) {
                    return this;
                } else {
                    ECCurve curve = this.getCurve();
                    int coord = curve.getCoordinateSystem();
                    switch (coord) {
                        case 0:
                            ECFieldElement X1 = this.x;
                            ECFieldElement _2Y1 = this.two(Y1);
                            ECFieldElement X = _2Y1.square();
                            ECFieldElement Z = this.three(X1.square()).add(this.getCurve().getA());
                            ECFieldElement Y = Z.square();
                            ECFieldElement d = this.three(X1).multiply(X).subtract(Y);
                            if (d.isZero()) {
                                return this.getCurve().getInfinity();
                            }

                            ECFieldElement D = d.multiply(_2Y1);
                            ECFieldElement I = D.invert();
                            ECFieldElement L1 = d.multiply(I).multiply(Z);
                            ECFieldElement L2 = X.square().multiply(I).subtract(L1);
                            ECFieldElement X4 = L2.subtract(L1).multiply(L1.add(L2)).add(X1);
                            ECFieldElement Y4 = X1.subtract(X4).multiply(L2).subtract(Y1);
                            return new Fp(curve, X4, Y4, this.withCompression);
                        case 4:
                            return this.twiceJacobianModified(false).add(this);
                        default:
                            return this.twice().add(this);
                    }
                }
            }
        }

        public ECPoint timesPow2(int e) {
            if (e < 0) {
                throw new IllegalArgumentException("'e' cannot be negative");
            } else if (e != 0 && !this.isInfinity()) {
                if (e == 1) {
                    return this.twice();
                } else {
                    ECCurve curve = this.getCurve();
                    ECFieldElement Y1 = this.y;
                    if (Y1.isZero()) {
                        return curve.getInfinity();
                    } else {
                        int coord = curve.getCoordinateSystem();
                        ECFieldElement W1 = curve.getA();
                        ECFieldElement X1 = this.x;
                        ECFieldElement Z1 = this.zs.length < 1 ? curve.fromBigInteger(ECConstants.ONE) : this.zs[0];
                        ECFieldElement zInv;
                        if (!Z1.isOne()) {
                            switch (coord) {
                                case 1:
                                    zInv = Z1.square();
                                    X1 = X1.multiply(Z1);
                                    Y1 = Y1.multiply(zInv);
                                    W1 = this.calculateJacobianModifiedW(Z1, zInv);
                                    break;
                                case 2:
                                    W1 = this.calculateJacobianModifiedW(Z1, (ECFieldElement) null);
                                case 3:
                                default:
                                    break;
                                case 4:
                                    W1 = this.getJacobianModifiedW();
                            }
                        }

                        ECFieldElement zInv2;
                        ECFieldElement M;
                        for (int i = 0; i < e; ++i) {
                            if (Y1.isZero()) {
                                return curve.getInfinity();
                            }

                            zInv2 = X1.square();
                            M = this.three(zInv2);
                            ECFieldElement _2Y1 = this.two(Y1);
                            ECFieldElement _2Y1Squared = _2Y1.multiply(Y1);
                            ECFieldElement S = this.two(X1.multiply(_2Y1Squared));
                            ECFieldElement _4T = _2Y1Squared.square();
                            ECFieldElement _8T = this.two(_4T);
                            if (!W1.isZero()) {
                                M = M.add(W1);
                                W1 = this.two(_8T.multiply(W1));
                            }

                            X1 = M.square().subtract(this.two(S));
                            Y1 = M.multiply(S.subtract(X1)).subtract(_8T);
                            Z1 = Z1.isOne() ? _2Y1 : _2Y1.multiply(Z1);
                        }

                        switch (coord) {
                            case 0:
                                zInv = Z1.invert();
                                zInv2 = zInv.square();
                                M = zInv2.multiply(zInv);
                                return new Fp(curve, X1.multiply(zInv2), Y1.multiply(M), this.withCompression);
                            case 1:
                                X1 = X1.multiply(Z1);
                                Z1 = Z1.multiply(Z1.square());
                                return new Fp(curve, X1, Y1, new ECFieldElement[]{Z1}, this.withCompression);
                            case 2:
                                return new Fp(curve, X1, Y1, new ECFieldElement[]{Z1}, this.withCompression);
                            case 3:
                            default:
                                throw new IllegalStateException("unsupported coordinate system");
                            case 4:
                                return new Fp(curve, X1, Y1, new ECFieldElement[]{Z1, W1}, this.withCompression);
                        }
                    }
                }
            } else {
                return this;
            }
        }

        protected ECFieldElement two(ECFieldElement x) {
            return x.add(x);
        }

        protected ECFieldElement three(ECFieldElement x) {
            return this.two(x).add(x);
        }

        protected ECFieldElement four(ECFieldElement x) {
            return this.two(this.two(x));
        }

        protected ECFieldElement eight(ECFieldElement x) {
            return this.four(this.two(x));
        }

        protected ECFieldElement doubleProductFromSquares(ECFieldElement a, ECFieldElement b, ECFieldElement aSquared, ECFieldElement bSquared) {
            return a.add(b).square().subtract(aSquared).subtract(bSquared);
        }

        public ECPoint negate() {
            if (this.isInfinity()) {
                return this;
            } else {
                ECCurve curve = this.getCurve();
                int coord = curve.getCoordinateSystem();
                return 0 != coord ? new Fp(curve, this.x, this.y.negate(), this.zs, this.withCompression) : new Fp(curve, this.x, this.y.negate(), this.withCompression);
            }
        }

        protected ECFieldElement calculateJacobianModifiedW(ECFieldElement Z, ECFieldElement ZSquared) {
            ECFieldElement a4 = this.getCurve().getA();
            if (!a4.isZero() && !Z.isOne()) {
                if (ZSquared == null) {
                    ZSquared = Z.square();
                }

                ECFieldElement W = ZSquared.square();
                ECFieldElement a4Neg = a4.negate();
                if (a4Neg.bitLength() < a4.bitLength()) {
                    W = W.multiply(a4Neg).negate();
                } else {
                    W = W.multiply(a4);
                }

                return W;
            } else {
                return a4;
            }
        }

        protected ECFieldElement getJacobianModifiedW() {
            ECFieldElement W = this.zs[1];
            if (W == null) {
                this.zs[1] = W = this.calculateJacobianModifiedW(this.zs[0], (ECFieldElement) null);
            }

            return W;
        }

        protected Fp twiceJacobianModified(boolean calculateW) {
            ECFieldElement X1 = this.x;
            ECFieldElement Y1 = this.y;
            ECFieldElement Z1 = this.zs[0];
            ECFieldElement W1 = this.getJacobianModifiedW();
            ECFieldElement X1Squared = X1.square();
            ECFieldElement M = this.three(X1Squared).add(W1);
            ECFieldElement _2Y1 = this.two(Y1);
            ECFieldElement _2Y1Squared = _2Y1.multiply(Y1);
            ECFieldElement S = this.two(X1.multiply(_2Y1Squared));
            ECFieldElement X3 = M.square().subtract(this.two(S));
            ECFieldElement _4T = _2Y1Squared.square();
            ECFieldElement _8T = this.two(_4T);
            ECFieldElement Y3 = M.multiply(S.subtract(X3)).subtract(_8T);
            ECFieldElement W3 = calculateW ? this.two(_8T.multiply(W1)) : null;
            ECFieldElement Z3 = Z1.isOne() ? _2Y1 : _2Y1.multiply(Z1);
            return new Fp(this.getCurve(), X3, Y3, new ECFieldElement[]{Z3, W3}, this.withCompression);
        }
    }

    public abstract static class AbstractFp extends ECPoint {
        protected AbstractFp(ECCurve curve, ECFieldElement x, ECFieldElement y) {
            super(curve, x, y);
        }

        protected AbstractFp(ECCurve curve, ECFieldElement x, ECFieldElement y, ECFieldElement[] zs) {
            super(curve, x, y, zs);
        }

        protected boolean getCompressionYTilde() {
            return this.getAffineYCoord().testBitZero();
        }

        protected boolean satisfiesCurveEquation() {
            ECFieldElement X = this.x;
            ECFieldElement Y = this.y;
            ECFieldElement A = this.curve.getA();
            ECFieldElement B = this.curve.getB();
            ECFieldElement lhs = Y.square();
            ECFieldElement Z;
            ECFieldElement Z2;
            ECFieldElement Z4;
            switch (this.getCurveCoordinateSystem()) {
                case 0:
                    break;
                case 1:
                    Z = this.zs[0];
                    if (!Z.isOne()) {
                        Z2 = Z.square();
                        Z4 = Z.multiply(Z2);
                        lhs = lhs.multiply(Z);
                        A = A.multiply(Z2);
                        B = B.multiply(Z4);
                    }
                    break;
                case 2:
                case 3:
                case 4:
                    Z = this.zs[0];
                    if (!Z.isOne()) {
                        Z2 = Z.square();
                        Z4 = Z2.square();
                        ECFieldElement Z6 = Z2.multiply(Z4);
                        A = A.multiply(Z4);
                        B = B.multiply(Z6);
                    }
                    break;
                default:
                    throw new IllegalStateException("unsupported coordinate system");
            }

            Z = X.square().add(A).multiply(X).add(B);
            return lhs.equals(Z);
        }

        public ECPoint subtract(ECPoint b) {
            return (ECPoint) (b.isInfinity() ? this : this.add(b.negate()));
        }
    }
}
