package com.jdpu.auth.authConfig.mult;

import com.alibaba.fastjson.JSON;
import com.jdpu.auth.authConfig.mult.authenticator.MultAuthenticator;
import com.jdpu.common.entity.vo.RestResponse;
import com.jdpu.common.oauth2.exception.AuthErrorType;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Component
public class MultAuthenticationFilter extends OncePerRequestFilter implements ApplicationContextAware {

    // 自定义过滤器  使用OncePerRequestFilter 保证只触发一次
    // FilterInvocationSecurityMetadataSource
    AffirmativeBased affirmativeBased = null;
    private static final String AUTH_TYPE_PARAM_NAME = "auth_type";
    private static final String OAUTH_TOKEN_URL = "/oauth/token";

    private Collection<MultAuthenticator> authenticators;
    private ApplicationContext applicationContext;
    private RequestMatcher requestMatcher;

    public MultAuthenticationFilter() {
        this.requestMatcher = new OrRequestMatcher(
                new AntPathRequestMatcher(OAUTH_TOKEN_URL, "GET"),
                new AntPathRequestMatcher(OAUTH_TOKEN_URL, "POST")
        );
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // SecurityContextHolderAwareRequestFilter：主要是包装请求对象 request。

        // HttpServletRequest request = (HttpServletRequest) servletRequest;
        // HttpServletResponse response = (HttpServletResponse) servletResponse;

        String grantType = request.getParameter("grant_type");
        System.out.println("MultAuthenticationFilter中拦截"+request.getRequestURL());
        // 拦截/oauth/token 即登录
        if (requestMatcher.matches(request) && "password".equals(grantType)) {

            //设置集成登录信息
            MultAuthentication multAuthentication = new MultAuthentication();
            multAuthentication.setAuthType(request.getParameter(AUTH_TYPE_PARAM_NAME));

            // 这里存了/oauth/token 传过来的6个参数 包括username和password

            multAuthentication.setAuthParameters(request.getParameterMap());
            // 放入当前线程中
            MultAuthenticationContext.set(multAuthentication);
            try {
                //预处理
                this.prepare(multAuthentication, response);
                System.out.println("MultAuthenticationFilter通过"+request.getRequestURL());
                filterChain.doFilter(request, response);

                //后置处理
                this.complete(multAuthentication);
            } catch (Exception e) {
                PrintWriter writer = response.getWriter();

                RestResponse fail = RestResponse.fail(AuthErrorType.ERROR_VERIFY_CODE);
                writer.write(JSON.toJSONString(fail));
                writer.flush();
                writer.close();
                return;
            } finally {
                MultAuthenticationContext.clear();
            }
        } else {

            System.out.println("MultAuthenticationFilter通过"+request.getRequestURL());
            filterChain.doFilter(request, response);
        }
    }

    /**
     * 进行预处理
     *
     * @param multAuthentication
     * @param response
     */
    private void prepare(MultAuthentication multAuthentication, HttpServletResponse response) {

        //延迟加载认证器
        if (this.authenticators == null) {
            synchronized (this) {
                // 获取某一接口的所有实现类 好像只有一个UsernamePasswordAuthenticator实现了 所以list只有一个元素
                Map<String, MultAuthenticator> integrationAuthenticatorMap = applicationContext.getBeansOfType(MultAuthenticator.class);
                if (integrationAuthenticatorMap != null) {
                    this.authenticators = integrationAuthenticatorMap.values();
                }
            }
        }

        if (this.authenticators == null) {
            this.authenticators = new ArrayList<>();
        }

        for (MultAuthenticator authenticator : authenticators) {
            if (authenticator.support(multAuthentication)) {
                authenticator.prepare(multAuthentication, response);
            }
        }
    }

    /**
     * 后置处理
     *
     * @param multAuthentication
     */
    private void complete(MultAuthentication multAuthentication) {
        for (MultAuthenticator authenticator : authenticators) {
            if (authenticator.support(multAuthentication)) {
                authenticator.complete(multAuthentication);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
