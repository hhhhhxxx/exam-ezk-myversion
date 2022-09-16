package com.jdpu.common.oauth2.exception;

import com.jdpu.common.entity.vo.RestResponse;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;


@EqualsAndHashCode(callSuper = true)
@JsonSerialize(using = CustomOauthExceptionSerializer.class)
@Getter
class CustomOauthException extends OAuth2Exception {

    private final RestResponse restResponse;

    CustomOauthException(OAuth2Exception oAuth2Exception) {
        super(oAuth2Exception.getSummary(), oAuth2Exception);

        // System.out.println("oauth2异常码"+oAuth2Exception.getOAuth2ErrorCode().toUpperCase());

        this.restResponse = RestResponse.fail(AuthErrorType.valueOf(oAuth2Exception.getOAuth2ErrorCode().toUpperCase()), oAuth2Exception);
    }
}
