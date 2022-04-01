package com.myzuji.study.java;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/01
 */
public interface RoutType {

    Object getValue(int a, int b);

    Object getValue(int a, String c);

    enum TypeEnum implements RoutType {
        BALANCE("余额") {
            @Override
            public Object getValue(int a, int b) {
                return "余额1";
            }

            @Override
            public Object getValue(int a, String c) {
                return "余额2";
            }
        }, CARD("卡号限制") {
            @Override
            public Object getValue(int a, int b) {
                return "卡号限制";
            }

            @Override
            public Object getValue(int a, String c) {
                return "卡号限制2";
            }
        };

        private final String desc;

        TypeEnum(String desc) {
            this.desc = desc;
        }


        @Override
        public Object getValue(int a, int b) {
            return null;
        }

        @Override
        public Object getValue(int a, String c) {
            return null;
        }
    }
}
