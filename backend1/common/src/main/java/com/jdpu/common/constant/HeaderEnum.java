package com.jdpu.common.constant;

public enum HeaderEnum {
    Authorization("Authorization");

    private final String name;

    /**
     * 构造函数
     */
    private HeaderEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
