package com.jdpu.thirdparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@ComponentScan("com.jdpu")
public class ExamThirdPartyApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExamThirdPartyApplication.class, args);
    }
}
