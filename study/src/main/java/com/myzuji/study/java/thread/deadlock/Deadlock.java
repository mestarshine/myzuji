package com.myzuji.study.java.thread.deadlock;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/01
 */
public class Deadlock implements Runnable {
    A a = new A();
    B b = new B();

    public Deadlock() {
        Thread.currentThread().setName("MainThread");
        Thread thread = new Thread(this, "RacingThread");
        thread.start();
        a.foo(b);
        System.out.println("Back in main thread");
    }

    public static void main(String[] args) {
        new Deadlock();
    }

    @Override
    public void run() {
        b.bar(a);
        System.out.println("Back in other thread");
    }
}
