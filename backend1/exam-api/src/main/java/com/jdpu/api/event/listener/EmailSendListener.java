package com.jdpu.api.event.listener;

import com.jdpu.common.entity.UserEntity;
import com.jdpu.userservice.event.OnRegistrationCompleteEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;


/**
 * @version 3.5.0
 * @description: The type Email send listener.
 * @date 2021/12/25 9:45
 */
@Component
public class EmailSendListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Override
    @NonNull
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        UserEntity user = event.getUser();
        System.out.println("User register Email sender :" + user.getName());
    }
}
