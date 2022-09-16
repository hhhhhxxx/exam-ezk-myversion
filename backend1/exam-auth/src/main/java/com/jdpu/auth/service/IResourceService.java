package com.jdpu.auth.service;

import com.jdpu.auth.param.ResourceQueryParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jdpu.common.dto.authority.AllocateRoleResourceDTO;
import com.jdpu.common.dto.authority.ResourceCategoryDTO;
import com.jdpu.common.dto.authority.ResourceCategoryNodeDTO;
import com.jdpu.common.dto.authority.ResourceDTO;
import com.jdpu.common.entity.authority.Resource;
import com.jdpu.common.entity.vo.RestResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;


public interface IResourceService extends IService<Resource> {

    /**
     * 加载权限资源数据
     */
    void loadResource();

    /**
     * 根据请求url匹配资源url。能够在系统资源列表中匹配到资源的url，表明有权限访问
     *
     * @param authRequest
     * @return
     */
    boolean matchRequestUrl(HttpServletRequest authRequest);

    /**
     * 根据角色id列表关联查询资源表，找到角色拥有的资源
     *
     * @param roleIds
     * @return
     */
    List<Resource> queryByRoleIds(Set<Integer> roleIds);

    /**
     * 根据id删除资源，同时删除角色-资源关联关系
     *
     * @param id
     * @return
     */
    boolean deleteWithAssociation(Integer id);

    /**
     * 判断当前请求url是否存在资源池中，不存在直接返回false；
     * 再判断请求url是否匹配当前用户所拥有的资源，如果用户没有该资源访问权限，返回false;
     *
     * @param roleIds
     * @param request
     * @return
     */
    boolean matchUserResources(Set<Integer> roleIds, HttpServletRequest request);

    /**
     * 分页查询资源列表
     *
     * @param resourceQueryParam
     * @return
     */
    Page<Resource> getResourcePages(ResourceQueryParam resourceQueryParam);

    /**
     * 根据资源分类查询资源列表
     *
     * @param categoryId 资源分类ID
     * @return
     */
    List<Resource> getByCategoryId(Integer categoryId);

    /**
     * 给角色分配资源
     *
     * @param allocateRoleResourceDTO
     */
    void allocateRoleResources(AllocateRoleResourceDTO allocateRoleResourceDTO);

    RestResponse<Boolean> saveOrUpdate_p(ResourceDTO resourceDTO);

    RestResponse<ResourceDTO> getById_p(Integer id);

    RestResponse<Boolean> delete_p(Integer id);

    RestResponse<Page<ResourceDTO>> getResourcePages_p(ResourceQueryParam resourceQueryParam);

    RestResponse<List<ResourceDTO>> getAll_p();

    RestResponse<List<ResourceCategoryDTO>> getAllCategories_p();

    RestResponse<Boolean> saveOrUpdateCategory_p(ResourceCategoryDTO resourceCategoryDTO);

    RestResponse<Boolean> deleteCategoryById_p(Integer id);

    RestResponse<Boolean> allocateRoleResources_p(AllocateRoleResourceDTO allocateRoleMenuDTO);

    RestResponse<List<ResourceCategoryNodeDTO>> getRoleResources_p(Integer roleId);
}
