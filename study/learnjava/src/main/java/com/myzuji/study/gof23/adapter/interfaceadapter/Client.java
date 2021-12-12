package com.myzuji.study.gof23.adapter.interfaceadapter;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/05
 */
public class Client {

    public static void main(String[] args) {
        AbsAdapter absAdapter = new AbsAdapter() {
            @Override
            public void m1() {
                System.out.println("使用类m1的方法");
            }
        };
        absAdapter.m1();

    }
}
