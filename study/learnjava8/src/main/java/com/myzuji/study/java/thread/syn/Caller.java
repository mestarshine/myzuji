package com.myzuji.study.java.thread.syn;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/01
 */
public class Caller implements Runnable {
    String msg;
    CallMe target;
    Thread thread;

    public Caller(String msg, CallMe target) {
        this.msg = msg;
        this.target = target;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        synchronized (target) {
            target.call(msg);
        }
    }
}
