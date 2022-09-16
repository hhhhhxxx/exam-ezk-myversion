package com.jdpu.examsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jdpu.common.utils.PageUtils;
import com.jdpu.examsystem.constant.ExamPaperStatus;
import com.jdpu.examsystem.entity.TExamArgumentsEntity;
import com.jdpu.examsystem.vo.ExamWithStatusVM;
import com.jdpu.examsystem.vo.StudentExamListVo;

import java.util.List;
import java.util.Map;


public interface TExamArgumentsService extends IService<TExamArgumentsEntity> {

    PageUtils queryPage(Map<String, Object> params);

    TExamArgumentsEntity getArgumentsByPaperId(Integer paperId);

    ExamPaperStatus getExamStatusByPaperId(Integer paperId);

    List<ExamWithStatusVM> getExamListByStatus(Integer examStatusCode);

    List<StudentExamListVo> getStudentExamList(Integer examStatusCode);

    boolean isExist(String paperId);

    ExamPaperStatus getExamStatusByArgumentId(Integer argumentId);
}

