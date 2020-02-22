package com.myzuji.backend.rpt.sys;

import com.myzuji.backend.domain.system.SysToken;
import com.myzuji.backend.dto.Token;
import com.myzuji.backend.rpt.base.BaseRptImpl;
import org.springframework.stereotype.Repository;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/22
 */
@Repository
public class TokenRptImpl extends BaseRptImpl<Token> implements TokenRpt {

    @Override
    public void save(SysToken token) {
        saveOrUpdate(token);
    }

    @Override
    public SysToken getById(String token) {
        return null;
    }

    @Override
    public void update(SysToken token) {
        saveOrUpdate(token);
    }

    @Override
    public void delete(String uuid) {

    }
}
