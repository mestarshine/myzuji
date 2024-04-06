package com.myzuji.sadk.org.bouncycastle.math.field;

import com.myzuji.sadk.org.bouncycastle.util.Integers;

import java.math.BigInteger;

public class GenericPolynomialExtensionField implements PolynomialExtensionField {
    protected final FiniteField subfield;
    protected final Polynomial minimalPolynomial;

    GenericPolynomialExtensionField(FiniteField subfield, Polynomial polynomial) {
        this.subfield = subfield;
        this.minimalPolynomial = polynomial;
    }

    public BigInteger getCharacteristic() {
        return this.subfield.getCharacteristic();
    }

    public int getDimension() {
        return this.subfield.getDimension() * this.minimalPolynomial.getDegree();
    }

    public FiniteField getSubfield() {
        return this.subfield;
    }

    public int getDegree() {
        return this.minimalPolynomial.getDegree();
    }

    public Polynomial getMinimalPolynomial() {
        return this.minimalPolynomial;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof GenericPolynomialExtensionField)) {
            return false;
        } else {
            GenericPolynomialExtensionField other = (GenericPolynomialExtensionField) obj;
            return this.subfield.equals(other.subfield) && this.minimalPolynomial.equals(other.minimalPolynomial);
        }
    }

    public int hashCode() {
        return this.subfield.hashCode() ^ Integers.rotateLeft(this.minimalPolynomial.hashCode(), 16);
    }
}
