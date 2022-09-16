package com.jdpu.common.xzsOld.utils;


/**
 * 错误处理工具类
 */
public class ErrorUtil {
    /**
     * Parameter error format string.
     */
    public static String parameterErrorFormat(String field, String msg) {
        return "【" + field + " : " + msg + "】";
    }
}
