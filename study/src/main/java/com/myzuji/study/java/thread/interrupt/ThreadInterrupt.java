package com.myzuji.study.java.thread.interrupt;

import java.util.concurrent.TimeUnit;

public class ThreadInterrupt {

    /**
     * Thread.java
     * t.interrupt() 打断线程，（设置 t 线程的打断标志位f=ture，并不是打断线程的运行）
     * t.isInterrupted() 查询打断标志位是否被设置（是不是曾经被打断过）
     * Thread.interrupted() 查看当前线程是否被打断，如果被打断，恢复标志位
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(()->{
            for (; ; ) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("线程被打断了");
                    System.out.println(Thread.currentThread().isInterrupted());
                    break;
                }
            }
        });
        t.start();
        TimeUnit.SECONDS.sleep(2);
        t.interrupt();
    }
}
