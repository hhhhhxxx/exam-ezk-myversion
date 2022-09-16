package com.jdpu.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 菜单表
 *
 * @author hhf
 * @email hhf@qq.com
 * @date 2022-03-04 15:53:04
 */
@Data
@TableName("menu")
public class MenuEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Integer id;
	/**
	 * 父菜单id
	 */
	private Integer parentId;
	/**
	 * 菜单路径
	 */
	private String href;
	/**
	 * 菜单图标
	 */
	private String icon;
	/**
	 * 菜单名称
	 */
	private String name;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 排序号
	 */
	private Integer orderNum;
	/**
	 * 是否展示
	 */
	private Integer shown;
	/**
	 * 菜单层级，从0开始
	 */
	private Integer level;
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
