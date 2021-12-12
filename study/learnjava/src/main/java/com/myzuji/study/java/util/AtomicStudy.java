package com.myzuji.study.java.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CAS (Compare And Swap):无锁优化，自旋
 * com.myzuji.study.java.thread.multiple.volat.VolatileTest1
 */
public class AtomicStudy {

    AtomicInteger count = new AtomicInteger();

    public static void main(String[] args) {
        AtomicStudy atomicStudy = new AtomicStudy();
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(atomicStudy::m, "thread-" + i));
        }

        threads.forEach((o) -> o.start());

        threads.forEach((o) -> {
            try {
                o.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    void m() {
        for (int i = 0; i < 10000; i++) {
            count.incrementAndGet();
        }
    }
}
