package com.jdpu.examsystem.controller;

import com.jdpu.common.entity.UserEntity;
import com.jdpu.examsystem.context.WebContext;
import com.jdpu.examsystem.entity.UserInfo;
import io.swagger.annotations.ApiOperation;
import com.jdpu.common.entity.vo.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/api/examsystem")
@Slf4j
public class WsExamSystemController {
    @Autowired
    WebContext webContext;

    // 获取userId 和 userName
    @ApiOperation("获取当前登陆用户信息放到session里,并返回给前端当前登陆用户信息")
    @PostMapping("/frontInfo")
    public RestResponse login(HttpServletRequest request){

        UserEntity user = webContext.getCurrentUser();

        HttpSession session = request.getSession();

        // 用户名可以重复 手机号不会
        session.setAttribute("username",user.getPhone());

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setUserName(user.getPhone());

        System.out.println("frontInfo的sessionID:"+session.getId()+"-----::username:"+session.getAttribute("username"));

        return RestResponse.ok(userInfo);
    }

    // 直接从session取
    @GetMapping("/currentUser")
    public RestResponse currentUser(HttpSession session){

        System.out.println("currentUser的sessionID:"+session.getId());

        String username = (String) session.getAttribute("username");


        if(username.isEmpty()) {
            return RestResponse.fail();
        }



        return RestResponse.ok(username);
    }
}
