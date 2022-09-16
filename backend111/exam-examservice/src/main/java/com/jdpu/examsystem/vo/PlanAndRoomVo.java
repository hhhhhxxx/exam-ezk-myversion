package com.jdpu.examsystem.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class PlanAndRoomVo {
    /**
     * 定时发放考试通知
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date noticeTime;
    /**
     * 考试类型
     */
    private Integer examType;
    /**
     * 考试是否发放[0未发放,1发放]
     */
    private Integer isPublish;
    /**
     * 考场批次
     */
    private String roomBatch;
    /**
     * 考场场次
     */
    private String roomRound;
}
