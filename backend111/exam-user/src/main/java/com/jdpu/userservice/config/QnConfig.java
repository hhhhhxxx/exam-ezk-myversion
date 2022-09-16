package com.jdpu.userservice.config;

import lombok.Data;

/**
 * 七牛云文件存储配置
 * @author zuck
 */
@Data
public class QnConfig {
    private String url;
    private String bucket;
    private String accessKey;
    private String secretKey;
}
