package com.jdpu.auth.authConfig.mult.authenticator.password;

import com.jdpu.auth.authConfig.mult.MultAuthentication;
import com.jdpu.auth.authConfig.mult.authenticator.AbstractPreparableMultAuthenticator;
import com.jdpu.common.entity.UserEntity;
import com.jdpu.auth.service.IUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletResponse;

/**
 * 默认登录处理
 **/
@Component
@Primary
public class UsernamePasswordAuthenticator extends AbstractPreparableMultAuthenticator {

    // @Autowired
    // private UserRemoteService userRemoteService;

    @Autowired
    public IUserService userService;

    @Override
    public UserEntity authenticate(MultAuthentication multAuthentication) {
        // 返回当前用户
        // return this.userRemoteService.getUserByPhone(multAuthentication.getUsername());

        // 改成直接调用
        return userService.getUserEntityByPhone(multAuthentication.getUsername());

    }

    @Override
    public void prepare(MultAuthentication multAuthentication, HttpServletResponse response) {
        // 预处理什么都不做
    }

    @Override
    public boolean support(MultAuthentication multAuthentication) {
        //  这里只是简单判断非空，如果是有多种登录方式的话，应该对这个字段作字符串比较，属于这种登录方式才返回true
        return StringUtils.isEmpty(multAuthentication.getAuthType());
    }
}
