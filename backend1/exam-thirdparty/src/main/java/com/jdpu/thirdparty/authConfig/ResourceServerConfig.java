package com.jdpu.thirdparty.authConfig;


import com.jdpu.common.oauth2.exception.CustomAccessDeineHandler;
import com.jdpu.common.oauth2.exception.CustomWebResponseExceptionTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
//开启oauth2,reousrce server模式
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

        // 处理 token accesss等异常
        OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
        oAuth2AuthenticationEntryPoint.setExceptionTranslator(new CustomWebResponseExceptionTranslator());


        resources
                //设置我这个resource的id, 这个在auth中配置, 这里必须照抄
                .resourceId("all")
                .tokenStore(tokenStore)
                //这个貌似是配置要不要把token信息记录在session中
                .stateless(true)
                .authenticationEntryPoint(oAuth2AuthenticationEntryPoint) // 默认就行 clientId错误不会出现 应为是服务器内部写死的
                .accessDeniedHandler(new CustomAccessDeineHandler());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeRequests()

                .antMatchers(
                        "/oauth/token",
                        "/oauth/check_token",
                        "/user/login",
                        "/user/refresh_token",
                        "/user/captcha").permitAll() // 运行访问的的请求
                .anyRequest().authenticated() // 其他全部要验证

                // .antMatchers("/**").permitAll()
                // 设置无状态 提高性能
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
