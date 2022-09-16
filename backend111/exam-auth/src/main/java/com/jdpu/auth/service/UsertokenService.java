package com.jdpu.auth.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jdpu.common.oauth2.exception.AuthErrorType;
import com.jdpu.auth.client.OAuthRemoteService;
import com.jdpu.common.entity.vo.RestResponse;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

/**
 * @Author: xJh
 * @Date: 2022/3/6
 */
@Slf4j
@Service
@RefreshScope
public class UsertokenService {

    @Qualifier("com.jdpu.auth.client.OAuthRemoteService")
    @Autowired
    private OAuthRemoteService oAuthRemoteService;
    @Value("${spring.oauth.client_id}") // 常量注入
    private String clientId;
    @Value("${spring.oauth.client_secret}")
    private String clientSecret;
    @Value("${spring.oauth.scope}")
    private String scope;
    @Value("${spring.oauth.grant_type}")
    private String grantType;
    @Value("${spring.oauth.refresh_grant_type}")
    private String refreshGrantType;

    /**
     * 发放access_token、refresh_token
     *
     * @param phone
     * @param password
     * @param code
     * @param type
     * @return
     */
    public RestResponse createAuthToken(String phone, String password, String code, Integer type) {
        log.info("phone:{}, password:{}, scope:{}, grantType:{}, clientId:{}, clientSecret:{}", phone, password, scope, grantType, clientId, clientSecret);
        String token = null;
        try {
            if (0 == type) {
                token = this.oAuthRemoteService.createToken(phone, password, scope, grantType, clientId, clientSecret, null);
            } else if (1 == type) {
//                token = this.oAuthRemoteService.createToken(phone, code, scope, grantType, clientId, clientSecret, "mobile");
                token = this.oAuthRemoteService.createToken(phone, code, scope, grantType, clientId, clientSecret, null);
            } else if (2 == type) {
                token = this.oAuthRemoteService.createToken(phone, code, scope, grantType, clientId, clientSecret, "weixin");
            }

            log.info("token:{}",token);

            // token为什么能解析成对象？做个标记 这里是异常的时候自定义的返回提所以有code
            JSONObject resultObject = JSON.parseObject(token);
            String resultCode = resultObject.getString("code");

            if (StringUtils.isNotBlank(resultCode) && AuthErrorType.INVALID_GRANT.getCode().equals(resultCode)) {

                JSONObject dataObject = JSON.parseObject(resultObject.getString("data"));
                String error_description = dataObject.getString("error_description");

                if("User is disabled".equals(error_description)) {
                    return RestResponse.fail(AuthErrorType.INVALID_GRANT,"用户被封禁");
                } else {
                    return RestResponse.fail(AuthErrorType.INVALID_GRANT,"用户名密码错误");
                }


            }
            return RestResponse.ok(resultObject);
        } catch (Exception e) {
            log.error("", e);
            return RestResponse.fail("登录失败");
        }
    }

    public String getRefreshTokenByWeixin(String unionId, String openId) {
        log.info("unionId:{}, openId:{}, scope:{}, grantType:{}, clientId:{}, clientSecret:{}", unionId, openId, scope, grantType, clientId, clientSecret);
        try {
            String token = this.oAuthRemoteService.createToken(unionId, openId, scope, grantType, clientId, clientSecret, "weixin");
            log.info("unionId:{}, openId:{}, refresh_token:{}", unionId, openId, token);
            if (StringUtils.isBlank(token)) {
                return null;
            }
            JSONObject jsonObject = JSON.parseObject(token);
            return jsonObject.getString("refresh_token");
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }
}
