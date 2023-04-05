package com.woobros.member.hub.domain.signup.dto;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class SignUpResponseDto {

    private static final String ENUM_NAME = "SignUpErrorEnum.";

    @NotNull(message = ENUM_NAME + "ID_NOT_NULL")
    private final int memberId;
}
