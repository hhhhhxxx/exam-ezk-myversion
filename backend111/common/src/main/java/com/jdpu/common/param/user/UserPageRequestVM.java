package com.jdpu.common.param.user;


import com.jdpu.common.xzsOld.base.BasePage;
import lombok.Data;

@Data
public class UserPageRequestVM extends BasePage {
    private String userName;
    private Integer roleId;
}
