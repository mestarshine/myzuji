package com.myzuji.study.java.lambda;

import java.util.function.Function;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/04
 */
public class UseFunctionInterfaceDemo {

    public static void main(String[] args) {
        Function<Integer, Integer> function = (n) -> {
            int result = 1;
            for (int i = 1; i <= n; i++) {
                result = i * result;
            }
            return result;
        };
        System.out.println("3的阶乘：" + function.apply(3));
    }
}
