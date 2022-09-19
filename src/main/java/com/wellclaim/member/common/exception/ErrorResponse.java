package com.wellclaim.member.common.exception;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class ErrorResponse {
    private final String errorMessage;
    private final String errorCode;
    private final Map<String, String> fieldErrorCodeMsgMap;
    private final List<String> fieldErrorCodes;
}
