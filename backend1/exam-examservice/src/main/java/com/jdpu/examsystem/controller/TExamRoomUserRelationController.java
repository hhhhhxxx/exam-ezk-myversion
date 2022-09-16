package com.jdpu.examsystem.controller;

import java.util.Arrays;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jdpu.examsystem.entity.TExamRoomUserRelationEntity;
import com.jdpu.examsystem.service.TExamRoomUserRelationService;
import com.jdpu.common.utils.PageUtils;
import com.jdpu.common.utils.R;



/**
 * 考场与用户关联表
 *
 * @author zuck
 * @email ${email}
 * @date 2022-04-11 15:44:48
 */
@RestController
@RequestMapping("/api/examsystem/texamroomuserrelation")
public class TExamRoomUserRelationController {
    @Autowired
    private TExamRoomUserRelationService tExamRoomUserRelationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
   // @RequiresPermissions("examsystem:texamroomuserrelation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = tExamRoomUserRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
   // @RequiresPermissions("examsystem:texamroomuserrelation:info")
    public R info(@PathVariable("id") Integer id){
		TExamRoomUserRelationEntity tExamRoomUserRelation = tExamRoomUserRelationService.getById(id);

        return R.ok().put("tExamRoomUserRelation", tExamRoomUserRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("examsystem:texamroomuserrelation:save")
    public R save(@RequestBody TExamRoomUserRelationEntity tExamRoomUserRelation){
		tExamRoomUserRelationService.save(tExamRoomUserRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
   // @RequiresPermissions("examsystem:texamroomuserrelation:update")
    public R update(@RequestBody TExamRoomUserRelationEntity tExamRoomUserRelation){
		tExamRoomUserRelationService.updateById(tExamRoomUserRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
  //  @RequiresPermissions("examsystem:texamroomuserrelation:delete")
    public R delete(@RequestBody Integer[] ids){
		tExamRoomUserRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
