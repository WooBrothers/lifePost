package com.woobros.member.hub.domain.signup.exception;

import com.woobros.member.hub.common.exception.MemberErrorEnum;
import com.woobros.member.hub.common.exception.MemberException;

public class SignUpException extends MemberException {

    private final MemberErrorEnum memberErrorEnum;

    public SignUpException(SignUpMemberErrorEnum errorEnum) {
        super(errorEnum);
        this.memberErrorEnum = errorEnum;
    }

    public String getErrorMessage() {
        return this.memberErrorEnum.getMessage();
    }

    public String getErrorCode() {
        return this.memberErrorEnum.getErrorCode();
    }

    public String getHttpStatusCode() {
        return this.memberErrorEnum.getHttpStatusCode();
    }
}
