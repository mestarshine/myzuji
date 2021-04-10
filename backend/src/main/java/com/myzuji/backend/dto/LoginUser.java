package com.myzuji.backend.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myzuji.backend.domain.system.SysMenu;
import com.myzuji.backend.domain.system.SysRole;
import com.myzuji.backend.domain.system.SysUser;
import com.myzuji.backend.domain.system.UserStatusEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigInteger;
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
public class LoginUser implements UserDetails {

    private static final long serialVersionUID = 7477249917989433791L;
    private String username;
    private String password;
    private String roleRight;
    private String userImg;
    private UserStatusEnum userStatus;

    @JsonIgnore
    @JSONField(serialize = false)
    private List<SysRole> sysRoles;

    @JsonIgnore
    @JSONField(serialize = false)
    private BigInteger addRight;

    @JsonIgnore
    @JSONField(serialize = false)
    private BigInteger delRight;

    @JsonIgnore
    @JSONField(serialize = false)
    private BigInteger editRight;

    @JsonIgnore
    @JSONField(serialize = false)
    private BigInteger queryRight;

    @JsonIgnore
    @JSONField(serialize = false)
    private List<MenuDTO> menuDTOS;
    private String token;
    /**
     * 登陆时间戳
     */
    private LocalDateTime loginTime;
    /**
     * 过期时间戳
     */
    private LocalDateTime expireTime;

    public LoginUser() {
    }

    public LoginUser(SysUser sysUser) {
        this.username = sysUser.getLoginName();
        this.password = sysUser.getPassword();
        this.roleRight = sysUser.getRoleRight();
        this.userStatus = sysUser.getUserStatus();
        this.userImg = sysUser.getHeadImgUrl();
    }

    /**
     * 计算用户权限
     */
    public void calculationRight() {
        this.sysRoles = SysRole.calculationRole(roleRight);
        this.addRight = SysMenu.calculationAddRight(sysRoles);
        this.delRight = SysMenu.calculationDelRight(sysRoles);
        this.editRight = SysMenu.calculationEditRight(sysRoles);
        this.queryRight = SysMenu.calculationQueryRight(sysRoles);
        this.menuDTOS = SysMenu.calculationMenu(sysRoles);
    }

    @Override
    @JsonIgnore
    @JSONField(serialize = false)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (sysRoles == null) {
            sysRoles = SysRole.calculationRole(roleRight);
        }
        sysRoles.stream().forEach(sysRole -> {
            authorities.add(new SimpleGrantedAuthority(sysRole.getId().toString()));
        });
        return authorities;
    }

    @Override
    @JsonIgnore
    @JSONField(serialize = false)
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    @JSONField(serialize = false)
    public boolean isAccountNonLocked() {
        return UserStatusEnum.LOCKED != userStatus;
    }

    @Override
    @JsonIgnore
    @JSONField(serialize = false)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    @JSONField(serialize = false)
    public boolean isEnabled() {
        return UserStatusEnum.DISABLED != userStatus;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    @JSONField(serialize = false)
    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getRoleRight() {
        return roleRight;
    }

    public void setRoleRight(String roleRight) {
        this.roleRight = roleRight;
    }

    public UserStatusEnum getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatusEnum userStatus) {
        this.userStatus = userStatus;
    }

    public List<SysRole> getSysRoles() {
        return sysRoles;
    }

    public void setSysRoles(List<SysRole> sysRoles) {
        this.sysRoles = sysRoles;
    }

    public BigInteger getAddRight() {
        return addRight;
    }

    public void setAddRight(BigInteger addRight) {
        this.addRight = addRight;
    }

    public BigInteger getDelRight() {
        return delRight;
    }

    public void setDelRight(BigInteger delRight) {
        this.delRight = delRight;
    }

    public BigInteger getEditRight() {
        return editRight;
    }

    public void setEditRight(BigInteger editRight) {
        this.editRight = editRight;
    }

    public BigInteger getQueryRight() {
        return queryRight;
    }

    public void setQueryRight(BigInteger queryRight) {
        this.queryRight = queryRight;
    }

    public List<MenuDTO> getMenuDTOS() {
        return menuDTOS;
    }

    public void setMenuDTOS(List<MenuDTO> menuDTOS) {
        this.menuDTOS = menuDTOS;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
}
