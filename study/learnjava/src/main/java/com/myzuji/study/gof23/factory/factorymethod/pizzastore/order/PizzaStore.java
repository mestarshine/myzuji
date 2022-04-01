package com.myzuji.study.gof23.factory.factorymethod.pizzastore.order;


// 相当于一个客户端，发出订购
public class PizzaStore {

    public static void main(String[] args) {

        // 创建北京口味的各种pizza
        new BJOrderPizza();

    }
}
