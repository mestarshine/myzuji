package com.myzuji.study.java.thread;

/**
 * Thread 两种实现方式
 *
 * @author shine
 * @date 2020/02/05
 */
public class MyThread {

    class ThreadOne extends Thread {
        @Override
        public void run() {
            System.out.println("Thread");
        }
    }

    class ThreadTwo implements Runnable {

        @Override
        public void run() {
            System.out.println("Runnable");
        }
    }
}
