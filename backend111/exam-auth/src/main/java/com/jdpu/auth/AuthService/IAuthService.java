package com.jdpu.auth.AuthService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import com.jdpu.common.entity.vo.RestResponse;
import javax.servlet.http.HttpServletRequest;


public interface IAuthService {
    /**
     * 调用签权服务，判断用户是否有权限
     *
     * @param authentication
     * @param userId 登录用户ID
     * @param url
     * @param method
     * @return Result
     */
    RestResponse authenticate(String authentication, String userId, String url, String method, HttpServletRequest request);

    /**
     * 判断url是否在忽略的范围内
     * 只要是配置中的开头，即返回true
     *
     * @param url
     * @return
     */
    boolean ignoreAuthentication(String url);

    /**
     * 查看签权服务器返回结果，有权限返回true
     *
     * @param authResult
     * @return
     */
    boolean hasPermission(RestResponse authResult);

    /**
     * 调用签权服务，判断用户是否有权限
     * @param authentication
     * @param userId 用户id，在gateway拦截器中已经解析出来登录用户的id。
     * @param url
     * @param method
     * @return true/false
     */
    boolean hasPermission(String authentication, String userId, String url, String method,HttpServletRequest request);

    /**
     * 是否无效authentication
     *
     * @param authentication
     * @return true表示无效，false表示有效
     */
    boolean invalidJwtAccessToken(String authentication);

    /**
     * 从认证信息中提取jwt token 对象
     *
     * @param jwtToken toke信息 header.payload.signature
     * @return Jws对象
     */
    Jws<Claims> getJwt(String jwtToken);
}
