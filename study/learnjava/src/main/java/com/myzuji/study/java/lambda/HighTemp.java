package com.myzuji.study.java.lambda;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/04
 */
public class HighTemp {

    private final int hTemp;

    public HighTemp(int hTemp) {
        this.hTemp = hTemp;
    }

    boolean sameTemp(HighTemp ht2) {
        return hTemp == ht2.hTemp;
    }

    boolean lessThanTemp(HighTemp ht2) {
        return hTemp < ht2.hTemp;
    }
}
