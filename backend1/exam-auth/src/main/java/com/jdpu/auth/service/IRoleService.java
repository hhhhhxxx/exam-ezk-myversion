package com.jdpu.auth.service;


import com.jdpu.auth.param.RoleQueryParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jdpu.common.dto.authority.AllocateUserRoleDTO;
import com.jdpu.common.dto.authority.RoleDTO;
import com.jdpu.common.entity.authority.Role;
import com.jdpu.common.entity.vo.RestResponse;

import java.util.List;
import java.util.Set;

/**
 * @Author: xJh
 * @Date: 2022/3/6
 */
public interface IRoleService extends IService<Role> {

    Set<RoleDTO> queryUserRolesByUserId(Integer userId);

    // 内容
    /**
     * 获取角色
     *
     * @param id
     * @return
     */
    Role get(Integer id);

    /**
     * 获取所有角色
     *
     * @return
     */
    List<Role> getAll();

    /**
     * 根据用户id查询用户拥有的角色
     *
     * @param userId
     * @return
     */
    List<Role> queryByUserId(Integer userId);

    /**
     * 根据id删除角色
     * 并且关联删除用户-角色，角色-菜单，角色-资源关系
     *
     * @param id
     * @return
     */
    boolean deleteWithAssociation(Integer id);

    /**
     * 给用户分配角色
     *
     * @param allocateUserRoleDTO
     * @return
     */
    void allocateUserRoles(AllocateUserRoleDTO allocateUserRoleDTO);

    Page<Role> getRolePages(RoleQueryParam roleQueryParam);

    RestResponse<Page<RoleDTO>> getRolePages_p(RoleQueryParam roleQueryParam);

    RestResponse<Set<RoleDTO>> getUserRoles_p(Integer userId);

    RestResponse<Boolean> saveOrUpdate_p(RoleDTO roleDTO);

    RestResponse<Boolean> delete_p(Integer id);

    RestResponse<RoleDTO> getById_p(Integer id);

    RestResponse<Boolean> allocateUserRoles_p(AllocateUserRoleDTO allocateUserRoleDTO);

    RestResponse<List<RoleDTO>> getAll_p();
}
