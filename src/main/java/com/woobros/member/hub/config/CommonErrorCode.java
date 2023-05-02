package com.woobros.member.hub.config;

public enum CommonErrorCode {

    FORBIDDEN("CE0001", "forbidden access", 403),
    UNAUTHORIZED("CE0001", "unauthorized access", 401),

    ;
    private final String errorCode;
    private final String message;
    private final int httpStatus;


    CommonErrorCode(String errorCode, String message, int httpStatus) {
        this.errorCode = errorCode;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return this.message;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public int getHttpStatus() {
        return this.httpStatus;
    }

}
