package com.myzuji.study.java.thread;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/01
 */
public class ThreadDemo implements Runnable {

    private static final int THREAD_NUM = 5;
    private String name;
    Thread thread;

    public ThreadDemo(String name) {
        this.name = name;
        this.thread = new Thread(this, name);
        System.out.println("new thread:" + thread);
        thread.start();
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < THREAD_NUM; i++) {
                System.out.println(name + ":" + i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println(name + "中断");
        }
        System.out.println(name + "退出");
    }

    public String getName() {
        return name;
    }

    public Thread getThread() {
        return thread;
    }
}
