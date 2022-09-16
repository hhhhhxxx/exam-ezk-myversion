package com.jdpu.auth.interceptor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import com.jdpu.auth.AuthService.impl.AuthService;
import com.jdpu.common.utils.UserContextHolder;
import com.jdpu.auth.entiy.UserManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class BossInterceptor implements HandlerInterceptor {

    @Qualifier("AuthService")
    @Autowired
    private AuthService authService;

    // @Autowired
    // private JwtService jwtService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        String method = request.getMethod();
        String authorization = request.getHeader("AUTHORIZATION");
        String requestURI = request.getRequestURI();

        log.info("boss鉴权：");
        log.info("method:{}", method);
        log.info("token:{}", authorization);
        log.info("uri:{}", requestURI);
        //如果token不为空，开始鉴权
        String userId = null;
        String userName = null;
        String userIp = request.getLocalAddr();

        if (StringUtils.isNotBlank(authorization)) {
            //1.获取jwt
            Jws<Claims> jwt = authService.getJwt(authorization);
            if (jwt != null && jwt.getBody() != null) {

                System.out.println("jwtBody" + jwt.getBody().toString());

                userId = (String) jwt.getBody().get("user_id");
                userName = (String) jwt.getBody().get("user_name");
                log.info("jwt解析出来的用户id:{},用户名:{}", userId, userName);
                Map<String, String> params = new HashMap<>();
                params.put(UserManager.X_USER_ID, userId);
                params.put(UserManager.X_USER_NAME, userName);
                params.put(UserManager.X_USER_IP, userIp);
                UserContextHolder.getInstance().setContext(params);
                log.info("get userId:{}, userName:{} from header", userId, userName);
            }
        } else {
            //TODO 没有权限标识的处理
            //TODO 如果是忽略的api 返回true
            if (authService.ignoreAuthentication(requestURI)) {
                return true;
            }
            if (!authService.ignoreAuthentication(requestURI)) {
                return false;
            }
        }
        // //判定权限
        // boolean hasPermission = true;
        //
        // // hasPermission = permissionService.permission(authorization, userId, requestURI, method, request);
        // hasPermission = authService.hasPermission(authorization, userId, requestURI, method, request);
        //
        // log.info("Check boss permission. userId:{}, have permission:{}, url:{}, method:{}", userId, hasPermission, requestURI, method);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContextHolder instance = UserContextHolder.getInstance();
        if (null != instance) {
            Map<String, String> context = instance.getContext();
            if (MapUtils.isNotEmpty(context)) {
                context.clear();
            }
            instance.setContext(null);
        }
    }
}
