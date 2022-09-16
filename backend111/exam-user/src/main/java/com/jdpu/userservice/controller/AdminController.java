package com.jdpu.userservice.controller;


import com.github.pagehelper.PageInfo;
import com.jdpu.common.entity.vo.RestResponse;
import com.jdpu.common.param.user.event.UserEventLogVM;
import com.jdpu.common.param.user.event.UserEventPageRequestVM;
import com.jdpu.common.xzsOld.utils.DateTimeUtil;
import com.jdpu.common.xzsOld.utils.PageInfoHelper;
import com.jdpu.common.xzsOld.entities.UserEventLog;
import com.jdpu.userservice.service.UserEventLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户服务-管理端
 * @author zuck
 */
@RestController
@RequestMapping("/api/admin/user")
public class  AdminController extends UserBaseApiController {
    private final UserEventLogService userEventLogService;


    @Autowired
    public AdminController(UserEventLogService userEventLogService) {
        this.userEventLogService = userEventLogService;
    }


    @RequestMapping("/test")
    public String current() {

        return "123";
    }

    /**
     * 用户日志
     */
    @RequestMapping(value = "/event/page/list", method = RequestMethod.POST)
    public RestResponse<PageInfo<UserEventLogVM>> eventPageList(@RequestBody UserEventPageRequestVM model) {
        PageInfo<UserEventLog> pageInfo = userEventLogService.page(model);
        PageInfo<UserEventLogVM> page = PageInfoHelper.copyMap(pageInfo, d -> {
            UserEventLogVM vm = modelMapper.map(d, UserEventLogVM.class);
            vm.setCreateTime(DateTimeUtil.dateFormat(d.getCreateTime()));
            return vm;
        });
        return RestResponse.ok(page);
    }
}
