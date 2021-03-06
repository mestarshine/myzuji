package com.myzuji.study.gof23.builder.tradition;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/05
 */
public abstract class AbstractHouse {

    // 打地基
    public abstract void buildBasic();

    // 砌墙
    public abstract void buildWalls();

    // 封顶
    public abstract void roofed();

    public void build() {
        buildBasic();
        buildWalls();
        roofed();
    }
}
