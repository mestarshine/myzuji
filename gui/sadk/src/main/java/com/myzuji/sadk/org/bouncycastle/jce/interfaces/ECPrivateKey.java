package com.myzuji.sadk.org.bouncycastle.jce.interfaces;

import com.myzuji.sadk.org.bouncycastle.jce.spec.ECParameterSpec;

import java.math.BigInteger;
import java.security.PrivateKey;

public interface ECPrivateKey extends ECKey, PrivateKey {
    BigInteger getD();

    @Override
    default ECParameterSpec getParams() {
        return null;
    }

    @Override
    default ECParameterSpec getParameters() {
        return null;
    }

    @Override
    default String getAlgorithm() {
        return "";
    }

    @Override
    default String getFormat() {
        return "";
    }

    @Override
    default byte[] getEncoded() {
        return new byte[0];
    }
}
