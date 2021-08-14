package com.myzuji.study.java.thread.pool.concurrent;

import java.util.concurrent.*;

public class OverrideDecorateTask extends ScheduledThreadPoolExecutor {
    public OverrideDecorateTask(int corePoolSize) {
        super(corePoolSize);
    }

    public OverrideDecorateTask(int corePoolSize, ThreadFactory threadFactory) {
        super(corePoolSize, threadFactory);
    }

    public OverrideDecorateTask(int corePoolSize, RejectedExecutionHandler handler) {
        super(corePoolSize, handler);
    }

    public OverrideDecorateTask(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactory, handler);
    }

    @Override
    protected <V> RunnableScheduledFuture<V> decorateTask(Runnable runnable, RunnableScheduledFuture<V> task) {
        System.out.println(1);
        return new MyRunnableScheduledFuture(task,null);
    }

    class MyRunnableScheduledFuture <V> extends FutureTask<V> implements RunnableScheduledFuture<V>{

        public MyRunnableScheduledFuture(Runnable runnable, V result) {
            super(runnable, result);
        }

        @Override
        public void run() {
        }

        @Override
        public boolean isPeriodic() {
            return true;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return 0;
        }

        @Override
        public int compareTo(Delayed o) {
            return 0;
        }
    }
}
