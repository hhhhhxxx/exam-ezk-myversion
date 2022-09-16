package com.jdpu.userservice.service;

import com.jdpu.common.param.user.message.MessagePageRequestVM;
import com.jdpu.common.param.user.message.MessageRequestVM;
import com.jdpu.common.xzsOld.entities.Message;
import com.jdpu.common.xzsOld.entities.MessageUser;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface MessageService {
    List<Message> selectMessageByIds(List<Integer> ids);

    PageInfo<MessageUser> studentPage(MessageRequestVM requestVM);

    PageInfo<Message> page(MessagePageRequestVM requestVM);

    List<MessageUser> selectByMessageIds(List<Integer> ids);

    void sendMessage(Message message, List<MessageUser> messageUsers);

    void read(Integer id);

    Integer unReadCount(Integer userId);

    Message messageDetail(Integer id);
}
