package com.jdpu.userservice.controller;


import com.jdpu.common.param.user.*;
import com.jdpu.common.param.user.event.UserEventLogVM;
import com.jdpu.common.param.user.message.MessageRequestVM;
import com.jdpu.common.param.user.message.MessageResponseVM;
import com.jdpu.common.xzsOld.utils.DateTimeUtil;
import com.jdpu.common.xzsOld.utils.PageInfoHelper;
import com.jdpu.common.xzsOld.entities.Message;
import com.jdpu.common.xzsOld.entities.MessageUser;
import com.jdpu.common.xzsOld.entities.UserEventLog;
import com.jdpu.common.entity.vo.RestResponse;
import com.github.pagehelper.PageInfo;
import com.jdpu.common.entity.UserEntity;
import com.jdpu.userservice.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 学生端
 * @author zuck
 */
@RestController
@RequestMapping("/api/student/user")
public class StudentController extends UserBaseApiController {
    private final UserEventLogService userEventLogService;
    private final MessageService messageService;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public StudentController(UserEventLogService userEventLogService, MessageService messageService, ApplicationEventPublisher eventPublisher) {
        this.userEventLogService = userEventLogService;
        this.messageService = messageService;
        this.eventPublisher = eventPublisher;
    }


    /**
     * 用户动态
     * TODO getCurrentUser方法
     */
    @RequestMapping(value = "/log", method = RequestMethod.POST)
    public RestResponse<List<UserEventLogVM>> log() {
        // 先不管日志

        UserEntity user = getCurrentUser();
        List<UserEventLog> userEventLogs = userEventLogService.getUserEventLogByUserId(user.getId());
        List<UserEventLogVM> userEventLogVMS = userEventLogs.stream().map(d -> {
            UserEventLogVM vm = modelMapper.map(d, UserEventLogVM.class);
            vm.setCreateTime(DateTimeUtil.dateFormat(d.getCreateTime()));
            return vm;
        }).collect(Collectors.toList());
        return RestResponse.ok(userEventLogVMS);
    }

    /**
     * 消息分页
     */
    @PostMapping("/message/page")
    public RestResponse<PageInfo<MessageResponseVM>> messagePageList(@RequestBody MessageRequestVM messageRequestVM) {
        messageRequestVM.setReceiveUserId(getCurrentUser().getId());
        PageInfo<MessageUser> messageUserPageInfo = messageService.studentPage(messageRequestVM);
        List<Integer> ids = messageUserPageInfo.getList().stream().map(d -> d.getMessageId()).collect(Collectors.toList());
        List<Message> messages = ids.size() != 0 ? messageService.selectMessageByIds(ids) : null;
        PageInfo<MessageResponseVM> page = PageInfoHelper.copyMap(messageUserPageInfo, e -> {
            MessageResponseVM vm = modelMapper.map(e, MessageResponseVM.class);
            messages.stream().filter(d -> e.getMessageId().equals(d.getId())).findFirst().ifPresent(message -> {
                vm.setTitle(message.getTitle());
                vm.setContent(message.getContent());
                vm.setSendUserName(message.getSendUserName());
            });
            vm.setCreateTime(DateTimeUtil.dateFormat(e.getCreateTime()));
            return vm;
        });
        return RestResponse.ok(page);
    }

    /**
     * 消息标记已读
     */
    @PostMapping("/message/read/{id}")
    public RestResponse read(@PathVariable Integer id) {
        messageService.read(id);
        return RestResponse.ok();
    }

    /**
     * 未读消息数量
     */
    @PostMapping("/message/unreadCount")
    public RestResponse unReadCount() {
        Integer count = messageService.unReadCount(getCurrentUser().getId());
        return RestResponse.ok(count);
    }
}
