package com.jdpu.api.controller.student;


import com.jdpu.api.context.UserBaseApiController;
import com.jdpu.api.entities.ExamPaperQuestionCustomerAnswer;
import com.jdpu.api.entities.Subject;
import com.jdpu.api.entities.TextContent;
import com.jdpu.api.entities.question.QuestionObject;
import com.jdpu.api.service.ExamPaperQuestionCustomerAnswerService;
import com.jdpu.api.service.QuestionService;
import com.jdpu.api.service.SubjectService;
import com.jdpu.api.service.TextContentService;
import com.jdpu.common.param.admin.question.QuestionEditRequestVM;
import com.jdpu.common.param.student.exam.ExamPaperSubmitItemVM;
import com.jdpu.common.param.student.question.answer.QuestionAnswerVM;
import com.jdpu.common.param.student.question.answer.QuestionPageStudentRequestVM;
import com.jdpu.common.param.student.question.answer.QuestionPageStudentResponseVM;
import com.github.pagehelper.PageInfo;

import com.jdpu.common.entity.vo.RestResponse;
import com.jdpu.common.xzsOld.utils.DateTimeUtil;
import com.jdpu.common.xzsOld.utils.HtmlUtil;
import com.jdpu.common.xzsOld.utils.JsonUtil;
import com.jdpu.common.xzsOld.utils.PageInfoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 错题本
 */
@RestController
@RequestMapping(value = "/api/student/question/answer")
public class StudentQuestionAnswerController extends UserBaseApiController {

    private final ExamPaperQuestionCustomerAnswerService examPaperQuestionCustomerAnswerService;
    private final QuestionService questionService;
    private final TextContentService textContentService;
    private final SubjectService subjectService;

    @Autowired
    public StudentQuestionAnswerController(ExamPaperQuestionCustomerAnswerService examPaperQuestionCustomerAnswerService, QuestionService questionService, TextContentService textContentService, SubjectService subjectService) {
        this.examPaperQuestionCustomerAnswerService = examPaperQuestionCustomerAnswerService;
        this.questionService = questionService;
        this.textContentService = textContentService;
        this.subjectService = subjectService;
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public RestResponse<PageInfo<QuestionPageStudentResponseVM>> pageList(@RequestBody QuestionPageStudentRequestVM model) {
        model.setCreateUser(getCurrentUser().getId());
        PageInfo<ExamPaperQuestionCustomerAnswer> pageInfo = examPaperQuestionCustomerAnswerService.studentPage(model);
        PageInfo<QuestionPageStudentResponseVM> page = PageInfoHelper.copyMap(pageInfo, q -> {
            Subject subject = subjectService.selectById(q.getSubjectId());
            QuestionPageStudentResponseVM vm = modelMapper.map(q, QuestionPageStudentResponseVM.class);
            vm.setCreateTime(DateTimeUtil.dateFormat(q.getCreateTime()));
            TextContent textContent = textContentService.selectById(q.getQuestionTextContentId());
            QuestionObject questionObject = JsonUtil.toJsonObject(textContent.getContent(), QuestionObject.class);
            String clearHtml = HtmlUtil.clear(questionObject.getTitleContent());
            vm.setShortTitle(clearHtml);
            vm.setSubjectName(subject.getName());
            return vm;
        });
        return RestResponse.ok(page);
    }

    /**
     * 答题详情
     */
    @RequestMapping(value = "/select/{id}", method = RequestMethod.POST)
    public RestResponse<QuestionAnswerVM> select(@PathVariable Integer id) {
        QuestionAnswerVM vm = new QuestionAnswerVM();
        ExamPaperQuestionCustomerAnswer examPaperQuestionCustomerAnswer = examPaperQuestionCustomerAnswerService.selectById(id);
        ExamPaperSubmitItemVM questionAnswerVM = examPaperQuestionCustomerAnswerService.examPaperQuestionCustomerAnswerToVM(examPaperQuestionCustomerAnswer);
        QuestionEditRequestVM questionVM = questionService.getQuestionEditRequestVM(examPaperQuestionCustomerAnswer.getQuestionId());
        vm.setQuestionVM(questionVM);
        vm.setQuestionAnswerVM(questionAnswerVM);
        return RestResponse.ok(vm);
    }

}
