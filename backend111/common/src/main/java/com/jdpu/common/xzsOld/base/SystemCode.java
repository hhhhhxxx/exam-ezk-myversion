package com.jdpu.common.xzsOld.base;

/**
 * @version 1.0.0
 * @description: 错误状态码枚举
 * @date 2022/1/24
 */
public enum SystemCode {
    /**
     * OK
     */
    OK(1, "成功"),
    /**
     * AccessTokenError
     */
    AccessTokenError(400, "用户登录令牌失效"),
    /**
     * UNAUTHORIZED
     */
    UNAUTHORIZED(401, "用户未登录"),
    /**
     * UNAUTHORIZED
     */
    AuthError(402, "用户名或密码错误"),
    /**
     * InnerError
     */
    InnerError(500, "系统内部错误"),
    /**
     * ParameterValidError
     */
    ParameterValidError(501, "参数验证错误"),
    /**
     * AccessDenied
     */
    AccessDenied(502, "用户没有权限访问");

    Integer code;
    String message;

    SystemCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
