package com.myzuji.study.concurrent.disruptor;

import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

/**
 * Disruptor队列操作工具类工厂
 */
public class DisruptorQueueFactory {

    private DisruptorQueueFactory() {
    }

    // 创建"点对电模式"的操作队列，即同一事件会被一组消费者其中之一消费
    public static <T> DisruptorQueue<T> getWorkPoolQueue(int queueSize, boolean isMoreProducer,
                                                         ADisruptorEvenHandler<T>... evenHandlers) {
        Disruptor<ObjectEvent<T>> disruptor = new Disruptor<>(new ObjectEventFactory<>(),
            queueSize, DaemonThreadFactory.INSTANCE,
            isMoreProducer ? ProducerType.MULTI : ProducerType.SINGLE,
            new YieldingWaitStrategy());
        disruptor.handleEventsWithWorkerPool(evenHandlers);
        return new DisruptorQueue<>(disruptor);
    }

    // 创建"发布订阅模式"的操作队列，即同一事件会被多个消费者并行消费
    public static <T> DisruptorQueue<T> getHandleEventsQueue(int queueSize, boolean isMoreProducer,
                                                             ADisruptorEvenHandler<T>... evenHandlers) {
        Disruptor<ObjectEvent<T>> disruptor = new Disruptor<>(new ObjectEventFactory<>(),
            queueSize, DaemonThreadFactory.INSTANCE,
            isMoreProducer ? ProducerType.MULTI : ProducerType.SINGLE,
            new YieldingWaitStrategy());
        disruptor.handleEventsWith(evenHandlers);
        return new DisruptorQueue<>(disruptor);
    }

    // 直接通过传入的 Disruptor 对象创建操作队列（如果消费者有依赖关系的话可以用此方法）
    public static <T> DisruptorQueue<T> getQueue(Disruptor<ObjectEvent<T>> disruptor) {
        return new DisruptorQueue<>(disruptor);
    }
}
