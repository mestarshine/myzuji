package com.myzuji.study.gof23.factory.factorymethod.pizzastore.pizza;

public class LDCheesePizza extends Pizza {

    @Override
    public void prepare() {
        setName("伦敦奶酪 pizza ");
        System.out.println("给制作伦敦奶酪 pizza 准备原材料");
    }
}
