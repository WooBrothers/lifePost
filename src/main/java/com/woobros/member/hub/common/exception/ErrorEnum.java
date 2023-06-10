package com.woobros.member.hub.common.exception;

public enum ErrorEnum {

    /* DB 관련 에러 메시지 */
    NOT_FOUND("data not found.", "DB0001", 404),

    /* 로직 관련 에러 메시지 */
    // LETTER domain LTxxxx
    LETTER_REQUEST_INVALID("letter request is invalid.", "LT0001", 409),
    ;

    private String message;
    private String errorCode;
    private int httpStatusCode;

    ErrorEnum(String message, String errorCode, int httpStatusCode) {
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

    public int getHttpStatusCode() {
        return this.httpStatusCode;
    }
}
