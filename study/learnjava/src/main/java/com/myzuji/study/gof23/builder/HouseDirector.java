package com.myzuji.study.gof23.builder;

/**
 * 指挥者，这里去指定制作流程，返回具体产品
 *
 * @author shine
 * @date 2020/02/05
 */
public class HouseDirector {

    HouseBuilder houseBuilder;

    //构造器传入 HouseBuilder
    public HouseDirector(HouseBuilder houseBuilder) {
        this.houseBuilder = houseBuilder;
    }

    //通过 setter 传入 HouseBuilder
    public void setHouseBuilder(HouseBuilder houseBuilder) {
        this.houseBuilder = houseBuilder;
    }

    // 如何处理建造房子的流程，交给指挥者
    public House constructHouse() {
        houseBuilder.buildBasic();
        houseBuilder.buildWalls();
        houseBuilder.roofed();
        return houseBuilder.buildHouse();
    }
}
