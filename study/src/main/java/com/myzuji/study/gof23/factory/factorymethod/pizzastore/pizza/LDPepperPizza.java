package com.myzuji.study.gof23.factory.factorymethod.pizzastore.pizza;

public class LDPepperPizza extends Pizza {

    @Override
    public void prepare() {
        setName("伦敦胡椒 pizza ");
        System.out.println("给制作伦敦胡椒 pizza 准备原材料");
    }
}
