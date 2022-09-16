package com.jdpu.examsystem.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 考试通知表
 *
 * @author zuck
 * @email ${email}
 * @date 2022-03-27 22:53:42
 */
@Data
@TableName("t_exam_notice")
public class TExamNoticeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 考试通知表id
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 关联考试参数表id
	 */
	private Integer examArgumentsId;
	/**
	 * 用户id
	 */
	private Integer userId;
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

}
