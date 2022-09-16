package com.jdpu.common.xzsOld.entities;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zuck
 */
@Data
public class Message implements Serializable {
    private static final long serialVersionUID = -3510265139403747341L;
    private Integer id;
    private String title; // 标题
    private String content; // 内容
    private Date createTime;
    private Integer sendUserId; // 发送者用户ID
    private String sendUserName; // 发送者用户名
    private String sendRealName; // 发送者真实姓名
    private Integer receiveUserCount; // 接收人数
    private Integer readCount; // 已读人数
}
