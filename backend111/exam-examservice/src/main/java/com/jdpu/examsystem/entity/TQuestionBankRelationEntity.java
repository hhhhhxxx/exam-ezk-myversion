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
 * @date 2022-04-10 15:49:42
 */
@Data
@TableName("t_question_bank_relation")
public class TQuestionBankRelationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 题库与题目关联表id
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 题目id
	 */
	private Integer questionId;
	/**
	 * 题库id
	 */
	private Integer bankId;

}
