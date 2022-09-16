package com.jdpu.api.service;


import com.jdpu.api.entities.ExamPaperAnswer;
import com.jdpu.api.entities.ExamPaperAnswerInfo;
import com.jdpu.common.param.admin.paper.ExamPaperAnswerPageRequestVM;
import com.jdpu.common.param.student.exam.ExamPaperSubmitVM;
import com.jdpu.common.param.student.exampaper.ExamPaperAnswerPageVM;
import com.github.pagehelper.PageInfo;
import com.jdpu.common.entity.UserEntity;
import com.jdpu.common.xzsOld.service.BaseService;


import java.util.List;

public interface ExamPaperAnswerService extends BaseService<ExamPaperAnswer> {


    /**
     * 学生考试记录分页
     *
     * @param requestVM 过滤条件
     * @return PageInfo<ExamPaperAnswer>
     */
    PageInfo<ExamPaperAnswer> studentPage(ExamPaperAnswerPageVM requestVM);

    /**
     * 计算试卷提交结果(不入库)
     *
     * @param examPaperSubmitVM
     * @param user
     * @return
     */
    ExamPaperAnswerInfo calculateExamPaperAnswer(ExamPaperSubmitVM examPaperSubmitVM, UserEntity user);


    /**
     * 试卷批改
     * @param examPaperSubmitVM  examPaperSubmitVM
     * @return String
     */
    String judge(ExamPaperSubmitVM examPaperSubmitVM);

    /**
     * 试卷答题信息转成ViewModel 传给前台
     *
     * @param id 试卷id
     * @return ExamPaperSubmitVM
     */
    ExamPaperSubmitVM examPaperAnswerToVM(Integer id);


    Integer selectAllCount();

    List<Integer> selectMothCount();

    PageInfo<ExamPaperAnswer> adminPage(ExamPaperAnswerPageRequestVM requestVM);
}
