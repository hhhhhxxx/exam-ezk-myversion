package com.jdpu.common.param.user;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.jdpu.common.entity.UserEntity;
import com.jdpu.common.xzsOld.base.BaseVM;
import lombok.Data;

import java.util.Date;

@Data
public class UserResponseVM extends BaseVM {

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
     * 用户状态：1能登录，0不能登录
     */
    private Integer status;
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
    public static UserResponseVM from(UserEntity user) {
        UserResponseVM vm = modelMapper.map(user, UserResponseVM.class);
        return vm;
    }
}
