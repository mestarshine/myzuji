package com.myzuji.study.java.thread.syn;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/01
 */
public class Synch {

    public static void main(String[] args) {
        CallMe target = new CallMe();
        Caller ob1 = new Caller("Hello", target);
        Caller ob2 = new Caller("Synchronized", target);
        Caller ob3 = new Caller("Word", target);
        try {
            ob1.thread.join();
            ob2.thread.join();
            ob3.thread.join();
        } catch (InterruptedException e) {
            System.out.println("interrupted");
        }

    }
}
