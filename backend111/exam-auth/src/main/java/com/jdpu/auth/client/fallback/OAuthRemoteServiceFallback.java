package com.jdpu.auth.client.fallback;

import com.jdpu.auth.client.OAuthRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author: xJh
 * @Date: 2022/3/6
 */
@Slf4j
@Component
public class OAuthRemoteServiceFallback  implements OAuthRemoteService {

    @Override
    public String createToken(String phone, String password, String scope, String grantType, String clientId, String clientSecret, String authType) {
        log.error("手机号[{}],发放access_token发生异常", phone);
        return null;
    }

    @Override
    public String refreshToken(String refreshToken, String grantType, String clientId, String clientSecret) {
        log.error("refreshToken[{}],刷新access_token发生异常", refreshToken);
        return null;
    }
}
