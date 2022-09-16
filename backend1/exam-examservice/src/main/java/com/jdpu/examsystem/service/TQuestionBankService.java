package com.jdpu.examsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jdpu.common.utils.PageUtils;
import com.jdpu.examsystem.entity.TQuestionBankEntity;

import java.util.Map;

/**
 *
 *
 * @author zuck
 * @email ${email}
 * @date 2022-04-10 15:26:22
 */
public interface TQuestionBankService extends IService<TQuestionBankEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

