package com.myzuji.study.gof23.prototype.improve;

public class Client {

    public static void main(String[] args) {
        System.out.println("原型模式完成对象的创建");
        Sheep sheep = new Sheep("tom", 1, "白色");
        Sheep sheep1 = (Sheep) sheep.clone(); // 克隆
        Sheep sheep2 = (Sheep) sheep.clone(); // 克隆
        Sheep sheep3 = (Sheep) sheep.clone(); // 克隆
        System.out.println("sheep1" + sheep1);
    }
}
