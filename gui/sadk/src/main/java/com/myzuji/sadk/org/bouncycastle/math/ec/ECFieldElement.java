package com.myzuji.sadk.org.bouncycastle.math.ec;

import com.myzuji.sadk.org.bouncycastle.math.raw.Mod;
import com.myzuji.sadk.org.bouncycastle.math.raw.Nat;
import com.myzuji.sadk.org.bouncycastle.util.Arrays;
import com.myzuji.sadk.org.bouncycastle.util.BigIntegers;

import java.math.BigInteger;
import java.util.Random;

public abstract class ECFieldElement implements ECConstants {
    public ECFieldElement() {
    }

    public abstract BigInteger toBigInteger();

    public abstract String getFieldName();

    public abstract int getFieldSize();

    public abstract ECFieldElement add(ECFieldElement var1);

    public abstract ECFieldElement addOne();

    public abstract ECFieldElement subtract(ECFieldElement var1);

    public abstract ECFieldElement multiply(ECFieldElement var1);

    public abstract ECFieldElement divide(ECFieldElement var1);

    public abstract ECFieldElement negate();

    public abstract ECFieldElement square();

    public abstract ECFieldElement invert();

    public abstract ECFieldElement sqrt();

    public int bitLength() {
        return this.toBigInteger().bitLength();
    }

    public boolean isOne() {
        return this.bitLength() == 1;
    }

    public boolean isZero() {
        return 0 == this.toBigInteger().signum();
    }

    public ECFieldElement multiplyMinusProduct(ECFieldElement b, ECFieldElement x, ECFieldElement y) {
        return this.multiply(b).subtract(x.multiply(y));
    }

    public ECFieldElement multiplyPlusProduct(ECFieldElement b, ECFieldElement x, ECFieldElement y) {
        return this.multiply(b).add(x.multiply(y));
    }

    public ECFieldElement squareMinusProduct(ECFieldElement x, ECFieldElement y) {
        return this.square().subtract(x.multiply(y));
    }

    public ECFieldElement squarePlusProduct(ECFieldElement x, ECFieldElement y) {
        return this.square().add(x.multiply(y));
    }

    public boolean testBitZero() {
        return this.toBigInteger().testBit(0);
    }

    public String toString() {
        return this.toBigInteger().toString(16);
    }

    public byte[] getEncoded() {
        return BigIntegers.asUnsignedByteArray((this.getFieldSize() + 7) / 8, this.toBigInteger());
    }

    public static class F2m extends ECFieldElement {
        public static final int GNB = 1;
        public static final int TPB = 2;
        public static final int PPB = 3;
        private int representation;
        private int m;
        private int[] ks;
        private LongArray x;


        public F2m(int m, int k1, int k2, int k3, BigInteger x) {
            if (k2 == 0 && k3 == 0) {
                this.representation = 2;
                this.ks = new int[]{k1};
            } else {
                if (k2 >= k3) {
                    throw new IllegalArgumentException("k2 must be smaller than k3");
                }

                if (k2 <= 0) {
                    throw new IllegalArgumentException("k2 must be larger than 0");
                }

                this.representation = 3;
                this.ks = new int[]{k1, k2, k3};
            }

            this.m = m;
            this.x = new LongArray(x);
        }


        public F2m(int m, int k, BigInteger x) {
            this(m, k, 0, 0, x);
        }

        private F2m(int m, int[] ks, LongArray x) {
            this.m = m;
            this.representation = ks.length == 1 ? 2 : 3;
            this.ks = ks;
            this.x = x;
        }

        public int bitLength() {
            return this.x.degree();
        }

        public boolean isOne() {
            return this.x.isOne();
        }

        public boolean isZero() {
            return this.x.isZero();
        }

        public boolean testBitZero() {
            return this.x.testBitZero();
        }

        public BigInteger toBigInteger() {
            return this.x.toBigInteger();
        }

        public String getFieldName() {
            return "F2m";
        }

        public int getFieldSize() {
            return this.m;
        }

