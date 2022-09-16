package com.jdpu.auth.config;


import com.jdpu.auth.interceptor.BossInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    // WebMvcConfigurerAdapter过期 采用WebMvcConfigurer

    // 要注入到容器中，不然拦截器的autowire无效 拦截器早于
    @Bean
    public BossInterceptor GlobalInterceptor(){
        return new BossInterceptor();
    }

    // 拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // registry.addInterceptor(new MyGlobalInterceptor())
        //         .addPathPatterns("/**")
        //         .excludePathPatterns("/user/**"); // /user是 xzs-user服务

        // xzs-user 本来就一个UseController 这里还放开了，相当于没有作用

        registry.addInterceptor(GlobalInterceptor())
                .addPathPatterns("/boss/**");   // 拦截boss
    }
}
