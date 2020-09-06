package com.myzuji.study.java.thread.multiple.volat;

import java.util.ArrayList;
import java.util.List;

/**
 * volatile 不能保证多个线程共同修改 running 变量时所带来的不一致问题，volatile 不能代替 synchronized
 */
public class VolatileTest1 {

    volatile int count = 0;

    public static void main(String[] args) {
        VolatileTest1 test1 = new VolatileTest1();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            threads.add(new Thread(test1::m, "thread-" + i));
        }
        threads.forEach((o) -> o.start());
        threads.forEach((o) -> {
            try {
                o.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(test1.count);
    }

    void m() {
        for (int i = 0; i < 10000; i++) {
            count++;
        }
    }
}
