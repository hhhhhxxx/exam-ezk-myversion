package com.jdpu.examsystem.entity;

import com.baomidou.mybatisplus.annotation.*;

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
@TableName("t_exam_room")
public class TExamRoomEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 考场编排表id
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 考场批次
	 */
	private String roomBatch;
	/**
	 * 考场场次
	 */
	private String roomRound;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;

}
