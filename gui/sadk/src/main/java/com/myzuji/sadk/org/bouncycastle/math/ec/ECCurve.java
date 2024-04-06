package com.myzuji.sadk.org.bouncycastle.math.ec;

import com.myzuji.sadk.org.bouncycastle.math.ec.endo.ECEndomorphism;
import com.myzuji.sadk.org.bouncycastle.math.ec.endo.GLVEndomorphism;
import com.myzuji.sadk.org.bouncycastle.math.field.FiniteField;
import com.myzuji.sadk.org.bouncycastle.math.field.FiniteFields;
import com.myzuji.sadk.org.bouncycastle.util.BigIntegers;
import com.myzuji.sadk.org.bouncycastle.util.Integers;

import java.math.BigInteger;
import java.util.Hashtable;
import java.util.Random;

public abstract class ECCurve {
    public static final int COORD_AFFINE = 0;
    public static final int COORD_HOMOGENEOUS = 1;
    public static final int COORD_JACOBIAN = 2;
    public static final int COORD_JACOBIAN_CHUDNOVSKY = 3;
    public static final int COORD_JACOBIAN_MODIFIED = 4;
    public static final int COORD_LAMBDA_AFFINE = 5;
    public static final int COORD_LAMBDA_PROJECTIVE = 6;
    public static final int COORD_SKEWED = 7;
    protected FiniteField field;
    protected ECFieldElement a;
    protected ECFieldElement b;
    protected BigInteger order;
    protected BigInteger cofactor;
    protected int coord = 0;
    protected ECEndomorphism endomorphism = null;
    protected ECMultiplier multiplier = null;

    public static int[] getAllCoordinateSystems() {
        return new int[]{0, 1, 2, 3, 4, 5, 6, 7};
    }

    protected ECCurve(FiniteField field) {
        this.field = field;
    }

    public abstract int getFieldSize();

    public abstract ECFieldElement fromBigInteger(BigInteger var1);

    public Config configure() {
        return new Config(this.coord, this.endomorphism, this.multiplier);
    }

    public ECPoint validatePoint(BigInteger x, BigInteger y) {
        ECPoint p = this.createPoint(x, y);
        if (!p.isValid()) {
            throw new IllegalArgumentException("Invalid point coordinates");
        } else {
            return p;
        }
    }


    public ECPoint validatePoint(BigInteger x, BigInteger y, boolean withCompression) {
        ECPoint p = this.createPoint(x, y, withCompression);
        if (!p.isValid()) {
            throw new IllegalArgumentException("Invalid point coordinates");
        } else {
            return p;
        }
    }

    public ECPoint createPoint(BigInteger x, BigInteger y) {
        return this.createPoint(x, y, false);
    }


    public ECPoint createPoint(BigInteger x, BigInteger y, boolean withCompression) {
        return this.createRawPoint(this.fromBigInteger(x), this.fromBigInteger(y), withCompression);
    }

    protected abstract ECCurve cloneCurve();

    protected abstract ECPoint createRawPoint(ECFieldElement var1, ECFieldElement var2, boolean var3);

    protected abstract ECPoint createRawPoint(ECFieldElement var1, ECFieldElement var2, ECFieldElement[] var3, boolean var4);

    protected ECMultiplier createDefaultMultiplier() {
        return (ECMultiplier) (this.endomorphism instanceof GLVEndomorphism ? new GLVMultiplier(this, (GLVEndomorphism) this.endomorphism) : new WNafL2RMultiplier());
    }

    public boolean supportsCoordinateSystem(int coord) {
        return coord == 0;
    }

    public PreCompInfo getPreCompInfo(ECPoint point, String name) {
        this.checkPoint(point);
        synchronized (point) {
            Hashtable table = point.preCompTable;
            return table == null ? null : (PreCompInfo) table.get(name);
        }
    }

    public void setPreCompInfo(ECPoint point, String name, PreCompInfo preCompInfo) {
        this.checkPoint(point);
        synchronized (point) {
            Hashtable table = point.preCompTable;
            if (null == table) {
                point.preCompTable = table = new Hashtable(4);
            }

            table.put(name, preCompInfo);
        }
    }

    public ECPoint importPoint(ECPoint p) {
        if (this == p.getCurve()) {
            return p;
        } else if (p.isInfinity()) {
            return this.getInfinity();
        } else {
            p = p.normalize();
            return this.validatePoint(p.getXCoord().toBigInteger(), p.getYCoord().toBigInteger(), p.withCompression);
        }
    }

