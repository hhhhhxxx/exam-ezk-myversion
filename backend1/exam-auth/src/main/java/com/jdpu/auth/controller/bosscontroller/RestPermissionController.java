package com.jdpu.auth.controller.bosscontroller;
import io.swagger.annotations.ApiOperation;
import com.jdpu.common.entity.vo.RestResponse;
import com.jdpu.common.service.JwtService;
import com.jdpu.common.dto.authority.PermissionDTO;
import com.jdpu.common.result.ResponseDTO;
import com.jdpu.auth.entiy.UserManager;
import com.jdpu.auth.service.IAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: xJh
 * @Date: 2022/3/18
 */
@RestController
@RequestMapping("/boss/permission")
@Slf4j
public class RestPermissionController {
    @Autowired
    private IAuthenticationService authProvider;
    @Autowired
    private JwtService jwtService;

    @ApiOperation(value = "获取用户资源和资源权限列表")
    @GetMapping("/getUserPermissions")
    public ResponseDTO<PermissionDTO> getUserPermissions(HttpServletRequest request){

        Integer id = UserManager.getUserId();

        log.info("获取到的用户id:{}",id);
        try {
            RestResponse<PermissionDTO> result = authProvider.listUserPermission_p(id);
            if (result.isSuccess()){
                return ResponseDTO.success(result.getData());
            }
        }catch (Exception e){
            log.error("Query user permissions failed. userId:{}", id, e);
            return ResponseDTO.ofError("查询用户权限失败！");
        }
        return ResponseDTO.ofError("查询用户权限失败！");
    }
}
