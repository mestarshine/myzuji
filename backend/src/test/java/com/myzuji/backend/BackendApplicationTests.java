package com.myzuji.backend;

import com.myzuji.backend.domain.system.SysRole;
import com.myzuji.backend.domain.system.SysRoleService;
import com.myzuji.backend.domain.system.SysUser;
import com.myzuji.backend.service.sys.SysUserService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/01
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BackendApplicationTests {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Test
    @Ignore
    public void saveUserTest() {
        List<Long> right = Arrays.asList(1L);
        sysUserService.save(0L, "admin", "123123", "admin", null,
            "13681917565", "363161476@qq.com", right);
    }

    @Test
    @Ignore
    public void saveRoleTest() {
        SysRole sysRole = new SysRole(0L, "管理员", null, null, null, null, null);
        sysRoleService.save(sysRole);
    }

    @Test
    @Ignore
    public void obtainUserByLoginNameTest() {
        SysUser sysUser = sysUserService.obtainUserByLoginName("admin");
        System.out.println(sysUser);
    }

}
