package com.jdpu.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 *
 *
 * @author hhx
 *
 */
@Data
@TableName("user")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 31415926535L;

	/**
	 * 用户id
	 */
	@TableId
	private Integer id;
	/**
	 * 用户昵称
	 */
	private String name;
	/**
	 * 用户头像地址
	 */
	private String portrait;
	/**
	 * 注册手机
	 */
	private String phone;
	/**
	 * 用户密码（可以为空，支持只用验证码注册、登录）
	 */
	private String password;
	/**
	 * 注册ip
	 */
	private String regIp;
	/**
	 * 是否有效用户
	 */
	private Boolean accountNonExpired;
	/**
	 * 账号是否未过期
	 */
	private Boolean credentialsNonExpired;
	/**
	 * 是否未锁定
	 */
	private Boolean accountNonLocked;
	/**
	 * 用户状态：1能登录，0不能登录
	 */
	private Integer status;
	/**
	 * 是否删除
	 */
	@TableLogic(value="0",delval="1")
	private Boolean isDel;
	/**
	 * 注册时间
	 */
	@JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date createTime;
	/**
	 * 记录更新时间
	 */
	@JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date updateTime;

	/**
	 * 整合xzs 的User
	 */
	// 真实姓名
	private String realName;

	private Integer age;
	/// 1.男 2女
	private Integer sex;

	@JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date birthDay;

	// 学生年级(1-12)
	private Integer userLevel;
}