    public void normalizeAll(ECPoint[] points) {
        this.checkPoints(points);
        if (this.getCoordinateSystem() != 0) {
            ECFieldElement[] zs = new ECFieldElement[points.length];
            int[] indices = new int[points.length];
            int count = 0;

            int j;
            for (j = 0; j < points.length; ++j) {
                ECPoint p = points[j];
                if (null != p && !p.isNormalized()) {
                    zs[count] = p.getZCoord(0);
                    indices[count++] = j;
                }
            }

            if (count != 0) {
                ECAlgorithms.montgomeryTrick(zs, 0, count);

                for (j = 0; j < count; ++j) {
                    int index = indices[j];
                    points[index] = points[index].normalize(zs[j]);
                }

            }
        }
    }

    public abstract ECPoint getInfinity();

    public FiniteField getField() {
        return this.field;
    }

    public ECFieldElement getA() {
        return this.a;
    }

    public ECFieldElement getB() {
        return this.b;
    }

    public BigInteger getOrder() {
        return this.order;
    }

    public BigInteger getCofactor() {
        return this.cofactor;
    }

    public int getCoordinateSystem() {
        return this.coord;
    }

    protected abstract ECPoint decompressPoint(int var1, BigInteger var2);

    public ECEndomorphism getEndomorphism() {
        return this.endomorphism;
    }

    public synchronized ECMultiplier getMultiplier() {
        if (this.multiplier == null) {
            this.multiplier = this.createDefaultMultiplier();
        }

        return this.multiplier;
    }

    public ECPoint decodePoint(byte[] encoded) {
        ECPoint p = null;
        int expectedLength = (this.getFieldSize() + 7) / 8;
        byte type = encoded[0];
        BigInteger X;
        BigInteger Y;
        switch (type) {
            case 0:
                if (encoded.length != 1) {
                    throw new IllegalArgumentException("Incorrect length for infinity encoding");
                }

                p = this.getInfinity();
                break;
            case 1:
            case 5:
            default:
                throw new IllegalArgumentException("Invalid point encoding 0x" + Integer.toString(type, 16));
            case 2:
            case 3:
                if (encoded.length != expectedLength + 1) {
                    throw new IllegalArgumentException("Incorrect length for compressed encoding");
                }

                int yTilde = type & 1;
                Y = BigIntegers.fromUnsignedByteArray(encoded, 1, expectedLength);
                p = this.decompressPoint(yTilde, Y);
                if (!p.satisfiesCofactor()) {
                    throw new IllegalArgumentException("Invalid point");
                }
                break;
            case 4:
                if (encoded.length != 2 * expectedLength + 1) {
                    throw new IllegalArgumentException("Incorrect length for uncompressed encoding");
                }

                X = BigIntegers.fromUnsignedByteArray(encoded, 1, expectedLength);
                Y = BigIntegers.fromUnsignedByteArray(encoded, 1 + expectedLength, expectedLength);
                p = this.validatePoint(X, Y);
                break;
            case 6:
            case 7:
                if (encoded.length != 2 * expectedLength + 1) {
                    throw new IllegalArgumentException("Incorrect length for hybrid encoding");
                }

                X = BigIntegers.fromUnsignedByteArray(encoded, 1, expectedLength);
                Y = BigIntegers.fromUnsignedByteArray(encoded, 1 + expectedLength, expectedLength);
                if (Y.testBit(0) != (type == 7)) {
                    throw new IllegalArgumentException("Inconsistent Y coordinate in hybrid encoding");
                }

                p = this.validatePoint(X, Y);
        }

        if (type != 0 && p.isInfinity()) {
            throw new IllegalArgumentException("Invalid infinity encoding");
        } else {
            return p;
        }
    }

    protected void checkPoint(ECPoint point) {
        if (null == point || this != point.getCurve()) {
            throw new IllegalArgumentException("'point' must be non-null and on this curve");
        }
    }

    protected void checkPoints(ECPoint[] points) {
        if (points == null) {
            throw new IllegalArgumentException("'points' cannot be null");
        } else {
            for (int i = 0; i < points.length; ++i) {
                ECPoint point = points[i];
                if (null != point && this != point.getCurve()) {
                    throw new IllegalArgumentException("'points' entries must be null or on this curve");
                }
            }

        }
    }

