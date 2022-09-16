package com.jdpu.examsystem.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jdpu.common.utils.PageUtils;
import com.jdpu.examsystem.entity.TExamInfoEntity;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author zuck
 * @email ${email}
 * @date 2022-03-23 16:43:28
 */
public interface TExamInfoService extends IService<TExamInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Integer insertByStudent(JSONObject jsonObject);

    boolean updateExitFull(JSONObject jsonObject);

    boolean updateOverNum(JSONObject jsonObject);

    boolean updateNonLiveCount(JSONObject jsonObject);

    boolean updateBeforeImg(JSONObject jsonObject);

    List<TExamInfoEntity> getExamInfoList(JSONObject jsonObject);

    boolean updateCheckSex(JSONObject jsonObject);

    boolean getIsEnterExam(Integer paperId, String stuName);
}

