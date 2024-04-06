package com.myzuji.sadk.org.bouncycastle.jce.interfaces;

import com.myzuji.sadk.org.bouncycastle.jce.spec.ECParameterSpec;

public interface ECKey {

    ECParameterSpec getParams();

    ECParameterSpec getParameters();
}
