package com.myzuji.study.java.thread.pool;

import com.myzuji.study.java.thread.pool.concurrent.MyRejectedExecutionHandler;
import com.myzuji.study.java.thread.pool.concurrent.MyThreadPoolExecutor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class ObtainThreadPoolException {

    static class MyThreadPool extends MyThreadPoolExecutor {
        public MyThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }

        public MyThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, MyRejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            super.afterExecute(r, t);
            int i = 1 / 0;
        }
    }

    public static void main(String[] args) {
        // afterExecute 抛出异常验证
        MyThreadPool myThreadPool = new MyThreadPool(1, 1, 0, TimeUnit.MINUTES, new LinkedBlockingDeque<>());

        myThreadPool.execute(()->System.out.println(1));
        myThreadPool.execute(()->System.out.println(1));
        myThreadPool.execute(()->System.out.println(1));
        myThreadPool.execute(()->System.out.println(1));

        // 通过 UncaughtExceptionHandler 捕捉线程异常
        MyThreadPool myThreadPool1 = new MyThreadPool(1, 1, 0, TimeUnit.MINUTES, new LinkedBlockingDeque<>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        System.out.println("糟糕，有异常");
                    }
                });
                return thread;
            }
        }, new MyThreadPoolExecutor.CallerRunsPolicy());

        myThreadPool1.execute(()->System.out.println(1));
        myThreadPool1.execute(()->System.out.println(1));
    }
}
