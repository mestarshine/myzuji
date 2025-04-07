package com.myzuji.study.java.thread;

import java.util.concurrent.TimeUnit;

/**
 * 停止线程的几种方式
 */
public class StopThreadStudy {

    /**
     * 容易产生数据不一致问题
     * stop 会释放所有锁资源 1.2已弃用
     * @throws InterruptedException
     */
    static void stopThread() throws InterruptedException {
        Thread t = new Thread(() -> {
            while (true) {
                System.out.println("go on");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        TimeUnit.SECONDS.sleep(5);
//        t.stop();
    }

    /**
     * 暂停会释放锁，可能导致数据不一致
     * 1.2已弃用
     * @throws InterruptedException
     */
    static void suspendResume() throws InterruptedException {
        Thread t = new Thread(() -> {
            while (true) {
                System.out.println("go on");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        TimeUnit.SECONDS.sleep(5);
//        t.suspend();
//        TimeUnit.SECONDS.sleep(3);
//        t.resume();
    }

    static volatile boolean running = true;

    /**
     *
     * 缺点：不能精确控制循环次数，还没有同步的时候，线程做了阻塞，无法恢复
     * @throws InterruptedException
     */
    static void volatileFlag() throws InterruptedException {
        Thread t = new Thread(() -> {
            long i = 0L;
            while (running) {
                i++;
            }
            System.out.println("end and i = " + i);
        });
        t.start();
        TimeUnit.SECONDS.sleep(5);
        running = false;
    }

    /**
     * 与 volatileFlag 存在相同问题
     * @throws InterruptedException
     */
    static void interrupt() throws InterruptedException {
        Thread t = new Thread(() -> {
            long i = 0L;
            while (!Thread.interrupted()) {
                i++;
            }
            System.out.println("end and i = " + i);
        });
        t.start();
        TimeUnit.SECONDS.sleep(5);
        t.interrupt();
    }

}
