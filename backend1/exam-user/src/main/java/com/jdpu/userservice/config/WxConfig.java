package com.jdpu.userservice.config;

import lombok.Data;

import java.time.Duration;
import java.util.List;

/**
 * 微信小程序配置
 * @author zuck
 */
@Data
public class WxConfig {
    private String appid;
    private String secret;
    private Duration tokenToLive;
    private List<String> securityIgnoreUrls;
}
