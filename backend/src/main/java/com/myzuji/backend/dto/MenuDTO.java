package com.myzuji.backend.dto;

import com.myzuji.backend.domain.system.MenuTypeEnum;
import com.myzuji.backend.domain.system.SysMenu;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/22
 */
public class MenuDTO {

    private Long id;

    private Long parentId = 0L;

    private String menuName;

    private String icon;

    private String href;

    private MenuTypeEnum menuType = MenuTypeEnum.SYS;

    private Integer sort = 1;

    private boolean hasChildren = false;

    private List<MenuDTO> childrenMenus;

    public MenuDTO(Long id, Long parentId, String menuName, String icon, String href,
                   MenuTypeEnum menuType, Integer sort) {
        this.id = id;
        this.parentId = parentId;
        this.menuName = menuName;
        this.icon = icon;
        this.href = href;
        this.menuType = menuType;
        this.sort = sort;
    }

    public static List<MenuDTO> makeMenuDTO(List<MenuDTO> menuDTOS, SysMenu menu) {
        menuDTOS.add(menuDTO(menu));
        return menuDTOS;
    }

    private static MenuDTO menuDTO(SysMenu menu) {
        return new MenuDTO(menu.getId(), menu.getParentId(), menu.getMenuName(), menu.getIcon(),
            menu.getHref(), menu.getMenuType(), menu.getSort());
    }


    /**
     * 构建菜单树
     *
     * @param menuDTOS 菜单列表
     * @return
     */
    public static List<MenuDTO> menuDTOBuild(List<MenuDTO> menuDTOS) {

        List<MenuDTO> trees = new ArrayList<>();

        for (MenuDTO menuDTO : menuDTOS) {

            if (0L == menuDTO.getParentId()) {
                trees.add(menuDTO);
            }

            for (MenuDTO it : menuDTOS) {
                if (menuDTO.getId().equals(it.getParentId())) {
                    if (menuDTO.getChildrenMenus() == null) {
                        menuDTO.setHasChildren(true);
                        menuDTO.setChildrenMenus(new ArrayList<>());
                    }
                    menuDTO.getChildrenMenus().add(it);
                }
            }
        }
        return trees;
    }

    public SysMenu menuBuild() {
        if (StringUtils.isBlank(menuName) || StringUtils.isBlank(href)) {
            return null;
        }
        SysMenu sysMenu = new SysMenu();
        BeanUtils.copyProperties(this, sysMenu);
        return sysMenu;
    }

    public Long getId() {
        return id;
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

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public List<MenuDTO> getChildrenMenus() {
        return childrenMenus;
    }

    public void setChildrenMenus(List<MenuDTO> childrenMenus) {
        this.childrenMenus = childrenMenus;
    }
}
