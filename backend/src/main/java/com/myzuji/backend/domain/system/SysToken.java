package com.myzuji.backend.domain.system;

import com.myzuji.backend.domain.base.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/22
 */
@Entity
@Table(name = "bs_sys_token")
@Access(value = AccessType.FIELD)
public class SysToken extends BaseEntity {

    @Column(name = "token")
    private String token;

    @Column(name = "login_user_context", length = 10000)
    private String loginUserContext;

    @Column(name = "expire_time")
    private LocalDateTime expireTime;

    public SysToken() {
    }

    public SysToken(String token, String loginUserContext, LocalDateTime expireTime) {
        this.token = token;
        this.loginUserContext = loginUserContext;
        this.expireTime = expireTime;
    }

    public SysToken withLoginUserContext(String context) {
        this.loginUserContext = context;
        return this;
    }

    public String getToken() {
        return token;
    }

    public String getLoginUserContext() {
        return loginUserContext;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }
}
