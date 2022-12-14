package com.jdpu.auth.service.impl;

import com.jdpu.auth.service.NewMvcRequestMatcher;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jdpu.common.dto.authority.AllocateRoleResourceDTO;
import com.jdpu.common.dto.authority.ResourceCategoryDTO;
import com.jdpu.common.dto.authority.ResourceCategoryNodeDTO;
import com.jdpu.common.dto.authority.ResourceDTO;
import com.jdpu.common.entity.authority.Resource;
import com.jdpu.common.entity.authority.ResourceCategory;
import com.jdpu.common.entity.authority.RoleResource;
import com.jdpu.common.entity.vo.RestResponse;
import com.jdpu.common.utils.ConvertUtils;
import com.jdpu.auth.mapper.ResourceMapper;
import com.jdpu.auth.param.ResourceQueryParam;
import com.jdpu.auth.service.IResourceCategoryService;
import com.jdpu.auth.service.IResourceService;
import com.jdpu.auth.service.IRoleResourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements IResourceService {

    @Autowired
    private HandlerMappingIntrospector mvcHandlerMappingIntrospector;

    @Autowired
    private IRoleResourceService roleResourceService;

    @Autowired
    private IResourceCategoryService resourceCategoryService;

    /**
     * ?????????????????????url????????????RequestMatcher?????????????????????????????????url
     */
    private static final Set<MvcRequestMatcher> resourceConfigAttributes = new HashSet<>();

    @Override
    public synchronized void loadResource() {
        Resource res = new Resource();
        res.setUrl("/boss/test/test");
        List<Resource> resources = this.baseMapper.selectList(new QueryWrapper<>());
        resources.add(res);
        resources.stream().forEach(resource -> resourceConfigAttributes.add(this.newMvcRequestMatcher(resource.getUrl())));
        log.info("init resourceConfigAttributes:{}", resourceConfigAttributes);
    }

    @Override
    public boolean matchRequestUrl(HttpServletRequest authRequest) {
        log.info("authRequest:uri:{},url:{}",authRequest.getRequestURI(),authRequest.getServletPath());
//        return  true;
        // ??????????????????url?????????true????????????method???
        log.info("resourceConfigAttributes:{}",resourceConfigAttributes);
        for (MvcRequestMatcher resourceConfigAttribute : resourceConfigAttributes) {
//            resourceConfigAttribute.matcher()
        }
        return resourceConfigAttributes.stream().filter(requestMatcher -> requestMatcher.matches(authRequest)).count() > 0;
    }

    @Override
    public List<Resource> queryByRoleIds(Set<Integer> roleIds) {
        return this.baseMapper.queryByRoleIds(roleIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdate(Resource resource) {
        boolean success = super.saveOrUpdate(resource);
        if (success) {
            // ????????????
            resourceConfigAttributes.add(this.newMvcRequestMatcher(resource.getUrl()));
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteWithAssociation(Integer id) {
        Resource resource = this.getById(id);
        roleResourceService.removeByResourceId(id);
        boolean success = this.removeById(id);
        if (success) {
            resourceConfigAttributes.remove(this.newMvcRequestMatcher(resource.getUrl()));
        }
        return success;
    }

    @Override
    public Page<Resource> getResourcePages(ResourceQueryParam resourceQueryParam) {
        Page<Resource> page = new Page<>(resourceQueryParam.getCurrent(), resourceQueryParam.getSize());
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper
                .eq(Objects.nonNull(resourceQueryParam.getId()), "id", resourceQueryParam.getId())
                .like(StringUtils.isNotBlank(resourceQueryParam.getName()), "name", resourceQueryParam.getName())
                .like(StringUtils.isNotBlank(resourceQueryParam.getUrl()), "url", resourceQueryParam.getUrl())
                .eq(Objects.nonNull(resourceQueryParam.getCategoryId()), "category_id", resourceQueryParam.getCategoryId())
                .ge(Objects.nonNull(resourceQueryParam.getStartCreateTime()), "created_time", resourceQueryParam.getStartCreateTime())
                .le(Objects.nonNull(resourceQueryParam.getEndCreateTime()), "created_time", resourceQueryParam.getEndCreateTime())
                .orderByDesc("id");

        return this.page(page, queryWrapper);
    }

    @Override
    public boolean matchUserResources(Set<Integer> roleIds, HttpServletRequest request) {
        loadResource();//????????????
        boolean existInResources = this.matchRequestUrl(request);
        if (!existInResources) {
            log.info("url???????????????????????????????????????: url:{}", request.getServletPath());
            return false;
        }
        List<Resource> resources = this.queryByRoleIds(roleIds);
        log.info("resources:{}",resources);
        for (Resource resource : resources) {
            NewMvcRequestMatcher matcher = this.newMvcRequestMatcher(resource.getUrl());
            if (matcher.matches(request)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Resource> getByCategoryId(Integer categoryId) {
        QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId);
        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void allocateRoleResources(AllocateRoleResourceDTO allocateRoleResourceDTO) {
        if (CollectionUtils.isEmpty(allocateRoleResourceDTO.getResourceIdList())) {
            // ??????????????????????????????
            allocateRoleResourceDTO.setResourceIdList(Lists.newArrayList());
        }
        // ??????????????????ID??????
        Set<Integer> roleResourceIds = roleResourceService.queryByRoleId(allocateRoleResourceDTO.getRoleId());
        // ???????????????????????????ID??????
        Set<Integer> allocateRoleResourceIds = Sets.newHashSet(allocateRoleResourceDTO.getResourceIdList());

        // ????????????????????????-????????????
        Set<Integer> needToDel = roleResourceIds.stream().filter(resourceId -> !allocateRoleResourceIds.contains(resourceId)).collect(Collectors.toSet());
        // ????????????????????????-????????????
        Set<Integer> needToInsert = allocateRoleResourceIds.stream().filter(resourceId -> !roleResourceIds.contains(resourceId)).collect(Collectors.toSet());

        if (CollectionUtils.isNotEmpty(needToDel)) {
            roleResourceService.removeByRoleIdAndResourceIds(allocateRoleResourceDTO.getRoleId(), needToDel);
        }

        if (CollectionUtils.isNotEmpty(needToInsert)) {
            List<RoleResource> roleResources = needToInsert.stream().map(resourceId -> {
                RoleResource roleResource = new RoleResource();
                roleResource.setRoleId(allocateRoleResourceDTO.getRoleId());
                roleResource.setResourceId(resourceId);
                roleResource.setCreatedBy(allocateRoleResourceDTO.getCreatedBy());
                roleResource.setUpdatedBy(allocateRoleResourceDTO.getUpdatedBy());
                roleResource.setCreatedTime(new Date());
                roleResource.setUpdatedTime(new Date());
                return roleResource;
            }).collect(Collectors.toList());
            roleResourceService.saveBatch(roleResources);
        }
    }

    // ?????????
    @Override
    public RestResponse<Boolean> saveOrUpdate_p(ResourceDTO resourceDTO) {
        Resource resource = ConvertUtils.convert(resourceDTO, Resource.class);
        return RestResponse.success(saveOrUpdate(resource));
    }

    @Override
    public RestResponse<ResourceDTO> getById_p(Integer id) {
        Resource resource = getById(id);
        return RestResponse.success(ConvertUtils.convert(resource, ResourceDTO.class));
    }

    @Override
    public RestResponse<Boolean> delete_p(Integer id) {
        return RestResponse.success(deleteWithAssociation(id));
    }

    @Override
    public RestResponse<Page<ResourceDTO>> getResourcePages_p(ResourceQueryParam resourceQueryParam) {
        Page<Resource> selectPage = getResourcePages(resourceQueryParam);
        Page<ResourceDTO> resourceDTOPage = new Page<>();
        BeanUtils.copyProperties(selectPage, resourceDTOPage);
        if (CollectionUtils.isNotEmpty(selectPage.getRecords())) {
            List<ResourceDTO> resourceDTOList = selectPage.getRecords().stream().map(
                    resource -> ConvertUtils.convert(resource, ResourceDTO.class)
            ).collect(Collectors.toList());
            resourceDTOPage.setRecords(resourceDTOList);
        }
        return RestResponse.success(resourceDTOPage);
    }

    @Override
    public RestResponse<List<ResourceDTO>> getAll_p() {
        List<Resource> resourceList = list();
        List<ResourceDTO> resourceDTOLists = ConvertUtils.convertList(resourceList, ResourceDTO.class);
        return RestResponse.success(resourceDTOLists);
    }

    @Override
    public RestResponse<List<ResourceCategoryDTO>> getAllCategories_p() {
        List<ResourceCategory> list = resourceCategoryService.list();
        return RestResponse.success(ConvertUtils.convertList(list, ResourceCategoryDTO.class));
    }

    @Override
    public RestResponse<Boolean> saveOrUpdateCategory_p(ResourceCategoryDTO resourceCategoryDTO) {
        ResourceCategory category = ConvertUtils.convert(resourceCategoryDTO, ResourceCategory.class);
        return RestResponse.success(resourceCategoryService.saveOrUpdate(category));
    }

    @Override
    public RestResponse<Boolean> deleteCategoryById_p(Integer id) {
        try {
            return RestResponse.success(resourceCategoryService.deleteById(id));
        } catch (Exception e) {
            return RestResponse.fail(e.getMessage());
        }
    }

    @Override
    public RestResponse<Boolean> allocateRoleResources_p(AllocateRoleResourceDTO allocateRoleMenuDTO) {
        return RestResponse.fail("?????????");
    }

    @Override
    public RestResponse<List<ResourceCategoryNodeDTO>> getRoleResources_p(Integer roleId) {
        List<ResourceCategory> categories = resourceCategoryService.list();
        if (CollectionUtils.isEmpty(categories)) {
            return RestResponse.success(Lists.newArrayList());
        }
        //??????????????????????????????ID??????
        Set<Integer> roleResourcesIds = roleResourceService.queryByRoleId(roleId);
        List<ResourceCategoryNodeDTO> categoryNodeDTOList = categories.stream().map(
                category -> buildResourceCategoryNode(category, roleResourcesIds)
        ).collect(Collectors.toList());

        return RestResponse.success(categoryNodeDTOList);
    }
    /**
     * ????????????????????????
     *
     * @param category
     * @param roleResourceIds
     * @return
     */
    private ResourceCategoryNodeDTO buildResourceCategoryNode(ResourceCategory category, Set<Integer> roleResourceIds) {
        ResourceCategoryNodeDTO categoryNodeDTO = ConvertUtils.convert(category, ResourceCategoryNodeDTO.class);
        // ????????????????????????????????????
        List<Resource> resources = getByCategoryId(category.getId());
        if (CollectionUtils.isEmpty(resources)) {
            return categoryNodeDTO;
        }

        List<ResourceDTO> resourceDTOList = resources.stream().map(resource -> {
            ResourceDTO resourceDTO = ConvertUtils.convert(resource, ResourceDTO.class);
            resourceDTO.setSelected(roleResourceIds.contains(resource.getId()));
            return resourceDTO;
        }).collect(Collectors.toList());

        categoryNodeDTO.setResourceList(resourceDTOList);
        categoryNodeDTO.setSelected(resourceDTOList.stream().filter(resourceDTO -> resourceDTO.isSelected()).count() > 0);
        return categoryNodeDTO;
    }

    /**
     * ??????RequestMatcher
     *
     * @param url
     * @return
     */
    private NewMvcRequestMatcher newMvcRequestMatcher(String url) {
        return new NewMvcRequestMatcher(mvcHandlerMappingIntrospector, url, null);
    }
}
