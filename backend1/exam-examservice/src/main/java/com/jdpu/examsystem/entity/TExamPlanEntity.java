package com.jdpu.examsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 *
 *
 * @author zuck
 * @email ${email}
 * @date 2022-04-11 15:37:04
 */
@Data
@TableName("t_exam_plan")
public class TExamPlanEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 考试计划表id
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
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

}
