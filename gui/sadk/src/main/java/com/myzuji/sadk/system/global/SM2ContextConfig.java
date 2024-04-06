package com.myzuji.sadk.system.global;

public class SM2ContextConfig {
    private static String _version = "3.2.X.X";
    private static boolean _useZValue = true;
    private static int _Sign_Format = 3;
    private static boolean _isBase64State = true;

    public SM2ContextConfig() {
    }

    public static String getVersion() {
        return _version;
    }

    public static void setUseZValue(boolean useZValueFlag) {
        _useZValue = useZValueFlag;
    }

    public static boolean getUseZValue() {
        return _useZValue;
    }

    public static int getSignFormat() {
        return _Sign_Format;
    }

    public static void setSignFormat(int SIGN_FORMAT) {
        _Sign_Format = SIGN_FORMAT;
    }

    public static void setBase64State(boolean isBase64State) {
        _isBase64State = isBase64State;
    }

    public static boolean getBase64State() {
        return _isBase64State;
    }
}

