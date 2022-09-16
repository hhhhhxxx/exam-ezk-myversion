package com.jdpu.examsystem.controller;

import java.util.Arrays;
import java.util.Map;


import com.jdpu.examsystem.service.TExamPlanService;
import com.jdpu.examsystem.service.TExamRoomService;
import com.jdpu.examsystem.service.TExamArguPlanRoomRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jdpu.examsystem.entity.TExamPlanEntity;
import com.jdpu.common.utils.PageUtils;
import com.jdpu.common.utils.R;



/**
 * 考试计划表
 *
 * @author zuck
 * @email ${email}
 * @date 2022-04-11 15:37:04
 */
@RestController
@RequestMapping("/api/examsystem/texamplan")
public class TExamPlanController {
    @Autowired
    private TExamPlanService tExamPlanService;
    @Autowired
    private TExamRoomService tExamRoomService;
    @Autowired
    private TExamArguPlanRoomRelationService examArguPlanRoomRelationService;



    /**
     * 列表
     */
    @RequestMapping("/list")
   // @RequiresPermissions("examsystem:texamplan:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = tExamPlanService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
   // @RequiresPermissions("examsystem:texamplan:info")
    public R info(@PathVariable("id") Integer id){
		TExamPlanEntity tExamPlan = tExamPlanService.getById(id);

        return R.ok().put("tExamPlan", tExamPlan);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("examsystem:texamplan:save")
    public R save(@RequestBody TExamPlanEntity tExamPlan){
		tExamPlanService.save(tExamPlan);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
   // @RequiresPermissions("examsystem:texamplan:update")
    public R update(@RequestBody TExamPlanEntity tExamPlan){
		tExamPlanService.updateById(tExamPlan);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
  //  @RequiresPermissions("examsystem:texamplan:delete")
    public R delete(@RequestBody Integer[] ids){
		tExamPlanService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
