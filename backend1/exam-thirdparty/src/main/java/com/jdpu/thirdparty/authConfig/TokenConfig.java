package com.jdpu.thirdparty.authConfig;
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


    /**
     * 返回jwt令牌转换器，生成jwt令牌
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();

        jwtAccessTokenConverter.setSigningKey(signingKey);
        // 签名器
        jwtAccessTokenConverter.setVerifier(new MacSigner(signingKey));


        // 资源服务可能不需要这两个转换器，生成token才需要，校验不需要。

        // 解决refresh_toekn 无user
        // myAccessTokenConvertor.setUserTokenConverter(myAuthenticationConverter);
        //
        // jwtAccessTokenConverter.setAccessTokenConverter(myAccessTokenConvertor);

        return jwtAccessTokenConverter;
    }

    @Bean
    public TokenStore tokenStore() {
        //使用jwt令牌
        return new JwtTokenStore(jwtAccessTokenConverter());
    }
}
