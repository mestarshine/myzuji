package com.myzuji.study.gof23.bridge.impl;

import com.myzuji.study.gof23.bridge.Brand;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/05
 */
public class Xiaomi implements Brand {

    @Override
    public void open() {
        System.out.println("小米手机开机");
    }

    @Override
    public void close() {
        System.out.println("小米手机关机");
    }

    @Override
    public void call() {
        System.out.println("小米手机打电话");
    }
}
