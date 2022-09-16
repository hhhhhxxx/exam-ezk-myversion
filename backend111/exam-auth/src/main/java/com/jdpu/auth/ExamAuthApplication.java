package com.jdpu.auth;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCreateCacheAnnotation// boss用到缓存
@MapperScan("com.jdpu.auth.mapper")
@ComponentScan("com.jdpu")
public class ExamAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExamAuthApplication.class, args);
    }

}
