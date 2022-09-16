package com.jdpu.api.mapper;

import com.jdpu.common.param.user.message.MessagePageRequestVM;
import com.jdpu.common.xzsOld.entities.Message;
import com.jdpu.common.xzsOld.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ApiMessageMapper extends BaseMapper<Message> {

    List<Message> page(MessagePageRequestVM requestVM);

    List<Message> selectByIds(List<Integer> ids);

    int readAdd(Integer id);
}
