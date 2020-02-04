package com.myzuji.study.java.lambda;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/04
 */
public class MyStringOps {

    static String strReverses(String str) {
        String result = "";
        int i;
        for (i = str.length() - 1; i >= 0; i--) {
            result += str.charAt(i);
        }
        return result;
    }

    String strReverse(String str) {
        String result = "";
        int i;
        for (i = str.length() - 1; i >= 0; i--) {
            result += str.charAt(i);
        }
        return result;
    }
}
