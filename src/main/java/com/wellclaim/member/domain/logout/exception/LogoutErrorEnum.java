package com.wellclaim.member.domain.logout.exception;

import com.wellclaim.member.common.exception.ErrorEnum;

public enum LogoutErrorEnum implements ErrorEnum {
    LOGOUT_ERROR("logout error occur", "LO0001", "400"),
    ;

    private final String message;
    private final String errorCode;
    private final String httpStatusCode;

    LogoutErrorEnum(String message, String errorCode, String httpStatusCode) {
        this.message = message;
        this.errorCode = errorCode;
        this.httpStatusCode = httpStatusCode;
    }

    public String getMessage(){
        return this.message;
    }

    public String getErrorCode(){
        return this.errorCode;
    }

    public String getHttpStatusCode(){
        return this.httpStatusCode;
    }
}
