package com.jdpu.examsystem.context;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.jdpu.common.constant.HeaderEnum;
import com.jdpu.common.entity.UserEntity;
import com.jdpu.common.entity.vo.RestResponse;
import com.jdpu.common.service.JwtService;
import com.jdpu.examsystem.feign.UserClient;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 环境上下文
 *
 * @author zuck
 */
@Component
public class WebContext {
    private static final String USER_ATTRIBUTES = "USER_ATTRIBUTES";

    /**
     * Instantiates a new Web context.
     *
     * @param userService the user service
     */

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtService jwtService;

    /**
     *
     * 设置当前用户信息
     */
    public void setCurrentUser(UserEntity user) {
        RequestContextHolder.currentRequestAttributes().setAttribute(USER_ATTRIBUTES, user, RequestAttributes.SCOPE_REQUEST);
    }

    public String getAuthorization() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        // 获取请求体 request
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        String authorization = request.getHeader("Authorization");

        return authorization;
    }

    public Integer getCurrentUserId() {

        String authorization = this.getAuthorization();

        if (StrUtil.isNotBlank(authorization)) {
            //1.获取jwt
            Jws<Claims> jwt = jwtService.getJwt(authorization);

            if (jwt != null && jwt.getBody() != null) {
                String idStr = (String) jwt.getBody().get("user_id");
                Integer id = Integer.valueOf(idStr);
                return id;
            }
        }
        return null;
    }


    /**
     * 获取当前用户信息
     */
    public UserEntity getCurrentUser() {

        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        // 获取请求体 request
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        RestResponse res = userClient.current(request.getHeader(HeaderEnum.Authorization.getName()));

        UserEntity userEntity = BeanUtil.toBean(res.getResponse(), UserEntity.class);


        return userEntity;
    }
}
