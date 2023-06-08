package com.woobros.member.hub.model.exception;

import com.woobros.member.hub.common.exception.ErrorEnum;
import com.woobros.member.hub.common.exception.ErrorException;

public class BusinessException extends ErrorException {

    private ErrorEnum errorEnum;

    public BusinessException(ErrorEnum errorEnum) {
        super(errorEnum);
        this.errorEnum = errorEnum;

    }
}
