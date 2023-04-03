package com.woobros.member.domain.login.exception;

import com.woobros.member.common.exception.ErrorEnum;
import lombok.Getter;

@Getter
public enum LoginErrorEnum implements ErrorEnum {
    LOGIN_ERROR("login error occur", "LI0001", "400", ""),
    ;

    private final String message;
    private final String errorCode;
    private final String httpStatusCode;
    private final String fieldName;

    LoginErrorEnum(String message, String errorCode, String httpStatusCode, String fieldName) {
        this.message = message;
        this.errorCode = errorCode;
        this.httpStatusCode = httpStatusCode;
        this.fieldName = fieldName;
    }
}
