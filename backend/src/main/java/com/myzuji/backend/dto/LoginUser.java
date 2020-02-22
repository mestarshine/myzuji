package com.myzuji.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myzuji.backend.domain.system.SysMenu;
import com.myzuji.backend.domain.system.SysRole;
import com.myzuji.backend.domain.system.SysUser;
import com.myzuji.backend.domain.system.UserStatusEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/15
 */
public class LoginUser extends SysUser implements UserDetails {

    private static final long serialVersionUID = 7477249917989433791L;

    private List<SysMenu> sysMenus;
    private List<SysRole> sysRoles;
    private String token;
    /**
     * 登陆时间戳
     */
    private LocalDateTime loginTime;
    /**
     * 过期时间戳
     */
    private LocalDateTime expireTime;

    public LoginUser(Long parentId, String loginName, String password, String nickName, String headImgUrl, String phone,
                     String email, String roleRight, String token, LocalDateTime loginTime, LocalDateTime expireTime) {
        super(parentId, loginName, password, nickName, headImgUrl, phone, email, roleRight);
        this.token = token;
        this.loginTime = loginTime;
        this.expireTime = expireTime;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        sysRoles.stream().forEach(sysRole -> authorities.add(new SimpleGrantedAuthority(sysRole.getId().toString())));
        return authorities;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return this.getLoginName();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return expireTime.isAfter(LocalDateTime.now());
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return UserStatusEnum.LOCKED != getUserStatus();
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return UserStatusEnum.DISABLED != getUserStatus();
    }

    public LoginUser withToken(String token) {
        this.token = token;
        return this;
    }

    public LoginUser withLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
        return this;
    }

    public LoginUser withExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
        return this;
    }

    public List<SysMenu> getSysMenus() {
        return sysMenus;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }
}
