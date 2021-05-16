package com.myzuji.study.java.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadState {

    /**
     * new Thread()                     线程处于 NEW 状态
     * t.start()                        线程处于 RUNNABLE 状态
     * 执行完任务                         线程处于 TERMINATED 状态
     * t.sleep()                        线程处于 TIMED_WAITING 状态
     * LockSupport.park()               线程处于 WAITING 状态
     * LockSupport.unpark()             线程处于 RUNNABLE 状态
     * synchronized                     线程处于 BLOCKED 状态
     * new ReentrantLock().lock()       线程处于 WAITING 状态
     * new ReentrantLock().unlock()     线程处于 RUNNABLE 状态
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            System.out.println("2：运行状态：" + Thread.currentThread().getState());
            for (int i = 0; i < 3; i++) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print(i + " ");
            }
        });
        System.out.println("1：初始状态：" + t1.getState());
        t1.start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("3：sleep状态：" + t1.getState());
        t1.join();
        System.out.println();
        System.out.println("4：任务完成：" + t1.getState());


        Thread t2 = new Thread(() -> {
            System.out.println("park 测试");
            LockSupport.park();
            int i = 0;
            while (!Thread.interrupted()) {
                i++;
            }
            System.out.println("park end and i = " + i);
        });
        t2.start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("5：park后：" + t2.getState());
        LockSupport.unpark(t2);
        TimeUnit.SECONDS.sleep(1);
        System.out.println("6：unpark后：" + t2.getState());
        t2.interrupt();

        final Object o = new Object();
        Thread t3 = new Thread(() -> {
            synchronized (o) {
                System.out.println("t3 通过 synchronized 得到了锁");
            }
        });
        new Thread(() -> {
            synchronized (o) {
                try {
                    TimeUnit.SECONDS.sleep(4);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        TimeUnit.SECONDS.sleep(1);
        t3.start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("7：synchronized：" + t3.getState());

        final Lock lock = new ReentrantLock();
        Thread t4 = new Thread(() -> {
            System.out.println("test ReentrantLock start");
            lock.lock();
            System.out.println("t4 得到了锁");
            lock.unlock();
            int i = 0;
            while (!Thread.interrupted()) {
                i++;
            }
            System.out.println("ReentrantLock lock end and i = " + i);
        });

        new Thread(() -> {
            lock.lock();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.unlock();
        }).start();

        TimeUnit.SECONDS.sleep(2);
        t4.start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("8：new ReentrantLock().lock()：" + t4.getState());
        TimeUnit.SECONDS.sleep(6);
        System.out.println("9：new ReentrantLock().unlock()：" + t4.getState());
        t4.interrupt();
    }
}
