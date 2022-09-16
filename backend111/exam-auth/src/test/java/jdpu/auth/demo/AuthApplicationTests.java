package jdpu.auth.demo;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jdpu.auth.ExamAuthApplication;
import io.jsonwebtoken.Claims;
import com.jdpu.common.constant.RoleNameEnum;
import com.jdpu.common.entity.UserEntity;
import com.jdpu.common.entity.authority.Role;
import com.jdpu.common.service.JwtService;
import com.jdpu.auth.mapper.UserMapper;
import com.jdpu.auth.service.IRoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collection;

@SpringBootTest(classes = ExamAuthApplication.class)
@RunWith(SpringRunner.class)
public class AuthApplicationTests {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IRoleService roleService;

    @Test
    public void test2() {

        System.out.println("hhx");

        System.out.println(RoleNameEnum.STUDENT);

        Role studentRole = roleService.lambdaQuery().eq(Role::getName, "学生").one();

        Page<UserEntity> pageInfo = new Page<>(1, 100);

        userMapper.queryUserNoInClass(pageInfo,null,studentRole.getId());
    }

    @Test
    public void testAuth() {
        System.out.println(jwtService.toString());

        String AuthStr = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiMTAwMDMwMDI3IiwidXNlcl9uYW1lIjoiMTg2MzExNDIyNTYiLCJzY29wZSI6WyJhbGwiXSwiY2xpZW50X2lwIjoiMTAuMjA3LjEyNS4yNTMiLCJleHAiOjE2NjE5NjMwMjksImF1dGhvcml0aWVzIjpbIkNPVVJTRV9NQU5BR0VSIiwiQVVUSE9SSVRZX01BTkFHRVIiXSwianRpIjoiMzUzOGU2NWItODk3MS00NDc1LTgzOGQtMjQxNzNjZWY1NmUxIiwiY2xpZW50X2lkIjoiY2xpZW50X2xhZ291In0.IZkCwgpgeiDicO0bTFMlSeefzjiTTJRCiVp4YSiGUi0";

        Claims body = jwtService.getJwt(AuthStr).getBody();

        System.out.println(body.toString());
    }

    @Test
    public void contextLoads() {

        // 可能是普通security认证对象
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 关键是 SecurityContextHolder， 分布式环境下通过redis或者jwt 传递token 重构authentication达到

        // 转型
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;

        // 取出用户星系
        Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();

        // 用户身份
        String name = userAuthentication.getName();

        ArrayList<String> authorities = new ArrayList<>();

        // 取出权限 放入list
        Collection<? extends GrantedAuthority> authoritiesCollection = userAuthentication.getAuthorities();
        authoritiesCollection.stream().forEach(item -> {
            authorities.add(((GrantedAuthority)item).getAuthority());
        });

        // 取出Oauth2.0 请求
        OAuth2Request oAuth2Request = oAuth2Authentication.getOAuth2Request();
    }
}
