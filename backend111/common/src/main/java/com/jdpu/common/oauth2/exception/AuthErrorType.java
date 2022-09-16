package com.jdpu.common.oauth2.exception;

import com.jdpu.common.exception.exception.ErrorType;

public enum AuthErrorType implements ErrorType {

    // oauthException
    INVALID_REQUEST(40001, "无效请求"),
    INVALID_CLIENT(40002, "无效client_id"),
    INVALID_GRANT(40003, "无效授权"),
    INVALID_SCOPE(40004, "无效scope"),
    INVALID_TOKEN(40005, "无效token"),
    INSUFFICIENT_SCOPE(40006, "授权不足"),
    REDIRECT_URI_MISMATCH(40007, "redirect url不匹配"),
    ACCESS_DENIED(40008, "拒绝访问"),
    METHOD_NOT_ALLOWED(40009, "不支持该方法"),
    SERVER_ERROR(400010, "权限服务错误"),
    UNAUTHORIZED_CLIENT(40011, "未授权客户端"),
    UNAUTHORIZED(40012, "未授权"),
    UNSUPPORTED_RESPONSE_TYPE(40013, "不支持的响应类型"),
    UNSUPPORTED_GRANT_TYPE(40014, "不支持的授权类型"),
    ERROR_VERIFY_CODE(40015, "验证码错误或已过期"),

    // 自定义
    LOGIN_FAILED(40021, "登录出现异常"),
    PERMISSION_DENIED(40022, "权限不足");
    /**
     * 错误类型码
     */
    private Integer code;
    /**
     * 错误类型描述信息
     */
    private String message;

    AuthErrorType(Integer code, String msg) {
        this.code = code;
        this.message = msg;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
