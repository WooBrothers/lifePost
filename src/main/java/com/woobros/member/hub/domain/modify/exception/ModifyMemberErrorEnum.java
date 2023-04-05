package com.woobros.member.hub.domain.modify.exception;

import com.woobros.member.hub.common.exception.MemberErrorEnum;

public enum ModifyMemberErrorEnum implements MemberErrorEnum {
    LOGOUT_ERROR("modify error occur", "MD0001", "400"),
    ;

    private final String message;
    private final String errorCode;
    private final String httpStatusCode;

    ModifyMemberErrorEnum(String message, String errorCode, String httpStatusCode) {
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
