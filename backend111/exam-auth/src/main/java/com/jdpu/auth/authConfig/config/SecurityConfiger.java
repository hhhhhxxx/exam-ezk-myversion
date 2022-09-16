package com.jdpu.auth.authConfig.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;

/**
 * @Author: xJh
 * @Date: 2022/3/5
 */
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // 没加这句 注解不生效
public class SecurityConfiger extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("userDetailsService")
    private UserDetailsService userDetailsService; // 实际是MultUserDetailsService

    @Autowired
    private PasswordEncoder passwordEncoder;

//    @Autowired
//    private JdbcUserDetailsService jdbcUserDetailsService;

    AffirmativeBased affirmativeBased = null;
    OAuth2AuthenticationProcessingFilter auth2AuthenticationProcessingFilter = null;
    AuthenticationManager authenticationManager = null;
    OAuth2AuthenticationManager manager = null;
    /**
     * 注册一个认证管理器对象到容器
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    /**
     * 密码编码对象（密码不进行加密处理）
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }


    /**
     * 处理用户名和密码验证事宜
     * 1）客户端传递username和password参数到认证服务器
     * 2）一般来说，username和password会存储在数据库中的用户表中
     * 3）根据用户表中数据，验证当前传递过来的用户信息的合法性
     */
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        // 在这个方法中就可以去关联数据库了，当前我们先把用户信息配置在内存中
//        // 实例化一个用户对象(相当于数据表中的一条用户记录)
//        /*UserDetails user = new User("admin","123456",new ArrayList<>());
//        auth.inMemoryAuthentication()
//                .withUser(user).passwordEncoder(passwordEncoder);*/
//
//        auth.userDetailsService(jdbcUserDetailsService).passwordEncoder(passwordEncoder);
//    }
//     @Autowired
//     JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); // 关闭跨站攻击防护
        http.httpBasic().disable(); // 关闭httpBasic， httpBasic是由http协议定义的最基础的认证方式
        http.formLogin().disable(); // 关闭自带的登录网页
        http.authorizeRequests()
                // resoureService优先级高于SecurityConfiger 这个放开就行了
                .anyRequest().permitAll()
                .and().logout().permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userDetailsService) // 自定义userDetailsService
                .passwordEncoder(passwordEncoder()); // 密码编码器
    }
}
