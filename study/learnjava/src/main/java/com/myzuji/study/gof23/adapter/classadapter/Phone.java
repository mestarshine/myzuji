package com.myzuji.study.gof23.adapter.classadapter;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/05
 */
public class Phone {

    public void charge(Voltage5V voltage5V) {
        if (voltage5V.output5v() == 5) {
            System.out.println("电压为5v，可以充电");
        } else {
            System.out.println("电压大于5v，不能充电");
        }
    }


}
