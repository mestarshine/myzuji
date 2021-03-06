package com.myzuji.study.concurrent.disruptor;

import com.myzuji.study.concurrent.disruptor.consumer.MyConsumer;
import com.myzuji.study.concurrent.disruptor.event.LongEvent;

public class DisruptorTest {
    public static void main(String[] args) throws Exception {

        DisruptorQueue<LongEvent> queue = DisruptorQueueFactory.getHandleEventsQueue(8, false, new MyConsumer());
        for (long l = 0; true; l++) {
            queue.add(new LongEvent(l));
            Thread.sleep(1000);
        }
    }
}
