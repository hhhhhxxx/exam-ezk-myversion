package com.jdpu.auth.controller.bosscontroller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import com.jdpu.common.dto.UserDTO;
import com.jdpu.common.entity.vo.RestResponse;
import com.jdpu.common.param.TeaQueryParam;
import com.jdpu.common.param.UserQueryParam;
import com.jdpu.auth.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: xJh
 * @Date: 2022/3/29
 */
@RestController
@RequestMapping("/boss/user")
@Slf4j
public class RestUserController {

    @Autowired
    private IUserService userService;
    // @Autowired
    // private AuthProvider authProvider;

    @ApiOperation(value = "分页查询用户信息")
    @PostMapping("getUserPages")
    @ResponseBody
    public RestResponse getUserPages(@RequestBody UserQueryParam userQueryParam) {
        log.info("分页查询用户信息:{}", JSON.toJSONString(userQueryParam));
        Integer currentPage = userQueryParam.getCurrentPage();
        Integer pageSize = userQueryParam.getPageSize();
        if (null == currentPage || currentPage <= 0) {
            userQueryParam.setCurrentPage(1);
        }
        if (null == pageSize || pageSize <= 0) {
            userQueryParam.setPageSize(10);
        }
        try {
            Page<UserDTO> queryCourses = userService.getUserPages_p(userQueryParam);
            return RestResponse.success(queryCourses);
        } catch (Exception e) {
            log.error("分页查询用户信息:", e);
            return RestResponse.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "分页查询教师信息")
    @PostMapping("getTeacherPages")
    @ResponseBody
    public RestResponse getTeacherPages(@RequestBody TeaQueryParam userQueryParam) {
        log.info("分页查询用户信息:{}", JSON.toJSONString(userQueryParam));
        Integer currentPage = userQueryParam.getPageIndex();
        Integer pageSize = userQueryParam.getPageSize();
        if (null == currentPage || currentPage <= 0) {
            userQueryParam.setPageSize(1);
        }
        if (null == pageSize || pageSize <= 0) {
            userQueryParam.setPageSize(10);
        }
        try {
            // Page<UserDTO> queryCourses = authProvider.getTeacherPages_p(userQueryParam);

            Page<UserDTO> queryCourses = userService.getTeacherPages(userQueryParam);

            return RestResponse.success(queryCourses);
        } catch (Exception e) {
            log.error("分页查询用户信息:", e);
            return RestResponse.fail(e.getMessage());
        }
    }
}
