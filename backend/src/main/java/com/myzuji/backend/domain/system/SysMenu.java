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
@Table(name = "bs_sys_menu")
@Access(value = AccessType.FIELD)
public class SysMenu extends BaseEntity {

    private static final long serialVersionUID = 21832738122477987L;

    private static final Logger LOGGER = LoggerFactory.getLogger(SysMenu.class);

    private static ThreadFactory threadName = new ThreadFactoryBuilder().setNameFormat("menu-query-%d").build();
    private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, threadName);
    private static List<SysMenu> allSysMenus = null;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "menu_ame")
    private String menuName;

    @Column(name = "icon")
    private String icon;

    @Column(name = "href")
    private String href;

    @Column(name = "menu_type")
    @Enumerated(EnumType.STRING)
    private MenuTypeEnum menuType;

    @Column(name = "sort")
    private Integer sort;

    public static List<SysMenu> getAllSysMenus() {
        if (allSysMenus == null) {
            loadAllSysMenus();
        }
        return allSysMenus;
    }

    private static void loadAllSysMenus() {
        obtainAllSysMenus();
        loadAllSysMenusTask();
    }

    private static void loadAllSysMenusTask() {
        executor.scheduleAtFixedRate(() -> obtainAllSysMenus(), 1, 1, TimeUnit.MINUTES);
    }

    private static void obtainAllSysMenus() {
        try {
            allSysMenus = Registry.queryBean(SysMenuService.class).obtainAllSysMenus();
        } catch (Exception e) {
            LOGGER.error("菜单加载失败：{}", e.toString());
        }
    }

    public Long getParentId() {
        return parentId;
    }

    public String getMenuName() {
        return menuName;
    }

    public String getIcon() {
        return icon;
    }

    public String getHref() {
        return href;
    }

    public MenuTypeEnum getMenuType() {
        return menuType;
    }

    public Integer getSort() {
        return sort;
    }
}
