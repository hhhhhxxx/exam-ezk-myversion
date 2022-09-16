package com.jdpu.userservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;


@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@MapperScan("com.jdpu.userservice.mapper")
@ComponentScan("com.jdpu")
public class ExamUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExamUserApplication.class, args);
    }
}