        public static void checkFieldElements(ECFieldElement a, ECFieldElement b) {
            if (a instanceof F2m && b instanceof F2m) {
                F2m aF2m = (F2m) a;
                F2m bF2m = (F2m) b;
                if (aF2m.representation != bF2m.representation) {
                    throw new IllegalArgumentException("One of the F2m field elements has incorrect representation");
                } else if (aF2m.m != bF2m.m || !Arrays.areEqual(aF2m.ks, bF2m.ks)) {
                    throw new IllegalArgumentException("Field elements are not elements of the same field F2m");
                }
            } else {
                throw new IllegalArgumentException("Field elements are not both instances of ECFieldElement.F2m");
            }
        }

        public ECFieldElement add(ECFieldElement b) {
            LongArray iarrClone = (LongArray) this.x.clone();
            F2m bF2m = (F2m) b;
            iarrClone.addShiftedByWords(bF2m.x, 0);
            return new F2m(this.m, this.ks, iarrClone);
        }

        public ECFieldElement addOne() {
            return new F2m(this.m, this.ks, this.x.addOne());
        }

        public ECFieldElement subtract(ECFieldElement b) {
            return this.add(b);
        }

        public ECFieldElement multiply(ECFieldElement b) {
            return new F2m(this.m, this.ks, this.x.modMultiply(((F2m) b).x, this.m, this.ks));
        }

        public ECFieldElement multiplyMinusProduct(ECFieldElement b, ECFieldElement x, ECFieldElement y) {
            return this.multiplyPlusProduct(b, x, y);
        }

        public ECFieldElement multiplyPlusProduct(ECFieldElement b, ECFieldElement x, ECFieldElement y) {
            LongArray ax = this.x;
            LongArray bx = ((F2m) b).x;
            LongArray xx = ((F2m) x).x;
            LongArray yx = ((F2m) y).x;
            LongArray ab = ax.multiply(bx, this.m, this.ks);
            LongArray xy = xx.multiply(yx, this.m, this.ks);
            if (ab == ax || ab == bx) {
                ab = (LongArray) ab.clone();
            }

            ab.addShiftedByWords(xy, 0);
            ab.reduce(this.m, this.ks);
            return new F2m(this.m, this.ks, ab);
        }

        public ECFieldElement divide(ECFieldElement b) {
            ECFieldElement bInv = b.invert();
            return this.multiply(bInv);
        }

        public ECFieldElement negate() {
            return this;
        }

        public ECFieldElement square() {
            return new F2m(this.m, this.ks, this.x.modSquare(this.m, this.ks));
        }

        public ECFieldElement squareMinusProduct(ECFieldElement x, ECFieldElement y) {
            return this.squarePlusProduct(x, y);
        }

        public ECFieldElement squarePlusProduct(ECFieldElement x, ECFieldElement y) {
            LongArray ax = this.x;
            LongArray xx = ((F2m) x).x;
            LongArray yx = ((F2m) y).x;
            LongArray aa = ax.square(this.m, this.ks);
            LongArray xy = xx.multiply(yx, this.m, this.ks);
            if (aa == ax) {
                aa = (LongArray) aa.clone();
            }

            aa.addShiftedByWords(xy, 0);
            aa.reduce(this.m, this.ks);
            return new F2m(this.m, this.ks, aa);
        }

        public ECFieldElement invert() {
            return new F2m(this.m, this.ks, this.x.modInverse(this.m, this.ks));
        }

        public ECFieldElement sqrt() {
            LongArray x1 = this.x;
            if (!x1.isOne() && !x1.isZero()) {
                LongArray x2 = x1.modSquareN(this.m - 1, this.m, this.ks);
                return new F2m(this.m, this.ks, x2);
            } else {
                return this;
            }
        }

        public int getRepresentation() {
            return this.representation;
        }

        public int getM() {
            return this.m;
        }

        public int getK1() {
            return this.ks[0];
        }

        public int getK2() {
            return this.ks.length >= 2 ? this.ks[1] : 0;
        }

