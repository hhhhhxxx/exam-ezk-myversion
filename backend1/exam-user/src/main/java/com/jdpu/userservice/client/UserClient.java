package com.jdpu.userservice.client;

import com.jdpu.common.entity.UserEntity;
import com.jdpu.common.entity.vo.RestResponse;
import com.jdpu.common.param.user.UserIdsVM;
import com.netflix.ribbon.proxy.annotation.Http;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@FeignClient(name="exam-auth",path = "/user")
public interface UserClient {
    @RequestMapping(value = "/current", method = RequestMethod.POST)
    RestResponse current();

    @GetMapping("/get/{id}")
    RestResponse getUserById(@PathVariable("id") Integer userId);

    @GetMapping("/get")
    RestResponse getUserByIds(@RequestBody UserIdsVM model);
}
