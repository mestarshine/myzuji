package com.myzuji.study.java;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/01
 */
public class Test {

    public static void main(String[] args) {
        RoutType.TypeEnum type = RoutType.TypeEnum.CARD;
        switch (type) {
            case BALANCE:
                System.out.println("BALANCE1");
            case CARD:
            default:
                System.out.println("默认");
        }
    }

}
