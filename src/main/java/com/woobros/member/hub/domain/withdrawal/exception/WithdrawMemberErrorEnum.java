package com.woobros.member.hub.domain.withdrawal.exception;

import com.woobros.member.hub.common.exception.MemberErrorEnum;

public enum WithdrawMemberErrorEnum implements MemberErrorEnum {
    LOGOUT_ERROR("withdraw error occur", "WD0001", "400"),
    ;

    private final String message;
    private final String errorCode;
    private final String httpStatusCode;

    WithdrawMemberErrorEnum(String message, String errorCode, String httpStatusCode) {
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
