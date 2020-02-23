package com.myzuji.backend.service.sys.impl;

import com.myzuji.backend.domain.system.SysRole;
import com.myzuji.backend.domain.system.SysRoleService;
import com.myzuji.backend.rpt.sys.SysRoleRpt;
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
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleRpt sysRoleRpt;

    @Override
    public List<SysRole> obtainAllSysRoles() {
        return sysRoleRpt.obtainAllSysRoles();
    }

    @Override
    public void save(SysRole sysRole) {
        sysRoleRpt.save(sysRole);
    }
}
