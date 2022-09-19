package com.wellclaim.member.domain.modify.exception;

import com.wellclaim.member.common.exception.ErrorEnum;

public enum ModifyErrorEnum implements ErrorEnum {
    LOGOUT_ERROR("modify error occur", "MD0001", "400"),
    ;

    private final String message;
    private final String errorCode;
    private final String httpStatusCode;

    ModifyErrorEnum(String message, String errorCode, String httpStatusCode) {
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
