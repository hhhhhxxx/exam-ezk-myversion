package com.jdpu.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 资源表
 *
 * @author hhf
 * @email hhf@qq.com
 * @date 2022-03-04 15:53:04
 */
@Data
@TableName("resource")
public class ResourceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 资源id
	 */
	@TableId
	private Integer id;
	/**
	 * 资源名称
	 */
	private String name;
	/**
	 * 资源url
	 */
	private String url;
	/**
	 * 分类id
	 */
	private Integer categoryId;
	/**
	 * 简介
	 */
	private String description;
	/**
	 * 创建时间
	 */
	private Date createdTime;
	/**
	 * 更新时间
	 */
	private Date updatedTime;
	/**
	 * 创建人
	 */
	private String createdBy;
	/**
	 * 更新人
	 */
	private String updatedBy;

}
