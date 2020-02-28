package com.myzuji.backend.dto;

import com.myzuji.backend.domain.base.BaseEntity;

import java.time.LocalDateTime;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/22
 */
public class Token extends BaseEntity {

    private static final long serialVersionUID = -8073450568180953594L;

    private String token;
    /**
     * 登陆时间戳（毫秒）
     */
    private LocalDateTime loginTime;

    public Token(String token, LocalDateTime loginTime) {
        super();
        this.token = token;
        this.loginTime = loginTime;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

}
