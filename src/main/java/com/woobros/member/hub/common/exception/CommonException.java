package com.woobros.member.hub.common.exception;

import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {

    private String message;
    private String errorCode;
    private int httpStatusCode;

    public CommonException(ErrorEnum errorEnum) {
        message = errorEnum.getMessage();
        errorCode = errorEnum.getErrorCode();
        httpStatusCode = errorEnum.getHttpStatusCode();
    }
}
