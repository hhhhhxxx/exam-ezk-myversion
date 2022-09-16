package com.jdpu.common.exception.exception;

import lombok.Getter;

@Getter
public enum SystemErrorType implements ErrorType {

    SYSTEM_ERROR(110000, "系统异常"),
    SYSTEM_BUSY(110001, "系统繁忙,请稍候再试"),

    GATEWAY_NOT_FOUND_SERVICE(11002, "服务未找到"),
    GATEWAY_ERROR(11003, "网关异常"),
    GATEWAY_CONNECT_TIME_OUT(11004, "网关超时"),

    ARGUMENT_NOT_VALID(11005, "请求参数校验不通过"),
    INVALID_TOKEN(11006, "无效token"),
    UPLOAD_FILE_SIZE_LIMIT(11007, "上传文件大小超过限制"),

    DUPLICATE_PRIMARY_KEY(11008,"唯一键冲突");

    /**
     * 错误类型码
     */
    private Integer code;
    /**
     * 错误类型描述信息
     */
    private String message;

    SystemErrorType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
