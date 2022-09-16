package com.jdpu.api.controller.student;


import com.jdpu.api.context.UserBaseApiController;
import com.jdpu.api.entities.ExamPaperAnswer;
import com.jdpu.api.entities.ExamPaperAnswerInfo;
import com.jdpu.api.entities.Subject;
import com.jdpu.api.entities.enums.ExamPaperAnswerStatusEnum;
import com.jdpu.api.event.CalculateExamPaperAnswerCompleteEvent;
import com.jdpu.api.event.UserEvent;
import com.jdpu.api.service.ExamPaperAnswerService;
import com.jdpu.api.service.ExamPaperService;
import com.jdpu.api.service.SubjectService;
import com.jdpu.common.param.admin.exam.ExamPaperEditRequestVM;
import com.jdpu.common.param.student.exam.ExamPaperReadVM;
import com.jdpu.common.param.student.exam.ExamPaperSubmitVM;
import com.jdpu.common.param.student.exampaper.ExamPaperAnswerPageResponseVM;
import com.jdpu.common.param.student.exampaper.ExamPaperAnswerPageVM;
import com.github.pagehelper.PageInfo;

import com.jdpu.common.entity.UserEntity;
import com.jdpu.common.entity.vo.RestResponse;
import com.jdpu.common.xzsOld.entities.UserEventLog;
import com.jdpu.common.xzsOld.utils.DateTimeUtil;
import com.jdpu.common.xzsOld.utils.ExamUtil;
import com.jdpu.common.xzsOld.utils.PageInfoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * 答卷
 */
@RestController
@RequestMapping(value = "/api/student/exampaper/answer")
public class StudentExamPaperAnswerController extends UserBaseApiController {

    private final ExamPaperAnswerService examPaperAnswerService;
    private final ExamPaperService examPaperService;
    private final SubjectService subjectService;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public StudentExamPaperAnswerController(ExamPaperAnswerService examPaperAnswerService, ExamPaperService examPaperService, SubjectService subjectService, ApplicationEventPublisher eventPublisher) {
        this.examPaperAnswerService = examPaperAnswerService;
        this.examPaperService = examPaperService;
        this.subjectService = subjectService;
        this.eventPublisher = eventPublisher;
    }

    /**
     * 考试记录分页
     * @param model
     * @return
     */
    @RequestMapping(value = "/pageList", method = RequestMethod.POST)
    public RestResponse<PageInfo<ExamPaperAnswerPageResponseVM>> pageList(@RequestBody @Valid ExamPaperAnswerPageVM model) {
        model.setCreateUser(getCurrentUser().getId());


        PageInfo<ExamPaperAnswer> pageInfo = examPaperAnswerService.studentPage(model);


        PageInfo<ExamPaperAnswerPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, e -> {
            ExamPaperAnswerPageResponseVM vm = modelMapper.map(e, ExamPaperAnswerPageResponseVM.class);
            Subject subject = subjectService.selectById(vm.getSubjectId());
            vm.setDoTime(ExamUtil.secondToVM(e.getDoTime()));
            vm.setSystemScore(ExamUtil.scoreToVM(e.getSystemScore()));
            vm.setUserScore(ExamUtil.scoreToVM(e.getUserScore()));
            vm.setPaperScore(ExamUtil.scoreToVM(e.getPaperScore()));
            vm.setSubjectName(subject.getName());
            vm.setCreateTime(DateTimeUtil.dateFormat(e.getCreateTime()));
            return vm;
        });
        return RestResponse.ok(page);
    }

    /**
     * 试卷提交
     * @param examPaperSubmitVM
     * @return
     */
    @RequestMapping(value = "/answerSubmit", method = RequestMethod.POST)
    public RestResponse answerSubmit(@RequestBody @Valid ExamPaperSubmitVM examPaperSubmitVM) {
        //TODO 前端要传一个开始时间过来，
        UserEntity user = getCurrentUser();
        ExamPaperAnswerInfo examPaperAnswerInfo = examPaperAnswerService.calculateExamPaperAnswer(examPaperSubmitVM, user);
        if (null == examPaperAnswerInfo) {
            return RestResponse.fail(2, "试卷不能重复做");
        }
        ExamPaperAnswer examPaperAnswer = examPaperAnswerInfo.getExamPaperAnswer();
        Integer userScore = examPaperAnswer.getUserScore();
        String scoreVm = ExamUtil.scoreToVM(userScore);
        UserEventLog userEventLog = new UserEventLog(user.getId(), user.getName(), user.getRealName(), new Date());
        String content = user.getName() + " 提交试卷：" + examPaperAnswerInfo.getExamPaper().getName()
                + " 得分：" + scoreVm
                + " 耗时：" + ExamUtil.secondToVM(examPaperAnswer.getDoTime());
        userEventLog.setContent(content);
        eventPublisher.publishEvent(new CalculateExamPaperAnswerCompleteEvent(examPaperAnswerInfo));
        eventPublisher.publishEvent(new UserEvent(userEventLog));
        return RestResponse.ok(scoreVm);
    }

    /**
     * 试卷批改
     * @param examPaperSubmitVM
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public RestResponse edit(@RequestBody @Valid ExamPaperSubmitVM examPaperSubmitVM) {
        boolean notJudge = examPaperSubmitVM.getAnswerItems().stream().anyMatch(i -> i.getDoRight() == null && i.getScore() == null);
        if (notJudge) {
            return RestResponse.fail(2, "有未批改题目");
        }

        ExamPaperAnswer examPaperAnswer = examPaperAnswerService.selectById(examPaperSubmitVM.getId());
        ExamPaperAnswerStatusEnum examPaperAnswerStatusEnum = ExamPaperAnswerStatusEnum.fromCode(examPaperAnswer.getStatus());
        if (examPaperAnswerStatusEnum == ExamPaperAnswerStatusEnum.Complete) {
            return RestResponse.fail(3, "试卷已完成");
        }
        String score = examPaperAnswerService.judge(examPaperSubmitVM);
        UserEntity user = getCurrentUser();
        UserEventLog userEventLog = new UserEventLog(user.getId(), user.getName(), user.getRealName(), new Date());
        String content = user.getName() + " 批改试卷：" + examPaperAnswer.getPaperName() + " 得分：" + score;
        userEventLog.setContent(content);
        eventPublisher.publishEvent(new UserEvent(userEventLog));
        return RestResponse.ok(score);
    }

    /**
     * 获取某条考试记录答题情况
     * @param id
     * @return
     */
    @RequestMapping(value = "/read/{id}", method = RequestMethod.POST)
    public RestResponse<ExamPaperReadVM> read(@PathVariable Integer id) {
        ExamPaperAnswer examPaperAnswer = examPaperAnswerService.selectById(id);
        ExamPaperReadVM vm = new ExamPaperReadVM();
        ExamPaperEditRequestVM paper = examPaperService.examPaperToVM(examPaperAnswer.getExamPaperId());
        ExamPaperSubmitVM answer = examPaperAnswerService.examPaperAnswerToVM(examPaperAnswer.getId());
        vm.setPaper(paper);
        vm.setAnswer(answer);
        return RestResponse.ok(vm);
    }


}
