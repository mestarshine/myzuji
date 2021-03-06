package com.myzuji.study.gof23.strategy;

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
