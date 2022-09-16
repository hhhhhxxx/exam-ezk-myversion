package com.jdpu.auth.authConfig.mult.authenticator;



import com.jdpu.auth.authConfig.mult.MultAuthentication;
import com.jdpu.common.entity.UserEntity;

import javax.servlet.http.HttpServletResponse;

/**
 *
 **/
public interface MultAuthenticator {

    /**
     * 处理集成认证
     *
     * @param multAuthentication
     * @return
     */
    UserEntity authenticate(MultAuthentication multAuthentication);

    /**
     * 进行预处理
     *
     * @param multAuthentication
     * @param request
     */
    void prepare(MultAuthentication multAuthentication, HttpServletResponse response);

    /**
     * 判断是否支持集成认证类型
     *
     * @param multAuthentication
     * @return
     */
    boolean support(MultAuthentication multAuthentication);

    /**
     * 认证结束后执行
     *
     * @param multAuthentication
     */
    void complete(MultAuthentication multAuthentication);
}
