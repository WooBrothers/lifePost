package com.woobros.member.hub.common.exception;

public class MemberException extends RuntimeException {

    private final MemberErrorEnum memberErrorEnum;

    public MemberException(MemberErrorEnum memberErrorEnum) {
        super(memberErrorEnum.getMessage());
        this.memberErrorEnum = memberErrorEnum;
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
