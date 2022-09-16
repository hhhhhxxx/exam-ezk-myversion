package com.jdpu.api.service.impl;

/**
 * @Author: xJh
 * @Date: 2022/3/24
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jdpu.api.entities.ExamPaperAnswer;
import com.jdpu.api.entities.count.AverageScoreAndTime;
import com.jdpu.api.mapper.ExamPaperAnswerMapper;
import com.jdpu.api.service.StudentCountService;
import com.jdpu.api.service.SubjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class StudentCountServiceImpl implements StudentCountService {

    @Autowired
    private SubjectService subjectService;
    @Autowired
    private ExamPaperAnswerMapper examPaperAnswerMapper;

    @Override
    public AverageScoreAndTime GetAverageScoreAndTime(Integer subjectId, Integer userId) {
        QueryWrapper<ExamPaperAnswer> queryWrapper = new QueryWrapper<>();
        List<ExamPaperAnswer> list = examPaperAnswerMapper.selectList(queryWrapper.eq("subject_id", subjectId).eq("create_user",userId));
        Double AverageScore=0.0;
        for (ExamPaperAnswer examPaperAnswer : list) {
            AverageScore=examPaperAnswer.getSystemScore()+AverageScore;

        }
        AverageScore=AverageScore/list.size();//获取平均分
        String score = String.format("%.2f", AverageScore);
        log.info("format:{}",score);
        AverageScoreAndTime averageScoreAndTime = new AverageScoreAndTime();
        averageScoreAndTime.setAverageScore(score);
        return averageScoreAndTime;
    }

    @Override
    public List<AverageScoreAndTime> GetAllAverageScoreAndTime(Integer userId) {
        QueryWrapper<ExamPaperAnswer> queryWrapper = new QueryWrapper<>();
        List<AverageScoreAndTime> list=new ArrayList<>();
        AverageScoreAndTime averageScoreAndTime=null;
        List<ExamPaperAnswer> examPaperAnswers = examPaperAnswerMapper.selectList(queryWrapper.eq("create_user", userId));
        for (ExamPaperAnswer examPaperAnswer : examPaperAnswers) {
            Integer subjectId = examPaperAnswer.getSubjectId();
            averageScoreAndTime = GetAverageScoreAndTime(subjectId, userId);
            list.add(averageScoreAndTime);
        }
        return list;
    }

    @Override
    public Integer FinishedPaper(Integer userId) {
        Integer count = examPaperAnswerMapper.selectCount(new QueryWrapper<ExamPaperAnswer>().eq("create_user", userId));
        return count;
    }
}
