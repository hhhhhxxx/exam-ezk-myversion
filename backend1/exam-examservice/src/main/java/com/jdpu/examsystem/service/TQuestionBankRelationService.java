package com.jdpu.examsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jdpu.common.utils.PageUtils;
import com.jdpu.examsystem.entity.TQuestionBankRelationEntity;

import java.util.Map;

/**
 *
 *
 * @author zuck
 * @email ${email}
 * @date 2022-04-10 15:49:42
 */
public interface TQuestionBankRelationService extends IService<TQuestionBankRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

