package com.jdpu.auth.entiy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: xJh
 * @Date: 2022/3/25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginEntiy { // 登录表单
    private String userName;
    private String password;
    private Boolean  remember;
    //验证码
    private String code;
    //验证码的key值
    private String capthchaKey;
}
