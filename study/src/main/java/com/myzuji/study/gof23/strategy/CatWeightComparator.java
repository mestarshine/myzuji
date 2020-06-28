package com.myzuji.study.gof23.strategy;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/05
 */
public class CatWeightComparator implements Comparator<Cat> {

    @Override
    public int compare(Cat o1, Cat o2) {
        if (o1.weight > o2.weight) {
            return -1;
        } else if (o1.weight < o2.weight) {
            return 1;
        } else {
            return 0;
        }
    }
}
