package com.woobros.member.hub.common.exception;

public class ErrorException extends RuntimeException {

    private final ErrorEnum errorEnum;

    public ErrorException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.errorEnum = errorEnum;
    }

    public String getErrorMessage() {
        return this.errorEnum.getMessage();
    }

    public String getErrorCode() {
        return this.errorEnum.getErrorCode();
    }

    public String getHttpStatusCode() {
        return this.errorEnum.getHttpStatusCode();
    }
}
