package com.myzuji.study.gof23.factory.simplefactory.pizzastore.order;

import com.myzuji.study.gof23.factory.simplefactory.pizzastore.pizza.CheesePizza;
import com.myzuji.study.gof23.factory.simplefactory.pizzastore.pizza.GreekPizza;
import com.myzuji.study.gof23.factory.simplefactory.pizzastore.pizza.PepperPizza;
import com.myzuji.study.gof23.factory.simplefactory.pizzastore.pizza.Pizza;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class OrderPizza {

    //定义个简单工厂对象
    SimpleFactory simpleFactory;
    Pizza pizza = null;

    /**
     * 传统的方法的优缺点
     * 优点：比较好理解，简单易操作
     * 缺点：违反类设计模式的 ocp 原则，即对扩展开放，对修改关闭
     */
    public OrderPizza() {
        Pizza pizza = null;
        String orderType; //pizza 类型
        do {
            orderType = getType();
            if (orderType.equals("greek")) {
                pizza = new GreekPizza();
                pizza.setName("greek pizza");
            } else if (orderType.equals("cheese")) {
                pizza = new CheesePizza();
                pizza.setName("cheese pizza");
            } else if (orderType.equals("pepper")) {
                pizza = new PepperPizza();
                pizza.setName("pepper pizza");
            } else {
                break;
            }
            pizza.prepare();
            pizza.bake();
            pizza.cut();
            pizza.box();
        } while (true);
    }

    public OrderPizza(SimpleFactory simpleFactory) {
        setFactory(simpleFactory);
    }

    // 些一个方法，可以获取客户希望订购的 pizza 种类
    private String getType() {
        try {
            BufferedReader strin = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("输入 pizza 类型:");
            String str = strin.readLine();
            return str;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setFactory(SimpleFactory simpleFactory) {
        String orderType = "";// 用户输入的
        this.simpleFactory = simpleFactory;

        do {
            orderType = getType();
            pizza = this.simpleFactory.createPizza(orderType);
            if (pizza != null) {//订购成功
                pizza.prepare();
                pizza.bake();
                pizza.cut();
                pizza.box();
            } else {
                System.out.println("订购 pizza 失败");
                break;
            }
        } while (true);
    }
}
