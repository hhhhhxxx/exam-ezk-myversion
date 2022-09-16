package com.jdpu.common.xzsOld.entities;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = -7797183521247423117L;
    private Integer id;
    private String userUuid;
    private String userName;
    private String password;
    private String realName;
    private Integer age;
    private Integer sex; //1.男 2女
    private Date birthDay;
    private Integer userLevel; // 学生年级(1-12)
    private String phone;
    private Integer role; // 1.学生  3.管理员
    private Integer status; // 1.启用 2禁用
    private String imagePath; //头像地址
    private Date createTime;
    private Date modifyTime;
    private Date lastActiveTime;
    private Boolean deleted;
    private String wxOpenId;
}
