package com.myzuji.study.java.thread.multiple.sync;

import java.util.concurrent.TimeUnit;

/**
 * synchronized 优化
 * 同步代码块中的语句越少越好
 *
 * @author shine
 * @date 2020/8/15
 */
public class FineCoarseLock {

    int count = 0;

    synchronized void m1() {
        // 不需要锁的业务
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 业务逻辑中只有下面这句需要 sync ，这时不应该整个方法上锁
        count++;

        // 不需要锁的业务
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    void m2() {
        // 不需要锁的业务
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 业务逻辑中只有下面这句需要 sync ，这时不应该整个方法上锁
        synchronized (this) {
            count++;
        }

        // 不需要锁的业务
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
