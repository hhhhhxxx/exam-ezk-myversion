package com.jdpu.auth.service;

import com.jdpu.common.dto.authority.PermissionDTO;
import com.jdpu.common.entity.vo.RestResponse;

import javax.servlet.http.HttpServletRequest;


public interface IAuthenticationService {

    /**
     * 验证用户是否有访问url的权限
     * @param userId
     * @param request
     * @return
     */
    boolean decide(String userId, HttpServletRequest request);

    /**
     * 查询用户菜单、资源权限
     * @param userId
     * @return
     */


    PermissionDTO listUserPermission(Integer userId);

    RestResponse<PermissionDTO> listUserPermission_p(Integer userId);

    RestResponse permission_p(String userId, String url, String method, HttpServletRequest request);
}