    public boolean equals(ECCurve other) {
        return this == other || null != other && this.getField().equals(other.getField()) && this.getA().toBigInteger().equals(other.getA().toBigInteger()) && this.getB().toBigInteger().equals(other.getB().toBigInteger());
    }

    public boolean equals(Object obj) {
        return this == obj || obj instanceof ECCurve && this.equals((ECCurve) obj);
    }

    public int hashCode() {
        return this.getField().hashCode() ^ Integers.rotateLeft(this.getA().toBigInteger().hashCode(), 8) ^ Integers.rotateLeft(this.getB().toBigInteger().hashCode(), 16);
    }

    public static class F2m extends AbstractF2m {
        private static final int F2M_DEFAULT_COORDS = 6;
        private int m;
        private int k1;
        private int k2;
        private int k3;
        private ECPoint.F2m infinity;
        private byte mu;
        private BigInteger[] si;

        public F2m(int m, int k, BigInteger a, BigInteger b) {
            this(m, k, 0, 0, (BigInteger) a, (BigInteger) b, (BigInteger) null, (BigInteger) null);
        }

        public F2m(int m, int k, BigInteger a, BigInteger b, BigInteger order, BigInteger cofactor) {
            this(m, k, 0, 0, (BigInteger) a, (BigInteger) b, order, cofactor);
        }

        public F2m(int m, int k1, int k2, int k3, BigInteger a, BigInteger b) {
            this(m, k1, k2, k3, (BigInteger) a, (BigInteger) b, (BigInteger) null, (BigInteger) null);
        }

        public F2m(int m, int k1, int k2, int k3, BigInteger a, BigInteger b, BigInteger order, BigInteger cofactor) {
            super(m, k1, k2, k3);
            this.mu = 0;
            this.si = null;
            this.m = m;
            this.k1 = k1;
            this.k2 = k2;
            this.k3 = k3;
            this.order = order;
            this.cofactor = cofactor;
            this.infinity = new ECPoint.F2m(this, (ECFieldElement) null, (ECFieldElement) null);
            this.a = this.fromBigInteger(a);
            this.b = this.fromBigInteger(b);
            this.coord = 6;
        }

        protected F2m(int m, int k1, int k2, int k3, ECFieldElement a, ECFieldElement b, BigInteger order, BigInteger cofactor) {
            super(m, k1, k2, k3);
            this.mu = 0;
            this.si = null;
            this.m = m;
            this.k1 = k1;
            this.k2 = k2;
            this.k3 = k3;
            this.order = order;
            this.cofactor = cofactor;
            this.infinity = new ECPoint.F2m(this, (ECFieldElement) null, (ECFieldElement) null);
            this.a = a;
            this.b = b;
            this.coord = 6;
        }

        protected ECCurve cloneCurve() {
            return new F2m(this.m, this.k1, this.k2, this.k3, this.a, this.b, this.order, this.cofactor);
        }

        public boolean supportsCoordinateSystem(int coord) {
            switch (coord) {
                case 0:
                case 1:
                case 6:
                    return true;
                default:
                    return false;
            }
        }

        protected ECMultiplier createDefaultMultiplier() {
            return (ECMultiplier) (this.isKoblitz() ? new WTauNafMultiplier() : super.createDefaultMultiplier());
        }

        public int getFieldSize() {
            return this.m;
        }

        public ECFieldElement fromBigInteger(BigInteger x) {
            return new ECFieldElement.F2m(this.m, this.k1, this.k2, this.k3, x);
        }

        public ECPoint createPoint(BigInteger x, BigInteger y, boolean withCompression) {
            ECFieldElement X = this.fromBigInteger(x);
            ECFieldElement Y = this.fromBigInteger(y);
            switch (this.getCoordinateSystem()) {
                case 5:
                case 6:
                    if (X.isZero()) {
                        if (!Y.square().equals(this.getB())) {
                            throw new IllegalArgumentException();
                        }
                    } else {
                        Y = Y.divide(X).add(X);
                    }
                default:
                    return this.createRawPoint(X, Y, withCompression);
            }
        }

        protected ECPoint createRawPoint(ECFieldElement x, ECFieldElement y, boolean withCompression) {
            return new ECPoint.F2m(this, x, y, withCompression);
        }

