package com.myzuji.sadk.system.global;

import com.myzuji.sadk.system.CompatibleConfig;

public class P10RequestContextConfig {
    private static boolean _isVerifyP10Request;

    public P10RequestContextConfig() {
    }

    public static boolean getP10RequestVerifyState() {
        return _isVerifyP10Request;
    }

    public static void setP10RequestVerifyState(boolean isVerifyP10Request) {
        _isVerifyP10Request = isVerifyP10Request;
    }

    static {
        _isVerifyP10Request = CompatibleConfig.P10RequestVerifiedSignedFlag;
    }
}
