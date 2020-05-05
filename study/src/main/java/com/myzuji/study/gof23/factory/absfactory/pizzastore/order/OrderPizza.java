package com.myzuji.study.gof23.factory.absfactory.pizzastore.order;

import com.myzuji.study.gof23.factory.absfactory.pizzastore.pizza.Pizza;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class OrderPizza {

    AbsFactory factory;

    public OrderPizza(AbsFactory factory) {
        setFactory(factory);
    }

    private void setFactory(AbsFactory absFactory) {
        Pizza pizza = null;
        String orderType = "";
        this.factory = absFactory;
        do {
            orderType = getType();
            pizza = factory.createPizza(orderType);
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
}
