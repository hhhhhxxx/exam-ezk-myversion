package com.jdpu.api.service.impl;

import com.jdpu.api.mapper.ApiMessageMapper;
import com.jdpu.api.mapper.ApiMessageUserMapper;
import com.jdpu.api.service.ApiMessageService;
import com.jdpu.common.param.user.message.MessagePageRequestVM;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jdpu.common.param.user.message.MessageRequestVM;
import com.jdpu.common.xzsOld.entities.Message;
import com.jdpu.common.xzsOld.entities.MessageUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ApiMessageServiceImpl implements ApiMessageService {

    private final ApiMessageMapper apiMessageMapper;
    private final ApiMessageUserMapper apiMessageUserMapper;

    @Autowired
    public ApiMessageServiceImpl(ApiMessageMapper apiMessageMapper, ApiMessageUserMapper apiMessageUserMapper) {
        this.apiMessageMapper = apiMessageMapper;
        this.apiMessageUserMapper = apiMessageUserMapper;
    }

    @Override
    public List<Message> selectMessageByIds(List<Integer> ids) {
        return apiMessageMapper.selectByIds(ids);
    }

    @Override
    public PageInfo<MessageUser> studentPage(MessageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                apiMessageUserMapper.studentPage(requestVM)
        );
    }

    @Override
    public PageInfo<Message> page(MessagePageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                apiMessageMapper.page(requestVM)
        );
    }

    @Override
    public List<MessageUser> selectByMessageIds(List<Integer> ids) {
        return apiMessageUserMapper.selectByMessageIds(ids);
    }

    @Override
    @Transactional
    public void sendMessage(Message message, List<MessageUser> messageUsers) {
        apiMessageMapper.insertSelective(message);
        messageUsers.forEach(d -> d.setMessageId(message.getId()));
        apiMessageUserMapper.inserts(messageUsers);
    }

    @Override
    @Transactional
    public void read(Integer id) {
        MessageUser messageUser = apiMessageUserMapper.selectByPrimaryKey(id);
        if (messageUser.getReaded())
            return;
        messageUser.setReaded(true);
        messageUser.setReadTime(new Date());
        apiMessageUserMapper.updateByPrimaryKeySelective(messageUser);
        apiMessageMapper.readAdd(messageUser.getMessageId());
    }

    @Override
    public Integer unReadCount(Integer userId) {
        return apiMessageUserMapper.unReadCount(userId);
    }

    @Override
    public Message messageDetail(Integer id) {
        MessageUser messageUser = apiMessageUserMapper.selectByPrimaryKey(id);
        return apiMessageMapper.selectByPrimaryKey(messageUser.getMessageId());
    }

}
