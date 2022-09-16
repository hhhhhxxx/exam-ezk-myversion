package com.jdpu.examsystem.service.impl;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdpu.common.param.clazz.UserClassCreateVM;
import com.jdpu.examsystem.service.UserClassService;
import com.jdpu.examsystem.dao.UserClassMapper;
import com.jdpu.examsystem.entity.UserClass;
import org.springframework.stereotype.Service;


/**
 * @Author: xJh
 * @Date: 2022/4/7
 */
@Service
public class UserClassServiceImpl extends ServiceImpl<UserClassMapper, UserClass> implements UserClassService {


    @Override
    public boolean bindInClass(UserClassCreateVM model) {
        UserClass userClass = new UserClass();
        userClass.setUserId(model.getUserId());
        userClass.setClassId(model.getClassId());

        // 老师可以加入多个班级 学生只有一个班级
        if(BooleanUtil.isTrue(model.getTeacher())) {
            return this.save(userClass);
        }

        // 查询是否已经绑定
        UserClass one = this.lambdaQuery()
                .eq(UserClass::getUserId, userClass.getUserId())
                .eq(UserClass::getClassId, userClass.getClassId())
                .one();

        if (one != null) {
            return false;
        }
        return this.save(userClass);
    }
}
