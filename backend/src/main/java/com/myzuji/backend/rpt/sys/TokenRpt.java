package com.myzuji.backend.rpt.sys;

import com.myzuji.backend.domain.system.SysToken;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/22
 */
public interface TokenRpt {

    void save(SysToken token);

    SysToken getById(String token);

    void update(SysToken token);

    void delete(String uuid);
}
