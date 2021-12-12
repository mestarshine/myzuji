package com.myzuji.study.concurrent.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * 事件生成工厂（用来初始化预分配事件对象）
 *
 * @param <T>
 */
public class ObjectEventFactory<T> implements EventFactory<ObjectEvent<T>> {

    @Override
    public ObjectEvent<T> newInstance() {
        return new ObjectEvent<>();
    }
}
