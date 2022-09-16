package com.jdpu.userservice.config;

import com.jdpu.common.constant.HeaderEnum;
import com.jdpu.userservice.context.WebContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignRequestInterceptor implements RequestInterceptor {

    @Autowired
    WebContext webContext;

    @Override
    public void apply(RequestTemplate template) {
        template.header(HeaderEnum.Authorization.getName(), webContext.getAuthorization());
    }
}
