package com.jdpu.examsystem.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class ExamNoticeVo {
    /**
     * 是否已读[0:未读;1:已读]
     */
    private Integer isRead;
    /**
     * 通知发放时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 考试开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date examStartTime;
    /**
     * 考试批次
     */
    private String examBatch;
    /**
     * 考试场次
     */
    private String examRound;
    /**
     * 及格标准
     */
    private Integer passScore;
    /**
     * 允许多终端考试(0:网页考试,1:app考试2:允许网页与app考试)
     */
    private String allowMultidevice;
    /**
     * 试卷名字
     */
    @NotBlank
    private String name;
    /**
     * 考试参数表id
     */
    private Integer argumentsId;
}
