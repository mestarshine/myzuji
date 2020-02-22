package com.myzuji.backend.service.sys;

import com.myzuji.backend.dto.LoginUser;
import com.myzuji.backend.dto.Token;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/22
 */
public interface TokenService {

    Token saveToken(LoginUser loginUser);

    void refresh(LoginUser loginUser);

    LoginUser getLoginUser(String jwtToken);

    boolean deleteToken(String jwtToken);
}
