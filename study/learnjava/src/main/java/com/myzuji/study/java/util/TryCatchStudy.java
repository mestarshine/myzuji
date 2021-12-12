package com.myzuji.study.java.util;

/**
 * 1. try catch
 * 2. try catch finally
 * 3. try finally
 * 异常被 catch 后，会正常执行后面的程序
 * 在 try finally 组合里，当 try 块抛出异常 ，执行完 finally 部分，程序就终止了
 */
public class TryCatchStudy {

    public static void main(String[] args) {
        try {
            int i = 1/0;
        } catch (Exception e) {
        } finally {
            System.out.println(1);
        }
        System.out.println(123);
    }
}
