package com.myzuji.study.gof23.factory.simplefactory.pizzastore.pizza;

public class PepperPizza extends Pizza {

    @Override
    public void prepare() {
        System.out.println("给胡椒 Pizza 准备原材料");
    }
}
