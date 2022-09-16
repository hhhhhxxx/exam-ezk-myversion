package com.jdpu.common.constant;

public enum RoleNameEnum {    /** 常量相关 */
    /**
     * 已创建(1)
     */
    STUDENT("学生"),
    TEACHER("教师");

    /**
     * 状态描述
     */
    private final String name;

    /**
     * 构造函数
     */
    private RoleNameEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
