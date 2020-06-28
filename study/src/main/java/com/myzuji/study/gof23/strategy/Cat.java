package com.myzuji.study.gof23.strategy;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/05
 */
public class Cat {

    Integer weight;
    Integer height;

    public Cat(int weight, int height) {
        this.weight = weight;
        this.height = height;
    }

    @Override
    public String toString() {
        return "Cat{" +
            "weight=" + weight +
            ", height=" + height +
            '}';
    }

}
