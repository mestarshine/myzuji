package com.myzuji.study.java.lang;

public class BitOperationStudy {

    /**
     * 位运算符主要针对二进制，它包括了：“与”、“或”、“非”、“异或”
     * 主要针对两个二进制数的位进行逻辑运算
     * '&' 与运算符，两个操作位都为1，结果才为1，否则结果为0
     * '|' 或运算符，两个操作位只要有一个为1，那么结果就是1，否则就为0
     * ‘~’ 非运算符，位为0，结果是1，如果位为1，结果是0
     * '^' 异或运算符，两个操作位，相同则结果为0，不同则结果为1
     *
     * @param args
     */
    public static void main(String[] args) {
        int a = 12;
        int b = 10;
        System.out.println("a二进制：" + Integer.toBinaryString(a));
        System.out.println("b二进制：" + Integer.toBinaryString(b));
        System.out.println("a & b 的结果是：" + (a & b));
        System.out.println("a & b 的结果是：" + Integer.toBinaryString(a & b));
        System.out.println("a | b 的结果是：" + (a | b));
        System.out.println("a | b 的结果是：" + Integer.toBinaryString(a | b));
        System.out.println("~b 的结果是：" + (~b));
        System.out.println("~b 的结果是：" + Integer.toBinaryString(~b));
        System.out.println("a ^ b 的结果是：" + (a ^ b));
        System.out.println("a ^ b 的结果是：" + Integer.toBinaryString(a ^ b));
    }
}
