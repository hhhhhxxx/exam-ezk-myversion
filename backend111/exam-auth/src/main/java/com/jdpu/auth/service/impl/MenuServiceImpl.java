package com.jdpu.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jdpu.common.dto.authority.AllocateRoleMenuDTO;
import com.jdpu.common.dto.authority.MenuDTO;
import com.jdpu.common.dto.authority.MenuNodeDTO;
import com.jdpu.common.entity.authority.Menu;
import com.jdpu.common.entity.authority.RoleMenu;
import com.jdpu.common.entity.vo.RestResponse;
import com.jdpu.common.utils.ConvertUtils;
import com.jdpu.auth.contant.PubContants;
import com.jdpu.auth.mapper.MenuMapper;
import com.jdpu.auth.param.MenuQueryParam;
import com.jdpu.auth.service.IMenuService;
import com.jdpu.auth.service.IRoleMenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private IRoleMenuService roleMenuService;

    @Override
    public List<Menu> queryByParentId(Integer id) {
        return this.list(new QueryWrapper<Menu>().eq("parent_id", id));
    }

    @Override
    public List<Menu> queryByIds(List<Integer> ids) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        return this.list(queryWrapper);
    }

    @Override
    public List<Menu> queryByRoleIds(Set<Integer> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Lists.newArrayList();
        }
        return menuMapper.queryByRoleIds(roleIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteWithAssociation(Integer id) {
        deleteMenuCascade(id);
        return true;
    }

    /**
     * 级联删除所有子菜单，及子菜单绑定的角色关系
     *
     * @param id
     */
    private void deleteMenuCascade(Integer id) {
        List<Menu> menus = this.queryByParentId(id);
        if (CollectionUtils.isNotEmpty(menus)) {
            menus.stream().forEach(menu -> deleteMenuCascade(menu.getId()));
        }
        roleMenuService.removeByMenuId(id);
        this.removeById(id);
    }

    @Override
    public Page<Menu> getMenuPages(MenuQueryParam menuQueryParam) {
        Page<Menu> page = new Page<>(menuQueryParam.getCurrent(), menuQueryParam.getSize());
        QueryWrapper<Menu> queryWrapper = new QueryWrapper();
        // 判断是否要查询子菜单
        if (Objects.nonNull(menuQueryParam.getId()) && menuQueryParam.isQuerySubLevel()) {
            queryWrapper.eq("parent_id", menuQueryParam.getId());
        } else if (Objects.nonNull(menuQueryParam.getId()) && !menuQueryParam.isQuerySubLevel()) {
            queryWrapper.eq("id", menuQueryParam.getId());
        }
        queryWrapper
                .like(StringUtils.isNotBlank(menuQueryParam.getName()), "name", menuQueryParam.getName())
                .eq(Objects.nonNull(menuQueryParam.getShown()), "shown", menuQueryParam.getShown())
                .ge(Objects.nonNull(menuQueryParam.getStartCreateTime()), "created_time", menuQueryParam.getStartCreateTime())
                .le(Objects.nonNull(menuQueryParam.getEndCreateTime()), "created_time", menuQueryParam.getEndCreateTime())
                .orderByDesc("id");



        return this.page(page, queryWrapper);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean switchShown(Menu menu) {
        Menu dbMenu = this.getById(menu.getId());
        if (Objects.isNull(dbMenu)) {
            return false;
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.set("shown", !dbMenu.isShown());
        updateWrapper.set("updated_by", menu.getUpdatedBy());
        updateWrapper.set("updated_time", menu.getUpdatedTime());
        updateWrapper.eq("id", menu.getId());
        return this.update(updateWrapper);
    }

    @Override
    public List<MenuNodeDTO> getMenuNodeList() {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper();
        queryWrapper.eq("level", PubContants.MENU_TOP_LEVEL);
        List<Menu> topLevelMenuList = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(topLevelMenuList)) {
            return Lists.newArrayList();
        }
        List<MenuNodeDTO> menuNodeList = topLevelMenuList.stream().map(menu -> fillMenuNode(menu)).collect(Collectors.toList());
        Collections.sort(menuNodeList);
        return menuNodeList;
    }

    /**
     * 查询子菜单
     *
     * @param menu
     */
    @Override
    public MenuNodeDTO fillMenuNode(Menu menu) {
        MenuNodeDTO menuNodeDTO = ConvertUtils.convert(menu, MenuNodeDTO.class);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id", menuNodeDTO.getId());
        List<Menu> list = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return menuNodeDTO;
        }
        List<MenuNodeDTO> subMenuList = list.stream().map(subMenu -> fillMenuNode(subMenu)).collect(Collectors.toList());
        Collections.sort(subMenuList);
        menuNodeDTO.setSubMenuList(subMenuList);
        return menuNodeDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void allocateRoleMenus(AllocateRoleMenuDTO allocateRoleMenuDTO) {
        if (CollectionUtils.isEmpty(allocateRoleMenuDTO.getMenuIdList())) {
            // 表示删除所有角色菜单
            allocateRoleMenuDTO.setMenuIdList(Lists.newArrayList());
        }
        // 角色已拥有的菜单
        Set<Integer> roleMenuIds = roleMenuService.queryByRoleIds(Sets.newHashSet(allocateRoleMenuDTO.getRoleId()));
        // 准备分配给角色的菜单，排除掉 -1(-1是不存在的id，是顶级菜单的父ID)
        Set<Integer> allocateRoleMenuIds = allocateRoleMenuDTO.getMenuIdList().stream()
                .filter(menuId -> !Objects.equals(menuId, PubContants.MENU_TOP_LEVEL_PARENT_ID)).collect(Collectors.toSet());

        // 找出本次删除的
        Set<Integer> needToDelMenus = roleMenuIds.stream().filter(id -> !allocateRoleMenuIds.contains(id)).collect(Collectors.toSet());
        // 找出本次新增的
        Set<Integer> needToInsertMenus = allocateRoleMenuIds.stream().filter(id -> !roleMenuIds.contains(id)).collect(Collectors.toSet());

        if (CollectionUtils.isNotEmpty(needToDelMenus)) {
            roleMenuService.removeByRoleIdAndMenuIds(allocateRoleMenuDTO.getRoleId(), needToDelMenus);
        }

        if (CollectionUtils.isNotEmpty(needToInsertMenus)) {
            List<RoleMenu> roleMenus = needToInsertMenus.stream().map(menuId -> {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(allocateRoleMenuDTO.getRoleId());
                roleMenu.setMenuId(menuId);
                roleMenu.setCreatedBy(allocateRoleMenuDTO.getCreatedBy());
                roleMenu.setUpdatedBy(allocateRoleMenuDTO.getUpdatedBy());
                roleMenu.setCreatedTime(new Date());
                roleMenu.setUpdatedTime(new Date());
                return roleMenu;
            }).collect(Collectors.toList());
            roleMenuService.saveBatch(roleMenus);
        }
    }

    @Override
    public List<Menu> getByRoleIdIgnoreIsShown(Integer roleId) {
        return menuMapper.queryByRoleIdIgnoreIsShown(roleId);
    }


    @Override
    public RestResponse<Boolean> saveOrUpdate_p(MenuDTO menuDto) {
        Menu menu = ConvertUtils.convert(menuDto, Menu.class);
        menu.setUpdatedTime(new Date());
        if (menu.getParentId()==-1){
            menu.setLevel(0);
        }else {
            Menu parentMenu = getById(menu.getParentId());
            menu.setLevel(parentMenu.getLevel()+1);
        }


        return RestResponse.ok(saveOrUpdate(menu));
    }

    @Override
    public RestResponse<List<MenuDTO>> getAll_p() {
        return RestResponse.ok(ConvertUtils.convertList(list(),MenuDTO.class));
    }

    @Override
    public RestResponse<MenuDTO> getById_p(Integer id) {
        MenuDTO menuDto = ConvertUtils.convert(getById(id), MenuDTO.class);
        return RestResponse.ok(menuDto);
    }

    @Override
    public RestResponse<Boolean> deleteById_p(Integer id) {
        boolean flag = deleteWithAssociation(id);
        return RestResponse.ok(flag);
    }

    @Override
    public RestResponse<Page<MenuDTO>> getMenuPages_p(MenuQueryParam menuQueryParam) {
        log.info("Get MenuPages with params:{}",menuQueryParam);

        Page<Menu> menuPages = getMenuPages(menuQueryParam);
        Page<MenuDTO> menuDTOPage = new Page<>();
        //进行对象属性的拷贝
        BeanUtils.copyProperties(menuPages,menuDTOPage);
        if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty(menuPages.getRecords())){
            List<MenuDTO> listMenuDto = menuPages.getRecords().stream().map(menu -> ConvertUtils.convert(menu, MenuDTO.class)).collect(Collectors.toList());
            menuDTOPage.setRecords(listMenuDto);
        }
        return RestResponse.ok(menuDTOPage);
    }

    @Override
    public RestResponse<Boolean> switchShown_p(MenuDTO menuDTO) {
        return RestResponse.fail("没实现");
    }

    @Override
    public RestResponse<List<MenuNodeDTO>> getMenuNodeList_p() {
        return RestResponse.ok(getMenuNodeList());
    }

    @Override
    public RestResponse<List<MenuDTO>> getByRoleId_p(Integer roleId) {
        return RestResponse.ok(ConvertUtils.convertList(getByRoleIdIgnoreIsShown(roleId),MenuDTO.class));
    }

    @Override
    public RestResponse<Boolean> allocateRoleMenus_p(AllocateRoleMenuDTO allocateRoleMenuDTO) {
        log.info("Allocate role menus with params:{}", allocateRoleMenuDTO);
        allocateRoleMenus(allocateRoleMenuDTO);
        return RestResponse.ok(Boolean.TRUE);
    }

}
