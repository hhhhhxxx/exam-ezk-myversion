package com.jdpu.common.xzsOld.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

/**
 *
 */
@Data
@NoArgsConstructor
public class UserEventLog implements Serializable {
    private static final long serialVersionUID = -3951198127152024633L;
    private Integer id;
    private Integer userId; // 用户id
    private String userName; // 用户名
    private String realName; // 真实姓名
    private String content; // 内容
    private Date createTime; // 时间
    public UserEventLog(Integer userId, String userName, String realName, Date createTime) {
        this.userId = userId;
        this.userName = userName;
        this.realName = realName;
        this.createTime = createTime;
    }
}
