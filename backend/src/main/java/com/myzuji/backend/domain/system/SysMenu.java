package com.myzuji.backend.domain.system;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.myzuji.backend.domain.base.BaseEntity;
import com.myzuji.backend.dto.MenuDTO;
import com.myzuji.util.RightUtil;
import com.myzuji.util.ioc.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
@Table(name = "bs_sys_menu")
@Access(value = AccessType.FIELD)
public class SysMenu extends BaseEntity {

    private static final long serialVersionUID = 21832738122477987L;

    private static final Logger LOGGER = LoggerFactory.getLogger(SysMenu.class);

    private static ThreadFactory threadName = new ThreadFactoryBuilder().setNameFormat("menu-query-%d").build();
    private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, threadName);
    private static List<SysMenu> allSysMenus = null;

    @Column(name = "parent_id")
    private Long parentId = 0L;

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

    /**
     * 获取拥有添加权限的菜单
     *
     * @param sysRoles 角色列表
     * @return 权限值
     */
    public static BigInteger calculationAddRight(List<SysRole> sysRoles) {
        getAllSysMenus();
        if (allSysMenus == null || sysRoles == null || sysRoles.isEmpty()) {
            return null;
        }
        Set<Long> menuId = new HashSet<>();
        for (SysRole sysRole : sysRoles) {
            if (sysRoles.size() == 1) {
                return sysRole.getAddRight();
            }
            menuId = allSysMenus.stream()
                .filter(menu -> RightUtil.isRight(sysRole.getAddRight(), menu.getId()))
                .map(menu -> menu.getId())
                .collect(Collectors.toSet());
        }
        return RightUtil.addRight(menuId);
    }

    /**
     * 获取拥有删除权限的菜单
     *
     * @param sysRoles 角色列表
     * @return 权限值
     */
    public static BigInteger calculationDelRight(List<SysRole> sysRoles) {
        getAllSysMenus();
        if (allSysMenus == null || sysRoles == null || sysRoles.isEmpty()) {
            return null;
        }
        Set<Long> menuId = new HashSet<>();
        for (SysRole sysRole : sysRoles) {
            if (sysRoles.size() == 1) {
                return sysRole.getAddRight();
            }
            menuId = allSysMenus.stream()
                .filter(menu -> RightUtil.isRight(sysRole.getDelRight(), menu.getId()))
                .map(menu -> menu.getId())
                .collect(Collectors.toSet());
        }
        return RightUtil.addRight(menuId);
    }

    /**
     * 获取拥有编辑权限的菜单
     *
     * @param sysRoles 角色列表
     * @return 权限值
     */
    public static BigInteger calculationEditRight(List<SysRole> sysRoles) {
        getAllSysMenus();
        if (allSysMenus == null || sysRoles == null || sysRoles.isEmpty()) {
            return null;
        }
        Set<Long> menuId = new HashSet<>();
        for (SysRole sysRole : sysRoles) {
            if (sysRoles.size() == 1) {
                return sysRole.getAddRight();
            }
            menuId = allSysMenus.stream()
                .filter(menu -> RightUtil.isRight(sysRole.getEditRight(), menu.getId()))
                .map(menu -> menu.getId())
                .collect(Collectors.toSet());
        }
        return RightUtil.addRight(menuId);
    }

    /**
     * 获取拥有查询权限的菜单
     *
     * @param sysRoles
     * @return
     */
    public static BigInteger calculationQueryRight(List<SysRole> sysRoles) {
        getAllSysMenus();
        if (allSysMenus == null || sysRoles == null || sysRoles.isEmpty()) {
            return null;
        }
        Set<Long> menuId = new HashSet<>();
        for (SysRole sysRole : sysRoles) {
            if (sysRoles.size() == 1) {
                return sysRole.getAddRight();
            }
            menuId = allSysMenus.stream()
                .filter(menu -> RightUtil.isRight(sysRole.getQueryRight(), menu.getId()))
                .map(menu -> menu.getId())
                .collect(Collectors.toSet());
        }
        return RightUtil.addRight(menuId);
    }

    /**
     * 根据角色构建所拥有的菜单树
     *
     * @param sysRoles
     * @return
     */
    public static List<MenuDTO> calculationMenu(List<SysRole> sysRoles) {
        getAllSysMenus();
        if (allSysMenus == null || sysRoles == null || sysRoles.isEmpty()) {
            return null;
        }
        List<MenuDTO> menuDTOS = new ArrayList<>();
        for (SysRole sysRole : sysRoles) {
            allSysMenus.stream()
                .filter(menu -> RightUtil.isRight(sysRole.getMenuRight(), menu.getId()))
                .map(menu -> MenuDTO.makeMenuDTO(menuDTOS, menu))
                .collect(Collectors.toList());
        }
        return MenuDTO.menuDTOBuild(menuDTOS);
    }

    public static List<SysMenu> getAllSysMenus() {
        if (allSysMenus == null) {
            loadAllSysMenus();
        }
        return allSysMenus;
    }

    private static synchronized void loadAllSysMenus() {
        obtainAllSysMenus();
        loadAllSysMenusTask();
    }

    private static void loadAllSysMenusTask() {
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                obtainAllSysMenus();
            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    private static void obtainAllSysMenus() {
        try {
            allSysMenus = Registry.queryBean(SysMenuService.class).obtainAllSysMenus();
        } catch (Exception e) {
            LOGGER.error("菜单加载失败：" + e.getMessage());
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
