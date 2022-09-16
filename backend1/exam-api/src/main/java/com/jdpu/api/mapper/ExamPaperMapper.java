package com.jdpu.api.mapper;

import com.jdpu.api.entities.ExamPaper;
import com.jdpu.common.param.admin.exam.ExamPaperPageRequestVM;
import com.jdpu.common.param.student.dashboard.PaperFilter;
import com.jdpu.common.param.student.dashboard.PaperInfo;
import com.jdpu.common.param.student.exam.ExamPaperPageVM;
import com.jdpu.common.xzsOld.entities.KeyValue;
import com.jdpu.common.xzsOld.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface ExamPaperMapper extends BaseMapper<ExamPaper> {

    List<ExamPaper> page(ExamPaperPageRequestVM requestVM);

    List<ExamPaper> taskExamPage(ExamPaperPageRequestVM requestVM);

    List<ExamPaper> studentPage(ExamPaperPageVM requestVM);

    List<PaperInfo> indexPaper(PaperFilter paperFilter);

    Integer selectAllCount();

    List<KeyValue> selectCountByDate(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    int updateTaskPaper(@Param("taskId") Integer taskId,@Param("paperIds") List<Integer> paperIds);

    int clearTaskPaper(@Param("paperIds") List<Integer> paperIds);
}
