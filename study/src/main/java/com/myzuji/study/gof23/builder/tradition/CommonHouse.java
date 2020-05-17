package com.myzuji.study.gof23.builder.tradition;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/05
 */
public class CommonHouse extends AbstractHouse {

    @Override
    public void buildBasic() {
        System.out.println("普通房子打地基");
    }

    @Override
    public void buildWalls() {
        System.out.println("普通房子砌墙");
    }

    @Override
    public void roofed() {
        System.out.println("普通房子封顶");
    }

}
