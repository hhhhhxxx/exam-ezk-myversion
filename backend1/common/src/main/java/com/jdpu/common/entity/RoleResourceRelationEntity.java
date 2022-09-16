package com.jdpu.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 角色和资源关系表
 *
 * @author hhf
 * @email hhf@qq.com
 * @date 2022-03-04 15:53:04
 */
@Data
@TableName("role_resource_relation")
public class RoleResourceRelationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 关系id
	 */
	@TableId
	private Integer id;
	/**
	 * 资源id
	 */
	private Integer resourceId;
	/**
	 * 角色id
	 */
	private Integer roleId;
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
