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
 * @date 2022-04-11 15:37:04
 */
@Data
@TableName("t_exam_argu_plan_room_relation")
public class TExamArguPlanRoomRelationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 考务管理表id
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 关联考试id
	 */
	private Integer examArgumentsId;
	/**
	 * 考试计划表id
	 */
	private Integer examPlanId;
	/**
	 * 考场编排表id
	 */
	private Integer examRoomId;

}
