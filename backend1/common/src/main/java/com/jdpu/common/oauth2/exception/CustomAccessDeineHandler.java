package com.jdpu.common.oauth2.exception;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jdpu.common.entity.vo.RestResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

// 自定义权限不足异常返回
public class CustomAccessDeineHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // 有登录 但是没有对应的权限
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        // response.getWriter().print("403 没有权限");

        // 使用自己的返回体
        PrintWriter writer = response.getWriter();



        // 既提示错误又提示异常
        RestResponse res = RestResponse.fail(AuthErrorType.PERMISSION_DENIED, accessDeniedException.getMessage());

        // 解决时间变对象的问题
        ObjectMapper objectMapper = new ObjectMapper();


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        objectMapper.setDateFormat(format);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        String resultJson = objectMapper.writeValueAsString(res);


        System.out.println(resultJson);

        writer.write(resultJson);
        writer.flush();
        writer.close();
    }

}
