package com.jdpu.examsystem.constant;

/**
 * 考试状态
 */
public enum ExamPaperStatus {
    /**
     * 未开始
     */
    EXAM_NOT_STARTED(50001,"考试未开始"),
    /**
     * 进行中,未超过限制进入时间
     */
    EXAM_STARTED_CAN_ENTER(50002,"正在考试"),
    /**
     * 进行中，超过限制进入时间
     */
    EXAM_STARTED_NOT_ENTER(50003,"考试开考限制进入"),
    /**
     * 已结束
     */
    EXAM_END(50004,"考试结束"),
    /**
     * 状态异常
     */
    EXAM_EXCEPTION(50005,"考试异常");

    int code;
    String message;

    ExamPaperStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
