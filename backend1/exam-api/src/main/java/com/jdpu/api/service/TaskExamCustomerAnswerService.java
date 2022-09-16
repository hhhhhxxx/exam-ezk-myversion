package com.jdpu.api.service;

import com.jdpu.api.entities.ExamPaper;
import com.jdpu.api.entities.ExamPaperAnswer;
import com.jdpu.api.entities.TaskExamCustomerAnswer;
import com.jdpu.common.xzsOld.service.BaseService;


import java.util.Date;
import java.util.List;

public interface TaskExamCustomerAnswerService extends BaseService<TaskExamCustomerAnswer> {

    void insertOrUpdate(ExamPaper examPaper, ExamPaperAnswer examPaperAnswer, Date now);

    TaskExamCustomerAnswer selectByTUid(Integer tid, Integer uid);

    List<TaskExamCustomerAnswer> selectByTUid(List<Integer> taskIds, Integer uid);
}
