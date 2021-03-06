package com.myzuji.study.java.thread.multiple.sync;

import java.util.concurrent.TimeUnit;

/**
 * synchronized 是可重入锁
 */
public class ParentSubSynTest {

    public static void main(String[] args) {
        new SubT().doSomething();
    }
}

class ParentT {
    synchronized void m() {
        System.out.println("parent start");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("parent end");
    }
}

class SubT extends ParentT {
    synchronized void doSomething() {
        System.out.println("sub method1 start");
        m();
        System.out.println("sub method1 end");
    }

    @Override
    synchronized void m() {
        System.out.println("sub method2 start");
        super.m();
        System.out.println("sub method2 start");
    }
}

