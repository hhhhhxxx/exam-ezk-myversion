package com.jdpu.auth.authConfig.config;

import com.jdpu.common.oauth2.exception.CustomWebResponseExceptionTranslator;
import com.jdpu.auth.authConfig.mult.MultAuthenticationFilter;
import com.jdpu.auth.authConfig.jwt.CustomTokenEnhancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * @Author: xJh
 * @Date: 2022/3/5
 */
@Slf4j
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    @Qualifier("userDetailsService")
    UserDetailsService userDetailsService;
    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;


    @Autowired
    private MultAuthenticationFilter multAuthenticationFilter;


    @Autowired
    private DataSource dataSource;

    /**
     * 客户端详情配置
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 数据库 自动配置
        clients.withClientDetails(jdbcClientDetailsService());
    }


    @Bean
    public ClientDetailsService jdbcClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }


    /**
     * 该方法用于创建tokenStore对象（令牌存储对象）
     * token以什么形式存储
     */
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;


    /**
     * 自定义token增强链
     */
    @Bean
    public TokenEnhancerChain tokenEnhancerChain() {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(new CustomTokenEnhancer(), jwtAccessTokenConverter));

        // 这个refresh_token时候不生效 多余
        // new CustomTokenEnhancer()
        return tokenEnhancerChain;
    }


    @Bean
    public AuthorizationServerTokenServices authorizationServerTokenServices() {
        //使用默认实现
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();

        defaultTokenServices.setClientDetailsService(jdbcClientDetailsService()); // 设置client服务
        defaultTokenServices.setTokenStore(tokenStore);
        defaultTokenServices.setSupportRefreshToken(true); // 是否开启令牌刷新
        //reuseRefreshTokens设置为false时，每次通过refresh_token获得access_token时，
        // 也会刷新refresh_token；也就是说，会返回全新的access_token与refresh_token。
        //默认值是true，只返回新的access_token，refresh_token不变。
        defaultTokenServices.setReuseRefreshToken(false);

        // 针对jwt令牌增强 自定义内容
        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain());

        // 设置令牌有效时间（一般设置为2个小时）
        defaultTokenServices.setAccessTokenValiditySeconds(600); // access_token就是我们请求资源需要携带的令牌
        // 设置刷新令牌的有效时间
        defaultTokenServices.setRefreshTokenValiditySeconds(259200); // 3天
        return defaultTokenServices;
    }

    /**
     * 配置token令牌管理相关
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        endpoints.
                authenticationManager(authenticationManager)
                .tokenServices(authorizationServerTokenServices()) // 设置token服务
                .userDetailsService(userDetailsService) // 设置用户服务
                .exceptionTranslator(customExceptionTranslator())
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);// 只能post请求
    }

    /**
     * 开启认证服务。
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        super.configure(security);
        security
                .allowFormAuthenticationForClients() //允许客户端表单认证
                .tokenKeyAccess("permitAll()")// 开启   oauth/token
                .checkTokenAccess("permitAll()")//开启 oauth/check_token
                .addTokenEndpointAuthenticationFilter(multAuthenticationFilter); // TokenEndpoint 添加新的自定义身份验证过滤器
    }


    /**
     * 自定义OAuth2异常处理
     */
    @Bean
    public WebResponseExceptionTranslator<OAuth2Exception> customExceptionTranslator() {
        return new CustomWebResponseExceptionTranslator();
    }
}
