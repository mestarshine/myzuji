package com.myzuji.study.java.thread.multiple.sync;

import java.util.concurrent.TimeUnit;

/**
 * 应用在执行过程中，如果出现异常，默认情况锁会被释放
 * 所以，并发处理过程中，有异常要多加小心，不然可能会发生不一致情况
 *
 * @author shine
 * @date 2020/8/8
 */
public class ExceptionSyncTest {

    int count = 0;

    public static void main(String[] args) {
        ExceptionSyncTest exceptionSyncTest = new ExceptionSyncTest();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                exceptionSyncTest.doSomething();
            }
        };
        new Thread(runnable, "t1").start();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(runnable, "t2").start();
    }

    private synchronized void doSomething() {
        System.out.println(Thread.currentThread().getName() + " start");
        for (; ; ) {
            count++;
            System.out.println(Thread.currentThread().getName() + " count = " + count);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (count == 5) {
                //此处抛出异常，锁将被释放，要想不给释放，可以在这里进行 catch ，然后让循环继续
                int i = count / 0;
                System.out.println(i);
            }
        }
    }
}
