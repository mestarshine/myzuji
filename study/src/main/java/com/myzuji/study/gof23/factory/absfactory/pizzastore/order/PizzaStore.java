package com.myzuji.study.gof23.factory.absfactory.pizzastore.order;


// 相当于一个客户端，发出订购
public class PizzaStore {

    public static void main(String[] args) {

        new OrderPizza(new BJFactory());

    }
}
