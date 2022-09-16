package com.jdpu.auth.controller;

import com.jdpu.auth.service.IAuthenticationService;
import com.jdpu.auth.service.IUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.jdpu.common.dto.UserDTO;
import com.jdpu.common.dto.authority.PermissionDTO;
import com.jdpu.common.entity.vo.RestResponse;
import com.jdpu.common.param.StuQueryParam;
import com.jdpu.common.param.TeaQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * @Author: xJh
 * @Date: 2022/3/7
 */
@RestController
@Api("权限认证")
@Slf4j
public class AuthenticationController {

    @Autowired
    private IAuthenticationService authenticationService;
    @Autowired
    private IUserService userService;

    @ApiOperation(value = "权限认证", notes = "根据用户userId，访问的url和method判断用户是否有访问权限")
    @GetMapping("/auth/permission")
    public RestResponse decide(@RequestParam String userId,
                         @RequestParam String url,
                         @RequestParam String method,
                         HttpServletRequest request) {

        return authenticationService.permission_p(userId,url,method,request);
    }

    @ApiOperation(value = "获取用户权限列表", notes = "根据用户userId查询用户菜单、资源权限")
    @GetMapping("/auth/listUserPermission")
    public RestResponse<PermissionDTO> listUserPermission(@RequestParam("userId") Integer userId) {

        return authenticationService.listUserPermission_p(userId);
    }

    // @ApiOperation("后台获取教师列表")
    // @PostMapping("/auth/getTeacherPages")
    // public Page<UserDTO> getTeacherPages(@RequestBody TeaQueryParam userQueryParam) {
    //     return userService.getTeacherPages(userQueryParam);
    // }
    //
    // @ApiOperation("后台获取学生列表")
    // @PostMapping("/auth/getStuPages")
    // public Page<UserDTO> getStuPages(@RequestBody StuQueryParam stuQueryParam) {
    //     return userService.getStuPages(stuQueryParam);
    // }
}
