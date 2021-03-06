package com.myzuji.study.gof23.builder;

/**
 * 抽象的建造者
 *
 * @author shine
 * @date 2020/02/05
 */
public abstract class HouseBuilder {

    protected House house = new House();

    //将建造的流程写好，抽象的方法
    public abstract void buildBasic();

    public abstract void buildWalls();

    public abstract void roofed();

    // 建造房子后，将产品返回
    public House buildHouse() {
        return house;
    }

}
