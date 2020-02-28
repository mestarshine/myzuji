package com.myzuji.backend.rpt.sys;

import com.myzuji.backend.domain.system.SysMenu;
import com.myzuji.backend.rpt.base.BaseRptImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/22
 */
@Repository
public class SysMenuRptImpl extends BaseRptImpl<SysMenu> implements SysMenuRpt {

    @Override
    public List<SysMenu> obtainAllSysMenus() {
        return getSession().createQuery("from SysMenu").getResultList();
    }

    @Override
    public SysMenu update(SysMenu menu) {
        saveOrUpdate(menu);
        return menu;
    }
}
