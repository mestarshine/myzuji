package com.myzuji.study.java.lang;


/**
 * 通过 Runtime 获取内存相关信息
 *
 * @author shine
 * @date 2020/03/06
 */
public class MemoryDemo {

    public static void main(String[] args) {
        long mem1, mem2;
        Integer[] someints = new Integer[1000];
        System.out.println("Total memory is :" + Runtime.getRuntime().totalMemory());
        mem1 = Runtime.getRuntime().freeMemory();
        System.out.println("Initial free memory :" + mem1);
        Runtime.getRuntime().gc();
        mem1 = Runtime.getRuntime().freeMemory();
        System.out.println("Free memory garbage collection :" + mem1);
        for (int i = 0; i < 1000; i++) {
            someints[i] = i;
        }
        mem2 = Runtime.getRuntime().freeMemory();
        System.out.println("Free memory after allocation :" + mem2);
        System.out.println("Memory used by allocation :" + (mem1 - mem2));
        for (int i = 0; i < 1000; i++) {
            someints[i] = null;
        }
        Runtime.getRuntime().gc();
        mem2 = Runtime.getRuntime().freeMemory();
        System.out.println("Free memory after collection discarded Integers :" + mem2);
    }
}
