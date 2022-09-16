package com.jdpu.examsystem.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jdpu.examsystem.vo.dto.admin.exam.ExamPaperEditRequestVM;
import lombok.Data;

import java.util.Date;

@Data
public class ExamWithStatusVM {
    /**
     * argument_id
     */
    private Integer argumentId;
    /**
     * 试卷信息
     */
    private ExamPaperEditRequestVM examPaperEditRequestVM;

    /**
     * 考试开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date examStartTime;
    /**
     * 考试截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date examEndTime;
    /**
     * 限时提交时间(...分钟内不允许提交)
     */
    private Integer limitSubmitTime;
    /**
     * 限时进入时间(....分钟后不允许参加考试)
     */
    private Integer limitEnterTime;
    /**
     * 考试批次
     */
    private String examBatch;
    /**
     * 考试场次
     */
    private String examRound;
    /**
     * 是否开启抓拍监控(0:关闭1:开启)
     */
    private Integer enableMonitor;
    /**
     * 识别到屏幕异常...次,强制收卷
     */
    private Integer limitScreenCount;
    /**
     * 及格标准
     */
    private Integer passScore;
    /**
     * 考试截止日前运行重做....次
     */
    private Integer allowRedo;
    /**
     * 允许学生查看分数(0:不允许1:允许)
     */
    private Integer allowCheckScore;
    /**
     * 允许多终端考试(0:网页考试,1:app考试2:允许网页与app考试)
     */
    private String allowMultidevice;
    /**
     * 监考老师
     */
    private String teacherName;
    /**
     * 考试状态
     */
    private Integer examStatus;
}
