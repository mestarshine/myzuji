package com.myzuji.study.gof23.factory.simplefactory.pizzastore.pizza;

public class GreekPizza extends Pizza {

    @Override
    public void prepare() {
        System.out.println("给制作 希腊 pizza 准备原材料;");
    }
}
