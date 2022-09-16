package com.jdpu.auth.client;

import com.jdpu.auth.client.fallback.OAuthRemoteServiceFallback;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: xJh
 * @Date: 2022/3/6
 */
@FeignClient(name="exam-auth",fallback = OAuthRemoteServiceFallback.class) // 发生异常调用fallback 熔断
public interface OAuthRemoteService {

    // 请求令牌
    @PostMapping("/oauth/token")
    String createToken(@RequestParam("username") String phone,
                       @RequestParam("password") String password,
                       @RequestParam("scope") String scope,
                       @RequestParam("grant_type") String grantType,
                       @RequestParam("client_id") String clientId,
                       @RequestParam("client_secret") String clientSecret,
                       // 可能是一个自定义字段 它就是
                       @RequestParam(value = "auth_type", required = false) String authType);
    // 刷新令牌
    @PostMapping("/oauth/token")
    String refreshToken(@RequestParam("refresh_token") String refreshToken,
                        @RequestParam("grant_type") String grantType,
                        @RequestParam("client_id") String clientId,
                        @RequestParam("client_secret") String clientSecret);
}
