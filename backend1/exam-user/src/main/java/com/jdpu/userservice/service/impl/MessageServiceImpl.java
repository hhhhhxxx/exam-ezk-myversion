package com.jdpu.userservice.service.impl;

import com.jdpu.common.param.user.message.MessagePageRequestVM;
import com.jdpu.common.param.user.message.MessageRequestVM;
import com.jdpu.common.xzsOld.entities.Message;
import com.jdpu.common.xzsOld.entities.MessageUser;
import com.jdpu.userservice.mapper.MessageMapper;
import com.jdpu.userservice.mapper.MessageUserMapper;
import com.jdpu.userservice.service.MessageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 消息
 * @author zuck
 */
@Service
public class MessageServiceImpl implements MessageService {
    private final MessageMapper messageMapper;
    private final MessageUserMapper messageUserMapper;

    @Autowired
    public MessageServiceImpl(MessageMapper messageMapper, MessageUserMapper messageUserMapper) {
        this.messageMapper = messageMapper;
        this.messageUserMapper = messageUserMapper;
    }

    @Override
    public List<Message> selectMessageByIds(List<Integer> ids) {
        return messageMapper.selectByIds(ids);
    }

    @Override
    public PageInfo<MessageUser> studentPage(MessageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                messageUserMapper.studentPage(requestVM)
        );
    }

    @Override
    public PageInfo<Message> page(MessagePageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                messageMapper.page(requestVM)
        );
    }

    @Override
    public List<MessageUser> selectByMessageIds(List<Integer> ids) {
        return messageUserMapper.selectByMessageIds(ids);
    }

    @Override
    @Transactional
    public void sendMessage(Message message, List<MessageUser> messageUsers) {
        messageMapper.insertSelective(message);
        messageUsers.forEach(d -> d.setMessageId(message.getId()));
        messageUserMapper.inserts(messageUsers);
    }

    @Override
    @Transactional
    public void read(Integer id) {
        MessageUser messageUser = messageUserMapper.selectByPrimaryKey(id);
        if (messageUser.getReaded())
            return;
        messageUser.setReaded(true);
        messageUser.setReadTime(new Date());
        messageUserMapper.updateByPrimaryKeySelective(messageUser);
        messageMapper.readAdd(messageUser.getMessageId());
    }

    @Override
    public Integer unReadCount(Integer userId) {
        return messageUserMapper.unReadCount(userId);
    }

    @Override
    public Message messageDetail(Integer id) {
        MessageUser messageUser = messageUserMapper.selectByPrimaryKey(id);
        return messageMapper.selectByPrimaryKey(messageUser.getMessageId());
    }
}
