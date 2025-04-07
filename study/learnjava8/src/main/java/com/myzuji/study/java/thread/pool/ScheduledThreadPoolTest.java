package com.myzuji.study.java.thread.pool;

import com.myzuji.study.java.thread.pool.concurrent.MyScheduledThreadPoolExecutor;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPoolTest {

    public static void main(String[] args) {
        /**
         * 构造器做了什么
         * 使用 DelayedWorkQueue 队列，调用 ThreadPoolExecutor 初始化线程池，
         */
        MyScheduledThreadPoolExecutor executor = new MyScheduledThreadPoolExecutor(1);
        /**
         * 延迟调度做了什么
         * 1. 检查线程池状态
         * 2. 向队列中添加任务
         * 3. 启动任务,线程调用 runWorker()
         * 4. runWorker()---> getTask()---> workQueue.take() 此处 workQueue 为 DelayedWorkQueue
         * 5. DelayedWorkQueue.take() 决定是执行任务
         */
        executor.schedule(() -> System.out.println("hello schedule"), 1, TimeUnit.SECONDS);
        /**
         * 执行做了什么
         * schedule(command, 0, NANOSECONDS);
         * 延迟为 0 的 schedule方法
         */
        executor.execute(() -> System.out.println("hello excute"));
        // todo 周期性调度器做了什么
        executor.scheduleAtFixedRate(() -> {
            System.out.println(LocalDateTime.now());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 0, 2, TimeUnit.SECONDS);

        executor.scheduleWithFixedDelay(() -> {
            System.out.println(LocalDateTime.now());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 0, 2, TimeUnit.SECONDS);


    }
}
