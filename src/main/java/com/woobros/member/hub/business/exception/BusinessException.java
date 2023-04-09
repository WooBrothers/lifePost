package com.woobros.member.hub.business.exception;

import com.woobros.member.hub.common.exception.MemberErrorEnum;
import com.woobros.member.hub.common.exception.MemberException;

public class BusinessException extends MemberException {

    private MemberErrorEnum memberErrorEnum;

    public BusinessException(MemberErrorEnum memberErrorEnum) {
        super(memberErrorEnum);
        this.memberErrorEnum = memberErrorEnum;

    }
}
