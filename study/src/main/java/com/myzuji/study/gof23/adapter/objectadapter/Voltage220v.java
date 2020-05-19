package com.myzuji.study.gof23.adapter.objectadapter;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/05
 */
public class Voltage220v {

    public int output220v() {
        int src = 220;
        System.out.println("电压=" + src + "伏");
        return src;
    }
}
