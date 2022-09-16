package com.jdpu.common.param.admin.exam.autopaper;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 自动组卷请求vm
 */
@Data
public class ExamAutoPaperRequestVM {
    private Integer id;
    @NotNull
    private Integer level;
    @NotNull
    private Integer subjectId;
    @NotNull
    private Integer paperType;
    @NotBlank
    private String name;
    @NotNull
    private Integer suggestTime;

    private List<String> limitDateTime;

    /**
     * 难度
     */
    private Integer difficult;

    private String score;

    /**
     * 题目数量
     */
    private Integer titleCount;
}
