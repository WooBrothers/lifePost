package com.wellclaim.member.domain.signup.exception;

import com.wellclaim.member.common.MemberException;
import com.wellclaim.member.common.exception.ErrorEnum;
import com.wellclaim.member.domain.logout.exception.LogoutErrorEnum;

public class SignUpException extends MemberException {
    private final ErrorEnum errorEnum;

    public SignUpException(LogoutErrorEnum errorEnum){
        super(errorEnum);
        this.errorEnum = errorEnum;
    }

    public String getErrorMessage(){
        return this.errorEnum.getMessage();
    }
    public String getErrorCode(){
        return this.errorEnum.getErrorCode();
    }
    public String getHttpStatusCode(){
        return this.errorEnum.getHttpStatusCode();
    }
}
