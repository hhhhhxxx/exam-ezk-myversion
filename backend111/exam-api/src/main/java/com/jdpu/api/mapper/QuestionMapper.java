package com.jdpu.api.mapper;

import com.jdpu.api.entities.Question;
import com.jdpu.common.param.admin.question.QuestionPageRequestVM;
import com.jdpu.common.xzsOld.entities.KeyValue;
import com.jdpu.common.xzsOld.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface QuestionMapper extends BaseMapper<Question> {

    List<Question> page(QuestionPageRequestVM requestVM);

    List<Question> selectByIds(@Param("ids") List<Integer> ids);

    Integer selectAllCount();

    List<KeyValue> selectCountByDate(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<Integer> selectIdsByDifficultAndSubjectId(Integer difficult, Integer subjectId);
}
