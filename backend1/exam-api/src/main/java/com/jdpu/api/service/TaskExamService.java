package com.jdpu.api.service;

import com.jdpu.api.entities.TaskExam;
import com.jdpu.common.param.admin.task.TaskPageRequestVM;
import com.jdpu.common.param.admin.task.TaskRequestVM;
import com.github.pagehelper.PageInfo;
import com.jdpu.common.entity.UserEntity;
import com.jdpu.common.xzsOld.service.BaseService;


import java.util.List;

public interface TaskExamService extends BaseService<TaskExam> {

    PageInfo<TaskExam> page(TaskPageRequestVM requestVM);

    void edit(TaskRequestVM model, UserEntity user);

    TaskRequestVM taskExamToVM(Integer id);

    List<TaskExam> getByGradeLevel(Integer gradeLevel);
}
