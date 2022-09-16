package com.jdpu.userservice.event;

import com.jdpu.common.xzsOld.entities.UserEventLog;
import org.springframework.context.ApplicationEvent;

/**
 * 用户事件类
 * @author zuck
 */
public class UserEvent extends ApplicationEvent {
    private final UserEventLog userEventLog;

    /**
     * 实例化用户事件类
     */
    public UserEvent(final UserEventLog userEventLog) {
        super(userEventLog);
        this.userEventLog = userEventLog;
    }

    /**
     * 获取用户事件日志.
     */
    public UserEventLog getUserEventLog() {
        return userEventLog;
    }
}
