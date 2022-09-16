package com.jdpu.userservice.mapper;


import com.jdpu.common.param.user.message.MessageRequestVM;
import com.jdpu.common.xzsOld.entities.MessageUser;
import com.jdpu.common.xzsOld.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageUserMapper extends BaseMapper<MessageUser> {
    List<MessageUser> selectByMessageIds(List<Integer> ids);

    int inserts(List<MessageUser> list);

    List<MessageUser> studentPage(MessageRequestVM requestVM);

    Integer unReadCount(Integer userId);
}
