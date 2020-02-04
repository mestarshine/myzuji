package com.myzuji.study.java.lambda;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/04
 */
public class LambdasAsArgumentsDemo {

    static String stringOp(StringFunc sf, String s) {
        return sf.func(s);
    }

    public static void main(String[] args) {
        String inStr = "lambdas 增强了 java 功能";
        String outStr;
        System.out.println("原始字符串：" + inStr);
        outStr = stringOp((str) -> str.toUpperCase(), inStr);
        System.out.println("大写后的字符串：" + outStr);
        outStr = stringOp((str) -> {
            String result = "";
            int i;
            for (i = 0; i < str.length(); i++) {
                if (str.charAt(i) != ' ') {
                    result += str.charAt(i);
                }
            }
            return result;
        }, inStr);
        System.out.println("移除空格后的字符串：" + outStr);

        StringFunc reverse = (str) -> {
            String result = "";
            int i;
            for (i = str.length() - 1; i >= 0; i--) {
                result += str.charAt(i);
            }
            return result;
        };
        System.out.println("反写：" + stringOp(reverse, inStr));
    }
}
