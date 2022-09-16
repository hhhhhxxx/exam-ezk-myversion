package com.jdpu.examsystem.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jdpu.examsystem.feign.exampaper.QuestionFeignService;
import com.jdpu.examsystem.service.TQuestionBankService;
import com.jdpu.examsystem.vo.dto.admin.question.QuestionEditRequestVM;
import com.jdpu.examsystem.entity.TQuestionBankRelationEntity;
import com.jdpu.examsystem.service.TQuestionBankRelationService;
import com.jdpu.common.entity.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jdpu.examsystem.entity.TQuestionBankEntity;
import com.jdpu.common.utils.PageUtils;
import com.jdpu.common.utils.R;



/**
 * 题库管理
 *
 * @author zuck
 * @email ${email}
 * @date 2022-04-10 15:26:22
 */
@RestController
@RequestMapping("/api/examsystem/tquestionbank")
public class TQuestionBankController {
    @Autowired
    private TQuestionBankService tQuestionBankService;
    @Autowired
    private TQuestionBankRelationService tQuestionBankRelationService;
    @Autowired
    private QuestionFeignService questionFeignService;

    /**
     * 根据题库id获取所有题目
     */
    @PostMapping("/getQuestionByBank")
    public R getQuestionByBank(@RequestBody String jsonStr){
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        String bankId = jsonObject.getStr("bankId");
        List<TQuestionBankRelationEntity> list = tQuestionBankRelationService.list(new QueryWrapper<TQuestionBankRelationEntity>().eq("bank_id", bankId));

        List<QuestionEditRequestVM> result = new ArrayList<>();
        list.forEach(relation -> {
            RestResponse<QuestionEditRequestVM> questionEditRequestVMRestResponse = questionFeignService.select(relation.getQuestionId());
            QuestionEditRequestVM questionEditRequestVM = questionEditRequestVMRestResponse.getResponse();
            result.add(questionEditRequestVM);
        });
        return R.ok().put("data",result);
    }

    /**
     * 题库中添加题目
     */
    @PostMapping("/addQuestion")
    public R addQuestion(@RequestBody String jsonStr){
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        String bankId = jsonObject.getStr("bankId");
        JSONArray questionIds = jsonObject.getJSONArray("questionIds");

        // 去重
        List<Integer> collect = questionIds.toList(Integer.class)
                .stream().distinct().collect(Collectors.toList());
        // 添加关联关系
        TQuestionBankRelationEntity relationEntity = new TQuestionBankRelationEntity();
        relationEntity.setBankId(Integer.valueOf(bankId));
        collect.forEach(questionId ->{
            // 先查看数据库是否存在重复数据
            TQuestionBankRelationEntity entity = tQuestionBankRelationService.getOne(
                    new QueryWrapper<TQuestionBankRelationEntity>().eq("question_id", questionId).eq("bank_id", bankId));
            if (entity != null) {
                return;
            }
            relationEntity.setQuestionId(questionId);
            tQuestionBankRelationService.save(relationEntity);
        });

        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
   // @RequiresPermissions("examsystem:tquestionbank:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = tQuestionBankService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
   // @RequiresPermissions("examsystem:tquestionbank:info")
    public R info(@PathVariable("id") Integer id){
		TQuestionBankEntity tQuestionBank = tQuestionBankService.getById(id);

        return R.ok().put("tQuestionBank", tQuestionBank);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("examsystem:tquestionbank:save")
    public R save(@RequestBody TQuestionBankEntity tQuestionBank){
		tQuestionBankService.save(tQuestionBank);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
   // @RequiresPermissions("examsystem:tquestionbank:update")
    public R update(@RequestBody TQuestionBankEntity tQuestionBank){
		tQuestionBankService.updateById(tQuestionBank);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
  //  @RequiresPermissions("examsystem:tquestionbank:delete")
    public R delete(@RequestBody Integer[] ids){
		tQuestionBankService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
