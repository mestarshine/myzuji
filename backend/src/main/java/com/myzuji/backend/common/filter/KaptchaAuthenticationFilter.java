package com.myzuji.backend.common.filter;

import com.google.code.kaptcha.Constants;
import com.myzuji.backend.common.constants.SecurityConstants;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/24
 */
public class KaptchaAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private String servletPath;

    public KaptchaAuthenticationFilter(String servletPath, AuthenticationFailureHandler authenticationFailureHandler) {
        super(servletPath);
        this.servletPath = servletPath;
        setAuthenticationFailureHandler(authenticationFailureHandler);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws
        IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if ("POST".equalsIgnoreCase(request.getMethod()) && servletPath.equals(request.getServletPath())) {
            String expect = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
            if (expect != null && !expect.equalsIgnoreCase(request.getParameter(SecurityConstants.CURRENT_VALIDATECODE))) {
                unsuccessfulAuthentication(request, response, new InsufficientAuthenticationException("输入的验证码不正确"));
                return;
            }
        }
        chain.doFilter(req, res);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
        AuthenticationException, IOException, ServletException {
        return null;
    }
}
