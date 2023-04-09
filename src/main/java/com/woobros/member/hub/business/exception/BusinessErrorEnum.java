package com.woobros.member.hub.business.exception;

import com.woobros.member.hub.common.exception.MemberErrorEnum;
import lombok.Getter;

@Getter
public enum BusinessErrorEnum implements MemberErrorEnum {

    NOT_FOUND("data not found", "BE0001", "404", ""),
    ;

    private final String message;
    private final String errorCode;
    private final String httpStatusCode;
    private String fieldName;

    BusinessErrorEnum(String message, String errorCode, String httpStatusCode,
        String fieldName) {
        this.message = message;
        this.errorCode = errorCode;
        this.httpStatusCode = httpStatusCode;
        this.fieldName = fieldName;
    }

    public BusinessErrorEnum setFieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }
}
