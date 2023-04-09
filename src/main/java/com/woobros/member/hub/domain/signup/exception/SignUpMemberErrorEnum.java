package com.woobros.member.hub.domain.signup.exception;

import com.woobros.member.hub.common.exception.ErrorConst;
import com.woobros.member.hub.common.exception.MemberErrorEnum;

public enum SignUpMemberErrorEnum implements MemberErrorEnum {

    LOGOUT_ERROR("sign", "SU0001", "400", ""),
    NAME_BLANK_ERROR("name" + ErrorConst.BLANK_ERROR, "SU0002", "400", "name"),
    NAME_SIZE_OUT_ERROR("name" + ErrorConst.SIZE_OUT_ERROR, "SU0003", "400", "name"),
    EMAIL_FORMAT_ERROR_ERROR("email" + ErrorConst.FORMAT_ERROR_ERROR, "SU0004", "400", "email"),
    PASSWORD_BLANK_ERROR("password" + ErrorConst.BLANK_ERROR, "SU0005", "400", "password"),
    PASSWORD_SIZE_OUT_ERROR("password" + ErrorConst.SIZE_OUT_ERROR, "SU0006", "400", "password"),
    NICK_BLANK_ERROR("nickname" + ErrorConst.BLANK_ERROR, "SU0007", "400", "nickName"),
    NICK_SIZE_OUT_ERROR("nickname" + ErrorConst.SIZE_OUT_ERROR, "SU0008", "400", "nickName"),
    EMAIL_DUPLICATED_ERROR("email" + ErrorConst.DUPLICATED, "SU0009", "409", "email"),
    ;


    private final String message;
    private final String errorCode;
    private final String httpStatusCode;
    private final String field;

    SignUpMemberErrorEnum(String message, String errorCode, String httpStatusCode, String field) {
        this.message = message;
        this.errorCode = errorCode;
        this.httpStatusCode = httpStatusCode;
        this.field = field;
    }

    public String getMessage() {
        return this.message;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getHttpStatusCode() {
        return this.httpStatusCode;
    }

    public String getField() {
        return this.field;
    }
}
