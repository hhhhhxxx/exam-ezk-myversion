package com.jdpu.api.service;

import com.jdpu.api.entities.count.AverageScoreAndTime;

import java.util.List;

/**
 * @Author: xJh
 * @Date: 2022/3/24
 * 学生统计模块接口
 */
public interface StudentCountService {

    //统计学科平均分，平均作答时间(因为没有记录考试时间)
    public AverageScoreAndTime GetAverageScoreAndTime(Integer subjectId, Integer userId);

    //根据userId所有学科的平均分以及评分作答时间
    public List<AverageScoreAndTime> GetAllAverageScoreAndTime(Integer userId);

    //统计已完成的试卷数量
    public Integer FinishedPaper(Integer userId);
}
