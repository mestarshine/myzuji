package com.myzuji.study.gof23.bridge;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/05
 */
public abstract class Phone {

    private final Brand brand;

    public Phone(Brand brand) {
        super();
        this.brand = brand;
    }

    protected void open() {
        this.brand.open();
    }

    protected void close() {
        this.brand.close();
    }

    protected void call() {
        this.brand.call();
    }
}
