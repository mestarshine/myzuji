package com.myzuji.backend.rpt.sys;

import com.myzuji.backend.domain.system.SysRole;
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
public class SysRoleRptImpl extends BaseRptImpl<SysRole> implements SysRoleRpt {

    @Override
    public List<SysRole> obtainAllSysRoles() {
        return getSession().createQuery("from SysRole").getResultList();
    }

    @Override
    public void save(SysRole sysRole) {
        saveOrUpdate(sysRole);
    }
}
