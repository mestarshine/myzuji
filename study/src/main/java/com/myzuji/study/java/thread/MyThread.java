package com.myzuji.study.java.thread;

import java.util.concurrent.*;

/**
 * 创建 Thread 三种方式
 *
 * @author shine
 * @date 2020/02/05
 */
public class MyThread {

    static class MyThreadT extends Thread {
        @Override
        public void run() {
            System.out.println("Thread");
        }
    }

    static class MyThreadRunnable implements Runnable {

        @Override
        public void run() {
            System.out.println("Runnable");
        }
    }

    static class MyThreadCallable implements Callable<String> {
        @Override
        public String call() throws Exception {
            System.out.println("Callable");
            return "SUCCESS";
        }
    }

    /**
     * 启动线程的5种方式
     * new MyThreadT().start();
     * new Thread(new MyThreadRunnable()).start();
     * new Thread(() -> System.out.println("Hello Lambda!")).start();
     * ThreadPool
     * Future Callable and FutureTask
     *
     * @param args
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new MyThreadT().start();
        new Thread(new MyThreadRunnable()).start();
        new Thread(() -> System.out.println("Hello Lambda!")).start();
        Thread t = new Thread(new FutureTask<String>(new MyThreadCallable()));
        t.start();
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(() -> System.out.println("Hello ThreadPool!"));
        Future<String> f = service.submit(new MyThreadCallable());
        System.out.println(f.get());
        service.shutdown();
    }
}
