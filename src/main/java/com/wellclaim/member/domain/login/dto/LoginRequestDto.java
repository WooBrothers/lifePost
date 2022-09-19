package com.wellclaim.member.domain.login.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Builder
public class LoginRequestDto {
    private static final String ENUM_NAME = "LoginErrorEnum.";

    @NotNull(message = ENUM_NAME + "ID_NOT_NULL")
    private final String id;
}
