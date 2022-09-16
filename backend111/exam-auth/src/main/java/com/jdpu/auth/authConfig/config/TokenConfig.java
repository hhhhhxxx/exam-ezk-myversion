package com.jdpu.auth.authConfig.config;

import com.jdpu.auth.authConfig.jwt.CustomAccessTokenConvertor;
import com.jdpu.auth.authConfig.jwt.CustomAuthenticationConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class TokenConfig {
    // 密钥
    private String signingKey = "123456";

    // 转token
    @Autowired
    private CustomAccessTokenConvertor customAccessTokenConvertor;

    // 装userDetail
    @Autowired
    private CustomAuthenticationConverter customAuthenticationConverter;

    /**
     * 返回jwt令牌转换器，生成jwt令牌
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();

        jwtAccessTokenConverter.setSigningKey(signingKey);
        // 签名器
        jwtAccessTokenConverter.setVerifier(new MacSigner(signingKey));

        // 解决refresh_toekn 无user
        customAccessTokenConvertor.setUserTokenConverter(customAuthenticationConverter);

        jwtAccessTokenConverter.setAccessTokenConverter(customAccessTokenConvertor);

        return jwtAccessTokenConverter;
    }

    @Bean
    public TokenStore tokenStore() {
        //使用jwt令牌
        return new JwtTokenStore(jwtAccessTokenConverter());
    }
}
