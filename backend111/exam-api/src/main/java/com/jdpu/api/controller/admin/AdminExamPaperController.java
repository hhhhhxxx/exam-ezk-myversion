package com.jdpu.api.controller.admin;

import com.jdpu.api.context.UserBaseApiController;
import com.jdpu.api.entities.ExamPaper;
import com.jdpu.api.service.ExamPaperService;
import com.jdpu.common.param.admin.exam.ExamPaperEditRequestVM;
import com.jdpu.common.param.admin.exam.ExamPaperPageRequestVM;
import com.jdpu.common.param.admin.exam.ExamResponseVM;
import com.jdpu.common.param.admin.exam.autopaper.ExamAutoPaperRequestVM;
import com.github.pagehelper.PageInfo;

import com.jdpu.common.entity.vo.RestResponse;
import com.jdpu.common.xzsOld.utils.DateTimeUtil;
import com.jdpu.common.xzsOld.utils.PageInfoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 试卷
 */
@RestController
@RequestMapping(value = "/api/admin/exam/paper")
public class AdminExamPaperController extends UserBaseApiController {

    private final ExamPaperService examPaperService;

    @Autowired
    public AdminExamPaperController(ExamPaperService examPaperService) {
        this.examPaperService = examPaperService;
    }

    /**
     * 自动组卷
     * @author zuck
     * @date 2022/4/7
     * {
     *     "id": 8,  //试卷id
     *     "level": 1,  //年级
     *     "subjectId": 1,  //学科
     *     "paperType": 6,  //试卷类型
     *     "name": "中级任务二",  //试卷名称
     *     "suggestTime": 20,  //考试时长
     *     "limitDateTime": null,  //限时
     *     "difficult": 5,, // 组卷难度
     *     "score": "18",  //试卷总分
     *     "titleCount": 12
     * }
     */
    @PostMapping("/autoCreatePaper")
    public RestResponse autoCreatePaper(@RequestBody ExamAutoPaperRequestVM autoPaperRequestVM){
        ExamPaper examPaper = examPaperService.autoCreatePaper(autoPaperRequestVM);
        ExamPaperEditRequestVM newVM = examPaperService.examPaperToVM(examPaper.getId());
        return RestResponse.ok(newVM);
    }

    /**
     * 试卷分页
     * @param model
     * {
     *     "id": null,
     *     "level": null,   //年级
     *     "subjectId": null,  //学科
     *     "pageIndex": 1,  //页码
     *     "pageSize": 10  //每页数量
     * }
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public RestResponse<PageInfo<ExamResponseVM>> pageList(@RequestBody ExamPaperPageRequestVM model) {
        PageInfo<ExamPaper> pageInfo = examPaperService.page(model);
        PageInfo<ExamResponseVM> page = PageInfoHelper.copyMap(pageInfo, e -> {
            ExamResponseVM vm = modelMapper.map(e, ExamResponseVM.class);
            vm.setCreateTime(DateTimeUtil.dateFormat(e.getCreateTime()));
            return vm;
        });
        return RestResponse.ok(page);
    }


    /**
     * 任务试卷分页
     */
    @RequestMapping(value = "/taskExamPage", method = RequestMethod.POST)
    public RestResponse<PageInfo<ExamResponseVM>> taskExamPageList(@RequestBody ExamPaperPageRequestVM model) {
        PageInfo<ExamPaper> pageInfo = examPaperService.taskExamPage(model);
        PageInfo<ExamResponseVM> page = PageInfoHelper.copyMap(pageInfo, e -> {
            ExamResponseVM vm = modelMapper.map(e, ExamResponseVM.class);
            vm.setCreateTime(DateTimeUtil.dateFormat(e.getCreateTime()));
            return vm;
        });
        return RestResponse.ok(page);
    }


    /**
     * 试卷编辑
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public RestResponse<ExamPaperEditRequestVM> edit(@RequestBody @Valid ExamPaperEditRequestVM model) {
        ExamPaper examPaper = examPaperService.savePaperFromVM(model, getCurrentUser());
        ExamPaperEditRequestVM newVM = examPaperService.examPaperToVM(examPaper.getId());
        return RestResponse.ok(newVM);
    }

    /**
     * 试卷查询
     */
    @RequestMapping(value = "/select/{id}", method = RequestMethod.POST)
    public RestResponse<ExamPaperEditRequestVM> select(@PathVariable Integer id) {
        ExamPaperEditRequestVM vm = examPaperService.examPaperToVM(id);
        return RestResponse.ok(vm);
    }

    /**
     * 试卷删除
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public RestResponse delete(@PathVariable Integer id) {
        ExamPaper examPaper = examPaperService.selectById(id);
        examPaper.setDeleted(true);
        examPaperService.updateByIdFilter(examPaper);
        return RestResponse.ok();
    }
}
