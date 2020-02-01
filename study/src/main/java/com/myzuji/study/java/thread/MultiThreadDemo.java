package com.myzuji.study.java.thread;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/01
 */
public class MultiThreadDemo {

    public static void main(String[] args) {
        new ThreadDemo("one");
        new ThreadDemo("two");
        new ThreadDemo("three");
        new ThreadDemo("four");
        new ThreadDemo("five");
        new ThreadDemo("six");
        new ThreadDemo("seven");
        new ThreadDemo("eight");
        new ThreadDemo("nine");
        new ThreadDemo("ten");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            System.out.println("主线程中断");
        }
        System.out.println("主线程退出");
    }
}
