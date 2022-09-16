package com.jdpu.examsystem.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdpu.common.param.clazz.ClassPageVM;
import com.jdpu.examsystem.dao.ClassMapper;
import com.jdpu.examsystem.entity.Class;
import com.jdpu.examsystem.service.UserClassService;
import com.jdpu.examsystem.entity.UserClass;
import com.jdpu.examsystem.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: xJh
 * @Date: 2022/4/7
 */
@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService {

    @Autowired
    private UserClassService userClassService;

    @Override
    public IPage<Class> getList(Integer current, Integer size, String name, Integer userId) {
        IPage<Class> page = new Page<>(current, size);

        QueryWrapper<Class> queryWrapper = new QueryWrapper<>();

        List<UserClass> userClassList = new ArrayList<>();

        if (userId != null) {
            userClassList = userClassService.list(new QueryWrapper<UserClass>().eq("user_id", userId));
        }

        List<Integer> classList = new ArrayList<>();
        if (userClassList.size() > 0) {

            for (UserClass userClass : userClassList) {
                classList.add(userClass.getClassId());
            }
            queryWrapper.in("id", classList);
        }

        if (name != null) {
            queryWrapper.like("name", name);
        }


        return this.page(page, queryWrapper);
    }

    @Override
    public Page<Class> pageList(ClassPageVM model) {

        Page<Class> pageInfo = new Page<>(model.getPageIndex(), model.getPageSize());

        LambdaQueryWrapper<Class> queryWrapper = new LambdaQueryWrapper<>();

        if(model.getTeacherId() == null) {
            // 查询全部班级 模糊查询名字
            queryWrapper.like(StrUtil.isNotEmpty(model.getClassNmae()),Class::getName,model.getClassNmae());
        } else {
            // 查询指定老师所在的班级 模糊查询名字
            List<UserClass> classList = userClassService.lambdaQuery()
                    .eq(UserClass::getUserId, model.getTeacherId())
                    .select(UserClass::getClassId)
                    .list();

            // 没有班级
            if(classList.size() < 1) {
                return pageInfo;
            }

            userClassService.lambdaQuery()
                    .eq(UserClass::getUserId, model.getTeacherId())
                    .select(UserClass::getClassId);

            List<Integer> classIds = classList.stream().map((item) -> {
                return item.getClassId();
            }).collect(Collectors.toList());

            queryWrapper
                    .in(Class::getId,classIds)
                    .like(StrUtil.isNotEmpty(model.getClassNmae()),Class::getName,model.getClassNmae());

        }

        Page<Class> page = this.page(pageInfo, queryWrapper);

        return page;
    }
}
