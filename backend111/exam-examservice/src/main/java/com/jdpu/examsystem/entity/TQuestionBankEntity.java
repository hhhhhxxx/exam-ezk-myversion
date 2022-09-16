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
 * @date 2022-04-10 15:26:22
 */
@Data
@TableName("t_question_bank")
public class TQuestionBankEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 题库id
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 题库名字
	 */
	private String name;
	/**
	 * 题库简介
	 */
	private String description;
	/**
	 * 题库锁定,不允许查看[1锁定,0未锁定]
	 */
	private Integer isLock;
	/**
	 * 题库审批情况[0不通过,1通过]
	 */
	private Integer isPass;
	/**
	 * 题库封面图片
	 */
	private String img;

}
