package com.myzuji.study.java.thread.pool.concurrent;

import java.util.concurrent.Executor;

/**
 * Executor 线程池的顶级接口，定义了一个执行无返回值任务的方法
 */
public interface MyExecutor extends Executor {

    /**
     * 执行无返回值任务
     * 根据 Executor 的实现，可能是在新线程、线程池、线程调用中执行
     * @param command
     */
    @Override
    void execute(Runnable command);
}
