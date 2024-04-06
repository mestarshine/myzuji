package com.myzuji.sadk.org.bouncycastle.jce.interfaces;

import com.myzuji.sadk.org.bouncycastle.jce.spec.ECParameterSpec;
import com.myzuji.sadk.org.bouncycastle.math.ec.ECPoint;

import java.security.PublicKey;

public interface ECPublicKey extends ECKey, PublicKey {
    ECPoint getQ();

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
