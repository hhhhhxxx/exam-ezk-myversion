package com.jdpu.auth.authConfig.mult.authenticator;

import com.jdpu.auth.authConfig.mult.MultAuthentication;
import com.jdpu.common.entity.UserEntity;

import javax.servlet.http.HttpServletResponse;

public abstract class AbstractPreparableMultAuthenticator implements MultAuthenticator {

    @Override
    public abstract UserEntity authenticate(MultAuthentication multAuthentication);

    @Override
    public abstract void prepare(MultAuthentication multAuthentication, HttpServletResponse response);

    @Override
    public abstract boolean support(MultAuthentication multAuthentication);

    @Override
    public void complete(MultAuthentication multAuthentication) {
        // 默认什么都不做了 其他登录方法需要实现
    }
}
