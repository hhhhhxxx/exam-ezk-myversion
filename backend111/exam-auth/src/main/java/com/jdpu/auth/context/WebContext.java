package com.jdpu.auth.context;

import cn.hutool.core.util.StrUtil;
import com.jdpu.auth.service.IUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import com.jdpu.common.dto.UserDTO;
import com.jdpu.common.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class WebContext {

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtService jwtService;


    /**
     * 获取当前用户信息
     */
    public UserDTO getCurrentUser() {

        // 当前用户
        UserDTO user = null;

        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();

        // 获取请求体 request
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        String authorization = request.getHeader("Authorization");


        if (StrUtil.isNotBlank(authorization)) {
            //1.获取jwt

            Jws<Claims> jwt = jwtService.getJwt(authorization);

            if (jwt != null && jwt.getBody() != null) {
                String idStr = (String) jwt.getBody().get("user_id");

                Integer id = Integer.valueOf(idStr);

                user = userService.getUserById(id);
            }
        }
        return user;
    }
}
