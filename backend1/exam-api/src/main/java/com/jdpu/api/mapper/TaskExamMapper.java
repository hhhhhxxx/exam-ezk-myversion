package com.jdpu.api.mapper;


import com.jdpu.api.entities.TaskExam;
import com.jdpu.common.param.admin.task.TaskPageRequestVM;
import com.jdpu.common.xzsOld.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TaskExamMapper extends BaseMapper<TaskExam> {

    List<TaskExam> page(TaskPageRequestVM requestVM);

    List<TaskExam> getByGradeLevel(Integer gradeLevel);
}
