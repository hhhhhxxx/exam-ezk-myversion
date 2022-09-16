package com.jdpu.examsystem.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jdpu.common.param.clazz.ClassPageVM;
import com.jdpu.common.param.clazz.ClassUpdateVM;
import com.jdpu.common.param.clazz.UserClassCreateVM;
import com.jdpu.examsystem.context.WebContext;
import com.jdpu.examsystem.entity.Class;
import com.jdpu.examsystem.entity.UserClass;
import com.jdpu.examsystem.service.ClassExamService;
import com.jdpu.examsystem.service.ClassService;
import com.jdpu.examsystem.service.UserClassService;
import io.swagger.annotations.ApiOperation;
import com.jdpu.common.entity.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: xJh
 * @Date: 2022/4/8
 */
@RestController
@RequestMapping("/api/examsystem/class")
public class ClassController {
    @Autowired
    private ClassService classService;
    @Autowired
    private UserClassService userClassService;

    @Autowired
    private ClassExamService classExamService;

    @Autowired
    private WebContext webContext;

    @ApiOperation("查看当前用户(教师)管理下的班级或者学生所在班级")
    @RequestMapping("/page/list")
    public RestResponse pageListClass(@RequestBody ClassPageVM model){

        if(model.getMy() != null) {

            Integer teacherId = webContext.getCurrentUserId();
            model.setTeacherId(teacherId);

        }

        Page<Class> classPage = this.classService.pageList(model);

        return RestResponse.ok(classPage);
    }


    @ApiOperation("添加班级或编辑班级")
    @PostMapping("/SaveOrUpdate")
    public RestResponse  saveOrUpdate(@RequestBody ClassUpdateVM model){

        System.out.println("---------:"+model.getAdd());

        Class clazz = BeanUtil.toBean(model, Class.class);

        boolean insert = classService.saveOrUpdate(clazz);

        if (!insert) {
            RestResponse.fail().setMessage("添加失败");
        }


        // 判断是否要加入班级
        if(model.getId() == null && model.getAdd() != null  && BooleanUtil.isTrue(model.getAdd()) ) {

            Integer teacherId = webContext.getCurrentUserId();

            UserClassCreateVM ucModel = new UserClassCreateVM();

            ucModel.setTeacher(true);
            // 插入成功会自动填回userId 所以可以get到
            ucModel.setClassId(clazz.getId());
            ucModel.setUserId(teacherId);

            userClassService.bindInClass(ucModel);
        }

        return RestResponse.ok();
    }

    @ApiOperation("删除班级")
    @RequestMapping("/delete/{id}")
    public RestResponse delete(@PathVariable("id") Integer ids){
        classService.removeById(ids);
        return RestResponse.ok();
    }

    @ApiOperation("学生或老师加入班级")
    @PostMapping("/bind")
    public RestResponse bindInClass(@RequestBody UserClassCreateVM model){

        boolean success = userClassService.bindInClass(model);

        if(success) {
            return RestResponse.ok();
        }
        return RestResponse.fail();
    }

    @ApiOperation("将学生或老师移除班级")
    @PostMapping("/delBind")
    public RestResponse delBindClass(@RequestBody UserClass userClass){

        LambdaQueryWrapper<UserClass> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper
                .eq(UserClass::getUserId,userClass.getUserId())
                .eq(UserClass::getClassId,userClass.getClassId());

        userClassService.remove(queryWrapper);

        return RestResponse.ok();
    }

    @GetMapping("/get/{classId}")
    public RestResponse getClassById(@PathVariable("classId") Integer classId){

        if(classId != null)  {
            Class clazz = classService.getById(classId);
            return RestResponse.success(clazz);
        }

        return RestResponse.fail();
    }
}
