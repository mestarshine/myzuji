package com.myzuji.backend.service.sys.impl;

import com.alibaba.fastjson.JSONObject;
import com.myzuji.backend.domain.system.SysLog;
import com.myzuji.backend.domain.system.SysToken;
import com.myzuji.backend.dto.LoginUser;
import com.myzuji.backend.dto.Token;
import com.myzuji.backend.rpt.sys.TokenRpt;
import com.myzuji.backend.service.sys.SysLogService;
import com.myzuji.backend.service.sys.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/22
 */
@Service
public class TokenServiceImpl implements TokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenServiceImpl.class);

    private static Key key = null;

    private static final String LOGIN_USER_KEY = "LOGIN_USER_KEY";

    /**
     * token过期秒数
     */
    @Value("${token.expire.seconds}")
    private Integer expireSeconds;

    @Autowired
    private TokenRpt tokenRpt;

    @Autowired
    private SysLogService logService;

    /**
     * 私钥
     */
    @Value("${token.jwtSecret}")
    private String jwtSecret;

    @Override
    public Token saveToken(LoginUser loginUser) {
        loginUser.withToken(UUID.randomUUID().toString())
            .withLoginTime(LocalDateTime.now())
            .withExpireTime(getExpireTime());

        SysToken token = new SysToken(loginUser.getToken(), JSONObject.toJSONString(loginUser), loginUser.getExpireTime());
        tokenRpt.save(token);
        // 登陆日志
        logService.save(new SysLog(loginUser.getId(), "登陆", true, null));

        String jwtToken = createJWTToken(loginUser);

        return new Token(jwtToken, loginUser.getLoginTime());
    }

    private LocalDateTime getExpireTime() {
        return LocalDateTime.now().plusSeconds(expireSeconds);
    }

    @Override
    public void refresh(LoginUser loginUser) {
        loginUser.withLoginTime(LocalDateTime.now()).withExpireTime(getExpireTime());

        SysToken token = tokenRpt.getById(loginUser.getToken());
        token.withLoginUserContext(JSONObject.toJSONString(loginUser));

        tokenRpt.update(token);
    }

    @Override
    public LoginUser getLoginUser(String jwtToken) {
        String uuid = getUUIDFromJWT(jwtToken);
        if (uuid != null) {
            SysToken token = tokenRpt.getById(uuid);
            return toLoginUser(token);
        }

        return null;
    }

    @Override
    public boolean deleteToken(String jwtToken) {
        String uuid = getUUIDFromJWT(jwtToken);
        if (uuid != null) {
            SysToken token = tokenRpt.getById(uuid);
            LoginUser loginUser = toLoginUser(token);
            if (loginUser != null) {
                tokenRpt.delete(uuid);
                logService.save(new SysLog(loginUser.getId(), "推出", true, null));

                return true;
            }
        }

        return false;
    }

    /**
     * 生成jwt
     *
     * @param loginUser
     * @return
     */
    private String createJWTToken(LoginUser loginUser) {
        Map<String, Object> claims = Collections.emptyMap();
        // 放入一个随机字符串，通过该串可找到登陆用户
        claims.put(LOGIN_USER_KEY, loginUser.getToken());

        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, getKeyInstance()).compact();
    }

    private LoginUser toLoginUser(SysToken token) {
        if (token == null) {
            return null;
        }

        // 校验是否已过期
        if (token.getExpireTime().isAfter(LocalDateTime.now())) {
            return JSONObject.parseObject(token.getLoginUserContext(), LoginUser.class);
        }

        return null;
    }


    private Key getKeyInstance() {
        if (key == null) {
            synchronized (TokenServiceImpl.class) {
                if (key == null) {// 双重锁
                    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtSecret);
                    key = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
                }
            }
        }

        return key;
    }

    private String getUUIDFromJWT(String jwt) {
        if ("null".equals(jwt) || StringUtils.isBlank(jwt)) {
            return null;
        }

        Map<String, Object> jwtClaims = null;
        try {
            jwtClaims = Jwts.parser().setSigningKey(getKeyInstance()).parseClaimsJws(jwt).getBody();
            return MapUtils.getString(jwtClaims, LOGIN_USER_KEY);
        } catch (ExpiredJwtException e) {
            LOGGER.error("{}已过期", jwt);
        } catch (Exception e) {
            LOGGER.error("{}", e);
        }

        return null;
    }
}
