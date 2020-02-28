package com.myzuji.backend.service.sys.impl;

import com.myzuji.backend.domain.system.SysMenu;
import com.myzuji.backend.domain.system.SysMenuService;
import com.myzuji.backend.dto.MenuDTO;
import com.myzuji.backend.rpt.sys.SysMenuRpt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/22
 */
@Service
@Transactional
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuRpt sysMenuRpt;

    @Override
    public List<SysMenu> obtainAllSysMenus() {
        return sysMenuRpt.obtainAllSysMenus();
    }

    @Override
    public SysMenu update(MenuDTO menuDTO) {
        return sysMenuRpt.update(menuDTO.menuBuild());
    }
}
