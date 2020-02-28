package com.myzuji.backend.common.util;

import com.myzuji.backend.dto.LoginUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/24
 */
public class UserUtil {

    public static LoginUser getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (authentication instanceof AnonymousAuthenticationToken) {
                return null;
            }

            if (authentication instanceof UsernamePasswordAuthenticationToken) {
                return (LoginUser) authentication.getPrincipal();
            }
        }

        return null;
    }
}
