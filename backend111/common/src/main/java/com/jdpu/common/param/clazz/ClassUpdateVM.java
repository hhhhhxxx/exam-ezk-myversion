package com.jdpu.common.param.clazz;

import lombok.Data;

/**
 * @author zuck
 */
@Data // 班级 添加和更新
public class ClassUpdateVM {
    private Integer id;

    private Integer count;

    private String description;

    private String name;
    // 添加的时候 本账号是否顺便加入班级
    private Boolean add;

    public Boolean getAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }
}
