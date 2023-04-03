package com.woobros.member.domain.logout.exception;

import com.woobros.member.common.MemberException;
import com.woobros.member.common.exception.ErrorEnum;

public class LogoutException extends MemberException {

    private final ErrorEnum errorEnum;

    public LogoutException(LogoutErrorEnum errorEnum) {
        super(errorEnum);
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
