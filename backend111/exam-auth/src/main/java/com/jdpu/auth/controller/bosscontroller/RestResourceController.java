package com.jdpu.auth.controller.bosscontroller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.jdpu.common.dto.authority.AllocateRoleResourceDTO;
import com.jdpu.common.dto.authority.ResourceCategoryDTO;
import com.jdpu.common.dto.authority.ResourceCategoryNodeDTO;
import com.jdpu.common.dto.authority.ResourceDTO;
import com.jdpu.common.entity.vo.RestResponse;
import com.jdpu.common.utils.ConvertUtils;
import com.jdpu.auth.entiy.UserManager;
import com.jdpu.auth.entiy.form.AllocateRoleResourceForm;
import com.jdpu.auth.entiy.form.ResourceCategoryForm;
import com.jdpu.auth.entiy.form.ResourceForm;
import com.jdpu.auth.param.ResourceQueryParam;
import com.jdpu.auth.service.IResourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Author: xJh
 * @Date: 2022/3/27
 */
@Api("资源模块")
@RestController
@RequestMapping("/boss/resource")
@Slf4j
public class RestResourceController {
    @Autowired
    private IResourceService resourceProvider;

    @ApiOperation(value = "保存或者更新资源")
    @PostMapping("/saveOrUpdate")
    public RestResponse<Boolean> saveOrUpdate(@RequestBody ResourceForm resourceForm) {
        ResourceDTO resourceDTO = ConvertUtils.convert(resourceForm, ResourceDTO.class);
        if (Objects.isNull(resourceDTO.getId())) {
            resourceDTO.setCreatedBy(UserManager.getUserName());
            resourceDTO.setCreatedTime(new Date());
        }
        resourceDTO.setUpdatedBy(UserManager.getUserName());
        resourceDTO.setUpdatedTime(new Date());
        return resourceProvider.saveOrUpdate_p(resourceDTO);
    }

    @ApiOperation(value = "获取资源")
    @GetMapping(value = "/{id}")
    public RestResponse<ResourceDTO> getById(@PathVariable("id") Integer id) {
        return resourceProvider.getById_p(id);
    }

    @ApiOperation(value = "删除资源")
    @PostMapping(value = "/delete/{id}")
    public RestResponse<Boolean> delete(@PathVariable("id") Integer id) {
        return resourceProvider.delete_p(id);
    }

    @ApiOperation("按照条件分页查询")
    @PostMapping("/getResourcePages")
    public RestResponse<Page<ResourceDTO>> getResourcePages(@RequestBody ResourceQueryParam resourceQueryParam){
        return resourceProvider.getResourcePages_p(resourceQueryParam);
    }


    @ApiOperation(value = "获取所有资源")
    @GetMapping("/getAll")
    public RestResponse<List<ResourceDTO>> getAll() {
        return resourceProvider.getAll_p();
    }

    @GetMapping("/category/getAll")
    public RestResponse<List<ResourceCategoryDTO>> getCategories(@RequestParam(value = "resourceId", required = false) Integer resourceId) {
        RestResponse<List<ResourceCategoryDTO>> result = resourceProvider.getAllCategories_p();
        if (!result.isSuccess() || CollectionUtils.isEmpty(result.getData())) {
            return RestResponse.success(Lists.newArrayList());
        }
        List<ResourceCategoryDTO> categoryDTOList = result.getData();
        if (Objects.nonNull(resourceId)) {
            RestResponse<ResourceDTO> res = resourceProvider.getById_p(resourceId);
            if (res.isSuccess() && Objects.nonNull(res.getData())) {
                categoryDTOList.stream().forEach(category -> {
                    if (Objects.equals(category.getId(), res.getData().getCategoryId())) {
                        category.setSelected(true);
                    }
                });
            }
        }
        return RestResponse.success(categoryDTOList);
    }
    @ApiOperation(value = "保存或更新资源分类")
    @PostMapping("/category/saveOrderUpdate")
    public RestResponse<Boolean> saveOrUpdateCategory(@RequestBody ResourceCategoryForm resourceCategoryForm) {
        ResourceCategoryDTO resourceCategoryDTO = ConvertUtils.convert(resourceCategoryForm, ResourceCategoryDTO.class);
        if (Objects.isNull(resourceCategoryDTO.getId())) {
            resourceCategoryDTO.setCreatedBy(UserManager.getUserName());
            resourceCategoryDTO.setCreatedTime(new Date());
        }
        resourceCategoryDTO.setUpdatedBy(UserManager.getUserName());
        resourceCategoryDTO.setUpdatedTime(new Date());
        return resourceProvider.saveOrUpdateCategory_p(resourceCategoryDTO);
    }

    @ApiOperation(value = "删除资源分类，如果资源分类下有资源，不允许删除")
    @PostMapping("/category/delete/{id}")
    public RestResponse<Boolean> deleteById(@PathVariable("id") Integer id) {
        return resourceProvider.deleteCategoryById_p(id);
    }


    @ApiOperation(value = "给角色分配资源")
    @PostMapping("/allocateRoleResources")
    public RestResponse<Boolean> allocateRoleResources(@RequestBody AllocateRoleResourceForm allocateRoleResourceForm) {
        log.info("Allocate role resources, params:{}", allocateRoleResourceForm);
        if (Objects.isNull(allocateRoleResourceForm.getRoleId())) {
            return RestResponse.fail("角色ID不能为空");
        }
        AllocateRoleResourceDTO allocateRoleMenuDTO = ConvertUtils.convert(allocateRoleResourceForm, AllocateRoleResourceDTO.class);
        allocateRoleMenuDTO.setCreatedBy(UserManager.getUserName());
        allocateRoleMenuDTO.setUpdatedBy(UserManager.getUserName());
        return resourceProvider.allocateRoleResources_p(allocateRoleMenuDTO);
    }

    @ApiOperation(value = "获取角色拥有的资源列表", notes = "在给角色分配资源时，跳转到角色-资源列表页，并标记哪些资源已分配给该角色")
    @GetMapping("/getRoleResources/{roleId}")
    public RestResponse<List<ResourceCategoryNodeDTO>> getRoleResources(@PathVariable Integer roleId) {
        return resourceProvider.getRoleResources_p(roleId);
    }
}
