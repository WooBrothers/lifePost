package com.woobros.member.hub.domain.withdrawal.exception;

import com.woobros.member.hub.common.exception.MemberErrorEnum;
import com.woobros.member.hub.common.exception.MemberException;
import com.woobros.member.hub.domain.logout.exception.LogoutMemberErrorEnum;

public class WithdrawException extends MemberException {

    private final MemberErrorEnum memberErrorEnum;

    public WithdrawException(LogoutMemberErrorEnum errorEnum) {
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
