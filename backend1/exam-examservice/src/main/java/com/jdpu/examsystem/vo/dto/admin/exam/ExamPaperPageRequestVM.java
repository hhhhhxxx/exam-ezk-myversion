package com.jdpu.examsystem.vo.dto.admin.exam;


import com.jdpu.common.xzsOld.base.BasePage;
import lombok.Data;

@Data
public class ExamPaperPageRequestVM extends BasePage {
    private Integer id;
    private Integer subjectId;
    private Integer level;
    private Integer paperType;
    private Integer taskExamId;
}
