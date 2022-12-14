package com.jdpu.common.xzsOld.entities;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 *
 */
@Data
public class MessageUser implements Serializable {
    private static final long serialVersionUID = -4042932811802896498L;
    private Integer id;
    private Integer messageId; // 消息内容ID
    private Integer receiveUserId; // 接收人ID
    private String receiveUserName; // 接收人用户名
    private String receiveRealName; // 接收人真实姓名
    private Boolean readed; // 是否已读
    private Date createTime;
    private Date readTime; // 阅读时间
}
