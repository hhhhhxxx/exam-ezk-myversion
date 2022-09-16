package com.jdpu.userservice.service;


import com.jdpu.common.param.user.event.UserEventPageRequestVM;
import com.jdpu.common.xzsOld.entities.UserEventLog;
import com.github.pagehelper.PageInfo;
import com.jdpu.common.xzsOld.service.BaseService;

import java.util.List;

public interface UserEventLogService extends BaseService<UserEventLog> {
    List<UserEventLog> getUserEventLogByUserId(Integer id);

    PageInfo<UserEventLog> page(UserEventPageRequestVM requestVM);

    List<Integer> selectMothCount();
}
