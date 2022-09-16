package com.jdpu.auth.controller.bosscontroller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import com.jdpu.common.dto.authority.AllocateUserRoleDTO;
import com.jdpu.common.dto.authority.RoleDTO;
import com.jdpu.common.dto.authority.UserRoleDTO;
import com.jdpu.common.entity.vo.RestResponse;
import com.jdpu.common.utils.ConvertUtils;
import com.jdpu.auth.entiy.UserManager;
import com.jdpu.auth.entiy.form.AllocateUserRoleForm;
import com.jdpu.auth.entiy.form.RoleForm;
import com.jdpu.auth.param.RoleQueryParam;
import com.jdpu.auth.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: xJh
 * @Date: 2022/3/27
 */
@RestController
@RequestMapping("/boss/role")
@Slf4j
public class RestRoleController {

    @Autowired
    private IRoleService roleProvider;

    @ApiOperation("通过条件分页获取所有角色")
    @PostMapping("/getRolePages")
    public RestResponse<Page<RoleDTO>> getRolePages(@RequestBody RoleQueryParam roleQueryParam){
        return roleProvider.getRolePages_p(roleQueryParam);
    }

    @ApiOperation("查询用户角色")
    @GetMapping("user/{userId}")
    public RestResponse<Set<RoleDTO>> getUserRoles(@PathVariable("userId") Integer userId){
        log.info("getUserRoles with userId:{}", userId);
        return roleProvider.getUserRoles_p(userId);
    }

    @ApiOperation("保存或者更新角色")
    @PostMapping("/SaveOrUpdate")
    public RestResponse<Boolean> saveOrUpdate(@RequestBody RoleForm roleForm){
        RoleDTO roleDTO = ConvertUtils.convert(roleForm, RoleDTO.class);
        //创建角色
        if (Objects.isNull(roleDTO.getId())){
            roleDTO.setCreatedTime(new Date());
            roleDTO.setCreatedBy(UserManager.getUserName());
        }
        roleDTO.setUpdatedBy(UserManager.getUserName());
        return roleProvider.saveOrUpdate_p(roleDTO);
    }

    @ApiOperation("删除角色")
    @PostMapping(value = "/{id}")
    public RestResponse<Boolean> delete(@PathVariable("id") Integer id) {
        return roleProvider.delete_p(id);
    }

    @ApiOperation(value = "获取角色")
    @GetMapping(value = "/{id}")
    public RestResponse<RoleDTO> getById(@PathVariable("id") Integer id) {
        return roleProvider.getById_p(id);
    }


    @ApiOperation("给用户分配角色")
    @PostMapping("/allocateUserRoles")
    public RestResponse<Boolean> allocateUserRoles(@RequestBody AllocateUserRoleForm allocateUserRoleForm){
        if (Objects.isNull(allocateUserRoleForm.getUserId())){
            return RestResponse.fail("用户id不能为空");
        }
        AllocateUserRoleDTO allocateUserRoleDTO = ConvertUtils.convert(allocateUserRoleForm, AllocateUserRoleDTO.class);
        allocateUserRoleDTO.setCreatedBy(UserManager.getUserName());
        allocateUserRoleDTO.setUpdatedBy(UserManager.getUserName());
        return roleProvider.allocateUserRoles_p(allocateUserRoleDTO);
    }

    @ApiOperation(value = "列出所有角色，并且标记当前用户ID是否拥有该角色",
            notes = "在分配角色时，展示所有有效角色，并且标记处用户当前已拥有的角色")
    @GetMapping("/getRolesWithUserPermission")
    public RestResponse<List<UserRoleDTO>> getRolesWithUserPermission(@RequestParam Integer userId){
        RestResponse<List<RoleDTO>> allRoleResult = roleProvider.getAll_p();
        RestResponse<Set<RoleDTO>> userRolesResult = roleProvider.getUserRoles_p(userId);
        if (!allRoleResult.isSuccess() || !userRolesResult.isSuccess()){
            return RestResponse.fail();
        }
        List<RoleDTO> roles = allRoleResult.getData();
        if (CollectionUtils.isEmpty(roles)){
            return RestResponse.success(Lists.newArrayList());
        }
        Set<RoleDTO> userRoles = userRolesResult.getData();
        List<UserRoleDTO> roleWithUserPermission = roles.stream().map(roleDTO -> {
            return new UserRoleDTO(roleDTO.getId(), roleDTO.getName(), userRoles.contains(roleDTO));
        }).collect(Collectors.toList());
        return RestResponse.success(roleWithUserPermission);
    }

    @ApiOperation(value = "获取所有角色")
    @GetMapping(value = "/all")
    public RestResponse<List<RoleDTO>> getAll() {
        return roleProvider.getAll_p();
    }
}
