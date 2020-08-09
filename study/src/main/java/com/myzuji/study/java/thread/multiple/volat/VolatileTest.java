package com.myzuji.study.java.thread.multiple.volat;

import java.util.concurrent.TimeUnit;

/**
 * 对比有无 volatile 的情况下，整个程序运行结果的区别
 * <p>
 * 保证线程可见线
 * - MESI
 * - 缓存一致性协议
 * 禁止指令重排序
 *
 * @author shine
 * @date 2020/8/9
 */
public class VolatileTest {

    volatile boolean running = true;

    public static void main(String[] args) {
        VolatileTest volatileTest = new VolatileTest();
        new Thread(volatileTest::m, "t1").start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        volatileTest.running = false;

    }

    void m() {
        System.out.println("m start");
        while (running) {
        }
        System.out.println("m end !");
    }

}
