package com.woobros.member.hub.domain.logout.exception;

import com.woobros.member.hub.common.exception.MemberErrorEnum;

public enum LogoutMemberErrorEnum implements MemberErrorEnum {
    LOGOUT_ERROR("email duplicated", "LO0001", "409"),
    ;

    private final String message;
    private final String errorCode;
    private final String httpStatusCode;

    LogoutMemberErrorEnum(String message, String errorCode, String httpStatusCode) {
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
