package com.myzuji.study.java.thread.pool.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

public abstract class MyAbstractExecutorService extends AbstractExecutorService {

    /**
     * the main mechanics of invokeAny.
     * 要么不执行，要么都执行，至少执行一个
     */
    private <T> T doInvokeAny(Collection<? extends Callable<T>> tasks,
                              boolean timed, long nanos)
        throws InterruptedException, ExecutionException, TimeoutException {
        if (tasks == null) {
            throw new NullPointerException();
        }
        int ntasks = tasks.size();
        if (ntasks == 0) {
            throw new IllegalArgumentException();
        }
        ArrayList<Future<T>> futures = new ArrayList<Future<T>>(ntasks);
        ExecutorCompletionService<T> ecs =
            new ExecutorCompletionService<T>(this);


        try {
            ExecutionException ee = null;
            final long deadline = timed ? System.nanoTime() + nanos : 0L;
            Iterator<? extends Callable<T>> it = tasks.iterator();

            futures.add(ecs.submit(it.next()));
            --ntasks;
            int active = 1;

            for (;;) {
                //尝试拿，拿不到返回null
                Future<T> f = ecs.poll();
                // 任务未执行成功，则继续添加任务
                if (f == null) {
                    if (ntasks > 0) {
                        --ntasks;
                        futures.add(ecs.submit(it.next()));
                        ++active;
                    }
                    else if (active == 0) {
                        break;
                    } else if (timed) {
                        f = ecs.poll(nanos, TimeUnit.NANOSECONDS);
                        if (f == null) {
                            throw new TimeoutException();
                        }
                        nanos = deadline - System.nanoTime();
                    }
                    else {
                        f = ecs.take();
                    }
                }
                if (f != null) {
                    --active;
                    try {
                        return f.get();
                    } catch (ExecutionException eex) {
                        ee = eex;
                    } catch (RuntimeException rex) {
                        ee = new ExecutionException(rex);
                    }
                }
            }

            if (ee == null) {
                //解决编译异常添加的临时代码
                //原代码为 ee = new ExecutionException();
                ee = new ExecutionException("任务执行异常",new RuntimeException());
            }
            throw ee;

        } finally {
            for (int i = 0, size = futures.size(); i < size; i++) {
                futures.get(i).cancel(true);
            }
        }
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
        throws InterruptedException, ExecutionException {
        try {
            return doInvokeAny(tasks, false, 0);
        } catch (TimeoutException cannotHappen) {
            assert false;
            return null;
        }
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks,
                           long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException {
        return doInvokeAny(tasks, true, unit.toNanos(timeout));
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
        throws InterruptedException {
        if (tasks == null) {
            throw new NullPointerException();
        }
        //结果代理
        ArrayList<Future<T>> futures = new ArrayList<Future<T>>(tasks.size());
        boolean done = false;
        try {
            //封装 futureTask,放入结果代理，并执行task
            for (Callable<T> t : tasks) {
                RunnableFuture<T> f = newTaskFor(t);
                futures.add(f);
                execute(f);
            }
            //非取消异常、执行异常，则抛出相关异常
            for (int i = 0, size = futures.size(); i < size; i++) {
                Future<T> f = futures.get(i);
                if (!f.isDone()) {
                    try {
                        //等待执行结果
                        f.get();
                    } catch (CancellationException ignore) {
                    } catch (ExecutionException ignore) {
                    }
                }
            }
            // futureTask 全部执行完
            done = true;
            return futures;
        } finally {
            // 没执行完，出现异常后，则取消所有任务，本质上使用的是 Thread.interrupt()
            if (!done) {
                for (int i = 0, size = futures.size(); i < size; i++) {
                    futures.get(i).cancel(true);
                }
            }
        }
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks,
                                         long timeout, TimeUnit unit)
        throws InterruptedException {
        if (tasks == null) {
            throw new NullPointerException();
        }
        long nanos = unit.toNanos(timeout);
        ArrayList<Future<T>> futures = new ArrayList<Future<T>>(tasks.size());
        boolean done = false;
        try {
            // 构造 futureTask 并放入结果代理中
            for (Callable<T> t : tasks) {
                futures.add(newTaskFor(t));
            }

            final long deadline = System.nanoTime() + nanos;
            final int size = futures.size();

            // 执行task 并计算耗时
            for (int i = 0; i < size; i++) {
                execute((Runnable)futures.get(i));
                nanos = deadline - System.nanoTime();
                // 是否超时，超时则直接返回，可能有部分任务未执行
                if (nanos <= 0L) {
                    return futures;
                }
            }

            for (int i = 0; i < size; i++) {
                Future<T> f = futures.get(i);
                // 任务是否完成
                if (!f.isDone()) {
                    if (nanos <= 0L) {
                        return futures;
                    }
                    try {
                        f.get(nanos, TimeUnit.NANOSECONDS);
                    } catch (CancellationException ignore) {
                    } catch (ExecutionException ignore) {
                    } catch (TimeoutException toe) {
                        return futures;
                    }
                    nanos = deadline - System.nanoTime();
                }
            }
            done = true;
            return futures;
        } finally {
            if (!done) {
                for (int i = 0, size = futures.size(); i < size; i++) {
                    futures.get(i).cancel(true);
                }
            }
        }
    }

}
