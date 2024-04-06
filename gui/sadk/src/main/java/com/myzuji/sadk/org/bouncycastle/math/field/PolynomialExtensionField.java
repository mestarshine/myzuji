package com.myzuji.sadk.org.bouncycastle.math.field;

public interface PolynomialExtensionField extends ExtensionField {
    Polynomial getMinimalPolynomial();
}
