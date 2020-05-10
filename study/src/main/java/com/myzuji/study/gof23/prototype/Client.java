package com.myzuji.study.gof23.prototype;

public class Client {

    public static void main(String[] args) {
        //传统方法
        //优点：好理解，简单易操作
        //缺点：在创建新的对象时，总是需要重新获取原始的属性，如果创建的对象比较复杂时，效率 较低
        Sheep sheep = new Sheep("tom", 1, "白色");
        Sheep sheep2 = new Sheep(sheep.getName(), sheep.getAge(), sheep.getColor());
        Sheep sheep3 = new Sheep(sheep.getName(), sheep.getAge(), sheep.getColor());
        Sheep sheep4 = new Sheep(sheep.getName(), sheep.getAge(), sheep.getColor());
        Sheep sheep5 = new Sheep(sheep.getName(), sheep.getAge(), sheep.getColor());
        System.out.println(sheep);
        System.out.println(sheep2);
        System.out.println(sheep3);
        System.out.println(sheep4);
        System.out.println(sheep5);

    }
}
