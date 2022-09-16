package com.jdpu.api.controller.admin;


import com.jdpu.api.context.UserBaseApiController;
import com.jdpu.api.entities.ExamPaperAnswer;
import com.jdpu.api.entities.Subject;
import com.jdpu.api.service.ExamPaperAnswerService;
import com.jdpu.api.service.SubjectService;
import com.jdpu.common.entity.UserEntity;
import com.jdpu.common.param.admin.paper.ExamPaperAnswerPageRequestVM;
import com.jdpu.common.param.student.exampaper.ExamPaperAnswerPageResponseVM;
import com.github.pagehelper.PageInfo;

import com.jdpu.common.entity.vo.RestResponse;
import com.jdpu.common.xzsOld.utils.DateTimeUtil;
import com.jdpu.common.xzsOld.utils.ExamUtil;
import com.jdpu.common.xzsOld.utils.PageInfoHelper;
import com.jdpu.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 答卷
 */
@RestController
@RequestMapping("/api/admin/examPaperAnswer")
public class AdminExamPaperAnswerController extends UserBaseApiController {

    private final ExamPaperAnswerService examPaperAnswerService;
    private final SubjectService subjectService;
    private final UserService userService;

    @Autowired
    public AdminExamPaperAnswerController(ExamPaperAnswerService examPaperAnswerService, SubjectService subjectService, UserService userService) {
        this.examPaperAnswerService = examPaperAnswerService;
        this.subjectService = subjectService;
        this.userService = userService;
    }


    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public RestResponse<PageInfo<ExamPaperAnswerPageResponseVM>> pageJudgeList(@RequestBody ExamPaperAnswerPageRequestVM model) {
        PageInfo<ExamPaperAnswer> pageInfo = examPaperAnswerService.adminPage(model);
        PageInfo<ExamPaperAnswerPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, e -> {
            ExamPaperAnswerPageResponseVM vm = modelMapper.map(e, ExamPaperAnswerPageResponseVM.class);
            Subject subject = subjectService.selectById(vm.getSubjectId());
            vm.setDoTime(ExamUtil.secondToVM(e.getDoTime()));
            vm.setSystemScore(ExamUtil.scoreToVM(e.getSystemScore()));
            vm.setUserScore(ExamUtil.scoreToVM(e.getUserScore()));
            vm.setPaperScore(ExamUtil.scoreToVM(e.getPaperScore()));
            vm.setSubjectName(subject.getName());
            vm.setCreateTime(DateTimeUtil.dateFormat(e.getCreateTime()));

            // 远程调用
            UserEntity user = userService.selectById(e.getCreateUser());

            if(user != null) {
                vm.setUserName(user.getName());
            }

            return vm;
        });
        return RestResponse.ok(page);
    }


}
