package com.myzuji.backend.controller;

import com.myzuji.backend.common.util.UserUtil;
import com.myzuji.backend.dto.LoginUser;
import com.myzuji.backend.service.sys.SysUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/24
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private SysUserService sysUserService;

    @ResponseBody
    @RequestMapping(value = "/current", method = RequestMethod.POST)
    public LoginUser currentUser() {
        LoginUser loginUser = UserUtil.getLoginUser();
        loginUser.calculationRight();
        return loginUser;
    }
}
