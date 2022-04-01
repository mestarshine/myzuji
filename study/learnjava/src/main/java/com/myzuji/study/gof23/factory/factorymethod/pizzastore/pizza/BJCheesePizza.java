package com.myzuji.study.gof23.factory.factorymethod.pizzastore.pizza;

public class BJCheesePizza extends Pizza {

    @Override
    public void prepare() {
        setName("北京奶酪 pizza ");
        System.out.println("给制作北京奶酪 pizza 准备原材料");
    }
}
