package com.woobros.member.common.exception;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

    private final String errorMessage;
    private final String errorCode;
    private final Map<String, String> fieldErrorCodeMsgMap;
    private final List<String> fieldErrorCodes;
}
