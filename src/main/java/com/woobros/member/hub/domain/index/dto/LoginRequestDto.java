package com.woobros.member.hub.domain.index.dto;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequestDto {

    private static final String ENUM_NAME = "LoginErrorEnum.";

    @NotNull(message = ENUM_NAME + "ID_NOT_NULL")
    private final String id;
}