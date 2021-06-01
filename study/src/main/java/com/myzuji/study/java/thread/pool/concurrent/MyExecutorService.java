package com.myzuji.study.java.thread.pool.concurrent;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * 对 Executor 增强线程池服务
 */
public interface MyExecutorService  extends ExecutorService {

    /**
     * 关闭线程池，不在接受新任务，但已提交的任务会执行完成
     */
    @Override
    void shutdown();

    /**
     * 立即关闭线程池，尝试停止正在运行的任务，未执行的任务将不在执行
     * 被迫停止及未执行的任务将以列表的形式返回
     * @return
     */
    @Override
    List<Runnable> shutdownNow();

    /**
     * 检查线程池是否已关闭
     * @return
     */
    @Override
    boolean isShutdown();

    /**
     * 检查线程池身份已终止，只有 shutdown() 或 shutdownNow()之后调用才能为 true
     * @return
     */
    @Override
    boolean isTerminated();

    /**
     * 在指定时间内线程池到达终止状态才会返回true
     * @param timeout
     * @param unit
     * @return
     * @throws InterruptedException
     */
    @Override
    boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException;

    /**
     * 执行有返回值的任务，任务的返回值为 task.call() 的结果
     * @param task
     * @param <T>
     * @return
     */
    @Override
    <T> Future<T> submit(Callable<T> task);

    /**
     * 执行有返回值的任务，任务的返回值为这里传入的 result
     * 只有当任务执行完成了调用 get() 时才会返回
     * @param task
     * @param result
     * @param <T>
     * @return
     */
    @Override
    <T> Future<T> submit(Runnable task, T result);

    /**
     * 执行有返回值的任务，任务的返回值为 null
     * 只有当任务执行完成了调用 get() 时才会返回
     * @param task
     * @return
     */
    @Override
    Future<?> submit(Runnable task);

    /**
     * 批量执行任务
     * 只有当这些任务都执行完成了这个方法才会返回
     * @param tasks
     * @param <T>
     * @return
     * @throws InterruptedException
     */
    @Override
    <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException;

    /**
     * 在指定时间内批量执行任务，未执行的任务将被取消
     * 这里的 timeout 是所有任务的总时间，不是单个任务的时间
     * @param tasks
     * @param timeout
     * @param unit
     * @param <T>
     * @return
     * @throws InterruptedException
     */
    @Override
    <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException;

    /**
     * 返回任意一个已完成任务的执行结果，未执行完成的任务将被取消
     * @param tasks
     * @param <T>
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Override
    <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException;

    /**
     * 在指定时间内如果有任务已完成、则返回任意一个已完成任务的执行结果，未执行完成的任务将被取消
     * @param tasks
     * @param timeout
     * @param unit
     * @param <T>
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    @Override
    <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException;
}