        protected ECPoint createRawPoint(ECFieldElement x, ECFieldElement y, ECFieldElement[] zs, boolean withCompression) {
            return new ECPoint.F2m(this, x, y, zs, withCompression);
        }

        public ECPoint getInfinity() {
            return this.infinity;
        }

        public boolean isKoblitz() {
            return this.order != null && this.cofactor != null && this.b.isOne() && (this.a.isZero() || this.a.isOne());
        }

        synchronized byte getMu() {
            if (this.mu == 0) {
                this.mu = Tnaf.getMu(this);
            }

            return this.mu;
        }

        synchronized BigInteger[] getSi() {
            if (this.si == null) {
                this.si = Tnaf.getSi(this);
            }

            return this.si;
        }

        protected ECPoint decompressPoint(int yTilde, BigInteger X1) {
            ECFieldElement x = this.fromBigInteger(X1);
            ECFieldElement y = null;
            if (x.isZero()) {
                y = this.b.sqrt();
            } else {
                ECFieldElement beta = x.square().invert().multiply(this.b).add(this.a).add(x);
                ECFieldElement z = this.solveQuadraticEquation(beta);
                if (z != null) {
                    if (z.testBitZero() != (yTilde == 1)) {
                        z = z.addOne();
                    }

                    switch (this.getCoordinateSystem()) {
                        case 5:
                        case 6:
                            y = z.add(x);
                            break;
                        default:
                            y = z.multiply(x);
                    }
                }
            }

            if (y == null) {
                throw new IllegalArgumentException("Invalid point compression");
            } else {
                return this.createRawPoint(x, y, true);
            }
        }

        private ECFieldElement solveQuadraticEquation(ECFieldElement beta) {
            if (beta.isZero()) {
                return beta;
            } else {
                ECFieldElement zeroElement = this.fromBigInteger(ECConstants.ZERO);
                ECFieldElement z = null;
                ECFieldElement gamma = null;
                Random rand = new Random();

                do {
                    ECFieldElement t = this.fromBigInteger(new BigInteger(this.m, rand));
                    z = zeroElement;
                    ECFieldElement w = beta;

                    for (int i = 1; i <= this.m - 1; ++i) {
                        ECFieldElement w2 = w.square();
                        z = z.square().add(w2.multiply(t));
                        w = w2.add(beta);
                    }

                    if (!w.isZero()) {
                        return null;
                    }

                    gamma = z.square().add(z);
                } while (gamma.isZero());

                return z;
            }
        }

        public int getM() {
            return this.m;
        }

        public boolean isTrinomial() {
            return this.k2 == 0 && this.k3 == 0;
        }

        public int getK1() {
            return this.k1;
        }

        public int getK2() {
            return this.k2;
        }

        public int getK3() {
            return this.k3;
        }


        public BigInteger getN() {
            return this.order;
        }


        public BigInteger getH() {
            return this.cofactor;
        }
    }

    public abstract static class AbstractF2m extends ECCurve {
        private static FiniteField buildField(int m, int k1, int k2, int k3) {
            if (k1 == 0) {
                throw new IllegalArgumentException("k1 must be > 0");
            } else if (k2 == 0) {
                if (k3 != 0) {
                    throw new IllegalArgumentException("k3 must be 0 if k2 == 0");
                } else {
                    return FiniteFields.getBinaryExtensionField(new int[]{0, k1, m});
                }
            } else if (k2 <= k1) {
                throw new IllegalArgumentException("k2 must be > k1");
            } else if (k3 <= k2) {
                throw new IllegalArgumentException("k3 must be > k2");
            } else {
                return FiniteFields.getBinaryExtensionField(new int[]{0, k1, k2, k3, m});
            }
        }

        protected AbstractF2m(int m, int k1, int k2, int k3) {
            super(buildField(m, k1, k2, k3));
        }
    }

    public static class Fp extends AbstractFp {
        private static final int FP_DEFAULT_COORDS = 4;
        BigInteger q;
        BigInteger r;
        ECPoint.Fp infinity;

        public Fp(BigInteger q, BigInteger a, BigInteger b) {
            this(q, a, b, (BigInteger) null, (BigInteger) null);
        }

        public Fp(BigInteger q, BigInteger a, BigInteger b, BigInteger order, BigInteger cofactor) {
            super(q);
            this.q = q;
            this.r = ECFieldElement.Fp.calculateResidue(q);
            this.infinity = new ECPoint.Fp(this, (ECFieldElement) null, (ECFieldElement) null);
            this.a = this.fromBigInteger(a);
            this.b = this.fromBigInteger(b);
            this.order = order;
            this.cofactor = cofactor;
            this.coord = 4;
        }

