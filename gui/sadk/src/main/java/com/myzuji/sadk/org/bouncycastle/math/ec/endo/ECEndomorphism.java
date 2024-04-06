package com.myzuji.sadk.org.bouncycastle.math.ec.endo;

import com.myzuji.sadk.org.bouncycastle.math.ec.ECPointMap;

public interface ECEndomorphism {
    ECPointMap getPointMap();

    boolean hasEfficientPointMap();
}
