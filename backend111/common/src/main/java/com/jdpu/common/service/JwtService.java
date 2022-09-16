package com.jdpu.common.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.stereotype.Service;

/**
 * @Author: xJh
 * @Date: 2022/3/25
 */
@Service
public interface JwtService {
    Jws<Claims> getJwt(String authorization);
}
