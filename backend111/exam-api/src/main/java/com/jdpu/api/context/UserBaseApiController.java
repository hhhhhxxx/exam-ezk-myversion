package com.jdpu.api.context;


import com.jdpu.common.entity.UserEntity;
import com.jdpu.common.xzsOld.base.BaseApiController;
import com.jdpu.userservice.context.WebContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zuck
 */
public class UserBaseApiController extends BaseApiController {

    @Autowired
    protected WebContext webContext;

    /**
     * 获取当前用户
     */
    protected UserEntity getCurrentUser() {
        return webContext.getCurrentUser();
    }
}
