package com.jdpu.examsystem.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 考试监考记录表
 *
 * @author zuck
 */
@Data
@TableName("t_exam_info")
public class TExamInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * exam_info_id
	 */
	@TableId(type = IdType.AUTO)
	@ExcelProperty("id")
	private Integer id;
	/**
	 * exam_arguments_id
	 */
	@ExcelProperty("考试id")
	private Integer examArgumentsId;
	/**
	 * 考生姓名
	 */
	@ExcelProperty("考生姓名")
	private String studentName;
	/**
	 * 检测到考生人数大于1人次数
	 */
	@ExcelProperty("考生人数异常")
	private Integer numOverCount;
	/**
	 * 考生退出全屏次数
	 */
	@ExcelProperty("考生屏幕异常")
	private Integer exitFullCount;
	/**
	 * 检测到非活体次数
	 */
	@ExcelProperty("考生非活体次数")
	private Integer nonLiveCount;
	/**
	 * 0:男，1:女
	 */
	@ExcelProperty("检测性别")
	private Integer checkSex;
	/**
	 * 考前人脸拍照照片(OSS存储)
	 */
	@ExcelIgnore
	private String beforeExamImg;
	/**
	 * 考试时抓拍图片(OSS存储)
	 */
	@ExcelIgnore
	private String duringExamImg;
	/**
	 * 创建记录时间
	 */
	@TableField(fill = FieldFill.INSERT)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@ExcelProperty("进入考试时间")
	private Date createTime;

}
