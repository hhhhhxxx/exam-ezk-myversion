package com.jdpu.thirdparty.aliyun.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@Configuration
public class ConstantConfig  {
    @Value("${aliyun.file.endpoint}")
    private   String endpoint;
    @Value("${aliyun.file.accessKeyId}")
    private  String accessKeyId;
    @Value("${aliyun.file.accessKeySecret}")
    private  String accessKeySecret;
    @Value("${aliyun.file.folder}")
    private  String folder;
    @Value("${aliyun.file.bucketName}")
    private  String bucketName;
    @Value("${aliyun.file.webUrl}")
    private  String webUrl;
}
