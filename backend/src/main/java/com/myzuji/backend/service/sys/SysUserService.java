package com.myzuji.backend.service.sys;

import com.myzuji.backend.domain.system.SysUser;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/22
 */
public interface SysUserService {

    SysUser obtainUserByUserName(String userName);
}
