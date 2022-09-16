package com.jdpu.api.service;


import com.jdpu.api.entities.Question;
import com.jdpu.common.param.admin.question.QuestionEditRequestVM;
import com.jdpu.common.param.admin.question.QuestionPageRequestVM;
import com.github.pagehelper.PageInfo;
import com.jdpu.common.xzsOld.service.BaseService;

import java.util.List;

public interface QuestionService extends BaseService<Question> {

    PageInfo<Question> page(QuestionPageRequestVM requestVM);

    Question insertFullQuestion(QuestionEditRequestVM model, Integer userId);

    Question updateFullQuestion(QuestionEditRequestVM model);

    QuestionEditRequestVM getQuestionEditRequestVM(Integer questionId);

    QuestionEditRequestVM getQuestionEditRequestVM(Question question);

    Integer selectAllCount();

    List<Integer> selectMothCount();

    /**
     * 获取问题id列表
     * @param difficult
     * @param subjectId
     * @return
     */
    List<Integer> selectIdsByDifficultAndSubjectId(Integer difficult, Integer subjectId);
}
