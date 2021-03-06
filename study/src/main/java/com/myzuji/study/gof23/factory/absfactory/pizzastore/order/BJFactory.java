package com.myzuji.study.gof23.factory.absfactory.pizzastore.order;

import com.myzuji.study.gof23.factory.absfactory.pizzastore.pizza.BJCheesePizza;
import com.myzuji.study.gof23.factory.absfactory.pizzastore.pizza.BJPepperPizza;
import com.myzuji.study.gof23.factory.absfactory.pizzastore.pizza.Pizza;

public class BJFactory implements AbsFactory {

    @Override
    public Pizza createPizza(String orderType) {
        System.out.println("使用的是抽象工厂模式");
        Pizza pizza = null;
        if (orderType.equals("cheese")) {
            pizza = new BJCheesePizza();
        } else if (orderType.equals("pepper")) {
            pizza = new BJPepperPizza();
        }
        return pizza;
    }
}
