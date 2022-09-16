package com.jdpu.examsystem.controller;

import java.util.Arrays;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jdpu.examsystem.entity.TQuestionBankRelationEntity;
import com.jdpu.examsystem.service.TQuestionBankRelationService;
import com.jdpu.common.utils.PageUtils;
import com.jdpu.common.utils.R;



/**
 * 题库与题目关联表
 *
 * @author zuck
 * @email ${email}
 * @date 2022-04-10 15:49:42
 */
@RestController
@RequestMapping("/api/examsystem/tquestionbankrelation")
public class TQuestionBankRelationController {
    @Autowired
    private TQuestionBankRelationService tQuestionBankRelationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
   // @RequiresPermissions("examsystem:tquestionbankrelation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = tQuestionBankRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
   // @RequiresPermissions("examsystem:tquestionbankrelation:info")
    public R info(@PathVariable("id") Integer id){
		TQuestionBankRelationEntity tQuestionBankRelation = tQuestionBankRelationService.getById(id);

        return R.ok().put("tQuestionBankRelation", tQuestionBankRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("examsystem:tquestionbankrelation:save")
    public R save(@RequestBody TQuestionBankRelationEntity tQuestionBankRelation){
		tQuestionBankRelationService.save(tQuestionBankRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
   // @RequiresPermissions("examsystem:tquestionbankrelation:update")
    public R update(@RequestBody TQuestionBankRelationEntity tQuestionBankRelation){
		tQuestionBankRelationService.updateById(tQuestionBankRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
  //  @RequiresPermissions("examsystem:tquestionbankrelation:delete")
    public R delete(@RequestBody Integer[] ids){
		tQuestionBankRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
}
