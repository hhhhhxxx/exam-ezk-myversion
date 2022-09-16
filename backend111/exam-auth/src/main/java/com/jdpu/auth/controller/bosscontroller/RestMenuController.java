package com.jdpu.auth.controller.bosscontroller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import com.jdpu.common.dto.authority.AllocateRoleMenuDTO;
import com.jdpu.common.dto.authority.MenuDTO;
import com.jdpu.common.dto.authority.MenuNodeDTO;
import com.jdpu.common.entity.vo.RestResponse;
import com.jdpu.common.utils.ConvertUtils;
import com.jdpu.auth.entiy.UserManager;
import com.jdpu.auth.entiy.form.AllocateRoleMenuForm;
import com.jdpu.auth.entiy.form.MenuForm;
import com.jdpu.auth.param.MenuQueryParam;
import com.jdpu.auth.service.IMenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: xJh
 * @Date: 2022/3/17
 */
@RestController
@RequestMapping("/boss/menu")
@Slf4j
public class RestMenuController {

    @Autowired
    private IMenuService menuProvider;

    @ApiOperation(value = "保存或者新增菜单")
    @PostMapping(value="/saveOrUpdate")
    public RestResponse<Boolean> saveOrUpdate(@RequestBody MenuForm menuForm){
        MenuDTO menuDto = ConvertUtils.convert(menuForm, MenuDTO.class);
        if (Objects.isNull(menuDto.getId())){
            //新增
            menuDto.setCreatedBy(UserManager.getUserName());
            menuDto.setCreatedTime(new Date());
        }
        menuDto.setUpdatedBy(UserManager.getUserName());
        return menuProvider.saveOrUpdate_p(menuDto);
    }

    @GetMapping("/getAll")
    public RestResponse<List<MenuDTO>> getAllMenu()
    {
        RestResponse<List<MenuDTO>> all = menuProvider.getAll_p();
        return all;
    }

    @ApiOperation("根据菜单id查询菜单")
    @GetMapping("/{id}")
    public RestResponse<MenuDTO> getMenuById(@PathVariable("id") Integer id){
        RestResponse<MenuDTO> menuDto = menuProvider.getById_p(id);
        return menuDto;
    }

    @ApiOperation("根据id删除菜单")
    @PostMapping("{id}")
    public RestResponse<Boolean> deleteMenuById(@PathVariable("id")Integer id){
        RestResponse<Boolean> booleanResult = menuProvider.deleteById_p(id);
        return booleanResult;
    }

    @ApiOperation("按照条件分页查询菜单")
    @PostMapping("/getMenuPages")
    public RestResponse<Page<MenuDTO>> getMenuPages(@RequestBody MenuQueryParam menuQueryParam){
        RestResponse<Page<MenuDTO>> menuPages = menuProvider.getMenuPages_p(menuQueryParam);
        return menuPages;
    }

    // @ApiOperation("/是否显示开关")
    // @GetMapping("/switchShown")
    // public Result<Boolean> switchShown(@RequestParam("id") Integer id) {
    //     MenuDTO menuDTO = new MenuDTO();
    //     menuDTO.setId(id);
    //     menuDTO.setUpdatedBy(UserManager.getUserName());
    //     menuDTO.setUpdatedTime(new Date());
    //     return menuProvider.switchShown_p(menuDTO);
    // }

    @ApiOperation("获取所有菜单并按层级展示")
    @GetMapping("/getMenuNodeList")
    public RestResponse<List<MenuNodeDTO>> getMenuNodeList() {
        return menuProvider.getMenuNodeList_p();
    }


