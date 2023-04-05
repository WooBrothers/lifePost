package com.woobros.member.hub.domain.signup.exception;

import com.woobros.member.hub.common.exception.MemberErrorEnum;

public enum SignUpMemberErrorEnum implements MemberErrorEnum {
    LOGOUT_ERROR("sign up error occur", "SU0001", "400"),
    ;

    private final String message;
    private final String errorCode;
    private final String httpStatusCode;

    SignUpMemberErrorEnum(String message, String errorCode, String httpStatusCode) {
        this.message = message;
        this.errorCode = errorCode;
        this.httpStatusCode = httpStatusCode;
    }

    public String getMessage() {
        return this.message;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getHttpStatusCode() {
        return this.httpStatusCode;
    }
}
