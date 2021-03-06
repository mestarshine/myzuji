package com.myzuji.study.concurrent.disruptor;

/**
 * 事件对象
 *
 * @param <T>
 */
public class ObjectEvent<T> {

    private T obj;

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }
}
