package com.myzuji.study.gof23.factory.absfactory.pizzastore.pizza;

public class BJPepperPizza extends Pizza {

    @Override
    public void prepare() {
        setName("北京胡椒 pizza ");
        System.out.println("给制作北京胡椒 pizza 准备原材料");
    }
}
