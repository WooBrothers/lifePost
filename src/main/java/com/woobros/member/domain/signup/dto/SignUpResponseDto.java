package com.woobros.member.domain.signup.dto;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpResponseDto {

    private static final String ENUM_NAME = "SignUpErrorEnum.";

    @NotNull(message = ENUM_NAME + "ID_NOT_NULL")
    private final int id;
}
