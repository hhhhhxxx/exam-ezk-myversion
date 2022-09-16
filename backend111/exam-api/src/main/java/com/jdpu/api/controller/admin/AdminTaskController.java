package com.jdpu.api.controller.admin;


import com.jdpu.api.context.UserBaseApiController;
import com.jdpu.common.entity.vo.RestResponse;
import com.jdpu.api.entities.TaskExam;
import com.jdpu.api.service.TaskExamService;
import com.jdpu.common.param.admin.task.TaskPageRequestVM;
import com.jdpu.common.param.admin.task.TaskPageResponseVM;
import com.jdpu.common.param.admin.task.TaskRequestVM;
import com.github.pagehelper.PageInfo;
import com.jdpu.common.xzsOld.utils.DateTimeUtil;
import com.jdpu.common.xzsOld.utils.PageInfoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 任务
 */
@RestController
@RequestMapping(value = "/api/admin/task")
public class AdminTaskController extends UserBaseApiController {

    private final TaskExamService taskExamService;

    @Autowired
    public AdminTaskController(TaskExamService taskExamService) {
        this.taskExamService = taskExamService;
    }

    /**
     * 任务分页
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public RestResponse<PageInfo<TaskPageResponseVM>> pageList(@RequestBody TaskPageRequestVM model) {
        PageInfo<TaskExam> pageInfo = taskExamService.page(model);
        PageInfo<TaskPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, m -> {
            TaskPageResponseVM vm = modelMapper.map(m, TaskPageResponseVM.class);
            vm.setCreateTime(DateTimeUtil.dateFormat(m.getCreateTime()));
            return vm;
        });
        return RestResponse.ok(page);
    }

    /**
     * 任务编辑
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public RestResponse edit(@RequestBody @Valid TaskRequestVM model) {
        taskExamService.edit(model, getCurrentUser());
        TaskRequestVM vm = taskExamService.taskExamToVM(model.getId());
        return RestResponse.ok(vm);
    }

    /**
     * 任务查询
     */
    @RequestMapping(value = "/select/{id}", method = RequestMethod.POST)
    public RestResponse<TaskRequestVM> select(@PathVariable Integer id) {
        TaskRequestVM vm = taskExamService.taskExamToVM(id);
        return RestResponse.ok(vm);
    }

    /**
     * 任务删除
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public RestResponse delete(@PathVariable Integer id) {
        TaskExam taskExam = taskExamService.selectById(id);
        taskExam.setDeleted(true);
        taskExamService.updateByIdFilter(taskExam);
        return RestResponse.ok();
    }
}
