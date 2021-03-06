package com.myzuji.study.gof23.factory.simplefactory.pizzastore.order;

import com.myzuji.study.gof23.factory.simplefactory.pizzastore.pizza.CheesePizza;
import com.myzuji.study.gof23.factory.simplefactory.pizzastore.pizza.GreekPizza;
import com.myzuji.study.gof23.factory.simplefactory.pizzastore.pizza.PepperPizza;
import com.myzuji.study.gof23.factory.simplefactory.pizzastore.pizza.Pizza;

//简单工厂类
public class SimpleFactory {

    // 简单工厂模式，也叫静态工厂模式
    public static Pizza createPizza2(String orderType) {

        System.out.println("使用简单工厂模式2");

        Pizza pizza = null;

        if (orderType.equals("greek")) {
            pizza = new GreekPizza();
            pizza.setName("greek pizza");
        } else if (orderType.equals("cheese")) {
            pizza = new CheesePizza();
            pizza.setName("cheese pizza");
        } else if (orderType.equals("pepper")) {
            pizza = new PepperPizza();
            pizza.setName("pepper pizza");
        }
        return pizza;
    }

    // 根据 orderType 返回对应的 Pizza 对象
    public Pizza createPizza(String orderType) {
        System.out.println("使用简单工厂模式");

        Pizza pizza = null;

        if (orderType.equals("greek")) {
            pizza = new GreekPizza();
            pizza.setName("greek pizza");
        } else if (orderType.equals("cheese")) {
            pizza = new CheesePizza();
            pizza.setName("cheese pizza");
        } else if (orderType.equals("pepper")) {
            pizza = new PepperPizza();
            pizza.setName("pepper pizza");
        }
        return pizza;
    }
}
