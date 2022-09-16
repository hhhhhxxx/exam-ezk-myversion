package com.jdpu.api.mapper;


import com.jdpu.api.entities.ExamPaperAnswer;
import com.jdpu.common.param.admin.paper.ExamPaperAnswerPageRequestVM;
import com.jdpu.common.param.student.exampaper.ExamPaperAnswerPageVM;
import com.jdpu.common.xzsOld.entities.KeyValue;
import com.jdpu.common.xzsOld.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
@Repository
public interface ExamPaperAnswerMapper extends BaseMapper<ExamPaperAnswer>, com.baomidou.mybatisplus.core.mapper.BaseMapper<ExamPaperAnswer> {

    List<ExamPaperAnswer> studentPage(ExamPaperAnswerPageVM requestVM);

    Integer selectAllCount();

    List<KeyValue> selectCountByDate(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    ExamPaperAnswer getByPidUid(@Param("pid") Integer paperId, @Param("uid") Integer uid);

    List<ExamPaperAnswer> adminPage(ExamPaperAnswerPageRequestVM requestVM);
}
