package com.myzuji.study.gof23.adapter.objectadapter;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/05
 */
public class VoltageAdapter implements Voltage5V {

    //关联关系中的聚合
    private final Voltage220v voltage220v;

    public VoltageAdapter(Voltage220v voltage220v) {
        this.voltage220v = voltage220v;
    }

    @Override
    public int output5v() {
        int dst = 0;
        if (null != voltage220v) {
            int src = voltage220v.output220v();
            System.out.println("使用对象适配器，进行适配");
            dst = src / 44;
            System.out.println("适配完成，输出5V");
        }
        return dst;
    }
}
