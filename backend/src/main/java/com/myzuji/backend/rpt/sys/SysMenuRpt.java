package com.myzuji.backend.rpt.sys;

import com.myzuji.backend.domain.system.SysMenu;

import java.util.List;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/22
 */
public interface SysMenuRpt {

    List<SysMenu> obtainAllSysMenus();
}
