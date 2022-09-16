package com.jdpu.examsystem.controller;

import java.util.Arrays;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jdpu.examsystem.entity.TExamArguPlanRoomRelationEntity;
import com.jdpu.examsystem.service.TExamArguPlanRoomRelationService;
import com.jdpu.common.utils.PageUtils;
import com.jdpu.common.utils.R;



/**
 * 考试与考试计划、考场编排关系表
 *
 * @author zuck
 * @email ${email}
 * @date 2022-04-11 15:37:04
 */
@RestController
@RequestMapping("/api/examsystem/texamarguplanroomrelation")
public class TExamArguPlanRoomRelationController {
    @Autowired
    private TExamArguPlanRoomRelationService tExamArguPlanRoomRelationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
   // @RequiresPermissions("examsystem:texamarguplanroomrelation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = tExamArguPlanRoomRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
   // @RequiresPermissions("examsystem:texamarguplanroomrelation:info")
    public R info(@PathVariable("id") Integer id){
		TExamArguPlanRoomRelationEntity tExamArguPlanRoomRelation = tExamArguPlanRoomRelationService.getById(id);

        return R.ok().put("tExamArguPlanRoomRelation", tExamArguPlanRoomRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("examsystem:texamarguplanroomrelation:save")
    public R save(@RequestBody TExamArguPlanRoomRelationEntity tExamArguPlanRoomRelation){
		tExamArguPlanRoomRelationService.save(tExamArguPlanRoomRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
   // @RequiresPermissions("examsystem:texamarguplanroomrelation:update")
    public R update(@RequestBody TExamArguPlanRoomRelationEntity tExamArguPlanRoomRelation){
		tExamArguPlanRoomRelationService.updateById(tExamArguPlanRoomRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
  //  @RequiresPermissions("examsystem:texamarguplanroomrelation:delete")
    public R delete(@RequestBody Integer[] ids){
		tExamArguPlanRoomRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
