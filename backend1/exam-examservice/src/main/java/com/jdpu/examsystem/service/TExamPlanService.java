package com.jdpu.examsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jdpu.common.utils.PageUtils;
import com.jdpu.examsystem.entity.TExamPlanEntity;

import java.util.Map;

/**
 *
 *
 * @author zuck
 * @email ${email}
 * @date 2022-04-11 15:37:04
 */
public interface TExamPlanService extends IService<TExamPlanEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

