package com.myzuji.study.concurrent.disruptor.event;

/**
 * 定义事件 event：通过 disruptor 进行交换的数据类型
 */
public class LongEvent {

    private final long value;

    public LongEvent(long value) {
        this.value = value;
    }

    public void handler() {
        System.out.println("成功消费：" + value);
    }
}
