package com.jdpu.common.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import com.jdpu.common.service.JwtService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author: xJh
 * @Date: 2022/3/25
 */
@Service
public class JwtServiceImpl implements JwtService {
    /**
     * Authorization认证开头是"bearer "
     */
    private static final String BEARER = "Bearer ";


    /**
     * jwt token 密钥，主要用于token解析，签名验证
     */
    @Value("${constant.jwt.signingKey}")
    private String signingKey;

    @Override
    public Jws<Claims> getJwt(String jwtToken) {
        if (jwtToken.startsWith(BEARER)) {
            jwtToken = StringUtils.substring(jwtToken, BEARER.length());
        }


        // return Jwts.parser()  //得到DefaultJwtParser
        //         .setSigningKey(signingKey.getBytes()) //设置签名的秘钥
        //         .parseClaimsJws(jwtToken);

        return Jwts.parser()  //得到DefaultJwtParser
                .setSigningKey(signingKey.getBytes()) //设置签名的秘钥
                .parseClaimsJws(jwtToken);
    }

    public Claims parseJwt(String jwtToken){

        if (jwtToken.startsWith(BEARER)) {
            jwtToken = StringUtils.substring(jwtToken, BEARER.length());
        }

        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(signingKey) // 设置标识名
                    .parseClaimsJws(jwtToken)  //解析token
                    .getBody();
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        }
        return claims;
    }
}
