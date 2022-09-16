package com.jdpu.examsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jdpu.common.utils.PageUtils;
import com.jdpu.examsystem.entity.TExamNoticeEntity;
import com.jdpu.examsystem.vo.ExamNoticeVo;

import java.util.List;
import java.util.Map;

/**
 * 考试通知表
 *
 * @author zuck
 * @email ${email}
 * @date 2022-03-27 22:53:42
 */
public interface TExamNoticeService extends IService<TExamNoticeEntity> {

    PageUtils queryPage(Map<String, Object> params);

    boolean publishToUser(Integer argumentId, List<Integer> userIdList);

    List<ExamNoticeVo> getNoticeListByUserId(Integer userId);
}

