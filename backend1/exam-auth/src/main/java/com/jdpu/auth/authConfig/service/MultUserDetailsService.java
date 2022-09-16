package com.jdpu.auth.authConfig.service;


import com.jdpu.auth.service.IRoleService;
import com.jdpu.common.dto.authority.RoleDTO;
import com.jdpu.common.oauth2.entity.UserJwt;
import com.jdpu.auth.authConfig.mult.MultAuthentication;
import com.jdpu.auth.authConfig.mult.MultAuthenticationContext;
import com.jdpu.auth.authConfig.mult.authenticator.MultAuthenticator;
import com.jdpu.common.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 集成认证用户服务
 **/
@Service("userDetailsService")
@Slf4j
public class MultUserDetailsService implements UserDetailsService {

    private List<MultAuthenticator> authenticators;


    @Autowired
    private IRoleService roleService;

    @Autowired(required = false)
    public void setIntegrationAuthenticators(List<MultAuthenticator> authenticators) {
        this.authenticators = authenticators;
    }

    @Override
    public UserJwt loadUserByUsername(String username) throws UsernameNotFoundException {
        // 直接从threadLocal拿
        MultAuthentication multAuthentication = MultAuthenticationContext.get();
        //判断是否是集成登录
        if (multAuthentication == null) {
            multAuthentication = new MultAuthentication();
        }
        multAuthentication.setUsername(username);
        UserEntity user = this.authenticate(multAuthentication);
        log.info("authenticate:{}",user);

        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        // 返回给spring security
        return new UserJwt(
                user.getPhone(), // 登录填的是手机号就手机号啦，又搞成username
                user.getPassword(),
                !user.getIsDel() && user.getStatus().equals(1), // 禁止登录
                user.getAccountNonExpired(),
                user.getCredentialsNonExpired(),
                user.getAccountNonLocked(),
                this.obtainGrantedAuthorities(user),
                user.getId().toString(),
                user.getName());
    }

    /**
     * 获得登录者所有角色的权限集合.
     */
    protected Set<GrantedAuthority> obtainGrantedAuthorities(UserEntity user) {
        try {
            log.info("user:{}",user);
            //远程调用
            Set<RoleDTO> roles = roleService.queryUserRolesByUserId(user.getId());
            log.info("user:{},roles:{}", user.getName(), roles);
            return roles.stream().map(role -> new SimpleGrantedAuthority(role.getCode())).collect(Collectors.toSet());
        } catch (Exception e) {
            e.printStackTrace();
            HashSet<GrantedAuthority> grantedAuthorities = new HashSet<>();
            grantedAuthorities.add(new SimpleGrantedAuthority("NONE"));
            return grantedAuthorities;
        }
    }


    private UserEntity authenticate(MultAuthentication multAuthentication) {
        if (this.authenticators != null) {
            for (MultAuthenticator authenticator : authenticators) {
                if (authenticator.support(multAuthentication)) {
                    return authenticator.authenticate(multAuthentication);
                }
            }
        }
        return null;
    }
}
