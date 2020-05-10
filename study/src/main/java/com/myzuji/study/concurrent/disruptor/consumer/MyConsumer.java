package com.myzuji.study.concurrent.disruptor.consumer;

import com.myzuji.study.concurrent.disruptor.ADisruptorEvenHandler;
import com.myzuji.study.concurrent.disruptor.event.LongEvent;

public class MyConsumer extends ADisruptorEvenHandler<LongEvent> {

    @Override
    public void consume(LongEvent event) {
        event.handler();
    }
}
