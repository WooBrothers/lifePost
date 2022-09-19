package com.wellclaim.member.common.exception;

public interface ErrorEnum {
    String getMessage();
    String getErrorCode();
    String getHttpStatusCode();
}
