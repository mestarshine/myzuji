package com.myzuji.study.java.thread.pool.fjp;

import java.util.concurrent.TimeUnit;

/**
 * ForkJoinPool demo
 */
public class FJPDemo {
    public static void main(String[] args) throws InterruptedException {
        MyForkJoinPool forkJoinPool = new MyForkJoinPool();
        // todo 此 Hello World 怎么执行，FJP怎么初始化
        forkJoinPool.submit(() -> System.out.println("hello world"));
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.DAYS);
    }

}
