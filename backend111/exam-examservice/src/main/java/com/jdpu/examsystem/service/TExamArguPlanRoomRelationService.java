package com.jdpu.examsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jdpu.common.utils.PageUtils;
import com.jdpu.examsystem.entity.TExamArguPlanRoomRelationEntity;

import java.util.Map;

/**
 *
 *
 * @author zuck
 * @email ${email}
 * @date 2022-04-11 15:37:04
 */
public interface TExamArguPlanRoomRelationService extends IService<TExamArguPlanRoomRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

