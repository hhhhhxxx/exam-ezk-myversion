package com.jdpu.examsystem.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class StudentExamListVo {
    /**
     * 试卷id
     */
    private Integer id;
    /**
     * 试卷名称
     */
    @NotBlank
    private String name;
    /**
     * 考试开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date examStartTime;
    /**
     * 考试时长
     */
    private Integer examTime;
    /**
     * 状态
     */
    private Integer examStatusCode;
    /**
     * 分数
     */
    private Integer score;
    /**
     * 允许多终端考试(0:网页考试,1:app考试2:允许网页与app考试)
     */
    private String allowMultidevice;
}
