package com.jdpu.examsystem.feign;

import com.jdpu.common.entity.vo.RestResponse;
import com.jdpu.common.param.user.UserIdsVM;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="exam-auth",path = "/user")
public interface UserClient {
    @RequestMapping(value = "/current", method = RequestMethod.POST)
    RestResponse current(@RequestHeader String authorization);

    @GetMapping("/get/{id]")
    RestResponse getUserById(@PathVariable Integer userId);

    @GetMapping("/get")
    RestResponse getUserByIds(@RequestBody UserIdsVM model);
}
