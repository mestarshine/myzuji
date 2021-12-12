package com.myzuji.study.gof23.adapter.classadapter;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/05
 */
public class VoltageAdapter extends Voltage220v implements Voltage5V {

    @Override
    public int output5v() {
        int src = output220v();
        int dst = src / 44;
        return dst;
    }
}
