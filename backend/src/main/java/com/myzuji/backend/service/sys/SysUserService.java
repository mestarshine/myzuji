package com.myzuji.backend.service.sys;

import com.myzuji.backend.domain.system.SysUser;

import java.util.List;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/22
 */
public interface SysUserService {

    SysUser obtainUserByLoginName(String loginName);

    SysUser save(long parentId, String loginName, String password, String nickName, String headImgUrl, String phone,
                 String email, List<Long> roleRight);
}
