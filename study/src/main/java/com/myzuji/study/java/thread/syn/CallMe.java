package com.myzuji.study.java.thread.syn;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/01
 */
public class CallMe {

    void call(String msg) {
        System.out.print("[" + msg);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
        System.out.println("]");
    }
}
