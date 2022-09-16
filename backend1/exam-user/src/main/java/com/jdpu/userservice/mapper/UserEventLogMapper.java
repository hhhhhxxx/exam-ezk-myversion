package com.jdpu.userservice.mapper;


import com.jdpu.common.param.user.event.UserEventPageRequestVM;
import com.jdpu.common.xzsOld.entities.KeyValue;
import com.jdpu.common.xzsOld.entities.UserEventLog;
import com.jdpu.common.xzsOld.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserEventLogMapper extends BaseMapper<UserEventLog> {
    List<UserEventLog> getUserEventLogByUserId(Integer id);
    List<UserEventLog> page(UserEventPageRequestVM requestVM);
    List<KeyValue> selectCountByDate(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
