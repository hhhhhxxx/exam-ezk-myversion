package com.jdpu.common.oauth2.entity;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserJwt extends org.springframework.security.core.userdetails.User {
    // 用户id
    private String id;
    private String name;
    // 继承属性
    // private String password;
    // private final String username;
    // private final Set<GrantedAuthority> authorities;
    // private final boolean accountNonExpired;
    // private final boolean accountNonLocked;
    // private final boolean credentialsNonExpired;
    // private final boolean enabled;

    // username必须是phone 因为登录是添写phone
    public UserJwt(String username, String password, Collection<? extends GrantedAuthority> authorities, String id, String name) {
        super(username, password, authorities);
        // 自定义属性
        this.id = id;
        this.name = name;
    }

    public UserJwt(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, String id,String name) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