        public int getK3() {
            return this.ks.length >= 3 ? this.ks[2] : 0;
        }

        public boolean equals(Object anObject) {
            if (anObject == this) {
                return true;
            } else if (!(anObject instanceof F2m)) {
                return false;
            } else {
                F2m b = (F2m) anObject;
                return this.m == b.m && this.representation == b.representation && Arrays.areEqual(this.ks, b.ks) && this.x.equals(b.x);
            }
        }

        public int hashCode() {
            return this.x.hashCode() ^ this.m ^ Arrays.hashCode(this.ks);
        }
    }

    public static class Fp extends ECFieldElement {
        BigInteger q;
        BigInteger r;
        BigInteger x;

        static BigInteger calculateResidue(BigInteger p) {
            int bitLength = p.bitLength();
            if (bitLength >= 96) {
                BigInteger firstWord = p.shiftRight(bitLength - 64);
                if (firstWord.longValue() == -1L) {
                    return ONE.shiftLeft(bitLength).subtract(p);
                }
            }

            return null;
        }


        public Fp(BigInteger q, BigInteger x) {
            this(q, calculateResidue(q), x);
        }

        Fp(BigInteger q, BigInteger r, BigInteger x) {
            if (x != null && x.signum() >= 0 && x.compareTo(q) < 0) {
                this.q = q;
                this.r = r;
                this.x = x;
            } else {
                throw new IllegalArgumentException("x value invalid in Fp field element");
            }
        }

        public BigInteger toBigInteger() {
            return this.x;
        }

        public String getFieldName() {
            return "Fp";
        }

        public int getFieldSize() {
            return this.q.bitLength();
        }

        public BigInteger getQ() {
            return this.q;
        }

        public ECFieldElement add(ECFieldElement b) {
            return new Fp(this.q, this.r, this.modAdd(this.x, b.toBigInteger()));
        }

        public ECFieldElement addOne() {
            BigInteger x2 = this.x.add(ONE);
            if (x2.compareTo(this.q) == 0) {
                x2 = ZERO;
            }

            return new Fp(this.q, this.r, x2);
        }

        public ECFieldElement subtract(ECFieldElement b) {
            return new Fp(this.q, this.r, this.modSubtract(this.x, b.toBigInteger()));
        }

        public ECFieldElement multiply(ECFieldElement b) {
            return new Fp(this.q, this.r, this.modMult(this.x, b.toBigInteger()));
        }

        public ECFieldElement multiplyMinusProduct(ECFieldElement b, ECFieldElement x, ECFieldElement y) {
            BigInteger ax = this.x;
            BigInteger bx = b.toBigInteger();
            BigInteger xx = x.toBigInteger();
            BigInteger yx = y.toBigInteger();
            BigInteger ab = ax.multiply(bx);
            BigInteger xy = xx.multiply(yx);
            return new Fp(this.q, this.r, this.modReduce(ab.subtract(xy)));
        }

        public ECFieldElement multiplyPlusProduct(ECFieldElement b, ECFieldElement x, ECFieldElement y) {
            BigInteger ax = this.x;
            BigInteger bx = b.toBigInteger();
            BigInteger xx = x.toBigInteger();
            BigInteger yx = y.toBigInteger();
            BigInteger ab = ax.multiply(bx);
            BigInteger xy = xx.multiply(yx);
            return new Fp(this.q, this.r, this.modReduce(ab.add(xy)));
        }

        public ECFieldElement divide(ECFieldElement b) {
            return new Fp(this.q, this.r, this.modMult(this.x, this.modInverse(b.toBigInteger())));
        }

        public ECFieldElement negate() {
            return this.x.signum() == 0 ? this : new Fp(this.q, this.r, this.q.subtract(this.x));
        }

        public ECFieldElement square() {
            return new Fp(this.q, this.r, this.modMult(this.x, this.x));
        }

