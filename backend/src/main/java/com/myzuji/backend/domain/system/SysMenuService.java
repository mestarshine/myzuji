package com.myzuji.backend.domain.system;

import com.myzuji.backend.dto.MenuDTO;

import java.util.List;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/22
 */
public interface SysMenuService {

    List<SysMenu> obtainAllSysMenus();

    SysMenu update(MenuDTO menuDTO);

}
