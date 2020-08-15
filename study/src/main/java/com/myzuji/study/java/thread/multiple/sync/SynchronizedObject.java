package com.myzuji.study.java.thread.multiple.sync;

import java.util.concurrent.TimeUnit;

/**
 * 锁定某个对象 o，如果 o 的属性发生改变，不影响锁的使用
 * 但是如果 o 变成另外一个对象，则锁定对对象发生改变
 * 应避免将锁对象对引用变成另外的对象
 *
 * @author shine
 * @date 2020/8/15
 */
public class SynchronizedObject {
    Object o = new Object();

    public static void main(String[] args) {
        SynchronizedObject synchronizedObject = new SynchronizedObject();
        // 启动第一个线程
        new Thread(synchronizedObject::m, "t1").start();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 创建第二个线程
        Thread thread2 = new Thread(synchronizedObject::m, "t2");

        // 锁对象发生改变，所以 t2 线程得以执行，如果注释掉这句话，线程2将永远不会执行
        synchronizedObject.o = new Object();
        thread2.start();

    }

    void m() {
        synchronized (o) {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName());
            }
        }
    }
}
