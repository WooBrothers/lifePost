package com.wellclaim.member.common;

import com.wellclaim.member.common.exception.ErrorEnum;

public class MemberException extends RuntimeException{
    private final ErrorEnum errorEnum;

    public MemberException(ErrorEnum errorEnum){
        super(errorEnum.getMessage());
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
