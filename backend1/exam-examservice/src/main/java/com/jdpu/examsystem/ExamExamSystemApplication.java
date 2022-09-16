package com.jdpu.examsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@ComponentScan("com.jdpu")
public class ExamExamSystemApplication implements ServletContextInitializer {
    public static void main(String[] args) {
        SpringApplication.run(ExamExamSystemApplication.class, args);
    }

    //设置websocket发送内容长度
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.setInitParameter("org.apache.tomcat.websocket.textBufferSize","1024000");
    }
}
