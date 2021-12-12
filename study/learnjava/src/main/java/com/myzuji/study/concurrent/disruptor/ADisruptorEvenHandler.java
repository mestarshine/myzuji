package com.myzuji.study.concurrent.disruptor;


import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * 消费者抽象类
 *
 * @param <T>
 */
public abstract class ADisruptorEvenHandler<T> implements EventHandler<ObjectEvent<T>>, WorkHandler<ObjectEvent<T>> {

    @Override
    public void onEvent(ObjectEvent<T> event, long sequence, boolean endOfBatch) throws Exception {
        this.onEvent(event);
    }

    public void onEvent(ObjectEvent<T> event) {
        this.consume(event.getObj());
    }

    public abstract void consume(T event);
}
