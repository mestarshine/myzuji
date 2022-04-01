package com.myzuji.study.java.util;

/**
 * 通过 标号 跳出多层循环
 * 1. 标号对查询一条记录比较有用，当查出自己想要的一条记录时，就可以跳出循环
 * 2. 标号必须紧接在循环的头部，不能使用在非循环语句的前面
 */
public class LabelStudy {

    public static void main(String[] args) {
        System.out.println("i : j : k");
        label:
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 50; j++) {
                for (int k = 0; k < 10; k++) {
                    if (k == 3) {
                        break label;
                    }
                    System.out.println(i + " : " + j + " : " + k);
                }
            }
        }
    }
}
