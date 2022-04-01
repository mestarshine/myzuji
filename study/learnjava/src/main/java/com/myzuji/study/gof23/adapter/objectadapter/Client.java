package com.myzuji.study.gof23.adapter.objectadapter;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/05
 */
public class Client {

    public static void main(String[] args) {
        System.out.println("对象适配器");
        Phone phone = new Phone();
        phone.charge(new VoltageAdapter(new Voltage220v()));
    }
}
