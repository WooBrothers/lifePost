package com.woobros.member.hub.domain.index.exception;

import com.woobros.member.hub.common.exception.MemberErrorEnum;
import lombok.Getter;

@Getter
public enum LoginMemberErrorEnum implements MemberErrorEnum {
    LOGIN_ERROR("login error occur", "LI0001", "400", ""),
    ;

    private final String message;
    private final String errorCode;
    private final String httpStatusCode;
    private final String fieldName;

    LoginMemberErrorEnum(String message, String errorCode, String httpStatusCode,
        String fieldName) {

        this.message = message;
        this.errorCode = errorCode;
        this.httpStatusCode = httpStatusCode;
        this.fieldName = fieldName;
    }
}
