package com.jdpu.examsystem.controller;

import java.util.Arrays;
import java.util.Map;


import com.jdpu.examsystem.constant.ExamPaperStatus;
import com.jdpu.examsystem.feign.exampaper.ExamPaperFeignService;
import com.jdpu.examsystem.service.TExamNoticeService;
import com.jdpu.examsystem.vo.dto.admin.exam.ExamPaperEditRequestVM;
import com.jdpu.common.entity.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jdpu.examsystem.entity.TExamArgumentsEntity;
import com.jdpu.examsystem.service.TExamArgumentsService;
import com.jdpu.common.utils.PageUtils;
import com.jdpu.common.utils.R;


/**
 * 考试参数TExamArgumentsController
 */
@RestController
@RequestMapping("/api/examsystem/texamarguments")
public class TExamArgumentsController {
    @Autowired
    private TExamArgumentsService tExamArgumentsService;
    @Autowired
    private ExamPaperFeignService examPaperFeignService;
    @Autowired
    private TExamNoticeService tExamNoticeService;

    /**
     * 根据argument_id，获取考试状态
     */
    @GetMapping("/getExamStatusById/{id}")
    public RestResponse getExamStatusById(@PathVariable("id") Integer argumentId) {
        ExamPaperStatus status = tExamArgumentsService.getExamStatusByArgumentId(argumentId);
        return new RestResponse(status.getCode(),status.getMessage());
    }

    /**
     * 根据exam_paper_id，获取考试的状态
     *
     */
    @GetMapping("/getExamStatusByPaperId/{paperId}")
    public RestResponse getExamStatusByPaperId(@PathVariable("paperId") Integer paperId) {
        ExamPaperStatus status = tExamArgumentsService.getExamStatusByPaperId(paperId);
        return new RestResponse(status.getCode(),status.getMessage());
    }


    /**
     * 根据argument_id获取考试参数
     */
    @GetMapping("/getArgumentById/{id}")
    public RestResponse<TExamArgumentsEntity> getArgumentById(@PathVariable("id") Integer argumentId){
        TExamArgumentsEntity argumentsEntity = tExamArgumentsService.getById(argumentId);
        if (argumentsEntity == null) {
            return RestResponse.fail(500,"根据id获取考试参数失败");
        }
        return RestResponse.ok(argumentsEntity);
    }

    /**
     * 根据试卷表exam_paper_id获取考试参数
     */
    @GetMapping("/getArgumentsByPaperId/{paperId}")
    public RestResponse<TExamArgumentsEntity> getArgumentsByPaperId(@PathVariable("paperId") Integer paperId){
        TExamArgumentsEntity argumentsEntity = tExamArgumentsService.getArgumentsByPaperId(paperId);
        if (argumentsEntity == null) {
            return RestResponse.fail(500,"根据试卷表id获取考试参数失败");
        }
        return RestResponse.ok(argumentsEntity);
    }

    /**
     * test
     */
    @GetMapping("/select/{id}")
    public R selectExamPaper(@PathVariable("id") Integer id) {
        RestResponse<ExamPaperEditRequestVM> select = examPaperFeignService.select(id);
        return R.ok().put("data", select);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("examsystem:texamarguments:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = tExamArgumentsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("examsystem:texamarguments:info")
    public R info(@PathVariable("id") Integer id) {
        TExamArgumentsEntity tExamArguments = tExamArgumentsService.getById(id);

        return R.ok().put("tExamArguments", tExamArguments);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("examsystem:texamarguments:save")
    public R save(@RequestBody TExamArgumentsEntity tExamArguments) {
        tExamArgumentsService.save(tExamArguments);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("examsystem:texamarguments:update")
    public R update(@RequestBody TExamArgumentsEntity tExamArguments) {
        tExamArgumentsService.updateById(tExamArguments);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //  @RequiresPermissions("examsystem:texamarguments:delete")
    public R delete(@RequestBody Integer[] ids) {
        tExamArgumentsService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
