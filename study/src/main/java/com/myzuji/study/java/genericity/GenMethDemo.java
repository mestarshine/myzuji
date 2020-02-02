package com.myzuji.study.java.genericity;

/**
 * 泛型方法的用法
 *
 * @author shine
 * @date 2020/02/02
 */
public class GenMethDemo {

    public static void main(String[] args) {
        Integer[] nums = {1, 2, 3, 4, 5};
        if (isIn(2, nums)) {
            System.out.println("2 is in nums");
        }
    }

    static <T extends Comparable<T>, V extends T> boolean isIn(T x, V[] y) {
        for (int i = 0; i < y.length; i++) {
            if (x.equals(y[i])) {
                return true;
            }
        }
        return false;
    }


}
