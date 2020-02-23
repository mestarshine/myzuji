package com.myzuji.backend.service.sys.impl;

import com.myzuji.backend.domain.system.SysUser;
import com.myzuji.backend.rpt.sys.SysUserRpt;
import com.myzuji.backend.service.sys.SysUserService;
import com.myzuji.util.RightUtil;
import org.apache.commons.lang3.StringUtils;
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
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserRpt sysUserRpt;

    @Override
    public SysUser obtainUserByLoginName(String loginName) {
        return sysUserRpt.obtainUserByLoginName(loginName);
    }

    @Override
    public SysUser save(long parentId, String loginName, String password, String nickName, String headImgUrl, String phone,
                        String email, List<Long> roleRight) {
        if (StringUtils.isBlank(loginName) || StringUtils.isBlank(password) || roleRight == null || roleRight.size() == 0) {
            throw new IllegalArgumentException("必要参数为空");
        }
        SysUser user = new SysUser(parentId, loginName, password, nickName, headImgUrl, phone, email, RightUtil.addRight(roleRight).toString());
        return sysUserRpt.save(user);
    }
}
