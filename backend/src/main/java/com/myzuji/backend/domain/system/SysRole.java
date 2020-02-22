package com.myzuji.backend.domain.system;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.myzuji.backend.domain.base.BaseEntity;
import com.myzuji.util.ioc.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

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
    private Long parentId;

    @Column(name = "username")
    private String username;

    @Column(name = "menu_right")
    private String menuRight;

    @Column(name = "add_right")
    private String addRight;

    @Column(name = "del_right")
    private String delRight;

    @Column(name = "edit_right")
    private String editRight;

    @Column(name = "query_right")
    private String queryRight;


    public static List<SysRole> getAllSysMenus() {
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
        executor.scheduleAtFixedRate(() -> obtainAllSysRoles(), 1, 1, TimeUnit.MINUTES);
    }

    private static void obtainAllSysRoles() {
        try {
            allSysRoles = Registry.queryBean(SysRoleService.class).obtainAllSysRoles();
        } catch (Exception e) {
            LOGGER.error("菜单加载失败：{}", e.getMessage());
        }
    }

    // TODO: 2020-02-22  所有菜单放在内存中 按钮和菜单实时计算
    public Long getParentId() {
        return parentId;
    }

    public String getUsername() {
        return username;
    }

    public String getMenuRight() {
        return menuRight;
    }

    public String getAddRight() {
        return addRight;
    }

    public String getDelRight() {
        return delRight;
    }

    public String getEditRight() {
        return editRight;
    }

    public String getQueryRight() {
        return queryRight;
    }
}
