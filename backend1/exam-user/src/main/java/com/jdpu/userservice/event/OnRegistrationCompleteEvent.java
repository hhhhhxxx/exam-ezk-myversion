package com.jdpu.userservice.event;

import com.jdpu.common.entity.UserEntity;
import org.springframework.context.ApplicationEvent;


public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private final UserEntity user;

    public OnRegistrationCompleteEvent(final UserEntity user) {
        super(user);
        this.user = user;
    }

    public UserEntity getUser() {
        return user;
    }
}
