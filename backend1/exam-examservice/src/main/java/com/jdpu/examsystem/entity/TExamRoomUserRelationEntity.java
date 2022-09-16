package com.jdpu.examsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 *
 * @author zuck
 * @email ${email}
 * @date 2022-04-11 15:44:48
 */
@Data
@TableName("t_exam_room_user_relation")
public class TExamRoomUserRelationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 考场与用户关联表id
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 考场编排表id
	 */
	private Integer examRoomId;
	/**
	 * 用户表id
	 */
	private Integer userId;

}