        protected Fp(BigInteger q, BigInteger r, ECFieldElement a, ECFieldElement b) {
            this(q, r, a, b, (BigInteger) null, (BigInteger) null);
        }

        protected Fp(BigInteger q, BigInteger r, ECFieldElement a, ECFieldElement b, BigInteger order, BigInteger cofactor) {
            super(q);
            this.q = q;
            this.r = r;
            this.infinity = new ECPoint.Fp(this, (ECFieldElement) null, (ECFieldElement) null);
            this.a = a;
            this.b = b;
            this.order = order;
            this.cofactor = cofactor;
            this.coord = 4;
        }

        protected ECCurve cloneCurve() {
            return new Fp(this.q, this.r, this.a, this.b, this.order, this.cofactor);
        }

        public boolean supportsCoordinateSystem(int coord) {
            switch (coord) {
                case 0:
                case 1:
                case 2:
                case 4:
                    return true;
                case 3:
                default:
                    return false;
            }
        }

        public BigInteger getQ() {
            return this.q;
        }

        public int getFieldSize() {
            return this.q.bitLength();
        }

        public ECFieldElement fromBigInteger(BigInteger x) {
            return new ECFieldElement.Fp(this.q, this.r, x);
        }

        protected ECPoint createRawPoint(ECFieldElement x, ECFieldElement y, boolean withCompression) {
            return new ECPoint.Fp(this, x, y, withCompression);
        }

        protected ECPoint createRawPoint(ECFieldElement x, ECFieldElement y, ECFieldElement[] zs, boolean withCompression) {
            return new ECPoint.Fp(this, x, y, zs, withCompression);
        }

        public ECPoint importPoint(ECPoint p) {
            if (this != p.getCurve() && this.getCoordinateSystem() == 2 && !p.isInfinity()) {
                switch (p.getCurve().getCoordinateSystem()) {
                    case 2:
                    case 3:
                    case 4:
                        return new ECPoint.Fp(this, this.fromBigInteger(p.x.toBigInteger()), this.fromBigInteger(p.y.toBigInteger()), new ECFieldElement[]{this.fromBigInteger(p.zs[0].toBigInteger())}, p.withCompression);
                }
            }

            return super.importPoint(p);
        }

        public ECPoint getInfinity() {
            return this.infinity;
        }
    }

    public abstract static class AbstractFp extends ECCurve {
        protected AbstractFp(BigInteger q) {
            super(FiniteFields.getPrimeField(q));
        }

        protected ECPoint decompressPoint(int yTilde, BigInteger X1) {
            ECFieldElement x = this.fromBigInteger(X1);
            ECFieldElement rhs = x.square().add(this.a).multiply(x).add(this.b);
            ECFieldElement y = rhs.sqrt();
            if (y == null) {
                throw new IllegalArgumentException("Invalid point compression");
            } else {
                if (y.testBitZero() != (yTilde == 1)) {
                    y = y.negate();
                }

                return this.createRawPoint(x, y, true);
            }
        }
    }

    public class Config {
        protected int coord;
        protected ECEndomorphism endomorphism;
        protected ECMultiplier multiplier;

        Config(int coord, ECEndomorphism endomorphism, ECMultiplier multiplier) {
            this.coord = coord;
            this.endomorphism = endomorphism;
            this.multiplier = multiplier;
        }

        public Config setCoordinateSystem(int coord) {
            this.coord = coord;
            return this;
        }

        public Config setEndomorphism(ECEndomorphism endomorphism) {
            this.endomorphism = endomorphism;
            return this;
        }

        public Config setMultiplier(ECMultiplier multiplier) {
            this.multiplier = multiplier;
            return this;
        }

        public ECCurve create() {
            if (!ECCurve.this.supportsCoordinateSystem(this.coord)) {
                throw new IllegalStateException("unsupported coordinate system");
            } else {
                ECCurve c = ECCurve.this.cloneCurve();
                if (c == ECCurve.this) {
                    throw new IllegalStateException("implementation returned current curve");
                } else {
                    c.coord = this.coord;
                    c.endomorphism = this.endomorphism;
                    c.multiplier = this.multiplier;
                    return c;
                }
            }
        }
    }
}

