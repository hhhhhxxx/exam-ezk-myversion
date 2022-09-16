package com.jdpu.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;


@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.jdpu.api.mapper")
@ComponentScan("com.jdpu")
@SpringBootApplication
public class ExamApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExamApiApplication.class, args);
    }
}
