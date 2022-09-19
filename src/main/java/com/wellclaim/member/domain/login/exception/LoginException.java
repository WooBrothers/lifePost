package com.wellclaim.member.domain.login.exception;

import com.wellclaim.member.common.MemberException;
import com.wellclaim.member.common.exception.ErrorEnum;

public class LoginException extends MemberException {
    private final ErrorEnum errorEnum;

    public LoginException(LoginErrorEnum errorEnum){
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
