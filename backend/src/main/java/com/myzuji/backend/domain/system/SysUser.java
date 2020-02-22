package com.myzuji.backend.domain.system;

import com.myzuji.backend.domain.base.BaseEntity;

import javax.persistence.*;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/22
 */
@Entity
@Table(name = "bs_sys_user")
@Access(value = AccessType.FIELD)
public class SysUser extends BaseEntity {

    private static final long serialVersionUID = 1233255826905167879L;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "login_name")
    private String loginName;

    @Column(name = "password")
    private String password;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "head_img")
    private String headImgUrl;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "role_right")
    private String roleRight;

    @Column(name = "user_status")
    @Enumerated(EnumType.STRING)
    private UserStatusEnum userStatus;

    public SysUser(Long parentId, String loginName, String password, String nickName, String headImgUrl, String phone,
                   String email, String roleRight) {
        this.parentId = parentId;
        this.loginName = loginName;
        this.password = password;
        this.nickName = nickName;
        this.headImgUrl = headImgUrl;
        this.phone = phone;
        this.email = email;
        this.roleRight = roleRight;
        this.userStatus = UserStatusEnum.VALID;
    }

    /**
     * 用户是否被锁
     * true 用户被锁定
     *
     * @return
     */
    public boolean isLocked() {
        return userStatus == UserStatusEnum.LOCKED;
    }

    /**
     * 用户是否无效
     * true 无效用户
     *
     * @return
     */
    public boolean isDisable() {
        return userStatus == UserStatusEnum.DISABLED;
    }

    public Long getParentId() {
        return parentId;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getPassword() {
        return password;
    }

    public String getNickName() {
        return nickName;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getRoleRight() {
        return roleRight;
    }

    public UserStatusEnum getUserStatus() {
        return userStatus;
    }
}
