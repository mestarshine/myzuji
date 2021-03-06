package com.myzuji.study.gof23.strategy;

public interface Comparator<T> {

    /**
     * 比较 o1，o2
     * o1 < o2 返回 1
     * o1 = o2 返回 0
     * o1 > o2 返回 -1
     *
     * @param o1 参数一
     * @param o2 参数二
     * @return -1，0，1
     */
    int compare(T o1, T o2);
}
