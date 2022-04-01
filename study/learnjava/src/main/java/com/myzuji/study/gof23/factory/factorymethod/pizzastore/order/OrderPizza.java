package com.myzuji.study.gof23.factory.factorymethod.pizzastore.order;

import com.myzuji.study.gof23.factory.factorymethod.pizzastore.pizza.Pizza;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public abstract class OrderPizza {


    public OrderPizza() {
        Pizza pizza = null;
        String orderType; //pizza 类型
        do {
            orderType = getType();
            pizza = createPizza(orderType); //抽象方法，由工厂子类完成
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

    //定义个抽象方法，createPizza 让各个工厂子类自己实现
    abstract Pizza createPizza(String orderType);

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
