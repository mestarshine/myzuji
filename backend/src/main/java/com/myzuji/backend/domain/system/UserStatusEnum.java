package com.myzuji.backend.domain.system;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/22
 */
public enum UserStatusEnum {

    DISABLED("无效"),
    VALID("有效"),
    LOCKED("锁定");

    private String name;

    UserStatusEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
