package com.myzuji.sadk.org.bouncycastle.math.ec.custom.djb;

import com.myzuji.sadk.org.bouncycastle.math.ec.ECFieldElement;
import com.myzuji.sadk.org.bouncycastle.math.raw.Mod;
import com.myzuji.sadk.org.bouncycastle.math.raw.Nat256;
import com.myzuji.sadk.org.bouncycastle.util.Arrays;

import java.math.BigInteger;

public class Curve25519FieldElement extends ECFieldElement {
    public static final BigInteger Q;
    private static final int[] PRECOMP_POW2;
    protected int[] x;

    public Curve25519FieldElement(BigInteger x) {
        if (x != null && x.signum() >= 0 && x.compareTo(Q) < 0) {
            this.x = Curve25519Field.fromBigInteger(x);
        } else {
            throw new IllegalArgumentException("x value invalid for Curve25519FieldElement");
        }
    }

    public Curve25519FieldElement() {
        this.x = Nat256.create();
    }

    protected Curve25519FieldElement(int[] x) {
        this.x = x;
    }

    public boolean isZero() {
        return Nat256.isZero(this.x);
    }

    public boolean isOne() {
        return Nat256.isOne(this.x);
    }

    public boolean testBitZero() {
        return Nat256.getBit(this.x, 0) == 1;
    }

    public BigInteger toBigInteger() {
        return Nat256.toBigInteger(this.x);
    }

    public String getFieldName() {
        return "Curve25519Field";
    }

    public int getFieldSize() {
        return Q.bitLength();
    }

    public ECFieldElement add(ECFieldElement b) {
        int[] z = Nat256.create();
        Curve25519Field.add(this.x, ((Curve25519FieldElement) b).x, z);
        return new Curve25519FieldElement(z);
    }

    public ECFieldElement addOne() {
        int[] z = Nat256.create();
        Curve25519Field.addOne(this.x, z);
        return new Curve25519FieldElement(z);
    }

    public ECFieldElement subtract(ECFieldElement b) {
        int[] z = Nat256.create();
        Curve25519Field.subtract(this.x, ((Curve25519FieldElement) b).x, z);
        return new Curve25519FieldElement(z);
    }

    public ECFieldElement multiply(ECFieldElement b) {
        int[] z = Nat256.create();
        Curve25519Field.multiply(this.x, ((Curve25519FieldElement) b).x, z);
        return new Curve25519FieldElement(z);
    }

    public ECFieldElement divide(ECFieldElement b) {
        int[] z = Nat256.create();
        Mod.invert(Curve25519Field.P, ((Curve25519FieldElement) b).x, z);
        Curve25519Field.multiply(z, this.x, z);
        return new Curve25519FieldElement(z);
    }

    public ECFieldElement negate() {
        int[] z = Nat256.create();
        Curve25519Field.negate(this.x, z);
        return new Curve25519FieldElement(z);
    }

    public ECFieldElement square() {
        int[] z = Nat256.create();
        Curve25519Field.square(this.x, z);
        return new Curve25519FieldElement(z);
    }

    public ECFieldElement invert() {
        int[] z = Nat256.create();
        Mod.invert(Curve25519Field.P, this.x, z);
        return new Curve25519FieldElement(z);
    }

    public ECFieldElement sqrt() {
        int[] x1 = this.x;
        if (!Nat256.isZero(x1) && !Nat256.isOne(x1)) {
            int[] x2 = Nat256.create();
            Curve25519Field.square(x1, x2);
            Curve25519Field.multiply(x2, x1, x2);
            int[] x3 = x2;
            Curve25519Field.square(x2, x3);
            Curve25519Field.multiply(x3, x1, x3);
            int[] x4 = Nat256.create();
            Curve25519Field.square(x3, x4);
            Curve25519Field.multiply(x4, x1, x4);
            int[] x7 = Nat256.create();
            Curve25519Field.squareN(x4, 3, x7);
            Curve25519Field.multiply(x7, x3, x7);
            int[] x11 = x3;
            Curve25519Field.squareN(x7, 4, x11);
            Curve25519Field.multiply(x11, x4, x11);
            int[] x15 = x7;
            Curve25519Field.squareN(x11, 4, x15);
            Curve25519Field.multiply(x15, x4, x15);
            int[] x30 = x4;
            Curve25519Field.squareN(x15, 15, x30);
            Curve25519Field.multiply(x30, x15, x30);
            int[] x60 = x15;
            Curve25519Field.squareN(x30, 30, x60);
            Curve25519Field.multiply(x60, x30, x60);
            int[] x120 = x30;
            Curve25519Field.squareN(x60, 60, x120);
            Curve25519Field.multiply(x120, x60, x120);
            int[] x131 = x60;
            Curve25519Field.squareN(x120, 11, x131);
            Curve25519Field.multiply(x131, x11, x131);
            int[] x251 = x11;
            Curve25519Field.squareN(x131, 120, x251);
            Curve25519Field.multiply(x251, x120, x251);
            int[] t1 = x251;
            Curve25519Field.square(t1, t1);
            int[] t2 = x120;
            Curve25519Field.square(t1, t2);
            if (Nat256.eq(x1, t2)) {
                return new Curve25519FieldElement(t1);
            } else {
                Curve25519Field.multiply(t1, PRECOMP_POW2, t1);
                Curve25519Field.square(t1, t2);
                return Nat256.eq(x1, t2) ? new Curve25519FieldElement(t1) : null;
            }
        } else {
            return this;
        }
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (!(other instanceof Curve25519FieldElement)) {
            return false;
        } else {
            Curve25519FieldElement o = (Curve25519FieldElement) other;
            return Nat256.eq(this.x, o.x);
        }
    }

    public int hashCode() {
        return Q.hashCode() ^ Arrays.hashCode(this.x, 0, 8);
    }

    static {
        Q = Curve25519.q;
        PRECOMP_POW2 = new int[]{1242472624, -991028441, -1389370248, 792926214, 1039914919, 726466713, 1338105611, 730014848};
    }
}
