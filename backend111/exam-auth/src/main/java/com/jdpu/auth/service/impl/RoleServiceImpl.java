package com.jdpu.auth.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;

import com.google.common.collect.Sets;
import com.jdpu.common.dto.authority.AllocateUserRoleDTO;
import com.jdpu.common.dto.authority.RoleDTO;
import com.jdpu.common.entity.authority.Role;
import com.jdpu.common.entity.authority.UserRole;

import com.jdpu.common.entity.vo.RestResponse;
import com.jdpu.common.utils.ConvertUtils;
import com.jdpu.auth.mapper.RoleMapper;
import com.jdpu.auth.param.RoleQueryParam;
import com.jdpu.auth.service.IRoleMenuService;
import com.jdpu.auth.service.IRoleResourceService;
import com.jdpu.auth.service.IRoleService;
import com.jdpu.auth.service.IUserRoleService;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private IRoleMenuService roleMenuService;
    @Autowired
    private IRoleResourceService roleResourceService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean deleteWithAssociation(Integer id) {
        userRoleService.removeByRoleId(id);
        roleMenuService.removeByRoleId(id);
        roleResourceService.removeByRoleId(id);
        return this.removeById(id);
    }

    // 之前的远程调用OrganizationProvider
    @Override
    public Set<RoleDTO> queryUserRolesByUserId(Integer userId) {
        List<Role> roles = this.queryByUserId(userId);
        HashSet<RoleDTO> roleSets = Sets.newHashSet();
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(roles)){
            roles.stream().forEach(
                    role -> {
                        roleSets.add(ConvertUtils.convert(role,RoleDTO.class));
                    }
            );
        }
        return roleSets;
    }

    @Override
    public Role get(Integer id) {
        Role role = this.getById(id);
        if (Objects.isNull(role)) {
            return null;
        }
        role.setResourceIds(roleResourceService.queryByRoleId(id));
        return role;
    }

    @Override
    public List<Role> getAll() {
        return this.list();
    }

    @Override
    public List<Role> queryByUserId(Integer userId) {
        Set<Integer> roleIds = userRoleService.queryByUserId(userId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return Lists.newArrayList();
        }
        return this.listByIds(roleIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void allocateUserRoles(AllocateUserRoleDTO allocateUserRoleDTO) {
        if (CollectionUtils.isEmpty(allocateUserRoleDTO.getRoleIdList())) {
            // 如果id列表为空，表示要删除所有角色
            allocateUserRoleDTO.setRoleIdList(Lists.newArrayList());
        }
        // 用户已拥有的角色(id列表）
        Set<Integer> userRoleIds = userRoleService.queryByUserId(allocateUserRoleDTO.getUserId());
        // 当前要分配给用户的角色(id列表）
        Set<Integer> allocatedRoleIds = allocateUserRoleDTO.getRoleIdList().stream().collect(Collectors.toSet());

        // 找出本次删除的
        Set<Integer> needToDelRoles = userRoleIds.stream().filter(id -> !allocatedRoleIds.contains(id)).collect(Collectors.toSet());
        // 找出本次新增的
        Set<Integer> needToInsertRoles = allocatedRoleIds.stream().filter(id -> !userRoleIds.contains(id)).collect(Collectors.toSet());

        if (CollectionUtils.isNotEmpty(needToDelRoles)) {
            userRoleService.removeByRoleIds(allocateUserRoleDTO.getUserId(), needToDelRoles);
        }

        if (CollectionUtils.isNotEmpty(needToInsertRoles)) {
            List<UserRole> newUserRoles = needToInsertRoles.stream().map(roleId -> {
                UserRole userRole = new UserRole();
                userRole.setUserId(allocateUserRoleDTO.getUserId());
                userRole.setRoleId(roleId);
                userRole.setCreatedBy(allocateUserRoleDTO.getCreatedBy());
                userRole.setUpdatedBy(allocateUserRoleDTO.getUpdatedBy());
                userRole.setCreatedTime(new Date());
                userRole.setUpdatedTime(new Date());
                return userRole;
            }).collect(Collectors.toList());
            userRoleService.saveBatch(newUserRoles);
        }
    }

    @Override
    public Page<Role> getRolePages(RoleQueryParam roleQueryParam) {
        Page<Role> page = new Page<>(roleQueryParam.getCurrent(), roleQueryParam.getSize());
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq(Objects.nonNull(roleQueryParam.getId()), "id", roleQueryParam.getId())
                .like(StringUtils.isNotBlank(roleQueryParam.getName()), "name", roleQueryParam.getName())
                .eq(StringUtils.isNotBlank(roleQueryParam.getCode()), "code", roleQueryParam.getCode())
                .ge(Objects.nonNull(roleQueryParam.getStartCreateTime()), "created_time", roleQueryParam.getStartCreateTime())
                .le(Objects.nonNull(roleQueryParam.getEndCreateTime()), "created_time", roleQueryParam.getEndCreateTime())
                .orderByDesc("id");
        return this.page(page, queryWrapper);
    }


    @Override
    public RestResponse<Page<RoleDTO>> getRolePages_p(RoleQueryParam roleQueryParam) {
        Page<Role> rolePages = getRolePages(roleQueryParam);
        Page<RoleDTO> roleDTOPage = new Page<>();
        BeanUtils.copyProperties(rolePages,roleDTOPage);
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(rolePages.getRecords())){
            List<RoleDTO> records = rolePages.getRecords().stream().map(
                    role -> ConvertUtils.convert(role, RoleDTO.class)
            ).collect(Collectors.toList());
            roleDTOPage.setRecords(records);
        }
        return RestResponse.success(roleDTOPage);
    }

    @Override
    public RestResponse<Set<RoleDTO>> getUserRoles_p(Integer userId) {
        Set<RoleDTO> roleSets = queryUserRolesByUserId(userId);
        return RestResponse.success(roleSets);
    }

    @Override
    public RestResponse<Boolean> saveOrUpdate_p(RoleDTO roleDTO) {
        Role role = ConvertUtils.convert(roleDTO, Role.class);
        role.setUpdatedTime(new Date());
        saveOrUpdate(role);
        return RestResponse.success();
    }

    @Override
    public RestResponse<Boolean> delete_p(Integer id) {
        //删除角色后还要删除与角色相关联的东西
        //角色和资源
        //角色和用户
        //角色和菜案
        return RestResponse.success(deleteWithAssociation(id));
    }

    @Override
    public RestResponse<RoleDTO> getById_p(Integer id) {
        Role role = get(id);
        return RestResponse.success(ConvertUtils.convert(role, RoleDTO.class));
    }

    @Override
    public RestResponse<Boolean> allocateUserRoles_p(AllocateUserRoleDTO allocateUserRoleDTO) {
        log.info("Allocate user roles with params:{}", allocateUserRoleDTO);
        allocateUserRoles(allocateUserRoleDTO);
        return RestResponse.success(Boolean.TRUE);
    }

    @Override
    public RestResponse<List<RoleDTO>> getAll_p() {
        List<Role> roleList = getAll();
        List<RoleDTO> roleDTOList = Lists.newArrayList();
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(roleList)){
            roleList.stream().forEach(
                    role -> {
                        roleDTOList.add(ConvertUtils.convert(role,RoleDTO.class));
                    }
            );
        }
        return RestResponse.success(roleDTOList);
    }
}
