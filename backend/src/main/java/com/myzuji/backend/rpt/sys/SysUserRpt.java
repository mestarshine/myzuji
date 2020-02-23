package com.myzuji.backend.rpt.sys;

import com.myzuji.backend.domain.system.SysUser;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/22
 */
public interface SysUserRpt {

    SysUser save(SysUser sysUser);

    SysUser obtainUserByLoginName(String loginName);
}
