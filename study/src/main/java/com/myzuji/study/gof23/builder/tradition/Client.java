package com.myzuji.study.gof23.builder.tradition;

/**
 * 传统方式
 * 优点：好理解，简单易操作
 * 确定：程序结构，过于简单，没有设计缓存层对象，程序扩展和维护不好
 * 解决方案：将产品和产品建造过程解耦===> 建造者模式
 *
 * @author shine
 * @date 2020/02/05
 */
public class Client {

    public static void main(String[] args) {

        CommonHouse commonHouse = new CommonHouse();
        commonHouse.build();
    }
}
