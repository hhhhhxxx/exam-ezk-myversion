package com.jdpu.examsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jdpu.common.utils.PageUtils;
import com.jdpu.examsystem.entity.TExamRoomUserRelationEntity;

import java.util.Map;

/**
 *
 *
 * @author zuck
 * @email ${email}
 * @date 2022-04-11 15:44:48
 */
public interface TExamRoomUserRelationService extends IService<TExamRoomUserRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

