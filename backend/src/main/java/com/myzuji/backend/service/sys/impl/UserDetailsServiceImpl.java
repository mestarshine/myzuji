package com.myzuji.backend.service.sys.impl;

import com.myzuji.backend.domain.system.SysUser;
import com.myzuji.backend.service.sys.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/22
 */
@Service("dbUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.obtainUserByUserName(username);
        if (sysUser == null) {
            throw new AuthenticationCredentialsNotFoundException("用户名不存在");
        } else if (sysUser.isLocked()) {
            throw new LockedException("用户被锁定,请联系管理员");
        } else if (sysUser.isDisable()) {
            throw new DisabledException("用户已作废");
        }
        return null;
    }
}
