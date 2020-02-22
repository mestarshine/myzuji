package com.myzuji.backend.domain.system;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/22
 */
public enum MenuTypeEnum {

    SYS("系统"),
    BUSINESS("业务");

    private String name;

    MenuTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
