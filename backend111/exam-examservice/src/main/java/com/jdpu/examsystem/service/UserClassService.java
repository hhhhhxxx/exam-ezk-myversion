package com.jdpu.examsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jdpu.common.param.clazz.UserClassCreateVM;
import com.jdpu.examsystem.entity.UserClass;
import org.springframework.stereotype.Service;

/**
 * @Author: xJh
 * @Date: 2022/4/7
 */
@Service
public interface UserClassService extends IService<UserClass> {

    boolean bindInClass(UserClassCreateVM model);
}
