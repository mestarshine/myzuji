package com.myzuji.study.java.thread.pool;


import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolTest {

    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int CAPACITY = (1 << COUNT_BITS) - 1;

    // runState is stored in the high-order bits
    //接收新任务和进程队列任务
    private static final int RUNNING = -1 << COUNT_BITS;
    //不接受新任务，但是接收进程队列任务
    private static final int SHUTDOWN = 0 << COUNT_BITS;
    //不接受新任务，也不接受进程队列任务，并且打断正在进行的任务
    private static final int STOP = 1 << COUNT_BITS;
    //所有任务终止，待处理任务数量为0，线程转换为TIDYING，将会执行 terminated 钩子函数
    private static final int TIDYING = 2 << COUNT_BITS;
    //terminated() 执行完成
    private static final int TERMINATED = 3 << COUNT_BITS;
    private static final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));

    // Packing and unpacking ctl 线程运行状态（取高3位）
    private static int runStateOf(int c) {
        return c & ~CAPACITY;
    }

    //工作线程的数量（取低29位）最大工作线程是536870911
    private static int workerCountOf(int c) {
        return c & CAPACITY;
    }

    //运行状态与工作线程数(高三位是线程运作状态，低29位是线程工作数)
    private static int ctlOf(int rs, int wc) {
        return rs | wc;
    }

    public static void main(String[] args) throws Exception {
//        MyThreadPoolExecutor threadPoolExecutor = new MyThreadPoolExecutor(1, 2,
//            60L, TimeUnit.SECONDS,
//            new LinkedBlockingQueue<Runnable>(5));
//        for (int i = 0; i < 7; i++) {
//            threadPoolExecutor.execute(()->{
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            });
//            threadPoolExecutor.execute(()->{
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            });
//            System.out.println("wait");
//        }
        System.out.println("ctl："+Integer.toBinaryString(ctl.get()));
        System.out.println("COUNT_BITS："+Integer.toBinaryString(COUNT_BITS));
        System.out.println("CAPACITY："+Integer.toBinaryString(CAPACITY));
        System.out.println("~CAPACITY："+Integer.toBinaryString(~CAPACITY));
        System.out.println("RUNNING："+Integer.toBinaryString(RUNNING));
        System.out.println("SHUTDOWN："+Integer.toBinaryString(SHUTDOWN));
        System.out.println("STOP："+Integer.toBinaryString(STOP));
        System.out.println("TIDYING："+Integer.toBinaryString(TIDYING));
        System.out.println("TERMINATED："+Integer.toBinaryString(TERMINATED));
        int c = ctlOf(TIDYING, 2);
        System.out.println(Integer.toBinaryString(c));
        System.out.println(CAPACITY);
    }
}
