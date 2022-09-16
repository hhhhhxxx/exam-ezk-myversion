package com.jdpu.auth.AuthService.impl;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import io.jsonwebtoken.*;
import com.jdpu.auth.AuthService.IAuthService;
import com.jdpu.common.entity.vo.RestResponse;
import com.jdpu.auth.service.IAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;


@Slf4j
@Service("AuthService")
public class AuthService implements IAuthService {
    /**
     * Authorization认证开头是"bearer "
     */
    private static final String BEARER = "Bearer ";

    @Autowired
    private IAuthenticationService authenticationService;


    /**
     * jwt token 密钥，主要用于token解析，签名验证
     */
    @Value("${spring.security.oauth2.jwt.signingKey:123456}")
    private String signingKey;

    /**
     * 不需要网关签权的url配置(/oauth,/open)
     * 默认/oauth开头是不需要的
     */
    @Value("${gate.ignore.authentication.startWith:/oauth,/open}")
    private String ignoreUrls = "/oauth,/open";

    @Override
    public RestResponse authenticate(String authentication, String userId, String url, String method, HttpServletRequest request) {
        return authenticationService.permission_p(userId, url, method, request);
    }

    @Override
    public boolean ignoreAuthentication(String url) {
        return Stream.of(this.ignoreUrls.split(",")).anyMatch(ignoreUrl -> url.startsWith(StringUtils.trim(ignoreUrl)));
    }

    @Override
    public boolean hasPermission(RestResponse authResult) {
        log.debug("签权结果:{}", authResult.getResponse());
        return authResult.isOk() && (boolean) authResult.getResponse();
    }


    @Cached(name = "gateway_auth::", key = "#userId+#method+#url",
            cacheType = CacheType.LOCAL, expire = 10, timeUnit = TimeUnit.SECONDS, localLimit = 10000)
    @Override
    public boolean hasPermission(String authentication, String userId, String url, String method, HttpServletRequest request) {
        if (StringUtils.isBlank(authentication)) {
            log.error("authentication is null");
            return Boolean.FALSE;
        }
        //从认证服务获取是否有权限,远程调用
        return hasPermission(authenticate(authentication, userId, url, method, request));
    }

    @Override
    public Jws<Claims> getJwt(String jwtToken) {
        if (jwtToken.startsWith(BEARER)) {
            jwtToken = StringUtils.substring(jwtToken, BEARER.length());
        }
        return Jwts.parser()  //得到DefaultJwtParser
                .setSigningKey(signingKey.getBytes()) //设置签名的秘钥
                .parseClaimsJws(jwtToken);
    }

    @Override
    public boolean invalidJwtAccessToken(String authentication) {
        // 是否无效true表示无效
        boolean invalid = Boolean.TRUE;
        try {
            getJwt(authentication);
            invalid = Boolean.FALSE;
        } catch (SignatureException | ExpiredJwtException | MalformedJwtException ex) {
            log.error("user token error :{}", ex.getMessage());
        }
        return invalid;
    }
}
