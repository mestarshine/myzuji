package com.myzuji.study.java.thread.communication;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/01
 */
public class Queue {
    int anInt;
    boolean valueSet = false;

    synchronized int get() {
        while (!valueSet) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("InterruptedException caught");
            }
        }
        System.out.println("Got:" + anInt);
        valueSet = false;
        notify();
        return anInt;
    }

    synchronized void put(int anInt) {
        while (valueSet) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("InterruptedException caught");
            }
        }
        this.anInt = anInt;
        valueSet = true;
        System.out.println("Put:" + anInt);
        notify();
    }
}
