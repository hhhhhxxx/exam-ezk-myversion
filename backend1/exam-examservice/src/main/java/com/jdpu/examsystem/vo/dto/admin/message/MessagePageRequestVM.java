package com.jdpu.examsystem.vo.dto.admin.message;


import com.jdpu.common.xzsOld.base.BasePage;

public class MessagePageRequestVM extends BasePage {
    private String sendUserName;

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }
}
