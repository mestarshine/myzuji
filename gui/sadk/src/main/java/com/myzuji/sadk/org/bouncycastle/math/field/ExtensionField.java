package com.myzuji.sadk.org.bouncycastle.math.field;

public interface ExtensionField extends FiniteField {
    FiniteField getSubfield();

    int getDegree();
}
