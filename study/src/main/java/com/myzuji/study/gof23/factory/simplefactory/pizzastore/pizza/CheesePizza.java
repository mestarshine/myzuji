package com.myzuji.study.gof23.factory.simplefactory.pizzastore.pizza;

public class CheesePizza extends Pizza {

    @Override
    public void prepare() {
        System.out.println("给制作奶酪 pizza 准备原材料");
    }
}
