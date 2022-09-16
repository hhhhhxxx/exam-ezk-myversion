package com.jdpu.auth.service.impl;

import com.jdpu.auth.controller.HttpServletRequestAuthWrapper;
import com.jdpu.auth.service.IAuthenticationService;
import com.jdpu.auth.service.IMenuService;
import com.jdpu.auth.service.IResourceService;
import com.jdpu.auth.service.IUserRoleService;
import com.google.common.collect.Lists;
import com.jdpu.common.dto.authority.MenuNodeDTO;
import com.jdpu.common.dto.authority.PermissionDTO;
import com.jdpu.common.dto.authority.ResourceDTO;
import com.jdpu.common.entity.authority.Menu;
import com.jdpu.common.entity.authority.Resource;
import com.jdpu.common.entity.vo.RestResponse;
import com.jdpu.auth.contant.PubContants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthenticationServiceImpl implements IAuthenticationService {

    /**
     * 未在资源库中的URL默认标识
     */
    public static final String NONEXISTENT_URL = "NONEXISTENT_URL";

    @Autowired
    private IResourceService resourceService;
    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private IMenuService menuService;

    @Override
    public boolean decide(String userId, HttpServletRequest request) {
        log.info("Access user:{}, url:{}, method:{}", userId, request.getServletPath(), request.getMethod());
        // 用户是否有某些角色
        Set<Integer> roleIds = userRoleService.queryByUserId(Integer.parseInt(userId));
        log.info("roleIds：{}", roleIds);
        if (CollectionUtils.isEmpty(roleIds)) {
            return false;
        }
        // 用户是否有某些菜单权限
        List<Menu> menus = menuService.queryByRoleIds(roleIds);
        String requestURI = request.getRequestURI();
        log.info("requestURI:{}", requestURI);
        log.info("Menus:{}", menus);
        log.info("ServletPath:{}", request.getServletPath());
        if (hasMenuPermission(menus, request.getServletPath())) {
            return true;
        }
        // 用户是否有某些资源的权限
        return resourceService.matchUserResources(roleIds, request);
    }

    /**
     * 转换资源数据
     *
     * @param roleIds
     * @return
     */
    private List<ResourceDTO> convertResources(Set<Integer> roleIds) {
        List<Resource> list = resourceService.queryByRoleIds(roleIds);
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        }
        List<ResourceDTO> resourceList = Lists.newArrayList();
        list.stream().forEach(r -> {
            ResourceDTO resourceDTO = new ResourceDTO();
            BeanUtils.copyProperties(r, resourceDTO);
            resourceList.add(resourceDTO);
        });
        return resourceList;
    }

    @Override
    public PermissionDTO listUserPermission(Integer userId) {
        Set<Integer> roleIds = userRoleService.queryByUserId(userId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return new PermissionDTO(Lists.newArrayList(), Lists.newArrayList());
        }
        List<MenuNodeDTO> menuList = convertMenus(roleIds);
        Collections.sort(menuList);
        List<ResourceDTO> resourceList = convertResources(roleIds);
        return new PermissionDTO(menuList, resourceList);
    }

    @Override
    public RestResponse<PermissionDTO> listUserPermission_p(Integer userId) {
        log.info("List user permission.userId:{}", userId);
        PermissionDTO permissionDTO = listUserPermission(userId);
        return RestResponse.success(permissionDTO);
    }

    @Override
    public RestResponse permission_p(String userId, String url, String method, HttpServletRequest request) {
        boolean decide = decide(userId, new HttpServletRequestAuthWrapper(request, url, method));
        // boolean decide = decide(userId, request);
        log.info("Auth permission. userId:{}, url:{}, method:{}, permission:{}", userId, url, method, decide);
        return RestResponse.success(decide);
    }

    /**
     * 转换菜单数据，找出每个菜单的子菜单列表
     *
     * @param roleIds
     * @return
     */
    private List<MenuNodeDTO> convertMenus(Set<Integer> roleIds) {
        List<Menu> menus = menuService.queryByRoleIds(roleIds);
        if (CollectionUtils.isEmpty(menus)) {
            return Lists.newArrayList();
        }
        List<Menu> topLevelMenus = menus.stream()
                .filter(menu -> Objects.equals(menu.getLevel(), PubContants.MENU_TOP_LEVEL))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(topLevelMenus)) {
            return Lists.newArrayList();
        }
        return topLevelMenus.stream().map(menu -> menuService.fillMenuNode(menu)).collect(Collectors.toList());
    }

    /**
     * 判断用户是否有某菜单的权限，通过对比菜单href和当前url
     *
     * @param menus
     * @param url
     * @return
     */
    private boolean hasMenuPermission(List<Menu> menus, String url) {
        if (CollectionUtils.isEmpty(menus)) {
            return false;
        }
        for (Menu menu : menus) {
            if (StringUtils.isNotBlank(menu.getHref()) && StringUtils.startsWith(url, menu.getHref())) {
                return true;
            }
        }
        return false;
    }

}