    @ApiOperation("获取编辑菜单页面信息")
    @ApiImplicitParams({
            @ApiImplicitParam(type = "form", name = "id", value = "菜单ID", paramType = "String", required = true)
    })
    @GetMapping("/getEditMenuInfo/{id}")
    public RestResponse<Map> getEditMenuInfo(@PathVariable("id") Integer id) {
        Map<String, Object> resultMap = new HashMap<>();
        MenuDTO menuDTO = null;
        List<MenuNodeDTO> menuNodeDTOList = Lists.newArrayList();
        RestResponse<List<MenuNodeDTO>> menuNodeListResult = menuProvider.getMenuNodeList_p();
        RestResponse<MenuDTO> menuResult = menuProvider.getById_p(id);
        if (menuNodeListResult.isOk() && CollectionUtils.isNotEmpty(menuNodeListResult.getResponse())) {
            menuNodeDTOList = menuNodeListResult.getResponse();
        }

        if (menuResult.isOk() && Objects.nonNull(menuResult.getResponse())) {
            menuDTO = menuResult.getResponse();
        }

        if (Objects.nonNull(menuDTO)) {
            Integer parentId = menuDTO.getParentId();
            menuNodeDTOList.stream().forEach(menuNodeDTO -> setSelectedFlag(parentId, menuNodeDTO));
        }

        resultMap.put("menuInfo", menuDTO);
        resultMap.put("parentMenuList", menuNodeDTOList);
        return RestResponse.ok(resultMap);
    }
    /**
     * 选中当前菜单的上级菜单，置为选中状态
     *
     * @param parentId    当前菜单的父级菜单ID
     * @param menuNodeDTO 所有菜单列表
     */
    private void setSelectedFlag(Integer parentId, MenuNodeDTO menuNodeDTO) {
        if (Objects.equals(menuNodeDTO.getId(), parentId)) {
            menuNodeDTO.setSelected(true);
            return;
        }
        if (CollectionUtils.isEmpty(menuNodeDTO.getSubMenuList())) {
            return;
        }
        menuNodeDTO.getSubMenuList().stream().forEach(subMenuNode -> setSelectedFlag(parentId, subMenuNode));
    }

    @ApiOperation(value = "获取角色拥有的菜单列表",notes = "在给角色分配菜单时，跳转到角色-菜单列表页，并标记哪些菜单已分配给该角色")
    @GetMapping("/getRoleMenus/{roleId}")
    public RestResponse<List<MenuNodeDTO>> getRoleMenus(@PathVariable Integer roleId){
        //1.要获取到所有的菜单
        RestResponse<List<MenuDTO>> menusResult = menuProvider.getByRoleId_p(roleId);
        RestResponse<List<MenuNodeDTO>> menuNodeListResult = menuProvider.getMenuNodeList_p();

        List<MenuDTO> roleMenus = Lists.newArrayList();
        if (menusResult.isOk() && CollectionUtils.isNotEmpty(menusResult.getResponse())) {
            roleMenus = menusResult.getResponse();
        }

        List<MenuNodeDTO> menuNodes = Lists.newArrayList();
        if (menuNodeListResult.isOk() && CollectionUtils.isNotEmpty(menuNodeListResult.getResponse())) {
            menuNodes = menuNodeListResult.getResponse();
        }

        if (CollectionUtils.isEmpty(menuNodes)) {
            return RestResponse.fail("未查询到菜单列表");
        }

        final Set<Integer> roleMenuIds = roleMenus.stream().map(role -> role.getId()).collect(Collectors.toSet());
        menuNodes.stream().forEach(menuNodeDTO -> setSelectedFlag(menuNodeDTO, roleMenuIds));

        return RestResponse.ok(menuNodes);
    }
    private void setSelectedFlag(MenuNodeDTO menuNodeDTO, Set<Integer> roleMenuIds) {
        if (roleMenuIds.contains(menuNodeDTO.getId())) {
            menuNodeDTO.setSelected(true);
        }
        if (CollectionUtils.isEmpty(menuNodeDTO.getSubMenuList())) {
            return;
        }
        menuNodeDTO.getSubMenuList().stream().forEach(subMenuNode -> setSelectedFlag(subMenuNode, roleMenuIds));
    }

    @ApiOperation(value = "给角色分配菜单")
    @PostMapping("/allocateRoleMenus")
    public RestResponse<Boolean> allocateRoleMenus(@RequestBody AllocateRoleMenuForm allocateRoleMenuForm) {

        log.info("Allocate role menus, params:{}", allocateRoleMenuForm);
        if (Objects.isNull(allocateRoleMenuForm.getRoleId())) {
            return RestResponse.fail("角色ID不能为空");
        }
        AllocateRoleMenuDTO allocateRoleMenuDTO = ConvertUtils.convert(allocateRoleMenuForm, AllocateRoleMenuDTO.class);
        allocateRoleMenuDTO.setCreatedBy(UserManager.getUserName());
        allocateRoleMenuDTO.setUpdatedBy(UserManager.getUserName());

        return menuProvider.allocateRoleMenus_p(allocateRoleMenuDTO);
    }
}
