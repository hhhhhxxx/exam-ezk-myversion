package com.jdpu.api.controller.admin;


import com.jdpu.api.context.UserBaseApiController;
import com.jdpu.api.service.ApiMessageService;
import com.jdpu.common.param.user.message.MessagePageRequestVM;
import com.jdpu.common.param.user.message.MessageResponseVM;
import com.jdpu.common.param.user.message.MessageSendVM;
import com.github.pagehelper.PageInfo;

import com.jdpu.common.entity.UserEntity;
import com.jdpu.common.entity.vo.RestResponse;
import com.jdpu.common.xzsOld.entities.Message;
import com.jdpu.common.xzsOld.entities.MessageUser;
import com.jdpu.common.xzsOld.utils.DateTimeUtil;
import com.jdpu.common.xzsOld.utils.PageInfoHelper;
import com.jdpu.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 消息
 */
@RestController
@RequestMapping(value = "/api/admin/message")
public class AdminMessageController extends UserBaseApiController {

    private final ApiMessageService apiMessageService;
    private final UserService userService;

    @Autowired
    public AdminMessageController(ApiMessageService apiMessageService, UserService userService) {
        this.apiMessageService = apiMessageService;
        this.userService = userService;
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public RestResponse<PageInfo<MessageResponseVM>> pageList(@RequestBody MessagePageRequestVM model) {
        PageInfo<Message> pageInfo = apiMessageService.page(model);
        List<Integer> ids = pageInfo.getList().stream().map(d -> d.getId()).collect(Collectors.toList());
        List<MessageUser> messageUsers = ids.size() == 0 ? null : apiMessageService.selectByMessageIds(ids);
        PageInfo<MessageResponseVM> page = PageInfoHelper.copyMap(pageInfo, m -> {
            MessageResponseVM vm = modelMapper.map(m, MessageResponseVM.class);
            String receives = messageUsers.stream().filter(d -> d.getMessageId().equals(m.getId())).map(d -> d.getReceiveUserName())
                    .collect(Collectors.joining(","));
            vm.setReceives(receives);
            vm.setCreateTime(DateTimeUtil.dateFormat(m.getCreateTime()));
            return vm;
        });
        return RestResponse.ok(page);
    }


    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public RestResponse send(@RequestBody @Valid MessageSendVM model) {
        UserEntity user = getCurrentUser();
        List<UserEntity> receiveUser = userService.selectByIds(model.getReceiveUserIds());
        Date now = new Date();
        Message message = new Message();
        message.setTitle(model.getTitle());
        message.setContent(model.getContent());
        message.setCreateTime(now);
        message.setReadCount(0);
        message.setReceiveUserCount(receiveUser.size());
        message.setSendUserId(user.getId());
        message.setSendUserName(user.getName());
        message.setSendRealName(user.getRealName());
        List<MessageUser> messageUsers = receiveUser.stream().map(d -> {
            MessageUser messageUser = new MessageUser();
            messageUser.setCreateTime(now);
            messageUser.setReaded(false);
            messageUser.setReceiveRealName(d.getRealName());
            messageUser.setReceiveUserId(d.getId());
            messageUser.setReceiveUserName(d.getName());
            return messageUser;
        }).collect(Collectors.toList());
        apiMessageService.sendMessage(message, messageUsers);
        return RestResponse.ok();
    }

}
