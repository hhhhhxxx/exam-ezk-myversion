package com.jdpu.examsystem.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jdpu.common.param.clazz.ClassPageVM;
import com.jdpu.examsystem.entity.Class;

/**
 * @Author: xJh
 * @Date: 2022/4/7
 */
public interface ClassService extends IService<Class> {

    IPage<Class> getList(Integer current, Integer size, String name, Integer userId);


    Page<Class> pageList(ClassPageVM model);
}