        public ECFieldElement squareMinusProduct(ECFieldElement x, ECFieldElement y) {
            BigInteger ax = this.x;
            BigInteger xx = x.toBigInteger();
            BigInteger yx = y.toBigInteger();
            BigInteger aa = ax.multiply(ax);
            BigInteger xy = xx.multiply(yx);
            return new Fp(this.q, this.r, this.modReduce(aa.subtract(xy)));
        }

        public ECFieldElement squarePlusProduct(ECFieldElement x, ECFieldElement y) {
            BigInteger ax = this.x;
            BigInteger xx = x.toBigInteger();
            BigInteger yx = y.toBigInteger();
            BigInteger aa = ax.multiply(ax);
            BigInteger xy = xx.multiply(yx);
            return new Fp(this.q, this.r, this.modReduce(aa.add(xy)));
        }

        public ECFieldElement invert() {
            return new Fp(this.q, this.r, this.modInverse(this.x));
        }

        public ECFieldElement sqrt() {
            if (!this.isZero() && !this.isOne()) {
                if (!this.q.testBit(0)) {
                    throw new RuntimeException("not done yet");
                } else {
                    BigInteger legendreExponent;
                    if (this.q.testBit(1)) {
                        legendreExponent = this.q.shiftRight(2).add(ONE);
                        return this.checkSqrt(new Fp(this.q, this.r, this.x.modPow(legendreExponent, this.q)));
                    } else {
                        BigInteger X;
                        BigInteger fourX;
                        BigInteger k;
                        BigInteger qMinusOne;
                        if (this.q.testBit(2)) {
                            legendreExponent = this.x.modPow(this.q.shiftRight(3), this.q);
                            X = this.modMult(legendreExponent, this.x);
                            fourX = this.modMult(X, legendreExponent);
                            if (fourX.equals(ONE)) {
                                return this.checkSqrt(new Fp(this.q, this.r, X));
                            } else {
                                k = TWO.modPow(this.q.shiftRight(2), this.q);
                                qMinusOne = this.modMult(X, k);
                                return this.checkSqrt(new Fp(this.q, this.r, qMinusOne));
                            }
                        } else {
                            legendreExponent = this.q.shiftRight(1);
                            if (!this.x.modPow(legendreExponent, this.q).equals(ONE)) {
                                return null;
                            } else {
                                X = this.x;
                                fourX = this.modDouble(this.modDouble(X));
                                k = legendreExponent.add(ONE);
                                qMinusOne = this.q.subtract(ONE);
                                Random rand = new Random();

                                while (true) {
                                    BigInteger P;
                                    do {
                                        P = new BigInteger(this.q.bitLength(), rand);
                                    } while (P.compareTo(this.q) >= 0);

                                    if (this.modReduce(P.multiply(P).subtract(fourX)).modPow(legendreExponent, this.q).equals(qMinusOne)) {
                                        BigInteger[] result = this.lucasSequence(P, X, k);
                                        BigInteger U = result[0];
                                        BigInteger V = result[1];
                                        if (this.modMult(V, V).equals(fourX)) {
                                            return new Fp(this.q, this.r, this.modHalfAbs(V));
                                        }

                                        if (!U.equals(ONE) && !U.equals(qMinusOne)) {
                                            return null;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                return this;
            }
        }

        private ECFieldElement checkSqrt(ECFieldElement z) {
            return z.square().equals(this) ? z : null;
        }

        private BigInteger[] lucasSequence(BigInteger P, BigInteger Q, BigInteger k) {
            int n = k.bitLength();
            int s = k.getLowestSetBit();
            BigInteger Uh = ONE;
            BigInteger Vl = TWO;
            BigInteger Vh = P;
            BigInteger Ql = ONE;
            BigInteger Qh = ONE;

            int j;
            for (j = n - 1; j >= s + 1; --j) {
                Ql = this.modMult(Ql, Qh);
                if (k.testBit(j)) {
                    Qh = this.modMult(Ql, Q);
                    Uh = this.modMult(Uh, Vh);
                    Vl = this.modReduce(Vh.multiply(Vl).subtract(P.multiply(Ql)));
                    Vh = this.modReduce(Vh.multiply(Vh).subtract(Qh.shiftLeft(1)));
                } else {
                    Qh = Ql;
                    Uh = this.modReduce(Uh.multiply(Vl).subtract(Ql));
                    Vh = this.modReduce(Vh.multiply(Vl).subtract(P.multiply(Ql)));
                    Vl = this.modReduce(Vl.multiply(Vl).subtract(Ql.shiftLeft(1)));
                }
            }

            Ql = this.modMult(Ql, Qh);
            Qh = this.modMult(Ql, Q);
            Uh = this.modReduce(Uh.multiply(Vl).subtract(Ql));
            Vl = this.modReduce(Vh.multiply(Vl).subtract(P.multiply(Ql)));
            Ql = this.modMult(Ql, Qh);

            for (j = 1; j <= s; ++j) {
                Uh = this.modMult(Uh, Vl);
                Vl = this.modReduce(Vl.multiply(Vl).subtract(Ql.shiftLeft(1)));
                Ql = this.modMult(Ql, Ql);
            }

            return new BigInteger[]{Uh, Vl};
        }

        protected BigInteger modAdd(BigInteger x1, BigInteger x2) {
            BigInteger x3 = x1.add(x2);
            if (x3.compareTo(this.q) >= 0) {
                x3 = x3.subtract(this.q);
            }

            return x3;
        }

        protected BigInteger modDouble(BigInteger x) {
            BigInteger _2x = x.shiftLeft(1);
            if (_2x.compareTo(this.q) >= 0) {
                _2x = _2x.subtract(this.q);
            }

            return _2x;
        }

        protected BigInteger modHalf(BigInteger x) {
            if (x.testBit(0)) {
                x = this.q.add(x);
            }

            return x.shiftRight(1);
        }

        protected BigInteger modHalfAbs(BigInteger x) {
            if (x.testBit(0)) {
                x = this.q.subtract(x);
            }

            return x.shiftRight(1);
        }

        protected BigInteger modInverse(BigInteger x) {
            int bits = this.getFieldSize();
            int len = bits + 31 >> 5;
            int[] p = Nat.fromBigInteger(bits, this.q);
            int[] n = Nat.fromBigInteger(bits, x);
            int[] z = Nat.create(len);
            Mod.invert(p, n, z);
            return Nat.toBigInteger(len, z);
        }

        protected BigInteger modMult(BigInteger x1, BigInteger x2) {
            return this.modReduce(x1.multiply(x2));
        }

        protected BigInteger modReduce(BigInteger x) {
            if (this.r != null) {
                boolean negative = x.signum() < 0;
                if (negative) {
                    x = x.abs();
                }

                int qLen = this.q.bitLength();

                BigInteger u;
                BigInteger v;
                for (boolean rIsOne = this.r.equals(ONE); x.bitLength() > qLen + 1; x = u.add(v)) {
                    u = x.shiftRight(qLen);
                    v = x.subtract(u.shiftLeft(qLen));
                    if (!rIsOne) {
                        u = u.multiply(this.r);
                    }
                }

                while (x.compareTo(this.q) >= 0) {
                    x = x.subtract(this.q);
                }

                if (negative && x.signum() != 0) {
                    x = this.q.subtract(x);
                }
            } else {
                x = x.mod(this.q);
            }

            return x;
        }

        protected BigInteger modSubtract(BigInteger x1, BigInteger x2) {
            BigInteger x3 = x1.subtract(x2);
            if (x3.signum() < 0) {
                x3 = x3.add(this.q);
            }

            return x3;
        }

        public boolean equals(Object other) {
            if (other == this) {
                return true;
            } else if (!(other instanceof Fp)) {
                return false;
            } else {
                Fp o = (Fp) other;
                return this.q.equals(o.q) && this.x.equals(o.x);
            }
        }

        public int hashCode() {
            return this.q.hashCode() ^ this.x.hashCode();
        }
    }
}
