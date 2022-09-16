package com.jdpu.auth.authConfig.jwt;

import com.google.common.collect.Maps;
import com.jdpu.common.oauth2.entity.UserJwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.Map;

/**
 * 自定义token携带内容
 */
@Slf4j
public class CustomTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = Maps.newHashMap();
        // 自定义token内容，加入组织机构信息
        // additionalInfo.put("organization", authentication.getName());
        try {
            // 自定义token内容，加入userId
            UserJwt details = (UserJwt) authentication.getPrincipal();
            if (null != details) {
                additionalInfo.put("user_id", details.getId());
                additionalInfo.put("user_name", details.getUsername()); // 这里是jwt的username 就是phone refresh需要这个字段查数据库
                additionalInfo.put("user_username",details.getName()); // 真正的用户名
            }
        } catch (Exception e) {
            log.error("获取userId异常user name:{}", authentication.getName());
        }

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

        return accessToken;
    }
}
