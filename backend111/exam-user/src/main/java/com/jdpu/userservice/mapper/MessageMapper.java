package com.jdpu.userservice.mapper;


import com.jdpu.common.param.user.message.MessagePageRequestVM;
import com.jdpu.common.xzsOld.entities.Message;
import com.jdpu.common.xzsOld.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zuck
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    List<Message> page(MessagePageRequestVM requestVM);

    List<Message> selectByIds(List<Integer> ids);

    int readAdd(Integer id);
}
