package com.myzuji.backend.domain.system;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.myzuji.backend.domain.base.BaseEntity;
import com.myzuji.util.RightUtil;
import com.myzuji.util.ioc.Registry;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/22
 */
@Entity
@Table(name = "bs_sys_role")
@Access(value = AccessType.FIELD)
public class SysRole extends BaseEntity {

    private static final long serialVersionUID = -8073027733583921096L;

    private static final Logger LOGGER = LoggerFactory.getLogger(SysMenu.class);

    private static ThreadFactory threadName = new ThreadFactoryBuilder().setNameFormat("role-query-%d").build();
    private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, threadName);
    private static List<SysRole> allSysRoles = null;

    @Column(name = "parent_id")
    private Long parentId = 0L;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "menu_right")
    private BigInteger menuRight;

    @Column(name = "add_right")
    private BigInteger addRight;

    @Column(name = "del_right")
    private BigInteger delRight;

    @Column(name = "edit_right")
    private BigInteger editRight;

    @Column(name = "query_right")
    private BigInteger queryRight;

    public SysRole() {
    }

    public SysRole(Long parentId, String roleName, BigInteger menuRight, BigInteger addRight, BigInteger delRight, BigInteger editRight, BigInteger queryRight) {
        this.parentId = parentId;
        this.roleName = roleName;
        this.menuRight = menuRight;
        this.addRight = addRight;
        this.delRight = delRight;
        this.editRight = editRight;
        this.queryRight = queryRight;
    }

    public static List<SysRole> getAllSysRoles() {
        if (allSysRoles == null) {
            loadAllSysRoles();
        }
        return allSysRoles;
    }

    private static void loadAllSysRoles() {
        obtainAllSysRoles();
        loadAllSysRolesTask();
    }

    private static void loadAllSysRolesTask() {
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                obtainAllSysRoles();
            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    private static void obtainAllSysRoles() {
        try {
            allSysRoles = Registry.queryBean(SysRoleService.class).obtainAllSysRoles();
        } catch (Exception e) {
            LOGGER.error("菜单加载失败：" + e.getMessage());
        }
    }

    /**
     * 根据用户的角色值计算用户所拥有的角色
     *
     * @param userRoleRight 用户角色权限
     * @return
     */
    public static List<SysRole> calculationRole(String userRoleRight) {
        getAllSysRoles();
        if (allSysRoles == null) {
            return null;
        }
        if (StringUtils.isBlank(userRoleRight)) {
            return null;
        }
        return allSysRoles.stream().filter(role -> RightUtil.isRight(BigInteger.valueOf(Long.valueOf(userRoleRight)), role.getId())).collect(Collectors.toList());
    }

    public Long getParentId() {
        return parentId;
    }

    public String getRoleName() {
        return roleName;
    }

    public BigInteger getMenuRight() {
        return menuRight;
    }

    public BigInteger getAddRight() {
        return addRight;
    }

    public BigInteger getDelRight() {
        return delRight;
    }

    public BigInteger getEditRight() {
        return editRight;
    }

    public BigInteger getQueryRight() {
        return queryRight;
    }
}
